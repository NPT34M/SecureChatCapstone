package com.example.e2ee_mvp.search

import com.example.e2ee_mvp.home.contact.ContactPresenter
import com.example.e2ee_mvp.model.User
import com.example.e2ee_mvp.model.UserFriend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchPresenter(val view: SearchContract.View) : SearchContract.Presenter {
    init {
        view.presenter = this
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override fun getCurrentUserFriend() {
        val currentUId = firebaseAuth.currentUser?.uid
        val friends = HashMap<String, UserFriend?>()
        val ref = firebaseDatabase.getReference("/users/$currentUId/friends")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    friends.put(it.key!!, it.getValue(UserFriend::class.java))
                }
                getUsers(friends)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun getUsers(friends: HashMap<String, UserFriend?>) {
        val users = mutableListOf<User>()
        val ref = firebaseDatabase.getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (!(user?.uid in friends.keys) && user != null && user?.uid != firebaseAuth?.uid) {
                        users.add(user)
                    }
                }
                view.showUser(users)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun addToContact(friendId: String) {
        val currentUId = firebaseAuth.currentUser?.uid
        val myFriend = UserFriend(friendId)
        val otherFriend = UserFriend(currentUId!!)
        val refMy = firebaseDatabase.getReference("/users/$currentUId/friends/$friendId")
        refMy.setValue(myFriend)
        val refFriend = firebaseDatabase.getReference("/users/$friendId/friends/$currentUId")
        refFriend.setValue(otherFriend)
    }

    override fun start() {
        return
    }
}