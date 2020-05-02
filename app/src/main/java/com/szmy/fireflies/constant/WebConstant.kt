package com.szmy.fireflies.constant

class WebConstant {
    companion object{
        private const val BaseUrl = "http://39.97.187.236/service-user/"
        const val LoginUrl = BaseUrl + "Auth/login"
        const val RegisterUrl = BaseUrl + "Auth/register"
        const val CheckIdUrl = BaseUrl + "Auth/id"
        const val FullInfoUrl = BaseUrl + "Info/full/"
        const val SimpleInfoUrl = BaseUrl + "Info/simple/"
        const val FollowUrl = BaseUrl+"Follow/follow"

        const val endPoint = "http://oss-cn-hangzhou.aliyuncs.com"
        const val Bucket = "fireflies"
        const val StsServer = "http://39.97.187.236:10778/"
    }
}