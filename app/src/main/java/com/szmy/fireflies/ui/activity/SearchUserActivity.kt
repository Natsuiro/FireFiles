package com.szmy.fireflies.ui.activity

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import com.szmy.fireflies.R
import com.szmy.fireflies.constant.GlobalUtils
import com.szmy.fireflies.contract.SearchViewContract
import com.szmy.fireflies.presenter.SearchPresenter
import kotlinx.android.synthetic.main.activity_search_header.*
import kotlinx.android.synthetic.main.activity_search_user.*
import kotlinx.android.synthetic.main.user_info_item.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*
import java.util.Calendar.DAY_OF_WEEK
import java.util.Calendar.MONTH
import kotlin.collections.HashMap

class SearchUserActivity :BaseActivity(),SearchViewContract.View{

    var userId:Int = -1

    private val DAY_OF_WEEK = arrayOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat")
    private val MONTH = arrayOf("Jan","Feb","Mar","Apr","Jun","Jul","Aug","Sep","Oct","Nov","Dec")


    companion object{
        fun start(){
            GlobalUtils.getContext().startActivity<SearchUserActivity>()
        }
    }
val presenter = SearchPresenter(this)
    override fun getLayoutId(): Int {
        return R.layout.activity_search_user
    }
    override fun init() {
        super.init()


        follow.setOnClickListener {
            if (userId!=-1){
                val now = getNow()
                Log.d("now",now)
                presenter.follow(userId,now)
            }
        }

        user_info_item.visibility = View.GONE
        progress.visibility = View.GONE
        cancel.setOnClickListener {
            finish()
        }
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("queryText",""+query)
                presenter.search(query,"userName","fansCount","userId")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun startSearch() {
        progress.visibility = View.VISIBLE
        user_info_item.visibility = View.GONE
    }

    override fun onSearchSuccess(userInfo: HashMap<String, Any>) {
        user_info_item.visibility = View.VISIBLE
        progress.visibility = View.GONE
        val userName = userInfo["userName"]
        val fans = userInfo["fansCount"]
        userId = userInfo["userId"] as Int
        if (userName != null) {
            username.text = userName as String
        }
        if (fans != null) {
            fansCount.text = fans.toString()
        }
    }

    override fun onSearchFailed() {
        progress.visibility = View.GONE
        toast(getString(R.string.load_fail))
    }

    override fun onFollowSuccess() {
        follow.setBackgroundColor(Color.LTGRAY)
        follow.text = "已关注"
        follow.isEnabled = false
        toast("关注成功~")
    }


    override fun onFollowFailed() {
        toast("关注失败")
    }

    private fun getNow(): String {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("GMT")
        return DAY_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]+ "," + calendar.get(Calendar.DAY_OF_MONTH)
            .toString() + " " + MONTH[calendar.get(Calendar.MONTH) - 1] + " " + calendar.get(Calendar.YEAR)
            .toString() + " " + calendar.get(Calendar.HOUR_OF_DAY)
            .toString() + ":" + calendar.get(Calendar.MINUTE)
            .toString() + ":" + calendar.get(Calendar.SECOND)
            .toString() + " GMT"

    }

}