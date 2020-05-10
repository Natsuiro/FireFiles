package com.szmy.fireflies.serverapi

import com.szmy.fireflies.beans.UserFollowedListBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserFollowService {
    @POST("Follow/follow")
    fun follow(@Query("followedId") followedId:Int,
               @Query("time") time:String,
               @Header("token") token:String
               ):Call<ResponseBody>

    @GET("Follow/follow")
    fun getFollowedList(@Header("token") token: String):Call<UserFollowedListBean>

    @POST("Follow/unFollow")
    fun unFollow(@Query("followedId") followedId: Int,@Header("token") token: String):Call<ResponseBody>

}