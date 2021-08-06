package com.demo.securechatcapstone.authen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.authen.otpverify.OTPVerifyFragment
import com.demo.securechatcapstone.authen.otpverify.OTPVerifyPresenter
import com.demo.securechatcapstone.authen.phoneauth.PhoneAuthFragment
import com.demo.securechatcapstone.authen.phoneauth.PhoneAuthPresenter
import com.demo.securechatcapstone.authen.register.RegisterFragment
import com.demo.securechatcapstone.authen.register.RegisterPresenter
import com.demo.securechatcapstone.authen.unlock.UnlockFragment
import com.demo.securechatcapstone.authen.unlock.UnlockPresenter
import com.demo.securechatcapstone.home.AppActivity
import com.demo.securechatcapstone.localDB.LocalDataSource
import com.demo.securechatcapstone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), RegisterFragment.Callback,
    UnlockFragment.Callback, PhoneAuthFragment.Callback, OTPVerifyFragment.Callback {

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (firebaseAuth.currentUser == null) {
            PhoneAuthFragment().also {
                PhoneAuthPresenter(it)
            }.let {
                supportFragmentManager.beginTransaction().add(R.id.frame_layout, it).commit()
            }
        } else {
            val ref = firebaseDatabase.getReference("/users/${firebaseAuth.uid}")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user?.username.isNullOrEmpty()) {
                        ref.child("phoneNumber").get().addOnCompleteListener {
                            val bundle = Bundle()
                            val phone = it.result?.value
                            bundle.putString("phone", phone.toString())
                            RegisterFragment().also {
                                it.arguments = bundle
                                RegisterPresenter(it)
                            }.let {
                                supportFragmentManager.beginTransaction().add(R.id.frame_layout, it)
                                    .commit()
                            }
                        }
                    } else {
                        UnlockFragment().also {
                            UnlockPresenter(
                                it,
                                LocalDataSource.getAppDatabase(
                                    applicationContext,
                                    firebaseAuth.uid!!
                                )
                            )
                        }.let {
                            supportFragmentManager.beginTransaction().add(R.id.frame_layout, it)
                                .commit()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    override fun registerToUnlock() {
        UnlockFragment().also {
            UnlockPresenter(
                it,
                LocalDataSource.getAppDatabase(
                    applicationContext,
                    firebaseAuth.uid!!
                )
            )
        }.let {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, it)
                .addToBackStack(null).commit()
        }
    }

    override fun backToPhoneAuth() {
        PhoneAuthFragment().also {
            PhoneAuthPresenter(it)
        }.let {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, it)
                .addToBackStack(null).commit()
        }
    }

    override fun toMain() {
        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
    }

    override fun toVerify(string: String, id: String) {
        val bundle = Bundle()
        bundle.putString("phoneNum", string)
        bundle.putString("verifyId", id)
        OTPVerifyFragment().also {
            it.arguments = bundle
            OTPVerifyPresenter(it)
        }.let {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, it)
                .addToBackStack(null).commit()
        }
    }

    override fun toRegister(number: String) {
        val bundle = Bundle()
        bundle.putString("phone", number)
        RegisterFragment().also {
            it.arguments = bundle
            RegisterPresenter(it)
        }.let {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, it)
                .addToBackStack(null).commit()
        }
    }
}