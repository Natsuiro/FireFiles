package com.szmy.fireflies.contract

import com.szmy.fireflies.beans.SimpleUserInfo
import com.szmy.fireflies.beans.UserMsgListBean
import com.szmy.fireflies.presenter.BasePresenter
import java.util.ArrayList

interface HomeFragmentContract {
    interface Presenter:BasePresenter{
        fun pushMsg(content:String,time:String)
        fun getFollowedList()
        fun getMsgs(userId:Int,currPage:Int,isLastUser:Boolean)
    }

    interface View{
        fun onStartPushMsg()
        fun onPushSuccess()
        fun onPushFailed(msg:String)
        fun onHttpRequestFailed(message: String)
        fun onGetListSuccess(userList: ArrayList<SimpleUserInfo>)
        fun onGetListFailed(msg: String)
        fun onGetMsgsFailed(message: String)
        fun onGetMsgsSuccess(bean: UserMsgListBean, lastUser: Boolean)
        fun onStartGetMsgs()
    }

}