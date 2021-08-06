package com.demo.securechatcapstone.home.latestChat

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.demo.securechatcapstone.model.ChatMessage
import com.demo.securechatcapstone.model.LatestMessageModel

interface LatestMessageContract {
    interface View : BaseView<Presenter> {
        fun showLatestMessage(listLatestMessage: List<LatestMessageModel>)
    }

    interface Presenter : BasePresenter {
        fun listenForLatestMessage()
        fun getUserFromMessage(listChatMessage: List<ChatMessage>)
        fun verify():Boolean
        fun fetchCurrentUserLogin()
    }
}