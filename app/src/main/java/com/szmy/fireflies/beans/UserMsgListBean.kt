package com.szmy.fireflies.beans

data class UserMsgListBean(
    val msg :String,
    val code:Int,
    val page:PageBean,
    val user:SimpleUserInfo){

    data class PageBean(
        val totalCount:Int,
        val pageSize:Int,
        val totalPage:Int,
        val currPage:Int,
        val list:ArrayList<MsgBean>
    )
}