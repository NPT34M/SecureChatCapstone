package com.demo.securechatcapstone.localDB.conversation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationInfoLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "toUId")
    val toUserId: String?,

    @ColumnInfo(name = "key_exchange")
    val keyExchange: String?

) {
    constructor(toUid: String?, keyExchange: String?) : this(0, toUid, keyExchange)
}