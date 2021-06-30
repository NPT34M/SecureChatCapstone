package com.example.e2ee_mvp.home.my

import com.example.e2ee_mvp.BasePresenter
import com.example.e2ee_mvp.BaseView
import com.example.e2ee_mvp.model.User

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