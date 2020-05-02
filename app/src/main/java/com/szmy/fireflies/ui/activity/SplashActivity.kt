package com.szmy.fireflies.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.szmy.fireflies.R
import com.szmy.fireflies.contract.SplashContract
import com.szmy.fireflies.presenter.SplashPresenter
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SplashActivity : BaseActivity(),SplashContract.View{



    private val needPermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val permissionNotFound = arrayListOf<String>()


    private val presenter = SplashPresenter(this)

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun init() {
        super.init()



        for (permission in needPermission){
            if (!hasThisPermission(permission)){
                permissionNotFound.add(permission)
            }
        }
        if (permissionNotFound.size == 0){
            presenter.checkLoginStatus()
        }else{

            applyThesePermission(permissionNotFound.toTypedArray())
        }


    }
    private fun hasThisPermission(permission:String): Boolean {
        val checkSelfPermission =
            ActivityCompat.checkSelfPermission(this, permission)
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun applyThesePermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this,permissions,0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED){
                toast(R.string.permission_denied)
                finish()
            }
        }
        presenter.checkLoginStatus()
    }

    override fun onNotLoggedIn() {
        //jump to login
        LoginActivity.start()
        finish()
    }

    override fun onLoggedIn() {
        startActivity<MainActivity>()
        finish()
    }

}