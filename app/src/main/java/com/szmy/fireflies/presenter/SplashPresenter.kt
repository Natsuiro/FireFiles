package com.szmy.fireflies.presenter

import com.szmy.fireflies.contract.SplashContract
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.presenter.BasePresenter.Companion.handler

class SplashPresenter(val view : SplashContract.View) :SplashContract.Presenter{

    companion object{
        const val DELAY = 2000L
    }

    override fun checkLoginStatus() {
        val loginState = Prefs.getLoginState()

        handler.postDelayed({
            if (loginState){
                view.onLoggedIn()
            }else{
                view.onNotLoggedIn()
            }
        }, DELAY)
    }

}