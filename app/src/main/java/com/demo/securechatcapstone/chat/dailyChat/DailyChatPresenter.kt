package com.demo.securechatcapstone.chat.dailyChat

import android.util.Log
import com.demo.securechatcapstone.App
import com.demo.securechatcapstone.encryption.AESCrypto
import com.demo.securechatcapstone.encryption.GenerateNumber
import com.demo.securechatcapstone.encryption.Hashing
import com.demo.securechatcapstone.encryption.RSACrypto
import com.demo.securechatcapstone.localDB.AppDatabase
import com.demo.securechatcapstone.localDB.conversation.ConversationInfoLocal
import com.demo.securechatcapstone.model.ChatMessage
import com.demo.securechatcapstone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DailyChatPresenter(val view:DailyChatContract.View,appDatabase: AppDatabase):DailyChatContract.Presenter {
    private val appDatabase:AppDatabase
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
        val fromRef = firebaseDatabase.getReference("/daily-messages/$fromId/$toId").push()
        val toRef = firebaseDatabase.getReference("/daily-messages/$toId/$fromId").push()
        val hashKeyExchange = Hashing().hash(keyExchange, "SHA-256")
        val cipherText = AESCrypto().encrypt(hashKeyExchange.take(16), string)
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

        val latestMessFromRef = firebaseDatabase.getReference("/latest-messages/$fromId/daily/$toId")
        latestMessFromRef.setValue(chatMessage)
        val latestMessToRef = firebaseDatabase.getReference("/latest-messages/$toId/daily/$fromId")
        latestMessToRef.setValue(chatMessage)
    }

    override fun listenForMessage(user: User?, keyExchange: String?) {
        val toId = user?.uid
        val fromId = firebaseAuth.uid
        val ref = firebaseDatabase.getReference("/daily-messages/$fromId/$toId")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = mutableListOf<ChatMessage>()
                snapshot.children.forEach {
                    val chatMessage = it.getValue(ChatMessage::class.java)
                    val hashKeyExchange = Hashing().hash(keyExchange!!, "SHA-256")
                    if (chatMessage != null) {
                        chatMessage.text =
                            AESCrypto().decrypt(hashKeyExchange.take(16), chatMessage.text)!!
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

    override fun showKeyExchange(user: User?) {
        val hashKey =
            Hashing().hash(conversationDao.getOneKeyExchange(user?.uid!!), "SHA-256")
        val rs = hashKey.substring(0, 16) + "\n" + hashKey.substring(
            16,
            32
        ) + "\n" + hashKey.substring(32, 48) + "\n" + hashKey.substring(48)
        view.showKeyExchange(rs)
    }

    override fun start() {
        return
    }
}