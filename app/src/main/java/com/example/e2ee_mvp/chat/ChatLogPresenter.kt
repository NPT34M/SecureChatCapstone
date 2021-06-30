package com.example.e2ee_mvp.chat

import android.util.Log
import com.example.e2ee_mvp.model.ChatMessage
import com.example.e2ee_mvp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatLogPresenter(val view: ChatLogContract.View) : ChatLogContract.Presenter {
    init {
        view.presenter = this
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseDatabase.getInstance()

    override fun performSendMessage(user: User?) {
        val text = view.getTextMessage()
        val toId = user?.uid
        val fromId = firebaseAuth.uid
        if (fromId == null) return
        val fromRef = firebaseDB.getReference("/users-messages/$fromId/$toId").push()
        val toRef = firebaseDB.getReference("/users-messages/$toId/$fromId").push()

        val chatMessage =
            ChatMessage(fromRef.key!!, text, fromId, toId!!, System.currentTimeMillis() / 1000)

        fromRef.setValue(chatMessage).addOnCompleteListener {
            Log.d("SendMess", "Save our chat message: ${fromRef.key}")
        }
        toRef.setValue(chatMessage)

        val latestMessFromRef = firebaseDB.getReference("/latest-messages/$fromId/$toId")
        latestMessFromRef.setValue(chatMessage)
        val latestMessToRef = firebaseDB.getReference("/latest-messages/$toId/$fromId")
        latestMessToRef.setValue(chatMessage)
    }

    override fun listenForMessage(user: User?) {
        val toId = user?.uid
        val fromId = firebaseAuth.uid
        val ref = firebaseDB.getReference("/users-messages/$fromId/$toId")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = mutableListOf<ChatMessage>()
                snapshot.children.forEach {
                    val chatMessage = it.getValue(ChatMessage::class.java)
                    if(chatMessage!=null){
                        messageList.add(chatMessage)
                    }
                }
                view.showMessageLog(messageList)
                Log.d("BBBB","${view.hashCode()}")
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun start() {
        return
    }
}