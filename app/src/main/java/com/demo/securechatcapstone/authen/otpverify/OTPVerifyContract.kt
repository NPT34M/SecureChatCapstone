package com.demo.securechatcapstone.authen.otpverify

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.google.firebase.auth.PhoneAuthCredential

interface OTPVerifyContract {
    interface View : BaseView<Presenter> {
        fun showProgress(boolean: Boolean)
        fun verifyFail(string: String)
        fun verifySuccess(phone:String)
    }

    interface Presenter : BasePresenter {
        fun signWithCredential(credential: PhoneAuthCredential, phone: String)
    }
}