package com.example.e2ee_mvp.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.e2ee_mvp.home.AppActivity
import com.example.e2ee_mvp.R
import com.example.e2ee_mvp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

//        FirebaseDatabase.getInstance().getReference("/users/${FirebaseAuth.getInstance().uid}")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    currentUser = snapshot.getValue(User::class.java)
//                    toUser = intent.getParcelableExtra(AppActivity.USER_KEY)
//                    supportActionBar?.title = toUser?.username
//                    currentUser?.let {
//                        ChatLogFragment(it, toUser).also {
//                            ChatLogPresenter(it)
//                        }.let {
//                            Log.d("AAA", "chatlogg")
//                            supportFragmentManager.beginTransaction()
//                                .add(R.id.frame_layout_chat_log, it).commit()
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//            })
    }
}