package com.demo.securechatcapstone.authen.otpverify

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.FirebaseDatabase

class OTPVerifyPresenter(val view: OTPVerifyContract.View) : OTPVerifyContract.Presenter {
    init {
        view.presenter = this
    }

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun signWithCredential(credential: PhoneAuthCredential, phone: String) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            view.showProgress(false)
            if (it.isSuccessful) {
                savePhoneNumberToDatabase(it.result?.user?.uid, phone)
                view.verifySuccess(phone)
            }
        }
    }

    private fun savePhoneNumberToDatabase(uid: String?, phone: String) {
        val ref = firebaseDatabase.getReference("/users/${uid}/phoneNumber")
        ref.setValue(phone)
    }

    override fun start() {
        return
    }
}