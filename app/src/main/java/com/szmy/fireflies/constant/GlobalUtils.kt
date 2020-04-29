package com.szmy.fireflies.constant

import android.content.Context
import com.szmy.fireflies.app.FFApplication

object GlobalUtils {
    fun getContext():Context{
        return FFApplication.getContext()
    }
}