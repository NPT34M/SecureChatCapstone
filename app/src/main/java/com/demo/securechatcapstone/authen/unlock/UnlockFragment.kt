package com.demo.securechatcapstone.authen.unlock

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.demo.securechatcapstone.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_unlock.*

class UnlockFragment : Fragment(R.layout.fragment_unlock), UnlockContract.View {

    interface Callback {
        fun backToPhoneAuth()
        fun toMain()
    }

    var callback: Callback? = null

    override lateinit var presenter: UnlockContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        if (!presenter.isExist()) {
            setText("This is new account. Please create your password \n(This password use to unlock application)")
            showProgress(false)
            btnUnlockSubmit.setOnClickListener {
//                if (presenter.createUserInfoLocal(getPassword())) {
//                    unlockSuccess()
//                    callback?.toMain()
//                } else {
//                    unlockFail()
//                }
                presenter.createUserInfoLocal(getPassword())
            }
        } else {
            setText("Enter password to unlock application")
            showProgress(false)
            btnUnlockSubmit.setOnClickListener {
                if (presenter.checkUnlockPassword(getPassword())) {
                    unlockSuccess()
                    callback?.toMain()
                } else {
                    unlockFail()
                }
            }
        }
        btnUnlockLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            callback?.backToPhoneAuth()
        }
    }

    fun setText(s: String) {
        tvUnlock.text = s
    }

    override fun showProgress(boolean: Boolean) {
        if (boolean == true) {
            progressBarUnlock.visibility = View.VISIBLE
        }
        progressBarUnlock.visibility = View.GONE
    }

    override fun getPassword(): String {
        return edtUnlockPassword.text.toString()
    }

    override fun unlockSuccess() {
        Toast.makeText(requireContext(), "Unlock Sucessfull!!", Toast.LENGTH_SHORT).show()
    }

    override fun unlockFail() {
        Toast.makeText(requireContext(), "Unlock Fail!!", Toast.LENGTH_SHORT).show()
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