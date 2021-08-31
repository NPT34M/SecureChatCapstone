package com.demo.securechatcapstone.authen.unlock

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.demo.securechatcapstone.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_unlock.*

class UnlockFragment : Fragment(R.layout.fragment_unlock), UnlockContract.View {

    interface Callback {
        fun backToPhoneAuth()
        fun toMain()
    }

    var callback: Callback? = null

    override lateinit var presenter: UnlockContract.Presenter

    private lateinit var functions: FirebaseFunctions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        functions = Firebase.functions
        if (!presenter.isUserInfoExistInDevice()) {
            presenter.loadPrivateInfoFromDB()
        } else {
            setText("Enter password to unlock application")
            btnUnlockSubmit.setOnClickListener {
                failUnlockCount().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result!! >= 10) {
                            Toast.makeText(
                                requireContext(),
                                "Your account has been disabled for entering the wrong password many times ",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (presenter.checkUnlockPassword(getPassword())) {
                                unlockSuccess()
                            } else {
                                unlockFail()
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "${it.exception?.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

            }
        }
        btnUnlockLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            callback?.backToPhoneAuth()
        }
    }

    private fun failUnlockCount(): Task<Int> {
        return functions.getHttpsCallable("failUnlockCount").call().continueWith { task ->
            val data = task.result?.data as HashMap<*, *>
            val result = data.get("value").toString().toInt()
            result
        }
    }

    private fun countUnlock(flag: Boolean): Task<Int> {
        val data = hashMapOf("flag" to flag)
        return functions.getHttpsCallable("countUnlock").call(data).continueWith { task ->
            val rs = task.result?.data.toString().toInt()
            rs
        }
    }

    override fun loadFromRealTimeDBAndUnlock() {
        setText("You must remember and input unlock password when register user info")
        btnUnlockSubmit.setOnClickListener {
            failUnlockCount().addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!! >= 10) {
                        Toast.makeText(
                            requireContext(),
                            "Your account has been disabled for entering the wrong password many times ",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        presenter.checkSignaturePrivateInfo(getPassword())
                    }
                } else {
                    Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun createNew() {
        setText(
            "This is new account. Please create your password " +
                    "\n(This password use to unlock application)" +
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
        countUnlock(true).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Unlock Sucessfull!!", Toast.LENGTH_SHORT).show()
                callback?.toMain()
            } else {
                Toast.makeText(requireContext(), "${it.exception?.cause}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun unlockFail() {
        countUnlock(false).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Unlock Fail!!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "${it.exception?.cause}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
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