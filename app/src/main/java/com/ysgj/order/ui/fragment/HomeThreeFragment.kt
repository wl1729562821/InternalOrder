package com.ysgj.order.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.HomeThreeResponse
import cn.yc.library.bean.response.TransferOrderBean
import cn.yc.library.data.PageData
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.DialogListener
import cn.yc.library.listener.ItemClickListener
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.base.BaseFragment
import com.ysgj.order.ui.adapter.HomeThreeAdapter
import kotlinx.android.synthetic.main.fragment_home_three.*
import kotlinx.android.synthetic.main.fragment_home_three.view.*

class HomeThreeFragment:BaseFragment(){

    private var mViewModel: OrderViewModel?=null
    private var mAdapter:HomeThreeAdapter?=null

    private var mLoad: PromptDialog?=null
    private var mInit=false

    private var mPageData=PageData()

    override val layoutId: Int
        get() =R.layout.fragment_home_three

    override fun onError(code: Int, throwable: Throwable) {
        if(mPageData.requestModel==1){
            view?.home_three_rv?.finishLoadMore()
            return
        }
        refresh_layout?.isRefreshing=false
        mDialog?.dismiss()
        if(code==20){
            PromptDialog(activity!!,build = PromptDialog.DialogBuild().apply {
                content=getString(R.string.heihei_tv32)
                bt=R.string.bt_qd
                title=getString(R.string.prompt)
            }).show()
            return
        }
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        refresh_layout?.isRefreshing=false
        ((dat as? BaseResponse<*>)?.data as? TransferOrderBean)?.apply {
            mDialog?.dismiss()
            PromptDialog(activity!!,build = PromptDialog.DialogBuild().apply {
                content=getString(R.string.heihei_tv33)
                bt=R.string.bt_qd
                title=getString(R.string.prompt)
                mListener=object : DialogListener {
                    override fun dismiss() {
                        //刷新派单表界面
                        getBaseViewModel<LoginViewModel>()?.setRequestCode(10010)
                        mAdapter?.removeCurrent()
                    }
                }
            }).show()
            return
        }
        ((dat as? BaseResponse<*>)?.data as? HomeThreeResponse)?.apply {
            Log.e(TAG,"onNext $this")
            if(mPageData.requestModel==0){
                mAdapter?.refresh(this.dataSet?: arrayListOf())
                mInit=true
                showMessage(if(mAdapter?.itemCount?:0<=0){
                    R.string.request_empty_success
                }else{
                    R.string.request_success
                })
            }else{
                mAdapter?.loadMore(this.dataSet?: arrayListOf())
                mPageData.pageIndex++
                view?.home_three_rv?.finishLoadMore()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.also {
            mDialog= PromptDialog(it,PromptDialog.DialogBuild().apply {
                type=1
            })
            mLoad=PromptDialog(it,PromptDialog.DialogBuild().apply {
                type=1
            })
            view.home_three_rv?.apply {
                setRefreshLayout(view.refresh_layout)
                mAdapter=HomeThreeAdapter(it,object : ItemClickListener {
                    override fun <T> onClcik(data: T) {
                        mDialog?.show()
                        val bean=data as HomeThreeResponse.DataSetBean
                        mViewModel=mViewModel?:getViewModel()
                        mViewModel?.grabOrder(bean.poolId.toString())
                    }
                })
                layoutManager=LinearLayoutManager(it)
                adapter=mAdapter
                setRecyclerListener {
                    mPageData.requestModel=1
                    mViewModel?.getHomeTwo(PageData().apply {
                        pageIndex=mPageData.pageIndex+1
                    })
                }
            }
            toolbarInit {
                mToolbar=view?.toolbar
                data= ToolbarBean(getString(R.string.heihei_tv28),"",true).apply {
                    back=false
                }
                onSetting {
                    (activity as BaseActivity).phoneTask("phone")
                }
            }
        }
        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {
            if(it?.requestCode==152){
                request()
            }
        }
        view.refresh_layout?.setOnRefreshListener {
            request()
        }
        request()
    }

    private fun request(){
        mPageData.init()
        mAdapter?.refresh(arrayListOf())
        view?.refresh_layout?.isRefreshing=true
        mViewModel=mViewModel?:getViewModel()
        mViewModel?.getHomeThree(mPageData)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && mAdapter?.itemCount?:0<=0 &&!mInit) {
            mInit=true
            view?.refresh_layout?.isRefreshing=true
            request()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==10026 && resultCode==10026){
            request()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun clearData() {
        mAdapter?.refresh(arrayListOf())
    }

}