package com.demo.securechatcapstone.home.contact

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.demo.securechatcapstone.model.User
import com.demo.securechatcapstone.model.UserFriend

interface ContactContract {
    interface View : BaseView<Presenter>{
        fun showListContact(users:List<User>)
    }
    interface Presenter : BasePresenter{
        fun getUserFriends()
        fun getUserFromDB(friend:HashMap<String,UserFriend?>)
        fun fetchCurrentUserLogin()
        fun verifyUserLoggedIn():String
    }
}