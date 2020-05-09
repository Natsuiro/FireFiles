package com.szmy.fireflies.ui.activity

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.szmy.fireflies.R
import com.szmy.fireflies.factory.FragmentFactory
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.lang.RuntimeException


class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        super.init()
        initBottomNavigation()
        setCurrentFragment(R.id.home)
    }
    private fun initBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            setCurrentFragment(item.itemId)
            true
        }
    }

    private fun setCurrentFragment(fragmentId:Int){
        val fragment = FragmentFactory.instance.getFragment(fragmentId)
        if (fragment!=null){
            val beginTransaction = supportFragmentManager.beginTransaction()
            beginTransaction.replace(R.id.content,fragment)
            beginTransaction.commit()
        }else{
            throw RuntimeException()
        }
    }

}
