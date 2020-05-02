package com.szmy.fireflies.presenter

import android.util.Log
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.szmy.fireflies.R
import com.szmy.fireflies.constant.GlobalUtils
import com.szmy.fireflies.constant.WebConstant
import com.szmy.fireflies.contract.RegisterContract
import com.szmy.fireflies.extensions.toMD5
import com.szmy.fireflies.model.HttpUtils
import com.szmy.fireflies.ui.activity.RegisterActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.IOException


class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {

    val mContext = GlobalUtils.getContext()

    companion object {
        const val REGISTER_TYPE_USERNAME = 0
        const val REGISTER_TYPE_EMAIL = 1
        const val REGISTER_TYPE_PHONE = 2
    }


    override fun register(
        registerId: String,
        password: String,
        confirmPassword: String,
        timestamp: String,
        registerType: Int,
        bytes: ByteArray
    ) {
        if (checkRegisterId(registerId, registerType)) {
            if (checkPassword(password)) {
                if (checkConfirmPassword(confirmPassword, password)) {
                    toRegister(registerId, password, timestamp, bytes)
                } else {
                    view.onInputError(RegisterActivity.INPUT_CONFIRM_ERROR)
                }
            } else {
                view.onInputError(RegisterActivity.INPUT_PASSWORD_ERROR)
            }
        } else {
            view.onInputError(RegisterActivity.INPUT_ID_ERROR)
        }


    }

    override fun checkIdExist(userId: String) {
        val url = "${WebConstant.CheckIdUrl}?name=$userId"

        HttpUtils.get(url, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //这个错误是由于网络或者服务器错误产生的
                uiThread { view.onRegisterFailed("网络异常") }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()!!.string()
                val jsonObject = JSONObject(json)
                val count = jsonObject.get("count") as Int
                if (count > 0) {
                    uiThread {
                        view.onUserExist()
                    }
                } else {
                    uiThread { view.onUserNotExist() }
                }
            }

        })
    }


    private fun toRegister(
        registerId: String,
        password: String,
        timestamp: String,
        bytes: ByteArray
    ) {
        view.onRegisterStart()
        val paramsMap = HashMap<String, String>()
        paramsMap["name"] = registerId
        paramsMap["password"] = password.toMD5()
        paramsMap["time"] = timestamp
        //根据注册时间来定义头像图片的id
        paramsMap["headImg"] = bytes.toMD5()//?

        doAsync {
            upLoadToOss(paramsMap["headImg"], bytes)
        }

        HttpUtils.post(WebConstant.RegisterUrl, paramsMap, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //这个错误是由于网络或者服务器错误产生的
                uiThread { view.onRegisterFailed("网络异常") }
            }

            override fun onResponse(call: Call, response: Response) {
                /**
                 * json：
                 *  code
                 */
                //getStatusCode
                val code = response.code()

                Log.d("Register", "$code")
                uiThread {
                    if (code != 200) {
                        view.onRegisterFailed(mContext.getString(R.string.register_failed))
                    } else {
                        view.onRegisterSuccess()
                    }
                }
            }

        })

    }

    private fun upLoadToOss(s: String?, bytes: ByteArray) {
        val credentialProvider = OSSAuthCredentialsProvider(WebConstant.StsServer)
        val oss = OSSClient(GlobalUtils.getContext(), WebConstant.endPoint, credentialProvider)
        Log.d("upload", "upload:$s")
        // 构造上传请求。
        val put = PutObjectRequest(WebConstant.Bucket, "head_img/$s.jpg", bytes)
        oss.putObject(put)
    }

    private fun checkConfirmPassword(confirmPassword: String, password: String): Boolean =
        password == confirmPassword


    private fun checkPassword(password: String): Boolean = password.length >= 4


    private fun checkRegisterId(registerId: String, registerType: Int): Boolean {

        return if (registerType == REGISTER_TYPE_USERNAME) {
            registerId.length >= 4
        } else {
            true
        }
    }


}
