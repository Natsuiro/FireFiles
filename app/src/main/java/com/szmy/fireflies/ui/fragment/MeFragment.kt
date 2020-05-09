package com.szmy.fireflies.ui.fragment

import android.view.View
import androidx.core.view.GravityCompat
import com.szmy.fireflies.R
import com.szmy.fireflies.beans.UserInfo
import com.szmy.fireflies.constant.Utils
import com.szmy.fireflies.contract.MeFragmentContract
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.presenter.MeFragmentPresenter
import com.szmy.fireflies.ui.activity.LoginActivity
import com.szmy.fireflies.ui.activity.SearchUserActivity
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.fragment_me_header.*
import kotlinx.android.synthetic.main.user_simple_info.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MeFragment : BaseFragment(), MeFragmentContract.View {

    private val mContext = Utils.getContext()

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    private val presenter = MeFragmentPresenter(this)

    override fun init() {
        super.init()
        setting.setOnClickListener {
            drawer.openDrawer(GravityCompat.END)
        }

        btn_logout.setOnClickListener {
            activity?.finish()
            Prefs.setLoginState(false)
            mContext.startActivity<LoginActivity>()
        }

        search_user.setOnClickListener {
            mContext.startActivity<SearchUserActivity>()
        }
    }

    override fun onStart() {
        super.onStart()
        initUserInfo()
    }

    private fun initUserInfo() {
        val userId = Prefs.getUserId()
        presenter.getUserInfo(userId)
    }

    override fun getUserInfoSuccess(userInfo: UserInfo) {
        progress.visibility = View.GONE
        simple_info.visibility = View.VISIBLE
        userId.text = userInfo.userName
        msgCount.text = userInfo.msgCount.toString()
        fansCount.text = userInfo.fansCount.toString()
        followCount.text = userInfo.followCount.toString()
        brief.text = "暂无简介"
    }

    override fun getUserInfoFailed(message: String) {
        progress.visibility = View.GONE
        simple_info.visibility = View.VISIBLE
        mContext.toast("加载失败")
    }
    override fun startGetInfo() {
        progress.visibility = View.VISIBLE
        simple_info.visibility = View.GONE
    }


}