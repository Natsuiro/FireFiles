package com.szmy.fireflies.presenter

import android.util.Log
import com.szmy.fireflies.beans.MsgBean
import com.szmy.fireflies.beans.UserFollowedListBean
import com.szmy.fireflies.beans.UserMsgListBean
import com.szmy.fireflies.constant.MsgApi
import com.szmy.fireflies.constant.UserApi
import com.szmy.fireflies.contract.HomeFragmentContract
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.serverapi.MsgControllerService
import com.szmy.fireflies.serverapi.UserFollowService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class HomeFragmentPresenter(val view: HomeFragmentContract.View) : HomeFragmentContract.Presenter {

    var alreadyLoad = 0
    var userListSize = 0
    override fun pushMsg(content: String, time: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MsgApi.BaseUrl)
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()
        val service = retrofit.create(MsgControllerService::class.java)
        val callPush = service.pushMsg(content, time, "" + Prefs.getUserToken())
        callPush.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                uiThread { view.onHttpRequestFailed("网络异常:" + t.message) }
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val message = response.message()
                val status = response.code()
                if (status == 200) {
                    val responseBody = response.body() as ResponseBody
                    Log.d("push", responseBody.string())
                    uiThread { view.onPushSuccess() }

                } else {
                    uiThread {
                        view.onHttpRequestFailed(message)
                    }
                }
            }
        })
    }

    override fun getFollowedList() {
        view.onStartGetMsgs()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(UserApi.BaseUrl)
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()

        val service = retrofit.create(UserFollowService::class.java)
        val callList = service.getFollowedList("" + Prefs.getUserToken())
        callList.enqueue(object : Callback<UserFollowedListBean> {
            override fun onFailure(call: Call<UserFollowedListBean>, t: Throwable) {
                uiThread { view.onHttpRequestFailed("网络异常:" + t.message) }
            }

            override fun onResponse(
                call: Call<UserFollowedListBean>,
                response: Response<UserFollowedListBean>
            ) {

                val message = response.message()
                val status = response.code()
                Log.d("HomeFragmentPresenter", "$status:$message")
                if (status == 200) {
                    val listBean = response.body() as UserFollowedListBean
                    val code = listBean.code
                    val msg = listBean.msg
                    if (code == 0) {
                        userListSize = listBean.userList.size
                        uiThread { view.onGetListSuccess(listBean.userList) }
                    } else {
                        uiThread { view.onGetListFailed(msg) }
                    }
                } else {
                    uiThread { view.onGetListFailed(message) }
                }


            }

        })
    }

    override fun getUserMsgList(cur:Int, userId: Int, currPage: Int) {
        view.onStartGetMsgs()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MsgApi.BaseUrl)
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()
        val service = retrofit.create(MsgControllerService::class.java)
        val userMsgList = service.getUserMsgList(userId, currPage, "" + Prefs.getUserToken())

        userMsgList.enqueue(object : Callback<UserMsgListBean> {
            override fun onFailure(call: Call<UserMsgListBean>, t: Throwable) {
                uiThread { view.onHttpRequestFailed("网络异常:" + t.message) }
            }

            override fun onResponse(
                call: Call<UserMsgListBean>,
                response: Response<UserMsgListBean>
            ) {

                val status = response.code()
                val message = response.message()
                if (status == 200) {

                    val bean = response.body() as UserMsgListBean
                    val code = bean.code
                    val msg = bean.msg
                    if (code == 0) {
                        uiThread { view.onGetMsgsSuccess(bean, cur) }
                    } else {
                        uiThread { view.onGetMsgsFailed(msg) }
                    }
                } else {
                    uiThread { view.onGetMsgsFailed(message) }
                }

            }

        })


        if (++alreadyLoad == userListSize){

        }

    }

}