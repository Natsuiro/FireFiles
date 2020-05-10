package com.szmy.fireflies.ui.activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    companion object{
        const val TAG = "BaseActivity"
    }

    private val inputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        init()
        ActivityCollector.addActivity(this)
    }

    /**
     * 默认为空实现
     * 可以被子类覆盖用于实际的init操作
     */
    open fun init() {
        Log.d(TAG,this.toString())
    }

    abstract fun getLayoutId(): Int

    fun hideSoftKeyBoard(){
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

}