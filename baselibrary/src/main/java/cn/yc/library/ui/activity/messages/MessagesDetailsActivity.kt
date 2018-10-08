package cn.yc.library.ui.activity.messages

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.HistoryMessagesResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.adapter.MsgDetailsAdapter
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.OrderViewModel
import kotlinx.android.synthetic.main.activity_msg.*

class MessagesDetailsActivity :BaseActivity(){

    private var mVm: OrderViewModel?=null
    private var mAdapter: MsgDetailsAdapter?=null

    override val layoutId: Int
        get() = R.layout.activity_msg

    private var mType=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVm=getViewModel()
        mType=intent.getStringExtra("type")

        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(intent.getStringExtra("title"),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        msg_rv?.apply {
            layoutManager= LinearLayoutManager(this@MessagesDetailsActivity)
            mAdapter= MsgDetailsAdapter(this@MessagesDetailsActivity,null)
            adapter=mAdapter
        }
        refreshUI()
    }

    private fun refreshUI(){
        if(mType=="1"){
            mType="0"
        }
        system_view.visibility= View.GONE
        msg_rv.visibility= View.VISIBLE
        refresh_layout.setOnRefreshListener {
            request(if(mType=="5"){
                "0"
            }else{
                mType
            })
        }
        refresh_layout.isRefreshing=true
        request(if(mType=="5"){
            "0"
        }else{
            mType
        })
        header_line.visibility=if(mType=="3"|| mType=="4"){
            View.VISIBLE
        }else{
            View.GONE
        }

    }

    private fun request(type:String){
        refresh_layout.isRefreshing=true
        mVm?.getMessagHistory(type)
    }

    override fun onError(code: Int, throwable: Throwable) {
        refresh_layout.isRefreshing=false
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        refresh_layout.isRefreshing=false
        ((dat as? BaseResponse<*>)?.data as? HistoryMessagesResponse)?.apply {
            val type=if(mType=="5"){
                0
            }else{
                mType.toInt()
            }
            dataSet?.forEach {
                it.type=type
            }
            mAdapter?.refresh(this.dataSet)
            showMessage(message)
        }
    }
}