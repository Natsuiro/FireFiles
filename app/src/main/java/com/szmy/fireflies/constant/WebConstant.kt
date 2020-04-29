package com.szmy.fireflies.constant

class WebConstant {
    companion object{
        private const val BaseUrl = "http://39.97.187.236/service-user/"
        const val LoginUrl = BaseUrl + "Auth/login"
        const val RegisterUrl = BaseUrl + "Auth/register"
    }
}