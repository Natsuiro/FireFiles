package com.szmy.fireflies.serverapi

import com.szmy.fireflies.beans.LoginBean
import com.szmy.fireflies.constant.UserApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @POST(UserApi.LoginUrl)
    fun loginWithUserName(@Query("name") name:String, @Query("password") password:String):Call<LoginBean>

    @POST(UserApi.LoginUrl)
    fun loginWithPhone(@Query("phone") phone:String, @Query("password") password:String):Call<LoginBean>

    @POST(UserApi.LoginUrl)
    fun loginWithEmail(@Query("email") email:String, @Query("password") password:String):Call<LoginBean>

}