package com.szmy.fireflies.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szmy.fireflies.R
import com.szmy.fireflies.beans.MsgBean
import com.szmy.fireflies.beans.SimpleUserInfo
import com.szmy.fireflies.constant.Utils
import org.jetbrains.anko.find

class MsgListAdapter( private var list:ArrayList<MsgBean>) : RecyclerView.Adapter<MsgListAdapter.MsgListViewHolder>() {

    val context = Utils.getContext()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgListViewHolder {
        val view = View.inflate(context, R.layout.msg_list_item, null)
        return MsgListViewHolder(view)
    }

    fun setData(newList:ArrayList<MsgBean>){
        this.list = newList
    }

    override fun getItemCount(): Int {
        Log.d("msgList",list.size.toString())
        return list.size
    }

    override fun onBindViewHolder(holder: MsgListViewHolder, position: Int) {
        val msgBean = list[position]
        holder.content.text = msgBean.content
        holder.time.text = msgBean.time

        holder.username.text = "${msgBean.userId}"
    }

    inner class MsgListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username = itemView.find<TextView>(R.id.username)
        val time = itemView.find<TextView>(R.id.time)
        val content = itemView.find<TextView>(R.id.content)
    }

}