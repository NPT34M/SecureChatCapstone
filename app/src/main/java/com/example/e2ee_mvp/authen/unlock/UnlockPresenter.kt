package com.example.e2ee_mvp.authen.unlock

import com.example.e2ee_mvp.App
import com.example.e2ee_mvp.localDB.AppDatabase
import com.example.e2ee_mvp.localDB.user.UserInfoLocal
import com.google.firebase.auth.FirebaseAuth
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

    override fun verifyLogin(): Boolean {
        return firebaseAuth.uid == null
    }

    override fun isExist(): Boolean {
        val userDao = appDatabase.userInfoLocalDAO()
        val user = userDao.getOneUser(firebaseAuth.uid!!)
        return user != null
    }

    override fun createUserInfoLocal(password: String): Boolean {
        if (password.isEmpty()) {
            view.unlockFail()
            return false
        }
        val userDao = appDatabase.userInfoLocalDAO()
        val userInfo = UserInfoLocal(
            firebaseAuth.uid!!,
            BigInteger(BigInteger(App.pNumber!!).bitLength() - 2, Random()).toString(),
            password
        )
        userDao.insertNewUser(userInfo)
        return true
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