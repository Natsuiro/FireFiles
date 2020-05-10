package com.szmy.fireflies.contract

import com.szmy.fireflies.beans.SimpleUserInfo
import com.szmy.fireflies.presenter.BasePresenter
import java.util.ArrayList

interface FollowedListContract {

    interface Presenter:BasePresenter{
        fun getFollowedList()
        fun unFollow(userId:Int)
    }

    interface View{
        fun onHttpRequestFailed(msg: String)
        fun onGetListFailed(msg: String)
        fun onGetListSuccess(userList: ArrayList<SimpleUserInfo>)
        fun startGetData()

        fun onUnFollowSuccess()
        fun onUnFollowFailed(msg:String)
    }

}