package com.szmy.fireflies.presenter

import com.szmy.fireflies.beans.UserInfoBean
import com.szmy.fireflies.constant.UserApi
import com.szmy.fireflies.contract.MeFragmentContract
import com.szmy.fireflies.serverapi.UserInfoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class MeFragmentPresenter(val view:MeFragmentContract.View) :MeFragmentContract.Presenter{

    private val retrofit = Retrofit.Builder()
        .baseUrl(UserApi.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .callbackExecutor(Executors.newSingleThreadExecutor())
        .build()
    override fun getUserInfo(userId: Int) {

        view.startGetInfo()

        val service = retrofit.create(UserInfoService::class.java)
        val callInfo = service.getUserFullInfo(userId)
        callInfo.enqueue(object : Callback<UserInfoBean> {
            override fun onFailure(call: Call<UserInfoBean>, t: Throwable) {
                uiThread { view.getUserInfoFailed("网络异常:"+t.message) }
            }

            override fun onResponse(call: Call<UserInfoBean>, response: Response<UserInfoBean>) {
                val status = response.code()
                val message = response.message()
                if (status==200){
                    val userInfoBean = response.body() as UserInfoBean
                    val code = userInfoBean.code
                    val msg = userInfoBean.msg
                    if (code==0){
                        uiThread { view.getUserInfoSuccess(userInfoBean.user) }
                    }else{
                        uiThread { view.getUserInfoFailed(msg) }
                    }
                }else{
                    uiThread { view.getUserInfoFailed(message) }
                }
            }
        })
    }

}