package com.example.e2ee_mvp.home.contact

import android.widget.Adapter
import com.example.e2ee_mvp.BasePresenter
import com.example.e2ee_mvp.BaseView
import com.example.e2ee_mvp.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

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