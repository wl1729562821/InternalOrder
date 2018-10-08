package com.ysgj.order.ui.adapter

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.yc.library.base.BaseActivity
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.TimeBean
import cn.yc.library.bean.response.DeliveryOrderListBean
import cn.yc.library.bean.response.HomeThreeResponse
import cn.yc.library.listener.ItemClickListener
import cn.yc.library.utils.setBusinessType
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.ui.activity.order.GrapOrderActivity
import kotlinx.android.synthetic.main.item_home_three.view.*

class HomeThreeAdapter(val mAtv:Activity,val mListener:ItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mLaouy=LayoutInflater.from(mAtv)
    private val mData= arrayListOf<HomeThreeResponse.DataSetBean>()

    private var mBean= DeliveryOrderListBean.DataSetBean()

    internal class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }
    private val mDefaultHandler=DefaultHandler()

    private var mTimeTask= arrayListOf<TimeBean>()
    private var mEndTimeTask= arrayListOf<TimeBean>()

    private var mCurrentPosition=0

    fun removeCurrent(){
        if(mCurrentPosition>=0 && mCurrentPosition<mTimeTask.size){
            mDefaultHandler.removeCallbacks(mTimeTask[mCurrentPosition].runnable)
            mDefaultHandler.removeCallbacks(mEndTimeTask[mCurrentPosition].runnable)
            notifyItemRemoved(mCurrentPosition)
            mTimeTask.removeAt(mCurrentPosition)
            mEndTimeTask.removeAt(mCurrentPosition)
            mData.removeAt(mCurrentPosition)
        }
    }

    fun refresh(arrayList: List<HomeThreeResponse.DataSetBean>){
        for(i in 0 until mTimeTask.size){
            mDefaultHandler.removeCallbacks(mTimeTask[i].runnable)
            mDefaultHandler.removeCallbacks(mEndTimeTask[i].runnable)
        }
        notifyItemRangeRemoved(0,mData.size)
        mTimeTask.clear()
        mEndTimeTask.clear()
        mData.clear()
        mData.addAll(arrayList)
        var index=0
        mData?.forEach {
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

    fun loadMore(arrayList: List<HomeThreeResponse.DataSetBean>){
        var index=0
        arrayList.forEach {
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
        mData.addAll(arrayList)
        for (i in 0 until mTimeTask.size) {
            mTimeTask[i].run(i)
            mEndTimeTask[i].run(i)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeOneViewHolder(mLaouy.inflate(R.layout.item_home_three,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mData[position]
        holder.itemView?.apply {
            qd.setOnClickListener {
                mCurrentPosition=position
                mListener.onClcik(data)
            }
            home_one_id.text="${mAtv.getString(R.string.home_one_tv2)}${data.orderCode}"
            home_one_name.text="${data.nickname}"
            home_one_location_detailed.text="${data.appointmentMeetPlace}"
            home_one_time.text=data.estimatedTime
            img?.setBusinessType(data.leaseType)
            if(mTimeTask[position].textView==null){
                mTimeTask[position].textView=home_one_start
            }
            if(mEndTimeTask[position].textView==null){
                mEndTimeTask[position].textView=home_one_end
            }
        }
        holder.itemView.setOnClickListener {
            (mAtv as BaseActivity).getBaseViewModel<BaseViewModel>()?.setRequestCode(11106)
            data.estimatedTimeCountDown=mTimeTask[position].startTimeInt
            data.closeTimeCountDown=mEndTimeTask[position].startTimeInt
            (mAtv as com.ysgj.order.base.BaseActivity)?.getBaseViewModel<BaseViewModel>()?.setCurrentTimebean(hashMapOf<String,TimeBean>().apply {
                put("three_estimatedTimeCountDown",mEndTimeTask[position])
                put("three_closeTimeCountDown",mTimeTask[position])
            })
            val json=Gson().toJson(data)
            mAtv.startActivityForResult(Intent(mAtv,GrapOrderActivity::class.java).apply {
                putExtra("json",json)
            },10026)
        }
    }

    internal class HomeOneViewHolder(view:View):RecyclerView.ViewHolder(view)

}