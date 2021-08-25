package com.demo.securechatcapstone.authen.otpverify

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView
import com.google.firebase.auth.PhoneAuthCredential

interface OTPVerifyContract {
    interface View : BaseView<Presenter> {
        fun verifyFail(string: String)
        fun verifySuccess(phone: String)
        fun verifySuccess()
    }

    interface Presenter : BasePresenter {
        fun signWithCredential(credential: PhoneAuthCredential, phone: String)
    }
}