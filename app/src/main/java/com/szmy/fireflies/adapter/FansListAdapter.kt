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
import org.jetbrains.anko.find

class FansListAdapter(private val context:Context, private var list:ArrayList<SimpleUserInfo>) :RecyclerView.Adapter<FansListAdapter.FansListViewHolder>(){

    fun setDate(newList:ArrayList<SimpleUserInfo>){
        list = newList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FansListViewHolder {
        val itemView = View.inflate(context, R.layout.fans_list_item, null)
        return FansListViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        Log.d("List",""+list.size)
        return list.size
    }
    override fun onBindViewHolder(holder: FansListViewHolder, position: Int) {
        val info = list[position]
        holder.username.text = info.userName
        holder.sex.text = when(info.userSex){1->"男" else -> "女"}
    }
    inner class FansListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username = itemView.find<TextView>(R.id.username)
        val sex = itemView.find<TextView>(R.id.sex)
    }
}