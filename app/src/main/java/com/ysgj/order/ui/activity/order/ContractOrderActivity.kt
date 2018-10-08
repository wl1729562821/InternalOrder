package com.ysgj.order.ui.activity.order

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.HomeFourResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.ItemClickListener
import cn.yc.library.ui.activity.tran.TransferOrderActivity
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.ui.adapter.ContractOrderAdapter
import kotlinx.android.synthetic.main.activity_contractorder.*

class ContractOrderActivity:BaseActivity(){

    private var taskId:String=""
    private var applyId:String=""
    private var poolId:String=""
    private var mAdapter: ContractOrderAdapter?=null
    private var times=""

    private var mData= HomeFourResponse()

    private var mViewModel: OrderViewModel?=null

    override val layoutId: Int
        get() = R.layout.activity_contractorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel=getViewModel()
        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {
            if(it?.requestCode==1333){
                mAdapter?.refresh(arrayListOf())
                request()
            }
        }
        resaleorder_rv?.apply {
            layoutManager=LinearLayoutManager(this@ContractOrderActivity)
            mAdapter=ContractOrderAdapter(this@ContractOrderActivity)
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
            adapter=mAdapter
        }
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.heihei_tv21),"",true)
            onBack {
                finish()
            }
        }
        val json=intent.getStringExtra("json")
        mData=Gson().fromJson(json,HomeFourResponse::class.java)
        item_resaleorder_check.tag=0
        item_resaleorder_check?.setOnClickListener {
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
                showMessage("Please select at least one item")
                return@setOnClickListener
            }
            applyId=""
            taskId=""
            poolId=""
            times=""
            mAdapter?.getCheckList()?.apply {
                for(i in 0 until size){
                    if(i==0){
                        applyId+="${this[i].houseId}"
                        taskId+="${this[i].taskId}"
                        poolId+="${this[i].poolId}"
                        times+="${this[i].estimatedTime}"
                    }else{
                        applyId+=",${this[i].houseId}"
                        taskId+=",${this[i].taskId}"
                        poolId+=",${this[i].poolId}"
                        times+=",${this[i].estimatedTime}"
                    }
                }
            }
            startActivity(Intent(this,TransferOrderActivity::class.java).apply {
                putExtra("applyId",applyId)
                putExtra("poolId",poolId)
                putExtra("taskId",taskId)
                putExtra("jsonType",1)
                putExtra("json", Gson().toJson(data))
                putExtra("times",times)
                putExtra("taskType","0")
            })
        }
    }

    override fun onError(code: Int, throwable: Throwable) {
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        refresh_layout.isRefreshing=false
        ((dat as? BaseResponse<*>)?.data as? HomeFourResponse)?.apply {
            mData=this
            mAdapter?.refresh(this.dataSet?: arrayListOf())
            showMessage(if(mAdapter?.itemCount?:0<=0){
                R.string.request_empty_success
            }else{
                R.string.request_success
            })
        }
    }

    private fun request(){
        refresh_layout.isRefreshing=true
        mViewModel=mViewModel?:getViewModel()
        mViewModel?.getHomeFour()
    }
}