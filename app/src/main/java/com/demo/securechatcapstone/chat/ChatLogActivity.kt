package com.demo.securechatcapstone.chat

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.demo.securechatcapstone.home.AppActivity
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.chat.chatLog.ChatLogFragment
import com.demo.securechatcapstone.chat.chatLog.ChatLogPresenter
import com.demo.securechatcapstone.chat.dailyChat.DailyChatFragment
import com.demo.securechatcapstone.chat.dailyChat.DailyChatPresenter
import com.demo.securechatcapstone.encryption.Hashing
import com.demo.securechatcapstone.localDB.LocalDataSource
import com.demo.securechatcapstone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity(), ChatLogFragment.Callback, DailyChatFragment.Callback {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var toUser: User? = null
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        toUser = intent.getParcelableExtra(AppActivity.USER_KEY)
        val type = intent.getBooleanExtra(AppActivity.CHAT_TYPE, false)
        Picasso.get().load(toUser?.profileImage).into(imgChatLog)
        tvNameChatLog.text = toUser?.username
        if (type) {
            DailyChatFragment(toUser).also {
                DailyChatPresenter(
                    it,
                    LocalDataSource.getAppDatabase(applicationContext, firebaseAuth.uid!!)
                )
            }.let {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout_chat_log, it)
                    .commit()
            }
        } else {
            ChatLogFragment(toUser).also {
                ChatLogPresenter(
                    it,
                    LocalDataSource.getAppDatabase(applicationContext, firebaseAuth.uid!!)
                )
            }.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_chat_log, it).commit()
            }
        }
        val appDatabase = LocalDataSource.getAppDatabase(applicationContext, firebaseAuth.uid!!)
        val conversationDao = appDatabase.ConversationInfoLocalDAO()
        val hashKey = Hashing().hash(conversationDao.getOneKeyExchange(toUser?.uid!!), "SHA-256")
        val rs = hashKey.substring(0,16)+"\n"+hashKey.substring(16,32)+"\n"+hashKey.substring(32,48)+"\n"+hashKey.substring(48)
        icon_infor.setOnClickListener {
            displayDialogCheck(rs)
        }
    }

    private fun displayDialogCheck(hashKey: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("CONFIRM KEY EXCHANGE")
        builder.setMessage(hashKey)

        builder.setPositiveButton("OK", { dialog, which ->
            return@setPositiveButton
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun toApp() {
        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun dailyChatToApp() {
        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
        finish()
    }
}