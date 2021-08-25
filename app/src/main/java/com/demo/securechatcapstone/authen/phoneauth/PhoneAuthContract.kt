package com.demo.securechatcapstone.authen.phoneauth

import com.demo.securechatcapstone.BasePresenter
import com.demo.securechatcapstone.BaseView

interface PhoneAuthContract {
    interface View : BaseView<Presenter> {
        fun submitSuccess(num: String, id: String)
        fun submitFail(err: String)
        fun getContryCode(): String
        fun getPhoneNumber(): String
    }

    interface Presenter : BasePresenter {
        fun numberValidation(phone: String, code: String): Boolean
    }
}