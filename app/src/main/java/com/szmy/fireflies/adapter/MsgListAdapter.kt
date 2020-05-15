package com.szmy.fireflies.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szmy.fireflies.R
import com.szmy.fireflies.beans.MsgBean
import com.szmy.fireflies.constant.Utils
import com.szmy.fireflies.constant.Utils.getMsgTime
import com.szmy.fireflies.ui.fragment.HomeFragment
import org.jetbrains.anko.find

class MsgListAdapter(private var list: ArrayList<MsgBean>, private val fragment: HomeFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val context = Utils.getContext()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            val progressBar = ProgressBar(context)
            progressBar.setBackgroundColor(Color.parseColor("#EDE8E8"))
            progressBar.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            return ProgressViewHolder(progressBar)
        }
        val view = View.inflate(context, R.layout.msg_list_item, null)
        return MsgListViewHolder(view)
    }

    fun setData(newList: ArrayList<MsgBean>) {
        this.list = newList
    }

    override fun getItemViewType(position: Int): Int {

        if (position == list.size - 1) {
            return 1
        }
        return 0
    }

    override fun getItemCount(): Int {
        Log.d("msgList", list.size.toString())
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        if (type == 0) {
            val msgListViewHolder = holder as MsgListViewHolder
            val msgBean = list[position]
            msgListViewHolder.content.text = msgBean.content
            msgListViewHolder.time.text = getMsgTime(msgBean.time)
            msgListViewHolder.username.text = fragment.getUserName(msgBean.userId)
        }
    }

    inner class MsgListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.find<TextView>(R.id.username)
        val time = itemView.find<TextView>(R.id.time)
        val content = itemView.find<TextView>(R.id.content)
    }

    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}