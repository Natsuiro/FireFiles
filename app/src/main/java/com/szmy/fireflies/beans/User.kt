package com.szmy.fireflies.beans

import android.os.Parcel
import android.os.Parcelable

data class User(val username:String?,
                val userSex:Int,
                val userPhone:String?,
                val userEmail:String?,
                val userId:Int,
                val registrationTime:String?,
                val userHeadImg:String?,
                val msgCount:Int,
                val fansCount:Int,
                val followCount:Int)

