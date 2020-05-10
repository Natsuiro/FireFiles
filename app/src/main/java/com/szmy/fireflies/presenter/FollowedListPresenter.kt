package com.szmy.fireflies.presenter

import com.szmy.fireflies.beans.UserFollowedListBean
import com.szmy.fireflies.constant.UserApi
import com.szmy.fireflies.contract.FollowedListContract
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.serverapi.UserFollowService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class FollowedListPresenter(val view:FollowedListContract.View) :FollowedListContract.Presenter{

    private val retrofit = Retrofit.Builder()
        .baseUrl(UserApi.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .callbackExecutor(Executors.newSingleThreadExecutor())
        .build()

    override fun getFollowedList() {
        view.startGetData()
        val service = retrofit.create(UserFollowService::class.java)
        val callList = service.getFollowedList("" + Prefs.getUserToken())
        callList.enqueue(object :Callback<UserFollowedListBean>{
            override fun onFailure(call: Call<UserFollowedListBean>, t: Throwable) {
                uiThread { view.onHttpRequestFailed("网络异常:"+t.message) }
            }
            override fun onResponse(
                call: Call<UserFollowedListBean>,
                response: Response<UserFollowedListBean>) {

                val message = response.message()
                val status = response.code()
                if (status==200){
                    val listBean = response.body() as UserFollowedListBean
                    val code = listBean.code
                    val msg = listBean.msg
                    if (code==0){
                        uiThread { view.onGetListSuccess(listBean.userList) }
                    }else{
                        uiThread { view.onGetListFailed(msg) }
                    }
                }else{
                    uiThread { view.onHttpRequestFailed(message)}
                }


            }

        })

    }

    override fun unFollow(userId: Int) {
        val service = retrofit.create(UserFollowService::class.java)
        val callUnFollow = service.unFollow(userId, "" + Prefs.getUserToken())
        callUnFollow.enqueue(object :Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                uiThread { view.onHttpRequestFailed("网络异常:"+t.message) }
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                val status = response.code()
                val message = response.message()
                if (status==200){
                    val responseBody = response.body() as ResponseBody
                    val jsonObject = JSONObject(responseBody.string())
                    val code = jsonObject.getInt("code")
                    val msg = jsonObject.getString("msg")
                    if (code == 0){
                        uiThread { view.onUnFollowSuccess() }
                    }else{
                        uiThread { view.onUnFollowFailed(msg) }
                    }

                }else{
                    uiThread { view.onHttpRequestFailed(message) }
                }

            }

        })
    }

}