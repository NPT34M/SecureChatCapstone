package com.demo.securechatcapstone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserFriend(val userId: String) : Parcelable {
    constructor() : this("")
}