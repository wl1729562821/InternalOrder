package com.ysgj.order.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.HomeFourResponse
import cn.yc.library.data.PageData
import cn.yc.library.data.toolbarInit
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.base.BaseFragment
import com.ysgj.order.ui.activity.order.ContractOrderActivity
import com.ysgj.order.ui.activity.order.DeliveryOrderActivity
import com.ysgj.order.ui.adapter.HomeFourAdapter
import kotlinx.android.synthetic.main.fragment_home_four.*
import kotlinx.android.synthetic.main.fragment_home_four.view.*

class HomeFourFragment:BaseFragment(){

    private var mViewModel: OrderViewModel?=null
    private var mAdapter:HomeFourAdapter?=null
    private var mData= HomeFourResponse()

    private val mPageData=PageData()

    override val layoutId: Int
        get() =R.layout.fragment_home_four

    override fun <T> onNext(dat: T) {
        refresh_layout?.isRefreshing=false
        ((dat as? BaseResponse<*>)?.data as? HomeFourResponse)?.apply {
            if(mPageData.requestModel==0){
                mData=this
                mInit=true
                mAdapter?.refresh(this.dataSet?: arrayListOf())
                showMessage(if(mAdapter?.itemCount?:0<=0){
                    R.string.request_empty_success
                }else{
                    R.string.request_success
                })
            }else{
                mAdapter?.loadMore(this.dataSet?: arrayListOf())
                mPageData.pageIndex++
                view?.home_four_rv?.finishLoadMore()
            }
        }
    }

    override fun onError(code: Int, throwable: Throwable) {
        if(mPageData.requestModel==1){
            view?.home_four_rv?.finishLoadMore()
        }
        refresh_layout?.isRefreshing=false
        showMessage(throwable.message)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {
            if(it?.requestCode==1001){
                Log.e(TAG,"更新2=====================================")
                request()
            }
        }
        activity?.also {
            view?.home_four_rv?.apply {
                setRefreshLayout(view.refresh_layout)
                setLoadingListener {
                    mPageData.requestModel=1
                    mViewModel?.getHomeFour()
                }
                mAdapter=HomeFourAdapter(it)
                layoutManager=LinearLayoutManager(it)
                adapter=mAdapter
            }

            toolbarInit {
                mToolbar=view?.toolbar
                data= ToolbarBean(getString(R.string.heihei_tv21),getString(R.string.heihei_tv22),false).apply {
                    settingTitle=getString(R.string.heihei_tv23)
                }
                onSetting {
                    startActivity(Intent(activity, DeliveryOrderActivity::class.java).apply {
                    })
                }

                onBack {
                    startActivity(Intent(activity, ContractOrderActivity::class.java).apply {
                        val json=Gson().toJson(mData)
                        putExtra("json",json)
                    })
                }
            }
        }
        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {

        }
        view.refresh_layout?.setOnRefreshListener {
            request()
        }
        request()
    }

    private fun request(){
        mAdapter?.refresh(arrayListOf())
        mPageData.init()
        view?.refresh_layout?.isRefreshing=true
        mViewModel=mViewModel?:getViewModel()
        mViewModel?.getHomeFour()
    }

    private var mInit=false

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && mAdapter?.itemCount?:0<=0 && !mInit) {
            mInit=true
            view?.refresh_layout?.isRefreshing=true
            request()
        }
    }

    override fun clearData() {
        mAdapter?.refresh(arrayListOf())
    }
}