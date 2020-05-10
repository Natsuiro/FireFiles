package com.szmy.fireflies.serverapi

import com.szmy.fireflies.beans.LoginBean
import com.szmy.fireflies.beans.UserCountBean
import com.szmy.fireflies.beans.UserInfoBean
import com.szmy.fireflies.constant.UserApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserAuthService {
    @POST(UserApi.LoginUrl)
    fun loginWithUserName(@Query("name") name:String, @Query("password") password:String): Call<LoginBean>

    @POST(UserApi.LoginUrl)
    fun loginWithPhone(@Query("phone") phone:String, @Query("password") password:String): Call<LoginBean>

    @POST(UserApi.LoginUrl)
    fun loginWithEmail(@Query("email") email:String, @Query("password") password:String): Call<LoginBean>


    @POST("Auth/register")
    fun registerWithUserName(@Query("name") name:String,
                             @Query("password") password:String,
                             @Query("time") time:String,
                             @Query("headImg" ) headImg:String): Call<UserInfoBean>

    @GET("Auth/id")
    fun checkUserIsExisted(@Query("name") name: String):Call<UserCountBean>

}