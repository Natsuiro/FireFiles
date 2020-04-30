package com.szmy.fireflies.contract

import com.szmy.fireflies.presenter.BasePresenter

interface RegisterContract {

    interface Presenter : BasePresenter{
        fun register(
            registerId: String,
            password: String,
            confirmPassword: String,
            timestamp: String,
            registerType: Int,
            bytes: ByteArray
        )
        fun checkIdExist(userId:String)
    }

    interface View {
        fun onCheckUserStart()
        fun onRegisterStart()
        fun onInputError(errorCode:Int)
        fun onRegisterSuccess()
        fun onRegisterFailed(msg:String)
        fun onUserExist()
        fun onUserNotExist()
    }

}