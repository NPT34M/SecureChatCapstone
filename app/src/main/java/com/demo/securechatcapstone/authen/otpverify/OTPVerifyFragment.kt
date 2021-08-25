package com.demo.securechatcapstone.authen.otpverify

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.demo.securechatcapstone.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_otp_verify.*
import kotlinx.android.synthetic.main.fragment_phone_auth.*
import java.util.concurrent.TimeUnit

class OTPVerifyFragment : Fragment(R.layout.fragment_otp_verify), OTPVerifyContract.View {
    override lateinit var presenter: OTPVerifyContract.Presenter

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var phoneAuthCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    interface Callback {
        fun toRegister(number: String)
        fun OTPToUnlock()
    }

    var callback: Callback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phoneNumber = arguments?.getString("phoneNum")
        tvPhoneNumOTP.text = phoneNumber
        var verificationId = arguments?.getString("verifyId")
        startTimer()
        tvResendOTP.setOnClickListener {
            startTimer()
            val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber!!)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(phoneAuthCallbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        phoneAuthCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(requireContext(), "$p0", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                verificationId = p0
                Toast.makeText(requireContext(), "Code sent", Toast.LENGTH_SHORT).show()
            }
        }
        btnOTPVerify.setOnClickListener {
            btnOTPVerify.isEnabled = false
            if (pinViewOTP.text.toString().isEmpty()) {
                verifyFail("Code can not blank!")
                btnOTPVerify.isEnabled = true
                return@setOnClickListener
            }
            val code = pinViewOTP.text.toString()
            if (verificationId != null) {
                val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId!!, code)
                presenter.signWithCredential(phoneAuthCredential, phoneNumber!!)
            }
        }
    }

    val countDownTimer = object : CountDownTimer(60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val remindSec: Long = millisUntilFinished / 1000;
            try {
                tvResendOTP.visibility = View.GONE
                tvOTP.setText("Try resent code after " + (remindSec / 60) + ":" + (remindSec % 60))
            } catch (e: Exception) {

            }
        }

        override fun onFinish() {
            try {
                tvResendOTP.visibility = View.VISIBLE
                tvOTP.setText("Didn't get the code?")
            } catch (e: Exception) {
            }
            cancel()
        }
    }

    private fun startTimer() {
        countDownTimer.cancel()
        countDownTimer.start()
    }

    private fun stopTimer() {
        countDownTimer.cancel()
    }

    override fun verifyFail(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
        btnOTPVerify.isEnabled = true
    }

    override fun verifySuccess(phone: String) {
        callback?.toRegister(phone)
    }

    override fun verifySuccess() {
        callback?.OTPToUnlock()
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