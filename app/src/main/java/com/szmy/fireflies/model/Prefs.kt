package com.szmy.fireflies.model

import android.content.Context
import android.content.SharedPreferences
import com.szmy.fireflies.beans.User
import com.szmy.fireflies.constant.GlobalUtils

object Prefs {

    private val prefs: SharedPreferences by lazy {
        GlobalUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE)
    }

    fun saveUserId(userId:Int) {
        prefs.edit().putInt("userId",userId).apply()
    }
//    val username:String?,
//    val userPhone:String?,
//    val userEmail:String?,
//    val userId:Int,
//    val registrationTime:String?,
//    val userHeadImg:String?,
//    val msgCount:Int,
//    val fansCount:Int,
//    val followCount:Int

    fun getUserId() :Int{
        return prefs.getInt("userId",-1)
    }


    fun setUserToken(token: String) {
        prefs.edit()
            .putString("token", token)
            .apply()
    }

    fun getUserToken(): String? {
        return prefs.getString("token", null)
    }

    fun setLoginState(isLogin: Boolean) {
        prefs.edit()
            .putBoolean("isLogin", isLogin)
            .apply()
    }

    fun getLoginState(): Boolean {
        return prefs.getBoolean("isLogin", false)
    }

}