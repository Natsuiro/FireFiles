package com.szmy.fireflies.contract

import com.szmy.fireflies.presenter.BasePresenter

interface SplashContract {

    interface Presenter : BasePresenter {
        /**
         * check isLogin
         */
        fun checkLoginStatus()
    }

    interface View{
        /**
         * process with ui
         * when has not login
         */
        fun onNotLoggedIn()

        /**
         * or already login
         */
        fun onLoggedIn()
    }
}