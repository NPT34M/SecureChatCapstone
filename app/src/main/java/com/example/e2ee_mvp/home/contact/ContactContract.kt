package com.example.e2ee_mvp.home.contact

import com.example.e2ee_mvp.BasePresenter
import com.example.e2ee_mvp.BaseView
import com.example.e2ee_mvp.model.User

interface ContactContract {
    interface View : BaseView<Presenter>{
        fun showListContact(users:List<User>)
    }
    interface Presenter : BasePresenter{
        fun getUserFromDB()
        fun fetchCurrentUserLogin()
        fun verifyUserLoggedIn():String
    }
}