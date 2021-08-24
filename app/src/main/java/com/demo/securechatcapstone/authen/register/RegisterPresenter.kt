package com.demo.securechatcapstone.authen.register

import android.net.Uri
import android.util.Log
import com.demo.securechatcapstone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterPresenter(val view: RegisterContract.View) :
    RegisterContract.Presenter {

    init {
        view.presenter = this
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    override fun register(username: String, uri: Uri?, phoneNum: String) {
        if (username.isEmpty()) {
            view.registerFail("Display name can not blank!")
            return
        } else if (uri == null) {
            view.registerFail("Please choose your avatar!")
            return
        }
        uploadPhotoToFirebaseStorage(username, uri, phoneNum)
    }

    fun uploadPhotoToFirebaseStorage(username: String, uri: Uri?, phoneNum: String) {
        if (uri == null) {
            return
        }
        val fileName = UUID.randomUUID().toString()
        val uid = firebaseAuth.uid ?: ""
        val ref = firebaseStorage.getReference("/images/${fileName}")

        ref.putFile(uri)
            .addOnSuccessListener {
                Log.d("PutPhotoSuccess", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("DownloadURL", "File location: ${it}")
                    val user = User(uid, username, it.toString(), phoneNum, "", "", false)
                    saveUserToFirebaseRealTimeDB(user)
                }
            }
            .addOnFailureListener {
                Log.d("PutPhotoFailed", "Failed uploaded image: ${it.message}")
            }
    }

    fun saveUserToFirebaseRealTimeDB(user: User) {
        val ref = firebaseDatabase.getReference("/users/${user.uid}")
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("SaveUserSuccess", "Sucessfully save user to Firebase Database!")
                view.registerSuccess()
            }
            .addOnFailureListener {
                Log.d("SaveUserFailed", "Failed to save user: ${it.message}")
                view.registerFail(it.message.toString())
            }
    }

    override fun start() {
        return
    }

}