package com.szmy.fireflies.presenter

import android.util.Log
import com.szmy.fireflies.beans.UserFollowedListBean
import com.szmy.fireflies.beans.UserInfoBean
import com.szmy.fireflies.constant.UserApi
import com.szmy.fireflies.contract.SearchViewContract
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.serverapi.UserFollowService
import com.szmy.fireflies.serverapi.UserInfoService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class SearchPresenter(val view:SearchViewContract.View) :SearchViewContract.Presenter{

    private val retrofit = Retrofit.Builder()
        .baseUrl(UserApi.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .callbackExecutor(Executors.newSingleThreadExecutor())
        .build()

    override fun search(userId: Int) {

        view.startSearch()

        val service = retrofit.create(UserInfoService::class.java)
        val callSearch = service.getUserFullInfo(userId, "" + Prefs.getUserToken())
        callSearch.enqueue(object : Callback<UserInfoBean>{
            override fun onFailure(call: Call<UserInfoBean>, t: Throwable) {
                uiThread { view.onHttpRequestFailed("网络异常:"+t.message) }
            }
            override fun onResponse(
                call: Call<UserInfoBean>,
                response: Response<UserInfoBean>) {
                val status = response.code()
                val message = response.message()
                if (status==200){
                    val userInfoBean = response.body() as UserInfoBean
                    val code = userInfoBean.code
                    val msg = userInfoBean.msg
                    if (code==0){
                        uiThread { view.onSearchSuccess(userInfoBean.user) }
                    }else{
                        uiThread { view.onSearchFailed(msg) }
                    }

                }else{
                    uiThread { view.onHttpRequestFailed(message) }
                }

            }

        })
    }

    override fun follow(userId: Int,time:String) {
        val service = retrofit.create(UserFollowService::class.java)
        val callFollow = service.follow(userId, time, "" + Prefs.getUserToken())
        callFollow.enqueue(object :Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                uiThread { view.onFollowFailed("网络异常:"+t.message) }
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>) {

                val status = response.code()
                val message = response.message()
                val url = call.request().url()
                val header = call.request().header("token")
                Log.d("Follow", "$url...$header")
                if (status==200){
                    val responseBody = response.body() as ResponseBody
                    val jsonObject = JSONObject(responseBody.string())
                    val code = jsonObject.getInt("code")
                    val msg = jsonObject.getString("msg")
                    if(code==0){
                        uiThread { view.onFollowSuccess() }
                    }else{
                        uiThread { view.onFollowFailed(msg) }
                    }
                }else{
                    uiThread { view.onFollowFailed(message) }
                }
            }
        })
    }

    override fun checkUserHasFollowed(userId: Int) {
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
                        var hasFollowed = false
                        val userList = listBean.userList
                        for (user in userList){
                            if (user.userId == userId){
                                hasFollowed = true
                            }
                        }
                        if (hasFollowed){
                            uiThread { view.onUserHasFollowed() }
                        }else{
                            uiThread { view.onUserHasNotFollowed() }
                        }
                    }else{
                        uiThread { view.onUserCheckFailed(msg) }
                    }
                }else{
                    uiThread { view.onHttpRequestFailed(message) }
                }
            }
        })
    }

}