package com.demo.securechatcapstone.localDB.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserInfoLocalDAO {
    @Insert
    fun insertNewUser(vararg user:UserInfoLocal)

    @Query ("SELECT * FROM users")
    fun getAllUsers():List<UserInfoLocal>

    @Query ("SELECT * FROM users WHERE uid IN (:id)")
    fun getOneUser(id:String):UserInfoLocal?

    @Query ("SELECT pass2 FROM users WHERE uid IN (:id)")
    fun getPasswordOfUser(id:String):String

    @Query ("SELECT private_number FROM users WHERE uid IN (:id)")
    fun getPrivateNumOfUser(id:String):String
}