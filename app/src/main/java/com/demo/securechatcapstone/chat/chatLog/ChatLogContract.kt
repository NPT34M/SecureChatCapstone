package com.demo.securechatcapstone.chat.chatLog

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.demo.securechatcapstone.model.ChatMessage
import com.demo.securechatcapstone.model.User

interface ChatLogContract {
    interface View : BaseView<Presenter> {
        fun getTextMessage(): String
        fun clearText()
        fun showMessageLog(listMessage: List<ChatMessage>)
        fun warningDialog()
    }

    interface Presenter : BasePresenter {
        fun getKeyExchange(user: User?)
        fun performSendMessage(user: User?, string: String, isImage: Boolean, isDailyMessage:Boolean)
        fun listenForMessage(user: User?, keyExchange: String?)
    }
}