package com.example.e2ee_mvp.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import com.example.e2ee_mvp.adapter.ChatAdapter
import com.example.e2ee_mvp.R
import com.example.e2ee_mvp.home.latestChat.LatestMessagePresenter
import com.example.e2ee_mvp.model.ChatMessage
import com.example.e2ee_mvp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chat_log.*

class ChatLogFragment(val toUser: User?) :
    Fragment(R.layout.fragment_chat_log),
    ChatLogContract.View {
    override lateinit var presenter: ChatLogContract.Presenter

    private var adapter= FirebaseAuth.getInstance().uid?.let { ChatAdapter(it,toUser) }

    override fun showMessageLog(listMessage: List<ChatMessage>) {
        adapter?.submitList(listMessage)
        Log.d("Err","$adapter")
        //scroll to bottom
        adapter?.itemCount?.let { recyclerViewChatLog.smoothScrollToPosition(it) }
        clearText()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.listenForMessage(toUser)
        recyclerViewChatLog.adapter = adapter
        btnSendMessageChatLog.setOnClickListener {
            presenter.performSendMessage(toUser)
        }
    }

    override fun getTextMessage(): String {
        return edtTextChatLog.text.toString()
    }

    override fun clearText() {
        edtTextChatLog.text.clear()
    }


}