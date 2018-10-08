package com.ysgj.order.ui.adapter

import android.app.Activity
import android.os.Handler
import android.os.Message
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.yc.library.bean.TimeBean
import cn.yc.library.bean.response.DeliveryOrderListBean
import cn.yc.library.listener.ItemClickListener
import com.ysgj.order.R
import kotlinx.android.synthetic.main.item_resaleorder.view.*

class ResaleOrderAdapter(val mAtv:Activity):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var mList= arrayListOf<DeliveryOrderListBean.DataSetBean>()

    private val mLaouy=LayoutInflater.from(mAtv)

    private val mCheckData= hashMapOf<Int,Boolean>()

    private var mListener: ItemClickListener?=null

    internal class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }
    private val mDefaultHandler=DefaultHandler()

    private var mTimeTask= arrayListOf<TimeBean>()
    private var mEndTimeTask= arrayListOf<TimeBean>()

    fun getCheck():Boolean{
        for(i in 0 until mList.size){
            if(mCheckData[i]==true){
                return true
            }
        }
        return false
    }

    fun getCheckList():ArrayList<DeliveryOrderListBean.DataSetBean>{
        val list= arrayListOf<DeliveryOrderListBean.DataSetBean>()
        for (y in 0 until mList.size) {
            if(mCheckData[y]==true){
                list.add(mList[y])
            }
        }
        return list
    }

    fun setListener(itemClickListener: ItemClickListener){
        mListener=itemClickListener
    }

    fun refresh(data:DeliveryOrderListBean){
        Log.e("Adapter","refresh")
        for(i in 0 until mTimeTask.size){
            mDefaultHandler.removeCallbacks(mEndTimeTask[i].runnable)
        }
        notifyItemRangeRemoved(0,mList.size)
        mList.clear()
        mCheckData.clear()
        mTimeTask.clear()
        mEndTimeTask.clear()
        var index=0
        data.dataSet?.forEach {
            mList.add(it)
            mCheckData[index] = false
            mTimeTask.add(TimeBean().apply {
                val startTime=if(it.estimatedTimeCountDown<0){
                    -it.estimatedTimeCountDown
                }else{
                    it.estimatedTimeCountDown
                }
                position=index
                startTimeInt=startTime
                mHnalder=mDefaultHandler
            })
            mEndTimeTask.add(TimeBean().apply {
                val startTime=if(it.closeTimeCountDown<0){
                    -it.closeTimeCountDown
                }else{
                    it.closeTimeCountDown
                }
                position=index
                startTimeInt=startTime
                mHnalder=mDefaultHandler
            })
            index++
        }
        for (i in 0 until mTimeTask.size) {
            mTimeTask[i].run(i)
            mEndTimeTask[i].run(i)
        }
        notifyDataSetChanged()
    }

    fun setCheck(check:Boolean){
        Log.e("Adapter","setCheck $check")
        for(i in 0 until mList.size){
            mCheckData[i] = check
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResaleOrderViewHolder(mLaouy.inflate(R.layout.item_resaleorder,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mList[position]
        holder.itemView?.apply {
            if(mTimeTask[position].textView==null){
                mTimeTask[position].textView=home_one_start
            }
            if(mEndTimeTask[position].textView==null){
                mEndTimeTask[position].textView=home_one_end
            }
            home_one_id?.text="${mAtv.getString(R.string.home_one_tv2)} ${data.orderCode}"
            home_one_name?.text="${data.memberName}"
            home_one_location?.text="null"
            home_one_location_detailed?.text=if(data.address?.isNotEmpty()==true){
                data.address
            }else{
                "null"
            }
            item_resaleorder_check_img?.setBackgroundResource(if(mCheckData[position]==true){
                R.mipmap.check_selected
            }else{
                R.drawable.check_bg
            })
            item_resaleorder_check?.setOnClickListener {
                Log.e("Activity","onClick ${mCheckData[position]} ${mCheckData.size}")
                if(mCheckData[position]==false){
                    mCheckData[position] = true
                    item_resaleorder_check_img?.setBackgroundResource(R.mipmap.check_selected)
                }else{
                    mCheckData[position] = false
                    item_resaleorder_check_img?.setBackgroundResource(R.drawable.check_bg)
                }
                var check=true
                if(mCheckData.size==mList.size){
                    for(i in 0 until mCheckData.size){
                        Log.e("Activity","onClick2 ${mCheckData[i]}")
                        if(mCheckData[i] != true){
                            check=false
                        }
                    }
                    if(check){
                        mListener?.onClcik(true)
                    }else{
                        mListener?.onClcik(false)
                    }
                    Log.e("Activity","onClick3 $check")
                }
            }
        }
    }

    internal class ResaleOrderViewHolder(view:View):RecyclerView.ViewHolder(view)

}