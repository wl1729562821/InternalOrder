package com.ysgj.order.ui.activity.order

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.DeliveryOrderListBean
import cn.yc.library.data.PageData
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.ItemClickListener
import cn.yc.library.ui.activity.messages.MessagesActivity
import cn.yc.library.ui.activity.tran.TransferOrderActivity
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.ui.adapter.ResaleOrderAdapter
import kotlinx.android.synthetic.main.activity_resaleorder.*

class ResaleOrderActivity:BaseActivity(){

    private var mAdapter:ResaleOrderAdapter?=null
    private var taskId:String=""
    private var applyId:String=""
    private var poolId:String=""
    private var times=""

    override val layoutId: Int
        get() = R.layout.activity_resaleorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter=ResaleOrderAdapter(this@ResaleOrderActivity)
        mAdapter?.setListener(object : ItemClickListener {
            override fun <T> onClcik(data: T) {
                val check=data as Boolean
                if(!check){
                    item_resaleorder_check.setBackgroundResource(R.drawable.check_bg)
                    item_resaleorder_check.tag=0
                    //mAdapter?.setCheck(!true)
                }else{
                    item_resaleorder_check.setBackgroundResource(R.mipmap.check_selected)
                    item_resaleorder_check.tag=1
                    //mAdapter?.setCheck(true)
                }
            }
        })
        resaleorder_rv?.apply {
            layoutManager=LinearLayoutManager(this@ResaleOrderActivity)
            adapter=mAdapter
        }
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.toolbar_title_homeone),"",true)
            onBack {
                finish()
            }
            onSetting {
                startActivity(Intent(this@ResaleOrderActivity, MessagesActivity::class.java))
            }
        }
        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {
            if (it?.requestCode == 1333) {
                request()
            }
        }
        refresh_layout?.apply {
            setOnRefreshListener {
                request()
            }
        }
        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {
            if(it?.requestCode==1333){
                mAdapter?.refresh(DeliveryOrderListBean())
                request()
            }
        }
        item_resaleorder_check.tag=0
        check_parent.setOnClickListener {
            if(item_resaleorder_check.tag==0){
                item_resaleorder_check.setBackgroundResource(R.mipmap.check_selected)
                item_resaleorder_check.tag=1
                mAdapter?.setCheck(true)
            }else{
                item_resaleorder_check.setBackgroundResource(R.drawable.check_bg)
                item_resaleorder_check.tag=0
                mAdapter?.setCheck(false)
            }
        }
        startActivity.setOnClickListener {
            if(mAdapter?.getCheck()!=true){
                showMessage(R.string.app_tv15)
                return@setOnClickListener
            }
            applyId=""
            taskId=""
            poolId=""
            times=""
            mAdapter?.getCheckList()?.apply {
                for(i in 0 until size){
                    if(i==0){
                        applyId+="${this[i].applyId}"
                        taskId+="${this[i].taskId}"
                        poolId+="${this[i].poolId}"
                        times+="${this[i].estimatedTime}"
                    }else{
                        applyId+=",${this[i].applyId}"
                        taskId+=",${this[i].taskId}"
                        poolId+=",${this[i].poolId}"
                        times+=",${this[i].estimatedTime}"
                    }
                }
            }
            val data=DeliveryOrderListBean()
            data.dataSet=mAdapter?.getCheckList()?: arrayListOf()
            startActivity(Intent(this, TransferOrderActivity::class.java).apply {
                putExtra("applyId",applyId)
                putExtra("poolId",poolId)
                putExtra("taskId",taskId)
                putExtra("jsonType",1)
                putExtra("json", Gson().toJson(data))
                putExtra("times",times)
                putExtra("taskType","0")
            })
        }
        refresh_layout?.isRefreshing=true
        request()
    }

    private fun request(){
        refresh_layout.isRefreshing=true
        getViewModel<OrderViewModel>()?.apply {
            getOutGainDispatchOrder(PageData())
        }
    }

    override fun onError(code: Int, throwable: Throwable) {
        refresh_layout.isRefreshing=false
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        refresh_layout.isRefreshing=false
        (dat as? BaseResponse<*>)?.run {
            (data as? DeliveryOrderListBean)
        }?.apply {
            Log.e(TAG,"onNext $this")
            mAdapter?.refresh(this)
        }
    }
}