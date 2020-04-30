package com.szmy.fireflies.model

import android.content.Context
import android.content.SharedPreferences
import com.szmy.fireflies.constant.GlobalUtils

object Prefs {

    private val prefs: SharedPreferences by lazy {
        GlobalUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE)
    }

    fun setUserToken(token:String){
        prefs.edit()
            .putString("token",token)
            .apply()
    }

    fun getUserToken():String?{
        return prefs.getString("token",null)
    }

    fun setLoginState(isLogin:Boolean){
        prefs.edit()
            .putBoolean("isLogin",isLogin)
            .apply()
    }

    fun getLoginState():Boolean{
        return prefs.getBoolean("isLogin",false)
    }

}