package com.example.e2ee_mvp.authen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e2ee_mvp.R
import com.example.e2ee_mvp.authen.login.LoginFragment
import com.example.e2ee_mvp.authen.login.LoginPresenter
import com.example.e2ee_mvp.authen.register.RegisterFragment
import com.example.e2ee_mvp.authen.register.RegisterPresenter
import com.example.e2ee_mvp.home.AppActivity

class MainActivity : AppCompatActivity(), RegisterFragment.Callback, LoginFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        LoginFragment().also {
            LoginPresenter(it)
        }.let {
            supportFragmentManager.beginTransaction().add(R.id.frame_layout, it).commit()
        }
    }

    override fun toLogin() {
        LoginFragment().also {
            LoginPresenter(it)
        }.let {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, it)
                .addToBackStack(null).commit()
        }
    }

    override fun fromRegisterToMain() {
//        val intent = Intent(this, AppActivity::class.java)
//        startActivity(intent)
        LoginFragment().also {
            LoginPresenter(it)
        }.let {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, it)
                .addToBackStack(null).commit()
        }
    }

    override fun toRegister() {
        RegisterFragment().also {
            RegisterPresenter(it)
        }.let {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, it)
                .addToBackStack(null).commit()
        }
    }

    override fun loginToMain() {
        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
    }

}