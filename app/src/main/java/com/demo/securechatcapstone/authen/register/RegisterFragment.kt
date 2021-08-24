package com.demo.securechatcapstone.authen.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.demo.securechatcapstone.R
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(R.layout.fragment_register), RegisterContract.View {
    interface Callback {
        fun registerToUnlock()
    }

    var callback: Callback? = null

    override lateinit var presenter: RegisterContract.Presenter

    var selectPhotoURI: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phoneNumber = arguments?.getString("phone")
        edtRegisterPhoneNumber.setText(phoneNumber.toString())
        btnRegister.setOnClickListener {
            showRegisterProgress(true)
            presenter.register(getUsername(),getPhotoSelectURI(),phoneNumber!!)
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
        edtUsername.text.clear()
        imvChooseAvatar.setImageDrawable(null)
    }


    override fun showRegisterProgress(boolean: Boolean) {
        if (boolean) {
            progressBar.visibility = View.VISIBLE
        }
        progressBar.visibility = View.GONE
    }

    override fun registerSuccess() {
//        progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), "Register Successful!", Toast.LENGTH_SHORT).show()
        callback?.registerToUnlock()
    }

    override fun registerFail(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }

    override fun getUsername(): String {
        return edtUsername.text.toString()
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