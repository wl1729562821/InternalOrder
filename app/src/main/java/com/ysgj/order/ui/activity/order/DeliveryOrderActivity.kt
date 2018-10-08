package com.ysgj.order.ui.activity.order

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.DeliveryOrderResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.OrderViewModel
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.ui.adapter.DeliveryOrderAdapter
import kotlinx.android.synthetic.main.activity_deliveryorder.*

class DeliveryOrderActivity:BaseActivity(){

    private var mViewModel: OrderViewModel?=null
    private var mAdapter:DeliveryOrderAdapter?=null

    override val layoutId: Int
        get() = R.layout.activity_deliveryorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resaleorder_rv?.apply {
            layoutManager=LinearLayoutManager(this@DeliveryOrderActivity)
            mAdapter=DeliveryOrderAdapter(this@DeliveryOrderActivity)
            adapter=mAdapter
        }
        toolbarInit {
            mToolbar = toolbar
            data = ToolbarBean(getString(R.string.heihei_tv23), "", false)
            onBack {
                finish()
            }
        }
        refresh_layout?.setOnRefreshListener {
            request()
        }
        request()
    }

    override fun onError(code: Int, throwable: Throwable) {
        refresh_layout?.isRefreshing=false
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        refresh_layout?.isRefreshing=false
        ((dat as? BaseResponse<*>)?.data as? DeliveryOrderResponse)?.apply {
            Log.e(TAG,"onNext $this")
            mAdapter?.refresh(this.dataSet?: arrayListOf())
        }
    }

    private fun request(){
        refresh_layout.isRefreshing=true
        mViewModel=mViewModel?:getViewModel()
        mViewModel?.getDeliveryOrder()
    }
}