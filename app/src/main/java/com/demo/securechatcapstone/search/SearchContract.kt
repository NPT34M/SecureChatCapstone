package com.demo.securechatcapstone.search

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.demo.securechatcapstone.model.User
import com.demo.securechatcapstone.model.UserFriend

interface SearchContract {
    interface View : BaseView<Presenter> {
        fun showUser(listUser: List<User>)
        fun getSearchText(): String
        fun addSuccess()
        fun addFail()
    }

    interface Presenter : BasePresenter {
        fun getCurrentUserFriend(text: String)
        fun getUsers(friends: HashMap<String, UserFriend?>)
        fun getUsersWithText(text: String, friends: HashMap<String, UserFriend?>)
        fun addToContact(friendId: String)
    }
}