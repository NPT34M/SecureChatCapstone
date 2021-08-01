package com.example.e2ee_mvp.authen.register

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.e2ee_mvp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterPresenter(val view: RegisterContract.View) : RegisterContract.Presenter {
    init {
        view.presenter = this
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    override fun register(email: String, password: String) {
        view.showRegisterProgress()
        if (email.isEmpty() || password.isEmpty()) {
            view.registerFail("Email or Password or Username is blank!")
            return
        } else if (view.getPhotoSelectURI() == null) {
            view.registerFail("Please choose your image!!!")
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    uploadPhotoToFirebaseStorage(view.getPhotoSelectURI())
                } else {
                    view.registerFail(it.exception?.message.toString())
                }
            }
    }

    fun uploadPhotoToFirebaseStorage(uri: Uri?) {
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
                    val user = User(uid, view.getUsername(), it.toString())
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
//                view.clearAll()
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