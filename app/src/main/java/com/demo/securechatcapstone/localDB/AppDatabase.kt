package com.demo.securechatcapstone.localDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demo.securechatcapstone.localDB.conversation.ConversationInfoLocal
import com.demo.securechatcapstone.localDB.conversation.ConversationInfoLocalDAO
import com.demo.securechatcapstone.localDB.user.UserInfoLocal
import com.demo.securechatcapstone.localDB.user.UserInfoLocalDAO

@Database(entities = arrayOf(UserInfoLocal::class, ConversationInfoLocal::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInfoLocalDAO(): UserInfoLocalDAO
    abstract fun ConversationInfoLocalDAO(): ConversationInfoLocalDAO
}