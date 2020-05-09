package com.szmy.fireflies.presenter

import android.util.Log
import com.szmy.fireflies.beans.LoginBean
import com.szmy.fireflies.contract.LoginContract
import com.szmy.fireflies.extensions.toMD5
import com.szmy.fireflies.constant.UserApi
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.serverapi.LoginService
import com.szmy.fireflies.ui.activity.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class LoginPresenter(val view: LoginContract.View) : LoginContract.Presenter {

    private val retrofit:Retrofit = Retrofit.Builder()
        .baseUrl(UserApi.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .callbackExecutor(Executors.newSingleThreadExecutor())
        .build()
    override fun login(loginAccount: String, password: String, loginType: Int) {
        if (checkAccount(loginAccount,loginType)){
            if (checkPassword(password)){
                //通知view开始登录操作了

                uiThread {
                    view.onLoginStart()
                }

                toLogin(loginAccount,password,loginType)
            }else{
                //密码输入不合法
                uiThread {
                    view.onInputError()
                }

            }
        }else{
            //手机号输入不合法
            uiThread {
                view.onInputError()
            }
        }
    }
    private fun toLogin(loginAccount: String, password: String,loginType: Int) {

        Log.d("Login",loginAccount)
        Log.d("Login",password.toMD5())

        //动态代理
        val loginService = retrofit.create(LoginService::class.java)
        val loginCall = getLoginCall(loginAccount,password,loginService,loginType)
        loginCall?.enqueue(object :Callback<LoginBean>{
            override fun onFailure(call: Call<LoginBean>, t: Throwable) {
                uiThread {
                    view.onLoginFailed("网络异常:"+t.message)
                }
            }
            override fun onResponse(call: Call<LoginBean>, response: Response<LoginBean>) {
                val status = response.code()
                val message = response.message()
                if (status==200){
                        val loginBean = response.body() as LoginBean
                        val code = loginBean.code
                        val msg = loginBean.msg
                        //登录成功
                        if (code == 0){
                            //保存登录成功的信息
                            val token = loginBean.token
                            val id = loginBean.user.userId
                            Prefs.setUserToken(token)
                            Prefs.saveUserId(id)
                            Prefs.setLoginState(true)
                            uiThread {
                                view.onLoginSuccess()
                            }
                        }else{//登录失败
                            uiThread { view.onLoginFailed(msg) }
                        }
                }else{
                    uiThread { view.onLoginFailed(message) }
                }
            }
        })
    }
    private fun getLoginCall(Account: String, password: String, service: LoginService, loginType: Int):Call<LoginBean>?{
         return when(loginType){
             LoginActivity.LOGIN_TYPE_USERNAME ->  service.loginWithUserName(Account,password.toMD5())
             LoginActivity.LOGIN_TYPE_PHONE ->  service.loginWithPhone(Account,password.toMD5())
             LoginActivity.LOGIN_TYPE_EMAIL -> service.loginWithEmail(Account,password.toMD5())
             else -> null
        }

    }
    private fun checkAccount(loginAccount: String, loginType: Int): Boolean {
        return when(loginType){
            LoginActivity.LOGIN_TYPE_USERNAME -> checkUserName(loginAccount)
            LoginActivity.LOGIN_TYPE_PHONE -> checkPhone(loginAccount)
            LoginActivity.LOGIN_TYPE_EMAIL -> checkEmail(loginAccount)
            else -> false
        }
    }
    private fun checkEmail(loginAccount: String): Boolean {
        return loginAccount.matches(Regex(pattern = "[1-9a-zA-z_]\\w{0,14}@\\w+\\.(com|net|com.cn)"))
    }
    private fun checkPhone(loginAccount: String): Boolean {
        return loginAccount.matches(Regex(pattern = "^[1][3,4,5,7,8][0-9]{9}$"))
    }
    private fun checkUserName(loginAccount: String): Boolean {
        //过滤特殊字符，检查输入是否合法
        val regex = "^[a-z0-9A-Z]+$"
        return loginAccount.length>=4 && loginAccount.matches(Regex(regex))
    }
    private fun checkPassword(password: String): Boolean {
        //检查输入是否合法
        return password.isNotEmpty()
    }

}


