package com.demo.securechatcapstone.home.contact

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.adapter.UserAdapter
import com.demo.securechatcapstone.model.User
import kotlinx.android.synthetic.main.fragment_contact.*
import javax.inject.Inject

class ContactFragment() : Fragment(R.layout.fragment_contact), ContactContract.View {

    @Inject
    override lateinit var presenter: ContactContract.Presenter

    interface CallBack {
        fun backToLogin()
        fun toChatLog(user: User)
        fun toDailyChat(user: User)
        fun toFriendProfile(user: User)
    }

    var callback: CallBack? = null;

    private var adapter = UserAdapter({
        callback?.toChatLog(it)
    }, {
        callback?.toFriendProfile(it)
    }, { callback?.toDailyChat(it) })

    override fun showListContact(users: List<User>) {
        adapter.setData(users)
//        Log.e("AAA","${users.size}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (presenter.verifyUserLoggedIn()) {
            callback?.backToLogin()
        }
        presenter.getLimitUser()
        edtSearchContact.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                presenter.getLimitUser()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.getUserWithText(getSearchText())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        rcvFragmentContact.adapter = adapter
    }

    override fun getSearchText(): String {
        return edtSearchContact?.text.toString()
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