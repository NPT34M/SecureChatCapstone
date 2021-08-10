package com.demo.securechatcapstone.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.appbar.*

class AppActivity : AppCompatActivity(), ContactFragment.CallBack, MyProfileFragment.CallBack,
    LatestMessageFragment.CallBack {

    companion object {
        const val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        setSupportActionBar(myToolbar)
        val latestMessageFragment = LatestMessageFragment().also {
            LatestMessagePresenter(it)
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