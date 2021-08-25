package com.demo.securechatcapstone.chat.dailyChat

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.demo.securechatcapstone.model.ChatMessage
import com.demo.securechatcapstone.model.User

class DailyChatContract {
    interface View : BaseView<Presenter> {
        fun getTextMessage(): String
        fun clearText()
        fun showMessageLog(listMessage: List<ChatMessage>)
        fun warningDialog()
        fun showKeyExchange(str: String)
    }

    interface Presenter : BasePresenter {
        fun getKeyExchange(user: User?)
        fun performSendMessage(
            user: User?,
            string: String,
            isImage: Boolean,
            isDailyMessage: Boolean
        )

        fun listenForMessage(user: User?, keyExchange: String?)
        fun showKeyExchange(user: User?)
    }
}