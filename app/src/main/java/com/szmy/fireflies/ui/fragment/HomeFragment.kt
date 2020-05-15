package com.szmy.fireflies.ui.fragment

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szmy.fireflies.R
import com.szmy.fireflies.adapter.MsgListAdapter
import com.szmy.fireflies.beans.MsgBean
import com.szmy.fireflies.beans.SimpleUserInfo
import com.szmy.fireflies.beans.UserMsgListBean
import com.szmy.fireflies.constant.Utils.getNow
import com.szmy.fireflies.constant.Utils.getTime
import com.szmy.fireflies.contract.HomeFragmentContract
import com.szmy.fireflies.model.Prefs
import com.szmy.fireflies.presenter.HomeFragmentPresenter
import com.szmy.fireflies.ui.activity.PushMsgActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomeFragment : BaseFragment(), HomeFragmentContract.View {
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }
    private val targetCount = 10
    private val scrollListener = RecyclerListener()
    private val beanMap = HashMap<Int,UserMsgListBean>()
    val presenter = HomeFragmentPresenter(this)

    private val cache = Cache()
    var isLoading = false
    var realList = ArrayList<MsgBean>()//实际去加载的消息列表
    var userList = ArrayList<SimpleUserInfo>()
    var adapter = MsgListAdapter(cache.cacheList(),this)
    var hasLoad = false
    var noMoreData = 0


    inner class Cache{
        private var cacheList = ArrayList<MsgBean>()//缓存消息列表
        fun getCacheSize():Int = cacheList.size
        fun cacheList(): ArrayList<MsgBean> {
            return cacheList
        }
    }

    fun getUserName(userId:Int):String{
        userList.forEach {
            if (it.userId == userId) return it.userName
        }
        return "我"
    }

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

    //首先获取用户关注的列表，拿到所有关注用户，但实际上当关注数量很多的时候，我们不应该
    // 一次性加载所有用户的消息（message），因为这样会使得内存瞬间大幅度占用，会使程序崩
    // 溃，因此选择 按需加载，一次加载两个用户的消息，但是这样或许并不现实，因为消息实际上
    // 是按照时间顺序展示的。而不是用户顺序，所以第二个方案是 从已经有序的消息list中按需加载
    private fun getFollowedList() {
        presenter.getFollowedList()
    }

    private fun initRecyclerView() {
        msgList.layoutManager = LinearLayoutManager(context)
        msgList.adapter = adapter
        msgList.addOnScrollListener(RecyclerListener())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val msgContent = data?.getStringExtra("msgContent") as String
            presenter.pushMsg(msgContent, getNow())
        }
    }

    private fun actionsCollapse() {
        actions.collapse()
    }

    override fun onStartPushMsg() {
        showLoading()
    }

    override fun onPushSuccess() {
        hideLoading()
        //TODO 刷新列表,因为发送了新的消息，因此要将消息加载进内存
        context?.toast("发送成功")
        //load()
    }

    override fun onPushFailed(msg: String) {
        hideLoading()
        activity?.toast(msg)
    }
    override fun onHttpRequestFailed(message: String) {
        hideLoading()
        activity?.toast(message)
    }

    override fun onGetListSuccess(userList: ArrayList<SimpleUserInfo>) {
        hideLoading()
        this.userList = userList //拿到用户列表
        Log.d("userList_size",""+userList.size)
        loadMore()
    }
    //加载序号为next的用户下一页数据
    private fun loadNext(next:Int){
        val nextPage = beanMap[next]
        //拿到userId
        val myId = if (next==-1) Prefs.getUserId() else  userList[next].userId
        //如果不是第一次加载,通过pageBean可以知道现在是第几页，同时去加载下一页
        if (nextPage!=null){
            val curPage = nextPage.page.currPage
            val totalPage =  nextPage.page.totalPage
            if (curPage < totalPage){
                presenter.getUserMsgList(next,myId,curPage+1)
            }else noMoreData++//这个用户已经加载完了
        }else{//否则第一次加载，从0 page开始
            presenter.getUserMsgList(next,myId,1)
        }
    }
    override fun onGetListFailed(msg: String) {
        activity?.toast(msg)
        hideLoading()
    }

    private fun hideLoading() {
        loadingMore.visibility = View.GONE
    }
    private fun loadMore(){
        //先把缓存的消息使用完，再重新加载新的内容进缓存
        //从当前已经加载的最后的位置开始加载，但是由于有一个问题：加载新的缓存中顺序可能和旧的缓存顺序并不一致，
        //后加的消息可能会出现在前面
        val left = realList.size
        val target = left + targetCount
        val cacheSize = cache.getCacheSize()
        //目标位置比最大位置大，进行调整
        val right = if (target>cacheSize) cacheSize else target
        val cacheList = cache.cacheList()
        //还没有将缓存数据加载完
        Log.d("left:right","$left+$right")
        if (left < right){
            realList.addAll(cacheList.subList(left,right))
            //更新realList之后 进行数据刷新
            // 缓存中的数据是无序的,加入到realList中才排序
            // Collections.sort(realList,ListComparator())
            adapter.setData(realList)
            adapter.notifyDataSetChanged()
        }else{
            //缓存的数据已经使用完了，需要下载新的数据，加载用户的更多消息到缓存列表中
            Log.d("loadNext","加载更多用户消息")
            loadCache()
        }
    }
    private fun loadCache() {
        hasLoad = false
        Log.d("userList.size",""+userList.size)
        noMoreData = 0
        for (i in -1 until userList.size){
            loadNext(i)
        }
        if (noMoreData == userList.size + 1) {
            context?.toast("没有更多数据")
            msgList.scrollToPosition(realList.size-2)
        }
    }

    private fun showLoading() {
        if (realList.isEmpty())
            loadingMore.visibility = View.VISIBLE
    }
    override fun onGetMsgsFailed(message: String) {
        activity?.toast(message)
        hideLoading()
    }
    //通过分页获取cur用户消息列表成功 ，只要这个方法被回调，就说明还有新消息
    override fun  onGetMsgsSuccess(bean: UserMsgListBean, cur: Int) {
        hideLoading()
        beanMap[cur] = bean //update bean --> update curPage
        Log.d("cur id",""+cur)
        cache.cacheList().addAll(bean.page.list)//将消息列表添加到缓存列表中，而不是真正上加载的列表
        if(!hasLoad){
            hasLoad = true
            load()
        }
    }
    override fun onStartGetMsgs() {
        showLoading()
    }


    override fun onDestroyView() {
        msgList.removeOnScrollListener(scrollListener)
        realList.clear()
        super.onDestroyView()
    }

    inner class ListComparator:Comparator<MsgBean>{
        override fun compare(a: MsgBean, b: MsgBean): Int {
            val timeA = getTime(a.time)
            val timeB = getTime(b.time)
            val f = timeA-timeB
            if (f>0) return -1
            else if (f < 0) return 1
            return 0
        }
    }
    inner class RecyclerListener : RecyclerView.OnScrollListener() {
        //用来标记是否正在向最后一个滑动
        var isSlidingToLast = false
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val manager =
                recyclerView.layoutManager as LinearLayoutManager
            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //获取最后一个完全显示的ItemPosition
                val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = manager.itemCount
                // 判断是否滚动到底部，并且是向右滚动
                if (lastVisibleItem == totalItemCount - 1 && isSlidingToLast) {
                    //加载更多功能的代码
                    load()
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
            isSlidingToLast = dy > 0
        }
    }

    private fun load() {
        if (!isLoading) {
            isLoading = true
            delay({
                loadMore()
                isLoading = false
            }, 500)
        }
    }
}