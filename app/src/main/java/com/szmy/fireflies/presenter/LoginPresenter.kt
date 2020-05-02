package com.szmy.fireflies.presenter

import android.util.Log
import com.szmy.fireflies.beans.User
import com.szmy.fireflies.contract.LoginContract
import com.szmy.fireflies.extensions.toMD5
import com.szmy.fireflies.constant.WebConstant
import com.szmy.fireflies.model.HttpUtils
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.ui.activity.LoginActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LoginPresenter(val view: LoginContract.View) : LoginContract.Presenter {

    val TAG = "LoginPresenter"
    companion object {
        const val LOGIN_TYPE_USERNAME = 0
        const val LOGIN_TYPE_EMAIL = 1
        const val LOGIN_TYPE_PHONE = 2
    }

    override fun login(loginId: String, password: String, loginType: Int) {

        if (checkLoginId(loginId,loginType)){
            if (checkPassword(password)){
                //通知view开始登录操作了
                view.onLoginStart()
                toLogin(loginId,password)
            }else{
                //密码输入不合法
                view.onInputError(LoginActivity.INPUT_PASSWORD_ERROR)
            }
        }else{
            //用户名输入不合法
            view.onInputError(LoginActivity.INPUT_ID_ERROR)
        }

    }

    private fun toLogin(loginId: String, password: String) {
        //存放post请求时需要使用到的参数，以键值对的形式存放
        val paramsMap = HashMap<String,String>()
        paramsMap["name"] = loginId
        //对密码进行MDD5加密
        paramsMap["password"] = password.toMD5()


        Log.d(TAG,password.toMD5())
        //到model层去请求网络，通过回调监听请求结果
        HttpUtils.post(WebConstant.LoginUrl,paramsMap,object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                //这个错误是由于网络或者服务器错误产生的
                uiThread { view.onLoginFailed("网络异常") }
            }

            override fun onResponse(call: Call, response: Response) {
                /**
                 * json：
                 *  msg
                 *  token
                 *  code
                 */
                val json = response.body()!!.string()
                val jsonObject = JSONObject(json)
                val msg = jsonObject["msg"] as String
                //getStatusCode
                val statusCode = response.code()

                //Log.d(TAG,"$code")
                uiThread {
                    if (statusCode!=200){
                        view.onLoginFailed(msg)
                    }else{
                        val code = jsonObject["code"] as Int

                        if (code==500){
                            view.onLoginFailed("密码错误")
                        }else{
                            val token = jsonObject["token"] as String
                            //登录成功后才会拿到token，之后的请求都要使用到token，因此保存到本地文件
                            Prefs.setUserToken(token)
                            Prefs.setLoginState(true)
                            Prefs.saveUserId(2)

                            view.onLoginSuccess()
                        }
                    }
                }

            }

        })
    }

    private fun checkPassword(password: String): Boolean {
        //检查输入是否合法
        return password.length>=4
    }

    private fun checkLoginId(loginId: String, loginType: Int): Boolean {
        //过滤特殊字符，检查输入是否合法
        return loginId.length>=4
    }

}


