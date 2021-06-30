package com.example.e2ee_mvp.home.latestChat

import com.example.e2ee_mvp.BasePresenter
import com.example.e2ee_mvp.BaseView
import com.example.e2ee_mvp.model.ChatMessage
import com.example.e2ee_mvp.model.LatestMessageModel
import com.example.e2ee_mvp.model.User

interface LatestMessageContract {
    interface View : BaseView<Presenter> {
        fun showLatestMessage(listLatestMessage: List<LatestMessageModel>)
    }

    interface Presenter : BasePresenter {
        fun listenForLatestMessage()
        fun getUserFromMessage(listChatMessage: List<ChatMessage>)
        fun verify():String
        fun fetchCurrentUserLogin()
    }
}