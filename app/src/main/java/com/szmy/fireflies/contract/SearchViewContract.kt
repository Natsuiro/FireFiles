package com.szmy.fireflies.contract

import com.szmy.fireflies.beans.FullUserInfo
import com.szmy.fireflies.presenter.BasePresenter

interface SearchViewContract {
    interface Presenter:BasePresenter{
        fun search(accountId:Int)
        fun follow(accountId:Int,time:String)
        fun checkUserHasFollowed(accountId: Int)
    }
    interface View{
        fun startSearch()
        fun onSearchSuccess(userInfo: FullUserInfo)
        fun onSearchFailed(msg: String)
        fun onFollowSuccess()
        fun onFollowFailed(msg: String)
        fun onHttpRequestFailed(msg: String)
        fun onUserCheckFailed(msg: String)
        fun onUserHasFollowed()
        fun onUserHasNotFollowed()
        fun onStartFollow()
    }
}