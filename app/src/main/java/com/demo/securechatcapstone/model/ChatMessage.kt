package com.demo.securechatcapstone.model

class ChatMessage(
    val id: String,
    val text: String,
    val image: Boolean,
    val fromId: String,
    val toId: String,
    val timestamp: Long
) {
    constructor() : this("", "", false, "", "", -1)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }
        other as ChatMessage
        if (text != other.text || timestamp != other.timestamp || fromId != other.fromId || toId != other.toId) {
            return false
        }
        return true
    }
}