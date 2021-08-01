package com.example.e2ee_mvp.localDB

import android.content.Context
import androidx.room.Room
import com.example.e2ee_mvp.localDB.user.UserInfoLocalDAO
import com.google.firebase.auth.FirebaseAuth

object LocalDataSource {
    private var db: AppDatabase? = null
    private fun createDatabase(context: Context, id: String) {
        db = Room.databaseBuilder(context, AppDatabase::class.java, "${id}.db")
            .allowMainThreadQueries().build()
    }

    fun getAppDatabase(context: Context, id: String): AppDatabase {
        if (db == null) {
            createDatabase(context, id)
        }
        return db!!
    }
}