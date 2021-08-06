package com.demo.securechatcapstone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val uid: String,
    val username: String,
    val profileImage: String,
    val phoneNumber: String,
    val deviceInfo: String,
    val pubKey: String,
    val isOnline: Boolean,
) : Parcelable {
    constructor() : this("", "", "", "", "", "", false)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }
        other as User
        if (username != other.username || profileImage != other.profileImage || isOnline != other.isOnline) {
            return false
        }
        return true
    }
}