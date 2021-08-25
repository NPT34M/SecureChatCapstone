package com.demo.securechatcapstone.home.latestChat

import android.util.Log
import android.widget.Toast
import com.demo.securechatcapstone.App
import com.demo.securechatcapstone.encryption.AESCrypto
import com.demo.securechatcapstone.encryption.GenerateNumber
import com.demo.securechatcapstone.encryption.RSACrypto
import com.demo.securechatcapstone.localDB.AppDatabase
import com.demo.securechatcapstone.localDB.conversation.ConversationInfoLocal
import com.demo.securechatcapstone.model.ChatMessage
import com.demo.securechatcapstone.model.LatestMessageModel
import com.demo.securechatcapstone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.Signature

class LatestMessagePresenter(val view: LatestMessageContract.View, appDatabase: AppDatabase) :
    LatestMessageContract.Presenter {
    private val appDatabase: AppDatabase

    init {
        view.presenter = this
        this.appDatabase = appDatabase
    }

    companion object {
        var currentLoginUser: User? = null
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val userDao = appDatabase.userInfoLocalDAO()
    private val conversationDao = appDatabase.ConversationInfoLocalDAO()

    override fun listenForLatestMessage() {
//        fetchCurrentUserLogin()
        val fromId = firebaseAuth.uid
        val ref =
            firebaseDatabase.getReference("/latest-messages/$fromId")
        ref.keepSynced(true)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val latestMessageList = mutableListOf<ChatMessage>()
                snapshot.children.forEach {
                    it.children.forEach {
                        val chatMessage = it.getValue(ChatMessage::class.java)
                        if (chatMessage != null) {
                            latestMessageList.add(chatMessage)
                        }
                    }
                }
                getUserFromMessage(latestMessageList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun getUserFromMessage(listChatMessage: List<ChatMessage>) {
        val list = listChatMessage.sortedByDescending { it.timestamp }
        val userPriKey = userDao.getPrivateNumOfUser(firebaseAuth.uid!!)
        val latestMessageList = mutableListOf<LatestMessageModel>()
        if (list.size == 0) {
            view.showLatestMessage(latestMessageList)
            return
        }
        list.forEach {
            val chatPartnerId: String
            if (firebaseAuth.uid == it.fromId) {
                chatPartnerId = it.toId
            } else {
                chatPartnerId = it.fromId
            }
            var keyExchange = conversationDao.getOneKeyExchange(chatPartnerId)
            val ref = firebaseDatabase.getReference("/users/${chatPartnerId}")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val partnerUser = snapshot.getValue(User::class.java)
                    val signature = snapshot.child("pubKeySignature").value.toString()
                    if (keyExchange.isNullOrEmpty()) {
                        if (RSACrypto().verifySignature(
                                App.pubKeyServer,
                                partnerUser?.pubKey,
                                signature
                            )
                        ) {
                            keyExchange = GenerateNumber().secretKeyExchange(
                                App.pNumber!!,
                                partnerUser?.pubKey!!, userPriKey
                            ).toString()
                            ConversationInfoLocal(partnerUser.uid, keyExchange).let {
                                conversationDao.insertNewConversation(it)
                            }
//                            conversationDao.insertNewConversation(conversationInfoLocal)
                        } else {
                            Log.d("AAA", "Verify public key signature failed!!")
                            return
                        }
                    }
                    val text = AESCrypto().decrypt(keyExchange!!.take(16), it.text)
                    val latestMess =
                        LatestMessageModel(
                            it.id,
                            text!!,
                            it.image,
                            it.dailyMessage,
                            it.timestamp,
                            partnerUser
                        )
                    latestMessageList.add(latestMess)
                    if (latestMessageList.size == list.size) {
                        showLatest(latestMessageList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    private fun showLatest(listLatest: List<LatestMessageModel>) {
        view.showLatestMessage(listLatest)
    }

    override fun fetchCurrentUserLogin() {
        val uid = firebaseAuth.uid
        val ref = firebaseDatabase.getReference("/users/${uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentLoginUser = snapshot.getValue(User::class.java)
                Log.d("CurrentUserLogin", "User: ${currentLoginUser?.username} logged in!")
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun verify(): Boolean {
        return firebaseAuth.uid == null
    }

    override fun start() {
    }
}
