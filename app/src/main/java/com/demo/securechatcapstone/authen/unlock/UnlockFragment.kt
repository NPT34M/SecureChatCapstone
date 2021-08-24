package com.demo.securechatcapstone.authen.unlock

import android.app.AlertDialog
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
//        showProgress(true)
        if (!presenter.isUserInfoExistInDevice()) {
            presenter.loadPrivateInfoFromDB()
        } else {
            setText("Enter password to unlock application")
            btnUnlockSubmit.setOnClickListener {
                if (presenter.checkUnlockPassword(getPassword())) {
                    unlockSuccess()
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

    override fun loadFromRealTimeDBAndUnlock(){
        setText("You must remember and input unlock password when register user info")
        btnUnlockSubmit.setOnClickListener {
            presenter.checkSignaturePrivateInfo(getPassword())
        }
    }

    override fun createNew() {
        setText(
            "This is new account. Please create your password +" +
                    "\n(This password use to unlock application)+" +
                    "\n[Password must have 8 character, a-z, A-Z, 0-9 and special character(!@#$%^&*), can not contain blank]" +
                    "\n You must be remember this password for changing device, We doesn't save it"
        )
        btnUnlockSubmit.setOnClickListener {
            presenter.createUserInfoLocal(getPassword())
        }
    }

    private fun setText(s: String) {
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

    override fun dialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("STOP")
        builder.setMessage("This key will be modified!!")

        builder.setPositiveButton("OK", { dialog, which ->
            return@setPositiveButton
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun unlockSuccess() {
        Toast.makeText(requireContext(), "Unlock Sucessfull!!", Toast.LENGTH_SHORT).show()
        callback?.toMain()
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