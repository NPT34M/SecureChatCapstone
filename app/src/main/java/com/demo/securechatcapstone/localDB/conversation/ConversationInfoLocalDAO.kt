package com.demo.securechatcapstone.localDB.conversation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ConversationInfoLocalDAO {
    @Insert
    fun insertNewConversation(vararg conversation: ConversationInfoLocal)

    @Query("SELECT key_exchange FROM conversations WHERE toUId IN (:id)")
    fun getOneKeyExchange(id: String): String
}