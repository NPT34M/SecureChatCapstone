package com.demo.securechatcapstone.home.contact

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.demo.securechatcapstone.model.User
import com.demo.securechatcapstone.model.UserFriend

interface ContactContract {
    interface View : BaseView<Presenter>{
        fun showListContact(users:List<User>)
        fun getSearchText():String
    }
    interface Presenter : BasePresenter{
        fun getLimitUser()
        fun getUserWithText(text:String)
        fun fetchCurrentUserLogin()
        fun verifyUserLoggedIn():Boolean
    }
}