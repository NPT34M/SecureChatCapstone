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
            if (it.isSuccessful) {
                val uid = it.result?.user?.uid
                firebaseDatabase.getReference("/users/${uid}").child("username")
                    .get().addOnCompleteListener {
                        val displayName = it.result?.value
                        if (displayName == null) {
                            view.verifySuccess(phone)
                        }else{
                            view.verifySuccess()
                        }
                    }
            }else{
                //
                view.verifyFail(it.exception.toString())
            }
        }
    }

    override fun start() {
        return
    }
}