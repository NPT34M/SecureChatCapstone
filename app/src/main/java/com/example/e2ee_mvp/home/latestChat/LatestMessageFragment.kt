package com.example.e2ee_mvp.home.latestChat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.e2ee_mvp.adapter.LatestMessageAdapter
import com.example.e2ee_mvp.R
import com.example.e2ee_mvp.model.LatestMessageModel
import com.example.e2ee_mvp.model.User
import kotlinx.android.synthetic.main.fragment_latest_message.*

class LatestMessageFragment() : Fragment(R.layout.fragment_latest_message),
    LatestMessageContract.View {

    override lateinit var presenter: LatestMessageContract.Presenter

    interface CallBack {
        fun signoutFromLatest()
        fun latestToChatLog(user: User)
    }

    var callBack: CallBack? = null

    private val adapter = LatestMessageAdapter {
        callBack?.latestToChatLog(it)
    }

    override fun showLatestMessage(listLatestMessage: List<LatestMessageModel>) {
        adapter?.submitList(listLatestMessage)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.fetchCurrentUserLogin()
        if (presenter.verify() == "null") {
            callBack?.signoutFromLatest()
        }
        presenter.listenForLatestMessage()
        recyclerViewLatestMessage.adapter = adapter
    }

    override fun onDetach() {
        super.onDetach()
        callBack = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as CallBack)?.let {
            callBack = it
        }
    }

}