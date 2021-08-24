package com.demo.securechatcapstone.home.my

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_my_profile.*
import javax.inject.Inject

class MyProfileFragment : Fragment(R.layout.fragment_my_profile), MyProfileContract.View {

    interface CallBack {
        fun signout()
    }

    var callback: CallBack? = null
    @Inject
    override lateinit var presenter: MyProfileContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getUserInfo()
        btnLogout.setOnClickListener {
            presenter.logOut()
            callback?.signout()
        }
    }

    override fun showLoading() {
    }

    override fun setUser(user: User?) {
        Picasso.get().load(user?.profileImage).into(imgProfile)
        tvUsernameProfile.text = user?.username
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