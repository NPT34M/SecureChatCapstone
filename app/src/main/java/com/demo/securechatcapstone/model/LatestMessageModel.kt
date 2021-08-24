package com.demo.securechatcapstone.model

class LatestMessageModel(
    val id: String,
    val text: String,
    val image: Boolean,
    val daily: Boolean,
    val timestamp: Long,
    val user: User?
) {
    constructor() : this("", "", false, false, -1, User())

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }
        other as LatestMessageModel
        if (text != other.text || timestamp != other.timestamp || user != other.user) {
            return false
        }
        return true
    }
}