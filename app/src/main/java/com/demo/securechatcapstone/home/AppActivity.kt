package com.demo.securechatcapstone.home

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.demo.securechatcapstone.FriendProfileActivity
import com.demo.securechatcapstone.authen.MainActivity
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.chat.ChatLogActivity
import com.demo.securechatcapstone.home.contact.ContactFragment
import com.demo.securechatcapstone.home.contact.ContactPresenter
import com.demo.securechatcapstone.home.latestChat.LatestMessagePresenter
import com.demo.securechatcapstone.home.latestChat.LatestMessageFragment
import com.demo.securechatcapstone.model.User
import com.demo.securechatcapstone.home.my.MyProfileFragment
import com.demo.securechatcapstone.home.my.MyProfilePresenter
import com.demo.securechatcapstone.localDB.LocalDataSource
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.appbar.*

class AppActivity : AppCompatActivity(), ContactFragment.CallBack, MyProfileFragment.CallBack,
    LatestMessageFragment.CallBack {

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    companion object {
        const val USER_KEY = "USER_KEY"
        const val CHAT_TYPE = "CHAT_TYPE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        setSupportActionBar(myToolbar)
        val latestMessageFragment = LatestMessageFragment().also {
            LatestMessagePresenter(
                it,
                LocalDataSource.getAppDatabase(applicationContext, firebaseAuth.uid!!)
            )
        }
        val contactFragment = ContactFragment().also {
            ContactPresenter(it)
        }
        val my = MyProfileFragment().also {
            MyProfilePresenter(it)
        }
        makeCurrentFragment(latestMessageFragment, "Messenger")
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.latestMessageFragment -> {
                    makeCurrentFragment(latestMessageFragment, "Messenger")
                }
                R.id.contactFragment -> {
                    makeCurrentFragment(contactFragment, "Contacts")
                }
                R.id.myProfileFragment -> {
                    makeCurrentFragment(my, "My Profile")
                }
            }
            true
        }

    }


    private fun makeCurrentFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction().replace(R.id.frameHome, fragment).commit()
        tvTitleHome.text = title
    }

    override fun backToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun toChatLog(user: User) {
        val intent = Intent(this, ChatLogActivity::class.java)
        intent.putExtra(USER_KEY, user)
        intent.putExtra(CHAT_TYPE,false)
        startActivity(intent)
    }

    override fun latestToDaily(user: User) {
        val intent = Intent(this, ChatLogActivity::class.java)
        intent.putExtra(USER_KEY, user)
        intent.putExtra(CHAT_TYPE,true)
        startActivity(intent)
    }

    override fun toDailyChat(user: User) {
        val intent = Intent(this, ChatLogActivity::class.java)
        intent.putExtra(USER_KEY, user)
        intent.putExtra(CHAT_TYPE,true)
        startActivity(intent)
    }

    override fun signOutFromLatest() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun latestToChatLog(user: User) {
        val intent = Intent(this, ChatLogActivity::class.java)
        intent.putExtra(USER_KEY, user)
        startActivity(intent)
    }

    override fun showBottomSheet(user: User) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        val sheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.bottom_sheet_layout, findViewById<LinearLayout>(R.id.bottom_sheet)
        )
        sheetView.findViewById<View>(R.id.delete).setOnClickListener {
            deleteDialog(user)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()
    }

    private fun deleteDialog(user: User) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("WARNING")
        builder.setMessage("Do you want to delete this conversation?")

        builder.setPositiveButton("OK", { dialog, which ->
            firebaseDatabase.getReference("latest-messages/${FirebaseAuth.getInstance().uid}/message/${user.uid}")
                .removeValue().addOnCompleteListener {
                    if (it.isSuccessful) {
                        firebaseDatabase.getReference("users-messages/${FirebaseAuth.getInstance().uid}/${user.uid}")
                            .removeValue().addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this, "Message deleted", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Log.e("AAA", it.exception.toString())
                                }
                            }
                    } else {
                        Log.e("AAA", it.exception.toString())
                    }
                }
        })
        builder.setNegativeButton("Cancel", { dialog, which ->
            Toast.makeText(this, "Cancel delete message", Toast.LENGTH_SHORT)
                .show()
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun toFriendProfile(user: User) {
        val intent = Intent(this, FriendProfileActivity::class.java)
        intent.putExtra(USER_KEY, user)
        startActivity(intent)
    }

    override fun signout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}