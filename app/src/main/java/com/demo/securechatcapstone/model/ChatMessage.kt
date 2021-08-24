package com.demo.securechatcapstone.model

//class ChatMessage(
//    val id: String,
//    val text: String,
//    val image: Boolean,
//    val fromId: String,
//    val toId: String,
//    val timestamp: Long
//) {
//    constructor() : this("", "", false, "", "", -1)
//
//    override fun equals(other: Any?): Boolean {
//        if (javaClass != other?.javaClass) {
//            return false
//        }
//        other as ChatMessage
//        if (text != other.text || timestamp != other.timestamp || fromId != other.fromId || toId != other.toId) {
//            return false
//        }
//        return true
//    }
//}
class ChatMessage {

    lateinit var id: String
    var text: String = ""
        get() = field
        set(value) {
            field = value
        }
    var image: Boolean = false
    var dailyMessage: Boolean = false
    var fromId: String
    var toId: String
    var timestamp: Long = 0

    constructor() : this("", "", false, false, "", "", -1)
    constructor(
        id: String,
        text: String,
        image: Boolean,
        dailyMessage: Boolean,
        fromId: String,
        toId: String,
        timestamp: Long
    ) {
        this.id = id
        this.text = text
        this.image = image
        this.dailyMessage = dailyMessage
        this.fromId = fromId
        this.toId = toId
        this.timestamp = timestamp
    }

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