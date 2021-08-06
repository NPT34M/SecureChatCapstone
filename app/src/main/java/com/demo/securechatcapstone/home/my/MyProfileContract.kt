package com.demo.securechatcapstone.home.my

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.demo.securechatcapstone.model.User

interface MyProfileContract {
    interface View : BaseView<Presenter> {
        fun showLoading()
        fun setUser(user: User?)
    }

    interface Presenter : BasePresenter {
        fun getUserInfo()
        fun logOut()
    }
}