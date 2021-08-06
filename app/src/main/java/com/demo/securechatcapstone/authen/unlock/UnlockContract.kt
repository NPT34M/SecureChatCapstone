package com.demo.securechatcapstone.authen.unlock

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView

interface UnlockContract {
    interface View : BaseView<Presenter> {
        fun showProgress(boolean: Boolean)
        fun getPassword(): String
        fun unlockSuccess()
        fun unlockFail()
    }

    interface Presenter : BasePresenter {
        fun verifyLogin(): Boolean
        fun isExist(): Boolean
        fun createUserInfoLocal(string: String)
        fun checkUnlockPassword(string: String): Boolean
    }
}