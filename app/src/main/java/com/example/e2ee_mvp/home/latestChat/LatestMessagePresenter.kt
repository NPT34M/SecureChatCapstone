package com.example.e2ee_mvp.home.latestChat

import android.util.Log
import com.example.e2ee_mvp.model.ChatMessage
import com.example.e2ee_mvp.model.LatestMessageModel
import com.example.e2ee_mvp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LatestMessagePresenter(val view: LatestMessageContract.View) :
    LatestMessageContract.Presenter {
    init {
        view.presenter = this
    }

    companion object {
        var currentLoginUser: User? = null
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseDatabase.getInstance()

    override fun listenForLatestMessage() {
//        fetchCurrentUserLogin()
        val fromId = firebaseAuth?.uid
        val ref = firebaseDB.getReference("/latest-messages/$fromId")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val latestMessageList = mutableListOf<ChatMessage>()
                snapshot.children.forEach {
                    val chatMessage = it.getValue(ChatMessage::class.java)
                    if (chatMessage != null) {
                        latestMessageList.add(chatMessage)
                    }
                }
                getUserFromMessage(latestMessageList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun getUserFromMessage(listChatMessage: List<ChatMessage>) {
        var list = listChatMessage.sortedByDescending{ it.timestamp }
        val latestMessageList = mutableListOf<LatestMessageModel>()
        list.forEach {
            var chatPartnerId: String
            if (firebaseAuth.uid == it.fromId) {
                chatPartnerId = it.toId
            } else {
                chatPartnerId = it.fromId
            }
            val ref = firebaseDB.getReference("/users/${chatPartnerId}")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val partnerUser = snapshot.getValue(User::class.java)
                    val latestMess = LatestMessageModel(it.id, it.text, it.timestamp, partnerUser)
                    latestMessageList.add(latestMess)
                    view.showLatestMessage(latestMessageList)
                }


                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    override fun fetchCurrentUserLogin() {
        val uid = firebaseAuth.uid
        val ref = firebaseDB.getReference("/users/${uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentLoginUser = snapshot.getValue(User::class.java)
                Log.d("CurrentUserLogin", "User: ${currentLoginUser?.username} logged in!")
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun verify(): String {
        return firebaseAuth.uid.toString()
    }

    override fun start() {
    }
}
