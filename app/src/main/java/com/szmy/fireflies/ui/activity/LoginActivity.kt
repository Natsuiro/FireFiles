package com.szmy.fireflies.ui.activity

import android.view.View
import android.widget.AdapterView
import com.szmy.fireflies.R
import com.szmy.fireflies.contract.LoginContract
import com.szmy.fireflies.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity(), LoginContract.View {

    companion object{
        //登录方式
        const val LOGIN_TYPE_USERNAME = 0
        const val LOGIN_TYPE_EMAIL = 1
        const val LOGIN_TYPE_PHONE = 2
    }

    private val presenter = LoginPresenter(this)
    //加载布局资源
    override fun getLayoutId(): Int = R.layout.activity_login
    //活动的初始化
    override fun init() {
        super.init()
        initLogin()
        initRegister()
        initSpinner()
    }

    private fun initSpinner() {
        loginType.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val content = parent.getItemAtPosition(position).toString()
                loginAccount.hint = content
            }

        }
    }

    private fun initRegister() {
        newUser.setOnClickListener {
            register()
        }
    }

    private fun initLogin() {
        login.setOnClickListener {
            toLogin()
        }
    }

    private fun register() {
        startActivity<RegisterActivity>()
    }

    private fun toLogin() {
        //隐藏软键盘
        hideSoftKeyBoard()
        //拿到用户的输入
        presenter.login(getAccountInput(),getPassword(),getLoginType())
    }

    private fun getPassword() :String{
        return this.password.text.trim().toString()
    }

    private fun getAccountInput():String {
        return this.loginAccount.text.trim().toString()
    }

    private fun getLoginType(): Int {
        //根据具体逻辑判断登录的方式
        return when (loginType.selectedItemPosition) {
            0 -> LOGIN_TYPE_USERNAME
            1 -> LOGIN_TYPE_PHONE
            2 -> LOGIN_TYPE_EMAIL
            else -> -1
        }
    }

    override fun onLoginStart() {
        login.visibility = View.GONE
        login_progress.visibility = View.VISIBLE
    }

    override fun onInputError() {
        toast("密码或者账号输入不合法")
    }

    override fun onLoginSuccess() {
        login.visibility = View.VISIBLE
        login_progress.visibility = View.GONE
        startActivity<MainActivity>()
        finish()
    }

    override fun onLoginFailed(msg: String) {
        login.visibility = View.VISIBLE
        login_progress.visibility = View.GONE
        toast(msg)
    }

}