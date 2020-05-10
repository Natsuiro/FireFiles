package com.szmy.fireflies.contract
import com.szmy.fireflies.presenter.BasePresenter

interface LoginContract {

    interface Presenter :BasePresenter{

        /**
         * 登录方式可以使用户名登录，邮箱登录，手机号登录
         * 目前只支持用户名登录
         */
        fun login(loginAccount:String,password:String,loginType:Int)
    }

    interface View {
        fun onLoginStart()
        /**
         * 根据错误码来确定具体的错误类型
         */
        fun onInputError()
        fun onLoginSuccess()
        fun onLoginFailed(msg:String)
        fun onHttpRequestFailed(msg:String)
    }

}