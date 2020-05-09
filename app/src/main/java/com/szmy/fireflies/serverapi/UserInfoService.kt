package com.szmy.fireflies.serverapi

import com.szmy.fireflies.beans.UserInfoBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserInfoService {
    @GET("Info/full/{userId}")
    fun getUserFullInfo(@Path("userId")userId:Int):Call<UserInfoBean>

}