package com.szmy.fireflies.presenter

import android.util.Log
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.szmy.fireflies.beans.UserInfoBean
import com.szmy.fireflies.beans.UserCountBean
import com.szmy.fireflies.constant.Utils
import com.szmy.fireflies.constant.UserApi
import com.szmy.fireflies.contract.RegisterContract
import com.szmy.fireflies.extensions.toMD5
import com.szmy.fireflies.serverapi.UserAuthService
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {

    val mContext = Utils.getContext()

    private val retrofit = Retrofit.Builder()
        .baseUrl(UserApi.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .callbackExecutor(Executors.newSingleThreadExecutor())
        .build()

    override fun register(
        account: String,
        password: String,
        confirmPassword: String,
        timestamp: String,
        bytes: ByteArray) {

        if (checkAccount(account)) {
            if (checkPassword(password)) {
                if (checkConfirmPassword(confirmPassword, password)) {
                    toRegister(account, password, timestamp, bytes)
                } else {
                    view.onInputError()
                }
            } else {
                view.onInputError()
            }
        } else {
            view.onInputError()
        }
    }


    override fun checkIdExist(registerAccount: String){
        view.onRegisterStart()
        val service = retrofit.create(UserAuthService::class.java)
        val callCheck = service.checkUserIsExisted(registerAccount)

        callCheck.enqueue(object :Callback<UserCountBean>{
            override fun onFailure(call: Call<UserCountBean>, t: Throwable) {
                uiThread { view.onHttpRequestFailed("网络异常:"+t.message) }
            }
            override fun onResponse(call: Call<UserCountBean>, response: retrofit2.Response<UserCountBean>) {
                val status = response.code()
                val message = response.message()
                if (status==200){
                    val userCountBean = response.body() as UserCountBean
                    val code = userCountBean.code
                    val msg = userCountBean.msg
                    //查找成功
                    if (code == 0){
                        //保存登录成功的信息
                        val count = userCountBean.count
                        if (count!=0){
                            uiThread {
                                view.onAccountExist()
                            }
                        }else{
                            uiThread { view.onAccountNotExist() }
                        }
                    }else{//查找失败
                        uiThread { view.onRegisterFailed(msg) }
                    }
                }else{
                    uiThread { view.onHttpRequestFailed(message) }
                }
            }
        })
    }


    private fun toRegister(
        account: String,
        password: String,
        timestamp: String,
        bytes: ByteArray
    ) {
        val headImg = bytes.toMD5()
        doAsync {
            upLoadToOss(headImg, bytes)
        }

        val service = retrofit.create(UserAuthService::class.java)
        val callRegister =
            service.registerWithUserName(account, password.toMD5(), timestamp, headImg)

        callRegister.enqueue(object :Callback<UserInfoBean>{
            override fun onFailure(call: Call<UserInfoBean>, t: Throwable) {
                uiThread { view.onHttpRequestFailed("网络异常:"+t.message) }
            }

            override fun onResponse(
                call: Call<UserInfoBean>,
                response: retrofit2.Response<UserInfoBean>) {

                val status = response.code()
                val message = response.message()
                if (status==200){
                    val registerBean = response.body() as UserInfoBean
                    val code = registerBean.code
                    val msg = registerBean.msg
                    if (code==0){
                        uiThread { view.onRegisterSuccess() }

                    }else{
                        uiThread { view.onRegisterFailed(msg) }
                    }

                }else{
                    uiThread { view.onHttpRequestFailed(message) }
                }
            }

        })
    }

    private fun upLoadToOss(s: String?, bytes: ByteArray) {
        val credentialProvider = OSSAuthCredentialsProvider(UserApi.StsServer)
        val oss = OSSClient(Utils.getContext(), UserApi.endPoint, credentialProvider)
        Log.d("upload", "upload:$s")
        // 构造上传请求。
        val put = PutObjectRequest(UserApi.Bucket, "head_img/$s.jpg", bytes)
        oss.putObject(put)
    }

    private fun checkConfirmPassword(confirmPassword: String, password: String): Boolean =
        password == confirmPassword


    private fun checkPassword(password: String): Boolean{
        //检查输入是否合法
        return password.isNotEmpty()
    }


    private fun checkAccount(account: String): Boolean {
        //过滤特殊字符，检查输入是否合法
        val regex = "^[a-z0-9A-Z]+$"
        return account.length>=4 && account.matches(Regex(regex))
    }


}
