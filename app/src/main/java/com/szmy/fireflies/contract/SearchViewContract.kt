package com.szmy.fireflies.contract

import com.szmy.fireflies.presenter.BasePresenter

interface SearchViewContract {
    interface Presenter:BasePresenter{
        fun search(userId:String,vararg params:String)
        fun follow(userId:Int,time:String)
    }
    interface View{
        fun startSearch()
        fun onSearchSuccess(userInfo:HashMap<String,Any>)
        fun onSearchFailed()
        fun onFollowSuccess()
        fun onFollowFailed()
    }
}