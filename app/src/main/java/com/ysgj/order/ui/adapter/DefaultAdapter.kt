package com.ysgj.order.ui.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ysgj.order.R

class DefaultAdapter(private val mAtv: Activity, private val mData:ArrayList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var mSize=4

    fun add(){
        if(mSize>0){
            (mData.size..(mData.size+9)).forEach {
                mData.add("$it")
            }
        }else{
            (mData.size..(mData.size+4)).forEach {
                mData.add("$it")
            }
        }
        mSize--
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //(holder as? DefaultViewHolder)?.itemView?.test_tv?.text=mData[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DefaultViewHolder(LayoutInflater.from(mAtv)
                .inflate(R.layout.item_test,parent,false))
    }

    inner class DefaultViewHolder(view: View): RecyclerView.ViewHolder(view)
}