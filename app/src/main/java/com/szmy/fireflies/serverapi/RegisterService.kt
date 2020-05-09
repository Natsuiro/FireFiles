package com.szmy.fireflies.serverapi

import com.szmy.fireflies.beans.UserInfoBean
import com.szmy.fireflies.beans.UserCountBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RegisterService {
    @POST("Auth/register")
    fun registerWithUserName(@Query("name") name:String,
                             @Query("password") password:String,
                             @Query("time") time:String,
                             @Query("headImg" ) headImg:String): Call<UserInfoBean>

    @GET("Auth/id")
    fun checkUserIsExisted(@Query("name") name: String):Call<UserCountBean>

}