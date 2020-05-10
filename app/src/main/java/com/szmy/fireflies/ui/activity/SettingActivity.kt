package com.szmy.fireflies.ui.activity

import com.szmy.fireflies.R
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting_header.*
import org.jetbrains.anko.startActivity

class SettingActivity :BaseActivity(){
    override fun getLayoutId(): Int = R.layout.activity_setting
    override fun init() {
        super.init()
        btn_logout.setOnClickListener {
            ActivityCollector.finishAll()
            startActivity<LoginActivity>()
        }
        back.setOnClickListener {
            finish()
        }
    }


}