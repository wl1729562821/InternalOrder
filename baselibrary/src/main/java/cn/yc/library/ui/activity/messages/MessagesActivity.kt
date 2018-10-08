package cn.yc.library.ui.activity.messages

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.MessageListResponse
import cn.yc.library.bean.response.MessagesTypeResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.adapter.MsgAdapter
import cn.yc.library.utils.checkZh
import cn.yc.library.utils.getApp
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.OrderViewModel
import kotlinx.android.synthetic.main.activity_msg.*

class MessagesActivity:BaseActivity(){
    private var mVm: OrderViewModel?=null
    private var mAdapter: MsgAdapter?=null

    override val layoutId: Int
        get() = R.layout.activity_msg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mVm=getViewModel()
        getApp()?.getAppViewModel()?.apply {
            mMessages=0
            getRequestModule()?.observeForever {
                if(it?.requestCode==1413){
                    request()
                }
            }
        }
        header_line.visibility=View.GONE
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.heihei_tv13),"",false).apply {
                this.settingTitle=getString(R.string.app_tv14)
            }
            onBack {
                finish()
            }
            onSetting {
                startActivity(Intent(this@MessagesActivity, MessagesSettingsActivity::class.java))
            }
        }

        refresh_layout.setOnRefreshListener {
            request()
        }
        msg_rv?.apply {
            layoutManager=LinearLayoutManager(this@MessagesActivity)
            mAdapter=MsgAdapter(this@MessagesActivity,null)
            adapter=mAdapter
        }
        request()
        //request()
    }

    private fun request(){
        refresh_layout.isRefreshing=true
        mVm?.getMessagType("2")
    }

    override fun onError(code: Int, throwable: Throwable) {
        refresh_layout.isRefreshing=false
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        (dat as? BaseResponse<*>)?.apply {
            (data as? MessagesTypeResponse)?.apply {
                dataSet?.filter { !checkZh() }?.forEach {
                    it.zhCn=it.enUs
                }
                mAdapter?.setTypeList(this)
                mVm?.getMessageList()
                return
            }
            (data as? MessageListResponse)?.apply {
                refresh_layout.isRefreshing=false
                mAdapter?.refresh(dataSet)
                showMessage(message)
            }
        }
    }

    override fun finish() {
        getApp()?.getAppViewModel()?.mMessages=2
        super.finish()
    }

    override fun onDestroy() {
        getApp()?.getAppViewModel()?.mMessages=2
        super.onDestroy()
    }
}