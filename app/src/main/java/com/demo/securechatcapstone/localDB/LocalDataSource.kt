package com.demo.securechatcapstone.localDB

import android.content.Context
import androidx.room.Room

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