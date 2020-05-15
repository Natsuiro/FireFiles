package com.szmy.fireflies.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {


    companion object{
        val handler by lazy {
            Handler(Looper.getMainLooper())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    abstract fun getLayoutId(): Int

    open fun init() {
        Log.d("fragment",this.toString())
    }

    fun delay(r: () -> Unit, time:Long){
        handler.postDelayed(r,time)
    }


}