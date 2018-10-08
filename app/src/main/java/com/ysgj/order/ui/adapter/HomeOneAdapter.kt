package com.ysgj.order.ui.adapter

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.TimeBean
import cn.yc.library.bean.response.DeliveryOrderListBean
import cn.yc.library.utils.setBusinessType
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.ui.activity.order.HomeOneDetailsActivity
import kotlinx.android.synthetic.main.item_home_one.view.*

class HomeOneAdapter(val mAtv:Activity):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mLaouy=LayoutInflater.from(mAtv)

    private val mList= arrayListOf<DeliveryOrderListBean.DataSetBean>()

    private var mBean=DeliveryOrderListBean.DataSetBean()

    internal class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }
    private val mDefaultHandler=DefaultHandler()

    private var mTimeTask= arrayListOf<TimeBean>()
    private var mEndTimeTask= arrayListOf<TimeBean>()

    fun getCurrentBean():DeliveryOrderListBean.DataSetBean{
        return mBean
    }

    fun refresh(data:DeliveryOrderListBean){
        for(i in 0 until mTimeTask.size){
            mDefaultHandler.removeCallbacks(mTimeTask[i].runnable)
            mDefaultHandler.removeCallbacks(mEndTimeTask[i].runnable)
        }
        Log.e("Adapter","refresh")
        notifyItemRangeRemoved(0,mList.size)
        mList.clear()
        mTimeTask.clear()
        mEndTimeTask.clear()
        var index=0
        data.dataSet?.forEach {
            mList.add(it)
            mTimeTask.add(TimeBean().apply {
                val startTime=if(it.estimatedTimeCountDown<0){
                    0
                }else{
                    it.estimatedTimeCountDown
                }
                position=index
                startTimeInt=startTime
                mHnalder=mDefaultHandler
            })
            mEndTimeTask.add(TimeBean().apply {
                val startTime=if(it.closeTimeCountDown<0){
                    0
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

    fun loadMore(data:DeliveryOrderListBean){
        var index=0
        data.dataSet?.forEach {
            mList.add(it)
            mTimeTask.add(TimeBean().apply {
                val startTime=if(it.estimatedTimeCountDown<0){
                    0
                }else{
                    it.estimatedTimeCountDown
                }
                position=index
                startTimeInt=startTime
                mHnalder=mDefaultHandler
            })
            mEndTimeTask.add(TimeBean().apply {
                val startTime=if(it.closeTimeCountDown<0){
                    0
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

    fun addItem(data:DeliveryOrderListBean){
        Log.e("Adapter","addItem")
        if(data.dataSet?.isNotEmpty()==true){
            val start=if(mList.isEmpty()){
                0
            }else{
                mList.size-1
            }
            data.dataSet?.forEach {
                mList.add(it)
            }
            notifyItemRangeInserted(start,mList.size)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeOneViewHolder(mLaouy.inflate(R.layout.item_home_one,parent,false))
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
            home_one_id?.text="${mAtv.getString(R.string.home_one_tv2)}${data.orderCode}"
            home_one_name?.text="${data.memberName}"
            home_one_location?.text="null"
            home_one_time?.text=data.estimatedTime
            home_one_location_detailed?.text=if(data.address?.isNotEmpty()==true){
                data.address
            }else{
                "null"
            }
            home_one_img?.setBusinessType(data.leaseType)
            setOnClickListener {
                (mAtv as BaseActivity).getBaseViewModel<BaseViewModel>()?.setRequestCode(11106)
                mBean=data
                mAtv.startActivityForResult(Intent(mAtv,
                        HomeOneDetailsActivity::class.java).apply {
                    (mAtv as BaseActivity)?.getBaseViewModel<BaseViewModel>()?.setCurrentTimebean(hashMapOf<String,TimeBean>().apply {
                        put("estimatedTimeCountDown",mEndTimeTask[position])
                        put("closeTimeCountDown",mTimeTask[position])
                    })
                    val json= Gson().toJson(data)
                    putExtra("json",json)
                    putExtra("type",1)
                },1033)

            }
        }
    }

    internal class HomeOneViewHolder(view:View):RecyclerView.ViewHolder(view)

}