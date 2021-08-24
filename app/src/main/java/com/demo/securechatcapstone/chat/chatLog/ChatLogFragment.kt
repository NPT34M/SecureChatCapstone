package com.demo.securechatcapstone.chat.chatLog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.demo.securechatcapstone.adapter.ChatAdapter
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.model.ChatMessage
import com.demo.securechatcapstone.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_chat_log.*
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class ChatLogFragment(val toUser: User?) :
    Fragment(R.layout.fragment_chat_log),
    ChatLogContract.View {

    interface Callback {
        fun toApp()
    }

    var callback: Callback? = null

    @Inject
    override lateinit var presenter: ChatLogContract.Presenter

    private var adapter = FirebaseAuth.getInstance().uid?.let { ChatAdapter(it, toUser) }

    override fun showMessageLog(listMessage: List<ChatMessage>) {
        adapter?.submitList(listMessage)
        //scroll to bottom
        adapter?.itemCount?.let { recyclerViewChatLog?.smoothScrollToPosition(it) }
        clearText()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getKeyExchange(toUser)
        recyclerViewChatLog?.adapter = adapter
        btnSendMessageChatLog.setOnClickListener {
            presenter.performSendMessage(toUser, getTextMessage(), false, false)
        }
        btnImgSend.setOnClickListener {
            pickFromGallery()
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val uri: Uri? = data?.data
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val image = stream.toByteArray()
            val sImage = Base64.encodeToString(image, Base64.DEFAULT).replace("\n", "")
            presenter.performSendMessage(toUser, sImage, true, false)
        }
    }

    override fun getTextMessage(): String {
        return edtTextChatLog?.text.toString()
    }

    override fun warningDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("STOP")
        builder.setMessage("This conversation are not secure!!")

        builder.setPositiveButton("OK", { dialog, which ->
            callback?.toApp()
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun clearText() {
        edtTextChatLog?.text?.clear()
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext() as? Callback)?.let {
            callback = it
        }
    }
}