package com.szmy.fireflies.ui.activity

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.szmy.fireflies.R
import com.szmy.fireflies.adapter.FollowListAdapter
import com.szmy.fireflies.beans.SimpleUserInfo
import com.szmy.fireflies.contract.FollowedListContract
import com.szmy.fireflies.presenter.FollowedListPresenter
import kotlinx.android.synthetic.main.activity_followed_list.*
import kotlinx.android.synthetic.main.follow_list_header.*
import org.jetbrains.anko.toast

class FollowedListActivity :BaseActivity(),FollowedListContract.View{
    val presenter = FollowedListPresenter(this)
    private var adapter = FollowListAdapter(this, ArrayList())
    override fun getLayoutId(): Int {
        return R.layout.activity_followed_list
    }
    override fun init() {
        super.init()
        initRecyclerView()
        getFollowedList()
        cancel.setOnClickListener { finish() }
    }

    private fun getFollowedList() {
        presenter.getFollowedList()
    }

    private fun initRecyclerView() {
        Log.d("init","初始化initRecyclerView")
        followList.layoutManager = LinearLayoutManager(this)

        adapter.setOnUnFollowClickListener(object :FollowListAdapter.OnUnFollowClickListener{
            override fun onClick(position: Int, userInfo: SimpleUserInfo) {
                presenter.unFollow(userInfo.userId)
            }
        })

        followList.adapter = adapter
    }

    override fun onHttpRequestFailed(msg: String) {
        loading.visibility = View.GONE
        toast(msg)
    }

    override fun onGetListFailed(msg: String) {
        loading.visibility = View.GONE
        toast(msg)
    }

    override fun onGetListSuccess(userList: ArrayList<SimpleUserInfo>) {
        loading.visibility = View.GONE
        adapter.setDate(userList)
        adapter.notifyDataSetChanged()
    }

    override fun startGetData() {
        loading.visibility = View.VISIBLE
    }

    override fun onUnFollowSuccess() {
        toast("友尽成功")
        getFollowedList()
    }

    override fun onUnFollowFailed(msg: String) {
        toast(msg)
    }

}