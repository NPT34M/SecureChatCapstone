package com.example.e2ee_mvp.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e2ee_mvp.authen.MainActivity
import com.example.e2ee_mvp.R
import com.example.e2ee_mvp.adapter.ViewPageAdapter
import com.example.e2ee_mvp.chat.ChatLogActivity
import com.example.e2ee_mvp.home.contact.ContactFragment
import com.example.e2ee_mvp.home.contact.ContactPresenter
import com.example.e2ee_mvp.home.latestChat.LatestMessagePresenter
import com.example.e2ee_mvp.home.latestChat.LatestMessageFragment
import com.example.e2ee_mvp.model.User
import com.example.e2ee_mvp.home.my.MyProfileFragment
import com.example.e2ee_mvp.home.my.MyProfilePresenter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_app.*

class AppActivity : AppCompatActivity(), ContactFragment.CallBack, MyProfileFragment.CallBack,
    LatestMessageFragment.CallBack {
    companion object {
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        val latestMessageFragment = LatestMessageFragment().also {
            LatestMessagePresenter(it)
        }
        val contactFragment = ContactFragment().also {
            ContactPresenter(it)
        }
        val my = MyProfileFragment().also {
            MyProfilePresenter(it)
        }
        val viewPageAdapter = ViewPageAdapter(this)
        viewPageAdapter.addFragment(latestMessageFragment, "Latest Message")
        viewPageAdapter.addFragment(contactFragment, "Contact")
        viewPageAdapter.addFragment(my, "Profile")
        viewPagerApp.adapter = viewPageAdapter
        TabLayoutMediator(tabLayoutApp, viewPagerApp) { tab, position ->
            tab.text = viewPageAdapter.getTitle(position)
        }.attach()
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

    override fun signoutFromLatest() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun latestToChatLog(user: User) {
        val intent = Intent(this, ChatLogActivity::class.java)
        intent.putExtra(USER_KEY, user)
        startActivity(intent)
    }

//    override fun toFriendProfile(user: User) {
//        val intent = Intent(this, FriendProfileActivity::class.java)
//        intent.putExtra(USER_KEY, user)
//        startActivity(intent)
//    }

    override fun signout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}