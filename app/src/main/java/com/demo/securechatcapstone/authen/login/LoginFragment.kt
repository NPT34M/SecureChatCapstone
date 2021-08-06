package com.demo.securechatcapstone.authen.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.demo.securechatcapstone.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login), LoginContract.View {
    interface Callback {
        fun toRegister()
        fun loginToUnlock()
    }

    var callback: Callback? = null
    override lateinit var presenter: LoginContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (FirebaseAuth.getInstance().uid != null) {
            callback?.loginToUnlock()
            return
        }
        btnLogin.setOnClickListener {
            showLoginProgress()
            presenter.login(getEmailLogin(), getPasswordLogin())

        }
        tvRegister.setOnClickListener {
            callback?.toRegister()
        }
    }

    override fun showLoginProgress() {
        progressBar2.visibility = View.VISIBLE
    }

    override fun loginSuccess() {
        progressBar2.visibility = View.GONE
        callback?.loginToUnlock()
    }

    override fun loginFail(message: String) {
        progressBar2.visibility = View.GONE
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }

    override fun getEmailLogin(): String {
        return edtEmailLogin.text.toString()
    }

    override fun getPasswordLogin(): String {
        return edtPasswordLogin.text.toString()
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as? Callback)?.let {
            callback = it
        }
    }

}