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
import com.demo.securechatcapstone.search.SearchActivity
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.appbar.*

class AppActivity : AppCompatActivity(), ContactFragment.CallBack, MyProfileFragment.CallBack,
    LatestMessageFragment.CallBack {
//    private lateinit var toolbar:androidx.appcompat.widget.Toolbar

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
//        val viewPageAdapter = ViewPageAdapter(this)
//        viewPageAdapter.addFragment(latestMessageFragment, "Latest Message")
//        viewPageAdapter.addFragment(contactFragment, "Contact")
//        viewPageAdapter.addFragment(my, "Profile")
//        viewPagerApp.adapter = viewPageAdapter
//        TabLayoutMediator(tabLayoutApp, viewPagerApp) { tab, position ->
//            tab.text = viewPageAdapter.getTitle(position)
//        }.attach()
        makeCurrentFragment(latestMessageFragment)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.latestMessageFragment -> {
                    makeCurrentFragment(latestMessageFragment)
                    supportActionBar?.setTitle("Messengers")
                }
                R.id.contactFragment -> {
                    makeCurrentFragment(contactFragment)
                    supportActionBar?.setTitle("Contacts")
                }
                R.id.myProfileFragment -> {
                    makeCurrentFragment(my)
                    supportActionBar?.setTitle("My Profile")
                }
            }
            true
        }

    }


    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameHome, fragment).commit()
        supportActionBar?.setTitle("Messengers")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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