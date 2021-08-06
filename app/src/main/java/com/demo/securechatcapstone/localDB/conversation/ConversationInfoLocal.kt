package com.demo.securechatcapstone.localDB.conversation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConversationInfoLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "fromUId")
    val fromUserId: String?,

    @ColumnInfo(name = "ToUId")
    val toUserId: String?,

    @ColumnInfo(name = "key_exchange")
    val keyExchange:String?

)