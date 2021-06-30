package com.example.e2ee_mvp.home.contact

import android.content.Intent
import android.util.Log
import com.example.e2ee_mvp.chat.ChatLogActivity
import com.example.e2ee_mvp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactPresenter(val view: ContactContract.View) : ContactContract.Presenter {
    init {
        view.presenter = this
    }

    companion object {
        var currentLoginUser: User? = null
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override fun getUserFromDB() {
        fetchCurrentUserLogin()
        val users = mutableListOf<User>()
        val ref = firebaseDatabase.getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user?.uid != currentLoginUser?.uid && user != null) {
                        users.add(user)
                    }
                }
                view.showListContact(users)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun fetchCurrentUserLogin() {
        val uid = firebaseAuth.uid
        val ref = firebaseDatabase.getReference("/users/${uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentLoginUser = snapshot.getValue(User::class.java)
                Log.d("CurrentUserLogin", "User: ${currentLoginUser?.username} logged in!")
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun verifyUserLoggedIn(): String {
        return firebaseAuth.uid.toString()
    }

    override fun start() {
        return
    }
}