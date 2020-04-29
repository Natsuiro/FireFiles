package com.szmy.fireflies.model

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
            }
        }
        val build = builder.build()
        val request: Request = Request
            .Builder()
            .post(build)
            .url(url)
            .build()
        client.newCall(request).enqueue(callback)
    }

}