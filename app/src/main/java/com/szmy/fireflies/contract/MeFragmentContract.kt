package com.szmy.fireflies.contract

import com.szmy.fireflies.beans.User
import com.szmy.fireflies.presenter.BasePresenter

interface MeFragmentContract {

    interface Presenter :BasePresenter{
        //获取完整的用户信息
        fun getUserInfo(userId:Int,vararg params:String)

        //fun logout()
    }

    interface View{
        fun getUserInfoSuccess(userInfo:HashMap<String,Any>)
        fun getUserInfoFailed()
        fun startGetInfo()
    }

}