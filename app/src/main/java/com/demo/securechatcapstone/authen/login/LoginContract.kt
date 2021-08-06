package com.demo.securechatcapstone.authen.login

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView

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