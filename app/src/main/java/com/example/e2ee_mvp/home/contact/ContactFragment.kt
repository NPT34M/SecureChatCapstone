package com.example.e2ee_mvp.home.contact

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.e2ee_mvp.R
import com.example.e2ee_mvp.adapter.UserAdapter
import com.example.e2ee_mvp.model.User
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment() : Fragment(R.layout.fragment_contact), ContactContract.View {


    override lateinit var presenter: ContactContract.Presenter

    interface CallBack {
        fun backToLogin()
        fun toChatLog(user: User)
//        fun toFriendProfile(user:User)
    }
    var callback: CallBack? = null

    private var adapter = UserAdapter{
        callback?.toChatLog(it)
//        callback?.toFriendProfile(it)
    }
    override fun showListContact(users: List<User>) {
        adapter.setData(users)
//        Log.e("AAA","${users.size}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getUserFriends()
        rcvFragmentContact.adapter = adapter
        if (presenter.verifyUserLoggedIn() == "null") {
            callback?.backToLogin()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as? CallBack)?.let {
            callback = it
        }
    }
}