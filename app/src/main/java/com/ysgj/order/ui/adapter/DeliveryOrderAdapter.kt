package com.ysgj.order.ui.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.yc.library.bean.response.DeliveryOrderResponse
import com.ysgj.order.R
import kotlinx.android.synthetic.main.item_deliveryorder.view.*

class DeliveryOrderAdapter(val mAtv:Activity):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mList= arrayListOf<DeliveryOrderResponse.DataSetBean>()

    private val mLaouy=LayoutInflater.from(mAtv)

    fun refresh(list:List<DeliveryOrderResponse.DataSetBean>){
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeOneViewHolder(mLaouy.inflate(R.layout.item_deliveryorder,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mList[position]
        holder.itemView?.apply {
            deliveryorder_id.text="${mAtv.getString(R.string.home_one_tv2)}${data.poolId}"
            img.setBackgroundResource(if(data.leaseType==0){
                R.mipmap.rent
            }else{
                R.mipmap.sell
            })
            name.text=data.contacts
            address.text=data.appointmentMeetPlace
            time.text=data.estimatedTime
        }
    }

    internal class HomeOneViewHolder(view:View):RecyclerView.ViewHolder(view)

}