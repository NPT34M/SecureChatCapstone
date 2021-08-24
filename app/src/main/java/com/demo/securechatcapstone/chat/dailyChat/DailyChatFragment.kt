package com.demo.securechatcapstone.chat.dailyChat

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.adapter.ChatAdapter
import com.demo.securechatcapstone.model.ChatMessage
import com.demo.securechatcapstone.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_chat_log.*
import kotlinx.android.synthetic.main.fragment_daily_chat.*
import javax.inject.Inject

class DailyChatFragment(val toUser: User?) : Fragment(R.layout.fragment_daily_chat),
    DailyChatContract.View {

    interface Callback {
        fun dailyChatToApp()
    }

    var callback: Callback? = null

    @Inject
    override lateinit var presenter: DailyChatContract.Presenter

    private var adapter = FirebaseAuth.getInstance().uid?.let { ChatAdapter(it, toUser) }

    override fun showMessageLog(listMessage: List<ChatMessage>) {
        adapter?.submitList(listMessage)
        //scroll to bottom
        adapter?.itemCount?.let { recyclerViewChatLog?.smoothScrollToPosition(it) }
        clearText()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getKeyExchange(toUser)
        recyclerViewDailyChat?.adapter = adapter
        btnSendMessageDailyChat.setOnClickListener {
            presenter.performSendMessage(toUser, getTextMessage(), false, true)
        }
    }

    override fun getTextMessage(): String {
        return edtTextDailyChat?.text.toString()
    }

    override fun clearText() {
        edtTextDailyChat?.text?.clear()
    }


    override fun warningDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("STOP")
        builder.setMessage("This conversation are not secure!!")

        builder.setPositiveButton("OK", { dialog, which ->
            callback?.dailyChatToApp()
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext() as? Callback)?.let {
            callback = it
        }
    }
}