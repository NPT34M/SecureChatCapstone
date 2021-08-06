package com.demo.securechatcapstone.authen.unlock

import com.demo.securechatcapstone.App
import com.demo.securechatcapstone.encryption.GenerateNumber as genNum
import com.demo.securechatcapstone.localDB.AppDatabase
import com.demo.securechatcapstone.localDB.user.UserInfoLocal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.math.BigInteger
import java.util.*

class UnlockPresenter(val view: UnlockContract.View, appDatabase: AppDatabase) :
    UnlockContract.Presenter {
    private val appDatabase: AppDatabase

    init {
        view.presenter = this
        this.appDatabase = appDatabase
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun verifyLogin(): Boolean {
        return firebaseAuth.uid == null
    }

    override fun isExist(): Boolean {
        val userDao = appDatabase.userInfoLocalDAO()
        val user = userDao.getOneUser(firebaseAuth.uid!!)
        return user != null
    }

    override fun createUserInfoLocal(password: String) {
        if (password.isEmpty()) {
            view.unlockFail()
            return
        } else {
            val userDao = appDatabase.userInfoLocalDAO()
            val privateNumber = BigInteger(BigInteger(App.pNumber!!).bitLength() - 2, Random())
            val userInfo = UserInfoLocal(
                firebaseAuth.uid!!,
                privateNumber.toString(),
                password
            )
            val publicKey: BigInteger = genNum().genPublicKey(
                BigInteger(App.pNumber!!),
                BigInteger(App.gNumber!!),
                privateNumber
            )
            firebaseDatabase.getReference("/users/${firebaseAuth.uid}").child("pubKey")
                .setValue(publicKey.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        userDao.insertNewUser(userInfo)
                        view.unlockSuccess()
                    } else {
                        view.unlockFail()
                    }
                }
        }
    }

    override fun checkUnlockPassword(password: String): Boolean {
        if (password.isEmpty()) {
            return false
        }
        val userDao = appDatabase.userInfoLocalDAO()
        val password = userDao.getOneUser(firebaseAuth.uid!!)?.password2
        return password.equals(view.getPassword())
    }

    override fun start() {
        return
    }
}