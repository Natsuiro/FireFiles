package com.szmy.fireflies.presenter

import android.text.TextUtils
import android.util.Log
import com.szmy.fireflies.constant.WebConstant
import com.szmy.fireflies.contract.SearchViewContract
import com.szmy.fireflies.model.HttpUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class SearchPresenter(val view:SearchViewContract.View) :SearchViewContract.Presenter{
    override fun search(userId: String,vararg params:String) {
        val url = WebConstant.FullInfoUrl+userId
        HttpUtils.get(url,object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                uiThread {
                    view.onSearchFailed()
                }

            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string()
                if (!TextUtils.isEmpty(json)){
                    Log.d("json", "the json is$json")
                    val jsonObject = JSONObject(json)
                    val userObject = jsonObject["user"] as JSONObject
                    val hashMap = HashMap<String, Any>()
                    params.forEach {
                        hashMap[it] = userObject[it]
                    }
                    uiThread {
                        view.onSearchSuccess(hashMap)
                    }
                }

            }

        })
    }

    override fun follow(userId: Int,time:String) {
        val hashMap = HashMap<String, String>()
        hashMap["followedId"] = userId.toString()
        hashMap["time"] = time
        HttpUtils.post(WebConstant.FollowUrl,hashMap,object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                uiThread {
                    view.onFollowFailed()
                }
            }

            override fun onResponse(call: Call, response: Response) {

                val json = response.body()?.string()
                if (json!=null&& json.isNotEmpty()){
                    val jsonObject = JSONObject(json)
                    if (jsonObject.has("code")){
                        val code = jsonObject["code"]
                        if (code == 0){
                            uiThread {
                                view.onFollowSuccess()
                            }
                        }else{
                            uiThread {
                                view.onFollowFailed()
                            }
                        }
                    }
                }else{
                    uiThread {
                        view.onFollowFailed()
                    }
                }


            }
        })
    }

}