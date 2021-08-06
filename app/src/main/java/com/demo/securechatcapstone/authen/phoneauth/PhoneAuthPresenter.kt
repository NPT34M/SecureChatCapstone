package com.demo.securechatcapstone.authen.phoneauth

class PhoneAuthPresenter(val view: PhoneAuthContract.View) : PhoneAuthContract.Presenter {
    init {
        view.presenter = this
    }

    override fun numberValidation(phone: String, code: String): Boolean {
        if (phone.isEmpty()) {
            view.submitFail("Phone can not blank")
            return false
        } else if (code.isEmpty()) {
            view.submitFail("Country code can not blank")
            return false
        }
        return true
    }

    override fun start() {
        return
    }
}