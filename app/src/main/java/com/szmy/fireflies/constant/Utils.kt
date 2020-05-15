package com.szmy.fireflies.constant

import android.content.Context
import android.media.CamcorderProfile.get
import android.util.Log
import com.szmy.fireflies.app.FFApplication
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private val DAY_OF_WEEK = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    private val MONTH =
        arrayOf("Jan", "Feb", "Mar", "Apr","May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    fun getContext(): Context {
        return FFApplication.getContext()
    }

    fun getNow(): String {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("GMT")
        return DAY_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1] + "," + calendar.get(Calendar.DAY_OF_MONTH)
            .toString() + " " + MONTH[calendar.get(Calendar.MONTH)] + " " + calendar.get(
            Calendar.YEAR
        )
            .toString() + " " + (calendar.get(Calendar.HOUR_OF_DAY)+8)
            .toString() + ":" + calendar.get(Calendar.MINUTE)
            .toString() + ":" + calendar.get(Calendar.SECOND)
            .toString() + " GMT"
    }

    fun getMsgTime(createTime: String): String {

        var formatStr2 = ""
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.US)
        try {
            val time = format.parse(createTime) as Date
            val date = time.toString()
            //将西方形式的日期字符串转换成java.util.Date对象
            val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
            val datetime = sdf.parse(date) as Date
            formatStr2 = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss",Locale.US).format(datetime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return realTime(formatStr2)
    }

    private fun realTime(time:String) :String{

        val instance = Calendar.getInstance()
        val nowYear = instance.get(Calendar.YEAR)
        val nowMonth = instance.get(Calendar.MONTH)
        val nowDay = instance.get(Calendar.DAY_OF_MONTH)

        val year = time.substring(0, 4)
        val month = time.substring(5, 7)
        val day = time.substring(8, 10)

        Log.d("year", "$nowYear:$year")
        return if (nowYear.toString() == year){
            if (nowMonth.toString() == month){
                if (nowDay.toString() == day){
                    time.substring(11 until time.length)
                }else{
                    time.substring(8 until time.length)
                }
            }else{
                time.substring(5 until time.length)
            }
        }else{
            time
        }

    }


    fun getTime(createTime: String): Long {
        var res = 0L
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.US)
        try {
            val time = format.parse(createTime) as Date
            val date = time.toString()
            //将西方形式的日期字符串转换成java.util.Date对象
            val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
            val datetime = sdf.parse(date) as Date
            res = datetime.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return res
    }


}