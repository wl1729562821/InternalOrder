package cn.yc.library.ui.activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BrokeragesReponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.adapter.SettlementListAdapter
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.OrderViewModel
import kotlinx.android.synthetic.main.activity_settlementlist.*

class SettlementListActivity:BaseActivity(){

    private var mAdapter: SettlementListAdapter?=null
    private var mVm: OrderViewModel?=null

    override val layoutId: Int
        get() = R.layout.activity_settlementlist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVm=getViewModel()
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.app_tv16),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        settlementlist_rv?.apply {
            layoutManager=LinearLayoutManager(this@SettlementListActivity)
            mAdapter=SettlementListAdapter(this@SettlementListActivity)
            adapter=mAdapter
        }

        refresh_layout?.setOnRefreshListener {
            request()
        }
        request()
    }

    private fun request(){
        refresh_layout.isRefreshing=true
        mVm?.getBrokerages("1")
    }

    override fun onError(code: Int, throwable: Throwable) {
        showMessage(throwable.message)
        refresh_layout.isRefreshing=false
    }

    override fun <T> onNext(dat: T) {
        refresh_layout.isRefreshing=false
        (dat as? BrokeragesReponse)?.apply {
            mAdapter?.refresh(this)
        }
    }
}