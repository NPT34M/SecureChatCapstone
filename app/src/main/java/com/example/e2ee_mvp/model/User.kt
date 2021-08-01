package com.example.e2ee_mvp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class User(
    val uid: String,
    val username: String,
    val profileImage: String,
    val friends: @RawValue HashMap<String, UserFriend?>,
    val isOnline: Boolean,
) : Parcelable {
    constructor() : this("", "", "", HashMap(), false)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }
        other as User
        if (username != other.username || profileImage != other.profileImage || isOnline != other.isOnline || friends != other.friends) {
            return false
        }
        return true
    }
}