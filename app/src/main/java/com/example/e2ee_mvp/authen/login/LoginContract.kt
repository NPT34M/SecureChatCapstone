package com.example.e2ee_mvp.authen.login

import com.example.e2ee_mvp.BasePresenter
import com.example.e2ee_mvp.BaseView
import com.example.e2ee_mvp.authen.register.RegisterContract

interface LoginContract {
    interface View : BaseView<Presenter>{
        fun showLoginProgress()
        fun loginSuccess()
        fun loginFail(message:String)
        fun getEmailLogin():String
        fun getPasswordLogin():String
    }
    interface Presenter:BasePresenter{
        fun login(email:String,password:String)
    }
}