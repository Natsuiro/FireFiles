package com.szmy.fireflies.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.szmy.fireflies.R
import com.szmy.fireflies.adapter.FansListAdapter
import com.szmy.fireflies.beans.SimpleUserInfo
import com.szmy.fireflies.contract.FansListContract
import com.szmy.fireflies.presenter.FansListPresenter
import kotlinx.android.synthetic.main.activity_fans_list.*
import kotlinx.android.synthetic.main.activity_fans_list.loading
import kotlinx.android.synthetic.main.fans_list_header.*
import org.jetbrains.anko.toast

import java.util.ArrayList

class FansListActivity :BaseActivity(),FansListContract.View{

    val presenter = FansListPresenter(this)

    private var adapter = FansListAdapter(this, ArrayList())

    override fun getLayoutId(): Int {
        return R.layout.activity_fans_list
    }
    override fun init() {
        super.init()
        initRecyclerView()
        getFansList()
        cancel.setOnClickListener { finish() }
    }

    private fun getFansList() {
        presenter.getFansList()
    }

    private fun initRecyclerView() {
        fansList.layoutManager = LinearLayoutManager(this)
        fansList.adapter = adapter
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


}