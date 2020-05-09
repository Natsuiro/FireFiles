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
    //加载布局id
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }
    //活动的初始化
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

    //判断应用当前是否对指定权限授权
    private fun hasThisPermission(permission:String): Boolean {
        val checkSelfPermission =
            ActivityCompat.checkSelfPermission(this, permission)
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED
    }
    //动态申请未授权的权限
    private fun applyThesePermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this,permissions,0)
    }
    //申请权限的回调方法，处理申请结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED){
                toast(R.string.permission_denied)
                finish()//退出应用
            }
        }
        presenter.checkLoginStatus()
    }
    //未登录，则跳转到登录界面，结束本活动
    override fun onNotLoggedIn() {
        //jump to login
        startActivity<LoginActivity>()
        finish()
    }
    //已经登录，进入应用的主界面，结束本活动
    override fun onLoggedIn() {
        startActivity<MainActivity>()
        finish()
    }

}