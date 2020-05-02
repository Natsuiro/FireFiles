package com.szmy.fireflies.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import com.szmy.fireflies.R
import com.szmy.fireflies.contract.RegisterContract
import com.szmy.fireflies.presenter.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.util.*

class RegisterActivity : BaseActivity(), RegisterContract.View {

    private val presenter = RegisterPresenter(this)

    companion object {
        const val INPUT_ID_ERROR = 0
        const val INPUT_PASSWORD_ERROR = 1
        const val INPUT_CONFIRM_ERROR = 2
        const val INPUT_USER_EXIST_ERROR = 3
    }

    private val DAY_OF_WEEK = arrayOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat")
    private val MONTH = arrayOf("Jan","Feb","Mar","Apr","Jun","Jul","Aug","Sep","Oct","Nov","Dec")


    private lateinit var bytes: ByteArray

    override fun getLayoutId(): Int =
        R.layout.activity_register

    override fun init() {
        super.init()

        register.setOnClickListener {
            toCheckUser()
        }

        choose_icon.setOnClickListener {
            chooseIcon()
        }

    }

    private fun toCheckUser() {
        val id = username.text.trim().toString()
        presenter.checkIdExist(id)
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
        val name = username.text.trim().toString()
        val password = password.text.trim().toString()
        val confirm = confirm_password.text.trim().toString()
        val timeStamp = getNow()
        val registerType = getRegisterType()

        val bitmap = (icon.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        bytes = baos.toByteArray()
        presenter.register(name, password, confirm, timeStamp, registerType, bytes)
    }

    private fun getRegisterType(): Int {
        //根据具体逻辑判断注册的方式
        return RegisterPresenter.REGISTER_TYPE_USERNAME
    }

    private fun getNow(): String {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("GMT")
        return DAY_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK)]+ "," + calendar.get(Calendar.DAY_OF_MONTH)
            .toString() + " " + MONTH[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR)
            .toString() + " " + calendar.get(Calendar.HOUR_OF_DAY)
            .toString() + ":" + calendar.get(Calendar.MINUTE)
            .toString() + ":" + calendar.get(Calendar.SECOND)
            .toString() + " GMT"

    }

    override fun onCheckUserStart() {
        showProgressDialog(getString(R.string.on_check))
    }

    override fun onRegisterStart() {
        showProgressDialog(getString(R.string.on_register))
    }

    override fun onInputError(errorCode: Int) {

        when (errorCode) {
            INPUT_USER_EXIST_ERROR -> toast("用户已存在")
            INPUT_ID_ERROR -> toast("用户名不合法")
            INPUT_CONFIRM_ERROR -> toast("密码输入不一致")
            INPUT_PASSWORD_ERROR -> toast("密码不合法")
        }


    }

    override fun onRegisterSuccess() {
        dismissProgress()
        toast(getString(R.string.register_success))
        //finish()
    }

    override fun onRegisterFailed(msg: String) {
        dismissProgress()
        toast(msg)
    }

    override fun onUserExist() {
        dismissProgress()
        toast("用户已存在")
    }

    override fun onUserNotExist() {
        dismissProgress()
        toRegister()
    }

    override fun onStop() {
        super.onStop()
        dismissProgress()
    }
}