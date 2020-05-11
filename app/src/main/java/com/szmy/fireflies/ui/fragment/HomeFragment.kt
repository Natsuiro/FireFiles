package com.szmy.fireflies.ui.fragment

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.szmy.fireflies.R
import com.szmy.fireflies.adapter.MsgListAdapter
import com.szmy.fireflies.beans.MsgBean
import com.szmy.fireflies.beans.SimpleUserInfo
import com.szmy.fireflies.beans.UserMsgListBean
import com.szmy.fireflies.constant.Utils.getNow
import com.szmy.fireflies.contract.HomeFragmentContract
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.presenter.HomeFragmentPresenter
import com.szmy.fireflies.ui.activity.PushMsgActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.toast
import java.util.ArrayList

class HomeFragment : BaseFragment(), HomeFragmentContract.View {
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    val presenter = HomeFragmentPresenter(this)
    var list = ArrayList<MsgBean>()
    var adapter = MsgListAdapter(list)
    override fun init() {
        super.init()
        initRecyclerView()
        getMsgList()
        edit.setOnClickListener {
            val intent = Intent(context, PushMsgActivity::class.java)
            startActivityForResult(intent, 0)
            actionsCollapse()
        }
    }

    private fun getMsgList() {
        getFollowedList()
    }

    private fun getFollowedList() {
        presenter.getFollowedList()
    }

    private fun initRecyclerView() {
        msgList.layoutManager = LinearLayoutManager(context)
        msgList.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val msgContent = data?.getStringExtra("msgContent")
            if (msgContent != null) {
                //发布微博
                presenter.pushMsg(msgContent, getNow())

            }
        }
    }

    private fun actionsCollapse() {
        actions.collapse()
    }

    override fun onStartPushMsg() {
        loading.visibility = View.VISIBLE
    }

    override fun onPushSuccess() {
        loading.visibility = View.GONE
        list.clear()
        getFollowedList()
    }

    override fun onPushFailed(msg: String) {
        loading.visibility = View.GONE
        activity?.toast(msg)
    }

    override fun onHttpRequestFailed(message: String) {
        loading.visibility = View.GONE
        activity?.toast(message)
    }

    override fun onGetListSuccess(userList: ArrayList<SimpleUserInfo>) {
        ///获取id对应的msgList
        activity?.toast("正在加载")
        val isLastUser: Boolean = false
        for (index in 0 until userList.size) {
            presenter.getMsgs(userList[index].userId, 0, isLastUser)
        }
        presenter.getMsgs(Prefs.getUserId(), 0, true)
    }

    override fun onGetListFailed(msg: String) {
        activity?.toast(msg)
    }

    override fun onGetMsgsFailed(message: String) {
        activity?.toast(message)
    }

    override fun onGetMsgsSuccess(bean: UserMsgListBean, lastUser: Boolean) {
        list.addAll(bean.page.list)
        adapter.setData(list)
        adapter.notifyDataSetChanged()
        if (lastUser) loading.visibility = View.GONE

    }

    override fun onStartGetMsgs() {
        loading.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        list.clear()
    }

}