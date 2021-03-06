package com.demo.securechatcapstone.home.latestChat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.demo.securechatcapstone.adapter.LatestMessageAdapter
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.model.LatestMessageModel
import com.demo.securechatcapstone.model.User
import kotlinx.android.synthetic.main.fragment_latest_message.*
import javax.inject.Inject

class LatestMessageFragment : Fragment(R.layout.fragment_latest_message),
    LatestMessageContract.View {

    @Inject
    override lateinit var presenter: LatestMessageContract.Presenter

    interface CallBack {
        fun signOutFromLatest()
        fun latestToChatLog(user: User)
        fun latestToDaily(user: User)
        fun showBottomSheet(user: User)
    }

    var callBack: CallBack? = null

    private val adapter = LatestMessageAdapter({
        callBack?.latestToChatLog(it)
    }, { callBack?.latestToDaily(it) },
        { callBack?.showBottomSheet(it) })

    override fun showLatestMessage(listLatestMessage: List<LatestMessageModel>) {
        adapter?.submitList(listLatestMessage)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroy() {
        super.onDestroy()
    }

}