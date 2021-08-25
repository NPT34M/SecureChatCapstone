package com.demo.securechatcapstone.authen.register

import android.net.Uri
import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView

interface RegisterContract {
    interface View : BaseView<Presenter> {
        fun registerSuccess()
        fun registerFail(message: String)
        fun getUsername(): String
        fun getPhotoSelectURI(): Uri?
        fun clearAll()
    }

    interface Presenter : BasePresenter {
        fun register(username: String, uri: Uri?, phoneNum: String)
    }
}