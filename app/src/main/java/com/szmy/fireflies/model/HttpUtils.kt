package com.szmy.fireflies.model

import android.text.TextUtils
import android.util.Log
import okhttp3.*


object HttpUtils {

    fun post(
        url: String,
        params: HashMap<String, String>,
        callback: Callback
    ){
        val client = OkHttpClient()
        val builder = FormBody.Builder()
        val keySet = params.keys
        for (key in keySet){
            val value = params[key]
            if (value!=null){
                builder.add(key, value)
                Log.d("post", "$key=$value")
            }
        }

        val build = builder.build()
        val userToken = Prefs.getUserToken()

        Log.d("token",""+userToken)
        if (userToken!=null){
            val request: Request = Request
                .Builder()
                .header("token",userToken)
                .post(build)
                .url(url)
                .build()
            client.newCall(request).enqueue(callback)
        }
    }

    fun get(url: String, callback: Callback){
        val userToken = Prefs.getUserToken()
        if (userToken!=null){
            val client = OkHttpClient()
            val request: Request = Request
                .Builder()
                .header("token",userToken)
                .url(url)
                .build()
            client.newCall(request).enqueue(callback)
        }
    }

}