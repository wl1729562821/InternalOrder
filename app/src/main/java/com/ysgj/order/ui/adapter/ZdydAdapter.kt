package com.ysgj.order.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.qqtheme.framework.picker.DateTimePicker
import cn.qqtheme.framework.picker.SinglePicker
import cn.yc.library.bean.response.UploadResponse
import cn.yc.library.listener.ItemClickListener
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import kotlinx.android.synthetic.main.item_zdyd1.view.*
import kotlinx.android.synthetic.main.item_zdyd2.view.*

class ZdydAdapter(val mContext:BaseActivity,val mListener:ItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var mList= arrayListOf<UploadResponse.DataSetBean.AutoReplyListBean>()

    private var mClick=true

    fun refresh(list:List<UploadResponse.DataSetBean.AutoReplyListBean>,type:Int){
        notifyItemRangeRemoved(0,mList.size)
        mList.clear()
        list.forEach {
            it.type=type
            mList.add(it)
        }
        notifyDataSetChanged()
    }

    fun refresh(mType:Int,id:String){
        if(mList.size<3){
            val size=3-mList.size
            (0 until size).forEach {
                mList.add(UploadResponse.DataSetBean.AutoReplyListBean().apply {
                    houseId=id
                    type=mType
                    isOpen=0
                })
            }
        }else{
            (0 until mList.size).forEach {
                mList[it].type=mType
            }
        }
        Log.e("Adapter","refresh ${mList.size}")
        notifyDataSetChanged()
    }

    fun setEnabled(isEnabled:Boolean){
        mClick=isEnabled
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0->ZdydViewHolderOne(LayoutInflater.from(mContext).inflate(R.layout.item_zdyd1,parent,false))
            else->ZdydViewHolderTwo(LayoutInflater.from(mContext).inflate(R.layout.item_zdyd2,parent,false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mList[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mList[position]
        holder.itemView?.apply {
            isEnabled=mClick
            when(data.type){
                0->{
                    switch_button1.toggle(data.isOpen==1)
                    switch_button1.setOnCheckedChangeListener { view, isChecked ->
                        data.isOpen=if(isChecked){
                            1
                        }else{
                            0
                        }
                        zdsj1.isEnabled= data.isOpen==1
                        mList[position]=data
                    }
                    qzrq1.text=data.beginRentDate
                    qzrq1.setOnClickListener {
                        if(data.isOpen==1){
                            mContext.showTimePicker(DateTimePicker.OnYearMonthDayTimePickListener { year, month, day, hour, minute ->
                                val value = "$year-$month-$day $hour:$minute"
                                data.beginRentDate=value
                                qzrq1.text=data.beginRentDate
                                mList[position]=data
                            })
                        }
                    }
                    sc1.text=data.rentTime.toString()
                    sc1.setOnClickListener {
                        if(data.isOpen==1){
                            mContext.showPicker(arrayListOf<String>().apply {
                                for (i in 1..10) {
                                    add("$i")
                                }
                            }, SinglePicker.OnItemPickListener<String> { index, item ->
                                sc1.text="$item ${mContext.getString(R.string.ci)}"
                                data.rentTime="$item".toInt()
                                mList[position]=data
                            })
                        }
                    }
                    zfjd1.text="${data.payNode}${mContext.getString(R.string.ci)}"
                    zfjd1.setOnClickListener {
                        if(data.isOpen==1){
                            mContext.showPicker(arrayListOf<String>().apply {
                                for (i in 1..10) {
                                    add("$i")
                                }
                            }, SinglePicker.OnItemPickListener<String> { index, item ->
                                data.payNode=item.toInt()
                                zfjd1.text=data.payNode.toString()
                            })
                        }
                    }
                    zdsj1.isEnabled= data.isOpen==1
                    zdsj1.setText(data.houseRentPrice.toString())
                    zdsj1.addTextChangedListener(object :TextWatcher{
                        override fun afterTextChanged(s: Editable?) {

                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            data.houseRentPrice=s.toString()
                        }
                    })
                }
                1->{
                    switch_button.toggle(data.isOpen==1)
                    switch_button.setOnCheckedChangeListener { view, isChecked ->
                        data.isOpen=if(isChecked){
                            xj_img.isEnabled=true
                            dk_img.isEnabled=true
                            yes_img.isEnabled=true
                            no_img.isEnabled=true
                            price.isEnabled=true
                            1
                        }else{
                            xj_img.isEnabled=false
                            dk_img.isEnabled=false
                            yes_img.isEnabled=false
                            no_img.isEnabled=false
                            price.isEnabled=false
                            0
                        }
                        price.isEnabled= data.isOpen==1
                        mList[position]=data
                    }
                    if(data.payType==0){
                        xj_img.setBackgroundResource(R.mipmap.check_selected)
                        dk_img.setBackgroundResource(R.drawable.check_bg)
                        parent3.visibility=View.GONE
                    }else{
                        xj_img.setBackgroundResource(R.drawable.check_bg)
                        dk_img.setBackgroundResource(R.mipmap.check_selected)
                        parent3.visibility=View.VISIBLE
                    }
                    if(data.hasExpectApprove==0){
                        yes_img.setBackgroundResource(R.mipmap.check_selected)
                        no_img.setBackgroundResource(R.drawable.check_bg)
                    }else{
                        no_img.setBackgroundResource(R.mipmap.check_selected)
                        yes_img.setBackgroundResource(R.drawable.check_bg)
                    }
                    xj_img.setOnClickListener {
                        if(data.payType!=0){
                            data.payType=1
                            xj_img.setBackgroundResource(R.mipmap.check_selected)
                            dk_img.setBackgroundResource(R.drawable.check_bg)
                            parent3.visibility=View.GONE
                            mList[position]=data
                        }
                    }
                    dk_img.setOnClickListener {
                        if(data.payType!=1){
                            data.payType=0
                            xj_img.setBackgroundResource(R.drawable.check_bg)
                            dk_img.setBackgroundResource(R.mipmap.check_selected)
                            parent3.visibility=View.VISIBLE
                            mList[position]=data
                        }
                    }
                    yes_img.setOnClickListener {
                        if(data.hasExpectApprove!=0){
                            data.hasExpectApprove=0
                            yes_img.setBackgroundResource(R.mipmap.check_selected)
                            no_img.setBackgroundResource(R.drawable.check_bg)
                            mList[position]=data
                        }
                    }
                    no_img.setOnClickListener {
                        if(data.hasExpectApprove!=1){
                            data.hasExpectApprove=1
                            no_img.setBackgroundResource(R.mipmap.check_selected)
                            yes_img.setBackgroundResource(R.drawable.check_bg)
                            mList[position]=data
                        }
                    }
                    price.isEnabled= data.isOpen==1
                    price.setText(data.houseRentPrice.toString())
                }
            }
        }
    }

    private class ZdydViewHolderOne(view:View):RecyclerView.ViewHolder(view)

    private class ZdydViewHolderTwo(view:View):RecyclerView.ViewHolder(view)

    private class ZdydViewHolderThree(view:View):RecyclerView.ViewHolder(view)

}