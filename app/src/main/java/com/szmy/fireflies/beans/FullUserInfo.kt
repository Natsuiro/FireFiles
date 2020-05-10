package com.szmy.fireflies.beans

import android.os.Parcel
import android.os.Parcelable

data class FullUserInfo(
    val userId:Int,
    val userPhone:String,
    val userEmail:String,
    val registrationTime:String,
    val userName:String,
    val userSex:Int,
    val msgCount:Int,
    val fansCount:Int,
    val followCount:Int
)

