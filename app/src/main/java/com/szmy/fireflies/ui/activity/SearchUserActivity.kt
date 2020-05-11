package com.szmy.fireflies.ui.activity

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import com.szmy.fireflies.R
import com.szmy.fireflies.beans.FullUserInfo
import com.szmy.fireflies.constant.Utils.getNow
import com.szmy.fireflies.contract.SearchViewContract
import com.szmy.fireflies.presenter.SearchPresenter
import kotlinx.android.synthetic.main.activity_search_header.*
import kotlinx.android.synthetic.main.activity_search_user.*
import kotlinx.android.synthetic.main.user_info_item.*
import org.jetbrains.anko.toast


class SearchUserActivity :BaseActivity(),SearchViewContract.View{
    var accountId:Int = -1
    val presenter = SearchPresenter(this)
    override fun getLayoutId(): Int {
        return R.layout.activity_search_user
    }

    override fun init() {
        super.init()

        user_info_item.visibility = View.GONE

        initFollow()

        cancel.setOnClickListener {
            finish()
        }

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null){
                    presenter.search(query.toInt())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    private fun initFollow() {
        follow.setOnClickListener {
            if (accountId != -1) {
                val now = getNow()
                Log.d("now", now)
                presenter.follow(accountId, now)
            }
        }
    }

    override fun startSearch() {
        progress.visibility = View.VISIBLE
        user_info_item.visibility = View.GONE
    }

    override fun onSearchSuccess(userInfo: FullUserInfo) {
        user_info_item.visibility = View.VISIBLE
        progress.visibility = View.GONE
        username.text = userInfo.userName
        fansCount.text = userInfo.fansCount.toString()
        accountId = userInfo.userId
        checkUserHasFollowed(accountId)
    }

    private fun checkUserHasFollowed(userId: Int) {
        presenter.checkUserHasFollowed(userId)
    }

    override fun onSearchFailed(msg: String) {
        progress.visibility = View.GONE
        toast(msg)
    }

    override fun onFollowSuccess() {
        following.visibility = View.GONE
        follow.visibility = View.VISIBLE
        follow.setBackgroundColor(Color.LTGRAY)
        follow.text = "已关注"
        follow.isEnabled = false
        toast("关注成功~")
    }


    override fun onFollowFailed(msg: String) {
        following.visibility = View.GONE
        follow.visibility = View.VISIBLE
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

    override fun onStartFollow() {
        following.visibility = View.VISIBLE
        follow.visibility = View.GONE
    }


}