package com.szmy.fireflies.serverapi

import com.szmy.fireflies.beans.UserMsgListBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MsgControllerService {
    @POST("Msg/push")
    fun pushMsg(@Query("content") content:String,
                @Query("time") time:String,
                @Header("token") token:String
                ):Call<ResponseBody>
    @GET("Msg/get/{userId}/{currentPage}")
    fun getUserMsgList(@Path("userId") userId:Int,
                       @Path("currentPage") currentPage:Int,
                       @Header("token") token: String
                       ):Call<UserMsgListBean>

}