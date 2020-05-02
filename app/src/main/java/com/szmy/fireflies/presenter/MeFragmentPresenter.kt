package com.szmy.fireflies.presenter

import android.text.TextUtils
import android.util.Log
import com.szmy.fireflies.beans.User
import com.szmy.fireflies.constant.WebConstant
import com.szmy.fireflies.contract.MeFragmentContract
import com.szmy.fireflies.model.HttpUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MeFragmentPresenter(val view:MeFragmentContract.View) :MeFragmentContract.Presenter{

    override fun getUserInfo(userId: Int,vararg params:String) {
        val url = WebConstant.FullInfoUrl+userId
        Log.d("url",url)
        view.startGetInfo()
        HttpUtils.get(url,object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                uiThread {
                    view.getUserInfoFailed()
                }
            }
            override fun onResponse(call: Call, response: Response) {

                val json = response.body()?.string()
                if (json!=null){
                    Log.d("json", "the json is$json")
                    val jsonObject = JSONObject(json)
                    val userObject = jsonObject["user"] as JSONObject
                    val hashMap = HashMap<String, Any>()
                    params.forEach {
                        hashMap[it] = userObject[it]
                    }
                    uiThread {
                        view.getUserInfoSuccess(hashMap)
                    }
                }
            }

        })

    }

}