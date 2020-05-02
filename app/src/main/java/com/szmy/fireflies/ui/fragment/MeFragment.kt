package com.szmy.fireflies.ui.fragment

import android.view.View
import androidx.core.view.GravityCompat
import com.szmy.fireflies.R
import com.szmy.fireflies.contract.MeFragmentContract
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.presenter.MeFragmentPresenter
import com.szmy.fireflies.ui.activity.LoginActivity
import com.szmy.fireflies.ui.activity.SearchUserActivity
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.fragment_me_header.*
import kotlinx.android.synthetic.main.user_simple_info.*
import org.jetbrains.anko.toast

class MeFragment : BaseFragment(), MeFragmentContract.View {
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
            LoginActivity.start()
        }
        search_user.setOnClickListener {
            SearchUserActivity.start()
        }
    }
    override fun onStart() {
        super.onStart()
        initUserInfo()
    }

    private fun initUserInfo() {
        val userId = Prefs.getUserId()
        presenter.getUserInfo(userId, "userName", "msgCount", "fansCount", "followCount")
    }

    override fun getUserInfoSuccess(userInfo: HashMap<String, Any>) {
        progress.visibility = View.GONE
        simple_info.visibility = View.VISIBLE
        val userName = userInfo["userName"]
        val msg = userInfo["msgCount"]
        val fans = userInfo["fansCount"]
        val follow = userInfo["followCount"]

        if (userName != null) {
            userId.text = userName as String
        }
        if (msg != null) {
            msgCount.text = msg.toString()//(msg as Int).toString()
        }
        if (fans != null) {
            fansCount.text = fans.toString()
        }
        if (follow != null) {
            followCount.text = follow.toString()
        }

        brief.text = "暂无简介"
    }

    override fun getUserInfoFailed() {
        progress.visibility = View.GONE
        simple_info.visibility = View.VISIBLE
        context?.toast("加载失败")
    }
    override fun startGetInfo() {
        progress.visibility = View.VISIBLE
        simple_info.visibility = View.GONE
    }


}