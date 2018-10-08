package cn.yc.library.ui.activity

import android.os.Bundle
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BrokeragesReponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.utils.setStringRes
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_settlementdetails.*

class SettlementDetailsActivity:BaseActivity(){

    override val layoutId: Int
        get() = R.layout.activity_settlementdetails

    private var mData=BrokeragesReponse.DataSetBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.app_tv16),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        val json=intent.getStringExtra("json")
        mData=Gson().fromJson(json, BrokeragesReponse.DataSetBean::class.java)
        name_tv.text=mData.houseName
        fkrq_tv.text=mData.payTime?.toString()?:""
        zffs_tv.setStringRes(when(mData.payWay){
            0->R.string.heihei_tv55
            1->R.string.heihei_tv56
            2->R.string.heihei_tv57
            3->R.string.heihei_tv58
            else->R.string.heihei_tv58
        })
        id_tv.text=mData.orderCode
        jyzt.setStringRes(if(mData.payStatus==0){
            R.string.heihei_tv55
        }else{
            R.string.heihei_tv59
        })
    }
}