package com.example.e2ee_mvp.authen.login

import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(val view: LoginContract.View) : LoginContract.Presenter {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        view.presenter = this
    }

    override fun login(email: String, password: String) {
        view.showLoginProgress()
        if (email.isEmpty() || password.isEmpty()) {
            view.loginFail("Email or Password can not blank!")
            return
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    view.loginSuccess()
                } else {
                    view.loginFail(it.exception?.message.toString())
                }
            }
    }

    override fun start() {
        return
    }
}