package com.demo.securechatcapstone.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.demo.securechatcapstone.home.AppActivity
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.model.User

class ChatLogActivity : AppCompatActivity() {

    var toUser: User? = null
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        toUser = intent.getParcelableExtra(AppActivity.USER_KEY)
        supportActionBar?.title = toUser?.username
        ChatLogFragment(toUser).also {
            ChatLogPresenter(it)
        }.let {
            Log.d("AAA", "chatlogg")
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_chat_log, it).commit()
        }
    }
}