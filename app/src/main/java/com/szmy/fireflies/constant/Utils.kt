package com.szmy.fireflies.constant

import android.content.Context
import com.szmy.fireflies.app.FFApplication
import java.util.*

object Utils {
    private val DAY_OF_WEEK = arrayOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat")
    private val MONTH = arrayOf("Jan","Feb","Mar","Apr","Jun","Jul","Aug","Sep","Oct","Nov","Dec")

    fun getContext():Context{
        return FFApplication.getContext()
    }

     fun getNow(): String {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("GMT")
        return DAY_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK)-1]+ "," + calendar.get(Calendar.DAY_OF_MONTH)
            .toString() + " " + MONTH[calendar.get(Calendar.MONTH)-1] + " " + calendar.get(Calendar.YEAR)
            .toString() + " " + calendar.get(Calendar.HOUR_OF_DAY)
            .toString() + ":" + calendar.get(Calendar.MINUTE)
            .toString() + ":" + calendar.get(Calendar.SECOND)
            .toString() + " GMT"
    }
}