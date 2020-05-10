package com.szmy.fireflies.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szmy.fireflies.R
import com.szmy.fireflies.beans.SimpleUserInfo
import com.szmy.fireflies.beans.UserFollowedListBean
import org.jetbrains.anko.find

class FollowListAdapter(private val context:Context, private var list:ArrayList<SimpleUserInfo>) :RecyclerView.Adapter<FollowListAdapter.FollowListViewHolder>(){

    lateinit var mListener : OnUnFollowClickListener

    fun setDate(newList:ArrayList<SimpleUserInfo>){
        list = newList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowListViewHolder {
        val itemView = View.inflate(context, R.layout.follow_list_item, null)
        return FollowListViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        Log.d("List",""+list.size)
        return list.size
    }

    override fun onBindViewHolder(holder: FollowListViewHolder, position: Int) {
        val info = list[position]
        holder.username.text = info.userName
        holder.sex.text = when(info.userSex){1->"男" else -> "女"}
        holder.unFollow.setOnClickListener {
            mListener.onClick(position,info)
        }
    }

    inner class FollowListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username = itemView.find<TextView>(R.id.username)
        val sex = itemView.find<TextView>(R.id.sex)
        val unFollow = itemView.find<Button>(R.id.unFollow)
    }

    fun setOnUnFollowClickListener(listener:OnUnFollowClickListener){
        mListener = listener
    }

    interface OnUnFollowClickListener{
        fun onClick(position: Int,userInfo: SimpleUserInfo)
    }

}