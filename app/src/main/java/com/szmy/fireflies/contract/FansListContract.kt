package com.szmy.fireflies.contract

import com.szmy.fireflies.beans.SimpleUserInfo
import com.szmy.fireflies.presenter.BasePresenter
import java.util.ArrayList

interface FansListContract {

    interface Presenter:BasePresenter{
        fun getFansList()
    }

    interface View{
        fun onHttpRequestFailed(msg: String)
        fun onGetListFailed(msg: String)
        fun onGetListSuccess(userList: ArrayList<SimpleUserInfo>)
        fun startGetData()
    }

}