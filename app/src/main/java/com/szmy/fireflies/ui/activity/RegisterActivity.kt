package com.szmy.fireflies.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.szmy.fireflies.R
import com.szmy.fireflies.constant.Utils.getNow
import com.szmy.fireflies.contract.RegisterContract
import com.szmy.fireflies.presenter.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class RegisterActivity : BaseActivity(), RegisterContract.View {

    private val presenter = RegisterPresenter(this)

    //加载布局资源
    override fun getLayoutId(): Int =
        R.layout.activity_register
    //活动的初始化
    override fun init() {
        super.init()
        initRegister()
        initChooseIcon()
    }

    private fun initChooseIcon() {
        choose_icon.setOnClickListener {
            chooseIcon()
        }
    }

    private fun initRegister() {
        register.setOnClickListener {
            hideSoftKeyBoard()
            checkAccountIsExist(getAccountInput())
        }
    }

    private fun checkAccountIsExist(accountInput: String) {
        presenter.checkIdExist(accountInput)
    }


    private fun getAccountInput():String {
        return username.text.trim().toString()
    }

    private fun chooseIcon() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //用户操作完成，结果码返回是-1，即RESULT_OK
        if (resultCode == RESULT_OK) {
            //获取选中文件的定位符
            val uri = data?.data

            Log.d("uri", uri.toString())
            //使用content的接口
            val cr = this.contentResolver
            try {
                //获取图片
                val bitmap = BitmapFactory.decodeStream(uri?.let { cr.openInputStream(it) })

                icon.scaleType = ImageView.ScaleType.CENTER_CROP
                icon.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        } else {
            //操作错误或没有选择图片
            Log.i(TAG, "operation error")
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun toRegister() {
        val bitmap = (icon.drawable as BitmapDrawable).bitmap
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        presenter.register(
            getAccountInput(),
            getPasswordInput(),
            getConfirmInput(),
            getNow(),
            os.toByteArray())
    }

    private fun getConfirmInput(): String {
        return confirm_password.text.trim().toString()
    }

    private fun getPasswordInput():String {
        return password.text.trim().toString()
    }

    override fun onCheckUserStart() {
    }

    override fun onRegisterStart() {
        register.visibility = View.GONE
        register_progress.visibility = View.VISIBLE
    }

    override fun onInputError() {
        register.visibility = View.VISIBLE
        register_progress.visibility = View.GONE
        toast("账号或者密码输入不合法")
    }

    override fun onRegisterSuccess() {
        register.visibility = View.VISIBLE
        register_progress.visibility = View.GONE
        toast(getString(R.string.register_success))
        finish()
    }

    override fun onRegisterFailed(msg: String) {
        register.visibility = View.VISIBLE
        register_progress.visibility = View.GONE
        toast(msg)
    }

    override fun onAccountExist() {
        register.visibility = View.VISIBLE
        register_progress.visibility = View.GONE
        toast("用户已存在")
    }

    override fun onAccountNotExist() {
        toRegister()
    }

    override fun onHttpRequestFailed(msg: String) {
        toast(msg)
    }
}