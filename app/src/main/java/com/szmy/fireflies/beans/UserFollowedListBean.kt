package com.szmy.fireflies.beans

data class UserFollowedListBean(
    val msg:String,
    val code:Int,
    val userList:ArrayList<SimpleUserInfo>
){

    data class SimpleUserInfo(
        val userId:Int,
        val userName:String,
        val userSex:Int,
        val userHeadImg:String
    )

}