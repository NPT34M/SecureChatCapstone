package com.example.e2ee_mvp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val profileImage: String) : Parcelable {
    constructor() : this("", "", "")

    override fun equals(other: Any?): Boolean {
        if(javaClass!=other?.javaClass){
            return false
        }
        other as User
        if(username!=other.username||profileImage!=other.profileImage){
            return false
        }
        return true
    }
}