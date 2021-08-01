package com.example.e2ee_mvp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserFriend(val userId: String) : Parcelable {
    constructor() : this("")
}