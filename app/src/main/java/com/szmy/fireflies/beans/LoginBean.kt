package com.szmy.fireflies.beans

data class LoginBean(
    val msg:String,
    val code:Int,
    val user:UserInfo,
    val token:String )
{

    data class UserInfo(
        val userId:Int,
        val userName:String,
        val userSex:Int,
        val userHeadImg:String
    )


}