package com.example.e2ee_mvp.authen.unlock

import com.example.e2ee_mvp.BasePresenter
import com.example.e2ee_mvp.BaseView

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
        fun createUserInfoLocal(string: String):Boolean
        fun checkUnlockPassword(string: String): Boolean
    }
}