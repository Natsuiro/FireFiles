package com.szmy.fireflies.factory

import androidx.fragment.app.Fragment
import com.szmy.fireflies.R
import com.szmy.fireflies.ui.fragment.FoundFragment
import com.szmy.fireflies.ui.fragment.HomeFragment
import com.szmy.fireflies.ui.fragment.MeFragment
import com.szmy.fireflies.ui.fragment.MessageFragment

class FragmentFactory private constructor(){

    private val home by lazy {
        HomeFragment()
    }
    private val found by lazy {
        FoundFragment()
    }
    private val message by lazy {
        MessageFragment()
    }
    private val me by lazy {
        MeFragment()
    }

    companion object{
        val instance = FragmentFactory()
    }

    fun getFragment(tabId:Int):Fragment?{

        when(tabId){
            R.id.home -> return home
            R.id.found -> return found
            R.id.message -> return message
            R.id.me -> return me
        }
        return null
    }
}