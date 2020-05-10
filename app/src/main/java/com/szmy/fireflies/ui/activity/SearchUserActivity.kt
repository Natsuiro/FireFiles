package com.szmy.fireflies.ui.activity

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import com.szmy.fireflies.R
import com.szmy.fireflies.beans.UserInfo
import com.szmy.fireflies.constant.Utils.getNow
import com.szmy.fireflies.contract.SearchViewContract
import com.szmy.fireflies.presenter.SearchPresenter
import kotlinx.android.synthetic.main.activity_search_header.*
import kotlinx.android.synthetic.main.activity_search_user.*
import kotlinx.android.synthetic.main.user_info_item.*
import org.jetbrains.anko.toast

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

        cancel.setOnClickListener {
            finish()
        }

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("queryText",""+query)
                presenter.search(query.toInt())
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

    override fun onSearchSuccess(userInfo: UserInfo) {
        user_info_item.visibility = View.VISIBLE
        progress.visibility = View.GONE
        username.text = userInfo.userName
        fansCount.text = userInfo.fansCount.toString()
        userId = userInfo.userId
        checkUserHasFollowed(userId)
    }

    private fun checkUserHasFollowed(userId: Int) {
        presenter.checkUserHasFollowed(userId)
    }

    override fun onSearchFailed(msg: String) {
        progress.visibility = View.GONE
        toast(msg)
    }

    override fun onFollowSuccess() {
        follow.setBackgroundColor(Color.LTGRAY)
        follow.text = "已关注"
        follow.isEnabled = false
        toast("关注成功~")
    }


    override fun onFollowFailed(msg: String) {
        toast(msg)
    }

    override fun onHttpRequestFailed(msg: String) {
        toast(msg)
    }

    override fun onUserCheckFailed(msg: String) {
        toast(msg)
    }

    override fun onUserHasFollowed() {
        follow.setBackgroundColor(Color.LTGRAY)
        follow.text = "已关注"
        follow.isEnabled = false
    }

    override fun onUserHasNotFollowed() {
        follow.setBackgroundColor(resources.getColor(R.color.ff_color_blue))
        follow.text = "关注"
        follow.isEnabled = true
    }
}