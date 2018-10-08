package com.ysgj.order.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.DeliveryOrderListBean
import cn.yc.library.data.PageData
import cn.yc.library.data.toolbarInit
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.base.BaseFragment
import com.ysgj.order.ui.activity.HomeActivity
import com.ysgj.order.ui.activity.order.ResaleOrderActivity
import com.ysgj.order.ui.adapter.HomeOneAdapter
import kotlinx.android.synthetic.main.fragmnet_home_one.view.*

class HomeOneFragment : BaseFragment() {

    private var mModel: OrderViewModel? = null
    private var mData: DeliveryOrderListBean?=null
    private var mAdapter:HomeOneAdapter?=null
    private val mPage= PageData()

    private var mInit=false

    override val layoutId: Int
        get() = R.layout.fragmnet_home_one

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {
            if(it?.requestCode==1001 || it?.requestCode==10010){
                Log.e(TAG,"更新2=====================================")
                request()
            }
        }
        activity?.also {
            view?.home_one_rv?.apply {
                setRefreshLayout(view.refresh_layout)
                layoutManager=LinearLayoutManager(it)
                mAdapter=HomeOneAdapter(it)
                adapter=mAdapter
                setLoadingListener {
                    mModel = mModel ?: getViewModel()
                    mPage.requestModel=1
                    mModel?.getOutGainDispatchOrder(PageData().apply {
                        pageIndex=mPage.pageIndex+1
                    })
                }
            }
            toolbarInit {
                mToolbar = view?.toolbar
                data = ToolbarBean(getString(R.string.toolbar_title_homeone),
                        getString(R.string.toolbar_homeone_backtitle), true)
                onBack {
                    startActivity(Intent(activity, ResaleOrderActivity::class.java).apply {

                    })
                }
                onSetting {
                    (activity as BaseActivity).phoneTask("phone")
                }
            }
        }
        view?.refresh_layout?.apply {
            isNestedScrollingEnabled = true
            setOnRefreshListener {
                (activity as? HomeActivity)?.apply {
                    if(getRequest()){
                        mPage.pull=true
                        request()
                    }else{
                        view.refresh_layout.isRefreshing=false
                        setRequest(true)
                    }
                }
            }
        }

        (activity as BaseActivity).phoneTask("phone1")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.e(TAG, "onHiddenChanged $hidden")
        if (!hidden && mAdapter?.itemCount?:0<=0 && !mInit) {
            mInit=true
            view?.refresh_layout?.isRefreshing=true
            request()
        }
    }

    private fun request() {
        mAdapter?.refresh(DeliveryOrderListBean())
        mPage.init()
        view?.refresh_layout?.isRefreshing=true
        mModel = mModel ?: getViewModel()
        mModel?.getOutGainDispatchOrder(mPage)
    }

    override fun onError(code: Int, throwable: Throwable) {
        if(mPage.requestModel==1){
            view?.home_one_rv?.finishLoadMore()
        }
        showMessage(throwable.message)
        loadEnd()
    }

    override fun <T> onNext(dat: T) {
        Log.e(TAG,"onNext $dat")
        loadEnd()
        ((dat as? BaseResponse<*>)?.data as? DeliveryOrderListBean)?.apply {
            if(mPage.requestModel==0){
                mData=this
                mAdapter?.refresh(this)
                showMessage(if(mAdapter?.itemCount?:0<=0){
                    R.string.request_empty_success
                }else{
                    R.string.request_success
                })
            }else{
                mAdapter?.loadMore(this)
                mPage.pageIndex++
                view?.home_one_rv?.finishLoadMore()
            }
        }
    }

    private fun loadEnd(){
        view?.apply {
            //home_one_rv?.loadMoreFinish(false, mData?.pageInfo?.isHasNextPage != true)
            view?.refresh_layout?.isRefreshing = false
        }
    }

    override fun clearData() {
        mAdapter?.refresh(DeliveryOrderListBean())
    }
}