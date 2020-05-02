package com.szmy.fireflies.app

import android.app.Application
import android.content.Context
import com.szmy.fireflies.beans.User

class FFApplication : Application(){

    companion object{

        lateinit var mContext:Context

        fun getContext():Context{
            return mContext
        }


    }


    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
    }



}