package com.example.e2ee_mvp.chat

import com.example.e2ee_mvp.BasePresenter
import com.example.e2ee_mvp.BaseView
import com.example.e2ee_mvp.model.ChatMessage
import com.example.e2ee_mvp.model.User

interface ChatLogContract {
    interface View : BaseView<Presenter>{
        fun getTextMessage():String
        fun clearText()
        fun showMessageLog(listMessage:List<ChatMessage>)
    }
    interface Presenter : BasePresenter{
        fun performSendMessage(user: User?)
        fun listenForMessage(user: User?)
    }
}