package com.demo.securechatcapstone.authen.unlock

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView

interface UnlockContract {
    interface View : BaseView<Presenter> {
        fun showProgress(boolean: Boolean)
        fun getPassword(): String
        fun unlockSuccess()
        fun unlockFail()
        fun loadFromRealTimeDBAndUnlock()
        fun createNew()
        fun dialog()
    }

    interface Presenter : BasePresenter {
        fun verifyLogin(): Boolean
        fun loadPrivateInfoFromDB()
        fun isUserInfoExistInDevice(): Boolean
        fun createUserInfoLocal(password: String)
        fun checkSignaturePrivateInfo(password: String)
        fun checkUnlockPassword(string: String): Boolean
    }
}