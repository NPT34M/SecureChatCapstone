package com.example.e2ee_mvp.authen.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.e2ee_mvp.R
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(R.layout.fragment_register), RegisterContract.View {
    interface Callback {
        fun toLogin()
        fun fromRegisterToMain()
    }

    var callback: Callback? = null

    override lateinit var presenter: RegisterContract.Presenter

    var selectPhotoURI: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRegister.setOnClickListener {
            showRegisterProgress()
            presenter.register(getEmail(), getPassword())
        }
        tvAlreadyAccount.setOnClickListener {
            callback?.toLogin()
        }
        imvChooseAvatar.setOnClickListener {
            openImageChooser()
        }
    }

    fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK);
        intent.type = "image/*"
        startActivityForResult(intent, 0);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectPhotoURI = data.data
            imvChooseAvatar.setImageURI(selectPhotoURI)
        }
    }

    override fun getPhotoSelectURI(): Uri? {
        return selectPhotoURI
    }

    override fun clearAll() {
        edtEmail.text.clear()
        edtUsername.text.clear()
        edtPassword.text.clear()
        imvChooseAvatar.setImageDrawable(null)
    }

    override fun showRegisterProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun registerSuccess() {
        progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), "Register Successful!", Toast.LENGTH_SHORT).show()
        callback?.fromRegisterToMain()
    }

    override fun registerFail(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }

    override fun getUsername(): String {
        return edtUsername.text.toString()
    }

    override fun getEmail(): String {
        return edtEmail.text.toString()
    }

    override fun getPassword(): String {
        return edtPassword.text.toString()
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