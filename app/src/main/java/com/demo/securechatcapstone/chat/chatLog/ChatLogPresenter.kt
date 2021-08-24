package com.demo.securechatcapstone.chat.chatLog

import android.util.Log
import com.demo.securechatcapstone.App
import com.demo.securechatcapstone.encryption.AESCrypto
import com.demo.securechatcapstone.encryption.GenerateNumber
import com.demo.securechatcapstone.encryption.RSACrypto
import com.demo.securechatcapstone.localDB.AppDatabase
import com.demo.securechatcapstone.localDB.conversation.ConversationInfoLocal
import com.demo.securechatcapstone.model.ChatMessage
import com.demo.securechatcapstone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatLogPresenter(val view: ChatLogContract.View, appDatabase: AppDatabase) :
    ChatLogContract.Presenter {
    private val appDatabase: AppDatabase

    init {
        view.presenter = this
        this.appDatabase = appDatabase
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val userDao = appDatabase.userInfoLocalDAO()
    private val conversationDao = appDatabase.ConversationInfoLocalDAO()

    override fun getKeyExchange(user: User?) {
        var keyExchange = conversationDao.getOneKeyExchange(user?.uid!!)
        if (!keyExchange.isNullOrEmpty()) {
            listenForMessage(user, keyExchange)
        } else {
            firebaseDatabase.getReference("users/${user.uid}").child("pubKeySignature")
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val signature = it.result?.value.toString()
                        if (RSACrypto().verifySignature(
                                App.pubKeyServer,
                                user.pubKey,
                                signature
                            )
                        ) {
                            val privateKey = userDao.getPrivateNumOfUser(firebaseAuth.uid!!)
                            keyExchange =
                                GenerateNumber().secretKeyExchange(
                                    App.pNumber!!,
                                    user.pubKey,
                                    privateKey
                                ).toString()
                            ConversationInfoLocal(user.uid, keyExchange).let {
                                conversationDao.insertNewConversation(it)
                            }
                            listenForMessage(user, keyExchange)
                        } else {
                            view.warningDialog()
                        }
                    }
                }
        }
    }

    override fun performSendMessage(
        user: User?,
        string: String,
        isImage: Boolean,
        isDailyMessage: Boolean
    ) {
        if (string.isEmpty() && isImage == false) {
            return
        }
        val toId = user?.uid
        val keyExchange = conversationDao.getOneKeyExchange(toId!!)
        val fromId = firebaseAuth.uid
        if (fromId == null) return
        val fromRef = firebaseDatabase.getReference("/users-messages/$fromId/$toId").push()
        val toRef = firebaseDatabase.getReference("/users-messages/$toId/$fromId").push()
        val cipherText = AESCrypto().encrypt(keyExchange.take(16), string)
        val chatMessage =
            ChatMessage(
                fromRef.key!!,
                cipherText!!,
                isImage,
                isDailyMessage,
                fromId,
                toId,
                System.currentTimeMillis() / 1000
            )

        fromRef.setValue(chatMessage).addOnCompleteListener {
            Log.d("SendMess", "Save our chat message: ${fromRef.key}")
        }
        toRef.setValue(chatMessage)

        val latestMessFromRef = firebaseDatabase.getReference("/latest-messages/$fromId/message/$toId")
        latestMessFromRef.setValue(chatMessage)
        val latestMessToRef = firebaseDatabase.getReference("/latest-messages/$toId/message/$fromId")
        latestMessToRef.setValue(chatMessage)
    }

    override fun listenForMessage(user: User?, keyExchange: String?) {
        val toId = user?.uid
        val fromId = firebaseAuth.uid
        val ref = firebaseDatabase.getReference("/users-messages/$fromId/$toId")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = mutableListOf<ChatMessage>()
                snapshot.children.forEach {
                    val chatMessage = it.getValue(ChatMessage::class.java)
                    if (chatMessage != null) {
                        chatMessage.text =
                            AESCrypto().decrypt(keyExchange!!.take(16), chatMessage.text)!!
                        messageList.add(chatMessage)
                    }
                }
                view.showMessageLog(messageList)
                Log.d("BBBB", "${view.hashCode()}")
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun start() {
        return
    }
}