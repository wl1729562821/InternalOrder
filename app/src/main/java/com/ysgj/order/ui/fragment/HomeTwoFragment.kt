package com.ysgj.order.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.HomeTwoResponse
import cn.yc.library.data.PageData
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.base.BaseFragment
import com.ysgj.order.ui.adapter.HomeTwoAdapter
import kotlinx.android.synthetic.main.fragmnet_home_two.view.*

class HomeTwoFragment:BaseFragment(){

    private var mViewModel: OrderViewModel?=null
    private var mAdapter:HomeTwoAdapter?=null
    private var mInit=false

    private val mPageData=PageData()

    override val layoutId: Int
        get() = R.layout.fragmnet_home_two

    override fun onError(code: Int, throwable: Throwable) {
        if(mPageData.requestModel==1){
            view?.home_two_rv?.finishLoadMore()
        }
        view?.refresh_layout?.isRefreshing=false
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        view?.refresh_layout?.isRefreshing=false
        ((dat as? BaseResponse<*>)?.data as? HomeTwoResponse)?.apply {
            Log.e(TAG,"onNext $this")
            if(mPageData.requestModel==0){
                mInit=true
                mAdapter?.refresh(arrayListOf<HomeTwoResponse.DataSetBean>().apply {
                    dataSet?.forEach {
                        add(it)
                    }
                })
                //mAdapter?.refresh(this.dataSet?: arrayListOf())
                showMessage(if(mAdapter?.itemCount?:0<=0){
                    R.string.request_empty_success
                }else{
                    R.string.request_success
                })
            }else{
                mAdapter?.loadMore(arrayListOf<HomeTwoResponse.DataSetBean>().apply {
                    dataSet?.forEach {
                        add(it)
                    }
                })
                mPageData.pageIndex++
                view?.home_two_rv?.finishLoadMore()
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel=mViewModel?:getViewModel()
        activity?.apply {
            mDialog= PromptDialog(this)
        }
        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {
            if(it?.requestCode==1001 || it?.requestCode==1002){
                request()
            }
        }
        activity?.also {
            view?.home_two_rv?.apply {
                setRefreshLayout(view.refresh_layout)
                layoutManager=LinearLayoutManager(it)
                mAdapter=HomeTwoAdapter(it)
                adapter=mAdapter
                setLoadingListener {
                    mPageData.requestModel=1
                    mViewModel?.getHomeTwo(PageData().apply {
                        pageIndex=mPageData.pageIndex+1
                    })
                }
            }
            toolbarInit {
                mToolbar=view?.toolbar
                data= ToolbarBean(getString(R.string.home_atv_tv2),"",true).apply {
                    back=false
                }
                onSetting {
                    (activity as BaseActivity).phoneTask("phone")
                }
            }
        }

        view.refresh_layout?.apply {
            setOnRefreshListener {
                request()
            }
        }
        /*mAdapter?.apply {
            mData.observe(this@HomeTwoFragment, Observer { this::setList })
        }*/
        request()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.e(TAG, "onHiddenChanged $hidden")
        if (!hidden && mAdapter?.itemCount?:0<=0 && !mInit) {
            request()
        }
    }

    private fun request(){
        mPageData.init()
        mAdapter?.refresh(arrayListOf())
        view?.refresh_layout?.isRefreshing=true
        mViewModel?.getHomeTwo(mPageData)
    }

    override fun clearData() {
        mAdapter?.refresh(arrayListOf())
    }
}