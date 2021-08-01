package com.example.e2ee_mvp.home.contact

import android.util.Log
import com.example.e2ee_mvp.model.User
import com.example.e2ee_mvp.model.UserFriend
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

    override fun getUserFriends() {
        val uid = firebaseAuth?.uid
        val friends = HashMap<String, UserFriend?>()
        val ref = firebaseDatabase.getReference("/users/$uid/friends")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    friends.put(it.key!!, it.getValue(UserFriend::class.java))
                }
                getUserFromDB(friends)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun getUserFromDB(friend: HashMap<String, UserFriend?>) {
        fetchCurrentUserLogin()
        val users = mutableListOf<User>()
        val ref = firebaseDatabase.getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user?.uid != currentLoginUser?.uid && user != null && user?.uid in friend.keys) {
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