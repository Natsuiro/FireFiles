package com.szmy.fireflies.presenter

import android.os.Handler
import com.szmy.fireflies.contract.SplashContract
import com.szmy.fireflies.model.Prefs

class SplashPresenter(val view : SplashContract.View) :SplashContract.Presenter{

    companion object{
        const val DELAY = 2000L
    }
    //when handler be used at first time,the Handler() behind lazy will be exec
    private val handler by lazy {
        //now i need a handler
        Handler()
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