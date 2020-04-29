package com.szmy.fireflies.ui

import com.szmy.fireflies.R
import com.szmy.fireflies.contract.LoginContract
import com.szmy.fireflies.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity(), LoginContract.View {

    companion object{
        const val INPUT_ID_ERROR = 0
        const val INPUT_PASSWORD_ERROR = 1
    }


    private val presenter = LoginPresenter(this)

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun init() {
        super.init()
        login.setOnClickListener {
            toLogin()
        }
    }

    private fun toLogin() {

        //隐藏软键盘
        hideSoftKeyBoard()
        //拿到用户的输入
        val loginId = username.text.trim().toString()
        val password = password.text.trim().toString()
        presenter.login(loginId,password,getLoginType())
    }

    private fun getLoginType(): Int {
        //根据具体逻辑判断登录的方式
        return LoginPresenter.LOGIN_TYPE_USERNAME
    }

    override fun onLoginStart() {
        //弹出ProgressBar
        showProgressDialog(getString(R.string.logging))
    }
    override fun onInputError(errorCode: Int) {
        toast("errorCode: $errorCode")
    }

    override fun onLoginSuccess() {
        dismissProgress()
        toast(getString(R.string.login_success))
        startActivity<MainActivity>()
        finish()
    }


    override fun onLoginFailed(msg: String) {
        dismissProgress()
        toast(msg)
    }

}