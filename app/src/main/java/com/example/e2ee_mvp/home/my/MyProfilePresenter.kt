package com.example.e2ee_mvp.home.my

import com.example.e2ee_mvp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyProfilePresenter(val view: MyProfileContract.View) : MyProfileContract.Presenter {

    init {
        view.presenter = this
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun getUserInfo() {
        val currentUserId = firebaseAuth.uid
        var user: User? = null
        firebaseDatabase.getReference("/users/$currentUserId")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)
                    view.setUser(user)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override fun logOut() {
        firebaseAuth.signOut()
    }

    override fun start() {
    }
}