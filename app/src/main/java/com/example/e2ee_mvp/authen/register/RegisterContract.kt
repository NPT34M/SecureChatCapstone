package com.example.e2ee_mvp.authen.register

import android.net.Uri
import com.example.e2ee_mvp.BasePresenter
import com.example.e2ee_mvp.BaseView

interface RegisterContract {
    interface View : BaseView<Presenter> {
        fun showRegisterProgress()
        fun registerSuccess()
        fun registerFail(message: String)
        fun getUsername(): String
        fun getEmail(): String
        fun getPassword(): String
        fun getPhotoSelectURI(): Uri?
        fun clearAll()
    }

    interface Presenter : BasePresenter {
        fun register(email: String, password: String)
    }
}