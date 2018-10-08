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
import cn.yc.library.bean.response.HomeFourResponse
import cn.yc.library.utils.setBusinessType
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.ui.activity.order.HomeOneDetailsActivity
import kotlinx.android.synthetic.main.item_home_four.view.*

class HomeFourAdapter(val mAtv:Activity):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    internal class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }
    private val mDefaultHandler=DefaultHandler()

    private var mTimeTask= arrayListOf<TimeBean>()
    private var mEndTimeTask= arrayListOf<TimeBean>()

    private val mList= arrayListOf<HomeFourResponse.DataSetBean>().apply {
        /*add(HomeFourResponse.DataSetBean())
        add(HomeFourResponse.DataSetBean())
        add(HomeFourResponse.DataSetBean())*/
    }

    private val mLaouy=LayoutInflater.from(mAtv)

    fun refresh(list:List<HomeFourResponse.DataSetBean>){
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
        list.forEach {
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

    fun loadMore(list:List<HomeFourResponse.DataSetBean>){
        var index=0
        list.forEach {
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

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeOneViewHolder(mLaouy.inflate(R.layout.item_home_four,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mList[position]
        holder.itemView?.apply {
            setOnClickListener {
                (mAtv as BaseActivity).getBaseViewModel<BaseViewModel>()?.setRequestCode(11106)
                mAtv.startActivityForResult(Intent(mAtv,
                        HomeOneDetailsActivity::class.java).apply {
                    data.estimatedTimeCountDown=mTimeTask[position].startTimeInt
                    data.closeTimeCountDown=mEndTimeTask[position].startTimeInt
                    (mAtv as BaseActivity)?.getBaseViewModel<BaseViewModel>()?.setCurrentTimebean(hashMapOf<String,TimeBean>().apply {
                        put("estimatedTimeCountDown",mEndTimeTask[position])
                        put("closeTimeCountDown",mTimeTask[position])
                    })
                    val json= Gson().toJson(data)
                    putExtra("json",json)
                    putExtra("type",0)
                },1033)
            }
            if(mTimeTask[position].textView==null){
                mTimeTask[position].textView=home_one_start
            }
            if(mEndTimeTask[position].textView==null){
                mEndTimeTask[position].textView=home_one_end
            }
            home_one_id.text="${mAtv.getString(R.string.home_one_tv2)}${data.orderCode}"
            home_one_name.text="${data.ownerName}"
            home_one_location_detailed.text="${data.appointmentMeetPlace}"
            home_one_time.text=data.estimatedTime
            img.setBusinessType(data.leaseType)
        }
    }

    internal class HomeOneViewHolder(view:View):RecyclerView.ViewHolder(view)

}