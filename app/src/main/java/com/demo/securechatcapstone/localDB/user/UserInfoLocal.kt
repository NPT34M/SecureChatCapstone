package com.demo.securechatcapstone.localDB.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserInfoLocal(
    @PrimaryKey
    val uid: String,

    @ColumnInfo(name = "private_number")
    val privateNum: String?,

    @ColumnInfo(name = "pass2")
    val password2: String?
)