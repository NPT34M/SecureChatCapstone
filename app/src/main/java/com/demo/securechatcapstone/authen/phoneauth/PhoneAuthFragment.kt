package com.demo.securechatcapstone.authen.phoneauth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.demo.securechatcapstone.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_phone_auth.*
import java.util.concurrent.TimeUnit

class PhoneAuthFragment : Fragment(R.layout.fragment_phone_auth), PhoneAuthContract.View {
    override lateinit var presenter: PhoneAuthContract.Presenter
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var phoneAuthCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    interface Callback {
        fun toVerify(string: String, id: String)
    }

    var callback: Callback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSubmitPhoneAuth.setOnClickListener {
            showProgress(true)
            btnSubmitPhoneAuth.visibility = View.GONE
            if (presenter.numberValidation(getPhoneNumber(), getContryCode())) {
                val phoneResult = getContryCode() + getPhoneNumber()
                val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber(phoneResult)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(requireActivity())
                    .setCallbacks(phoneAuthCallbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            } else {
                btnSubmitPhoneAuth.visibility = View.VISIBLE
            }
        }
        phoneAuthCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                showProgress(false)
                btnSubmitPhoneAuth.visibility = View.VISIBLE
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showProgress(false)
                Toast.makeText(requireContext(), "$p0", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                showProgress(false)
                val mobile = getContryCode() + getPhoneNumber()
                submitSuccess(mobile, p0)
            }
        }
    }


    override fun submitSuccess(num: String, id: String) {
        callback?.toVerify(num, id)
    }

    override fun submitFail(err: String) {
        showProgress(false)
        Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(flag: Boolean) {
        if (flag) {
            phoneAuthProgressBar.visibility = View.VISIBLE
        }
        phoneAuthProgressBar.visibility = View.GONE
    }

    override fun getContryCode(): String {
        return edtCountryCode.text.trim().toString()
    }

    override fun getPhoneNumber(): String {
        return edtPhone.text.trim().toString()
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as Callback)?.let {
            callback = it
        }
    }
}