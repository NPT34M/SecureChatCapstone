package com.demo.securechatcapstone.chat

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.demo.securechatcapstone.model.ChatMessage
import com.demo.securechatcapstone.model.User

interface ChatLogContract {
    interface View : BaseView<Presenter>{
        fun getTextMessage():String
        fun clearText()
        fun showMessageLog(listMessage:List<ChatMessage>)
    }
    interface Presenter : BasePresenter{
        fun performSendMessage(user: User?,string: String,isImage:Boolean)
        fun listenForMessage(user: User?)
    }
}