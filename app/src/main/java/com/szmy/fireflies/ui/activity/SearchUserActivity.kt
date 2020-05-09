package com.szmy.fireflies.ui.activity

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import com.szmy.fireflies.R
import com.szmy.fireflies.constant.Utils.getNow
import com.szmy.fireflies.contract.SearchViewContract
import com.szmy.fireflies.presenter.SearchPresenter
import kotlinx.android.synthetic.main.activity_search_header.*
import kotlinx.android.synthetic.main.activity_search_user.*
import kotlinx.android.synthetic.main.user_info_item.*
import org.jetbrains.anko.toast
import kotlin.collections.HashMap

class SearchUserActivity :BaseActivity(),SearchViewContract.View{
    var userId:Int = -1
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
}