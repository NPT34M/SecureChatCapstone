package com.example.e2ee_mvp.localDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e2ee_mvp.localDB.user.UserInfoLocal
import com.example.e2ee_mvp.localDB.user.UserInfoLocalDAO

@Database(entities = arrayOf(UserInfoLocal::class),version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userInfoLocalDAO(): UserInfoLocalDAO
}