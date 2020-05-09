package com.szmy.fireflies.contract

import com.szmy.fireflies.presenter.BasePresenter

interface RegisterContract {

    interface Presenter : BasePresenter{
        fun register(
            account: String,
            password: String,
            confirmPassword: String,
            timestamp: String,
            bytes: ByteArray
        )
        fun checkIdExist(registerAccount:String)
    }

    interface View {
        fun onCheckUserStart()
        fun onRegisterStart()
        fun onInputError()
        fun onRegisterSuccess()
        fun onRegisterFailed(msg:String)
        fun onAccountExist()
        fun onAccountNotExist()
    }

}