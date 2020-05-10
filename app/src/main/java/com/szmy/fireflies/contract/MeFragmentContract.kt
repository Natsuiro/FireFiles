package com.szmy.fireflies.contract

import com.szmy.fireflies.beans.FullUserInfo
import com.szmy.fireflies.presenter.BasePresenter

interface MeFragmentContract {

    interface Presenter :BasePresenter{
        //获取完整的用户信息
        fun getUserInfo(userId:Int)

        //fun logout()
    }

    interface View{
        fun getUserInfoSuccess(userInfo: FullUserInfo)
        fun getUserInfoFailed(message: String)
        fun startGetInfo()
    }

}