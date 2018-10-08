package com.ysgj.order.ui.activity.order

import android.os.Bundle
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.data.toolbarInit
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import kotlinx.android.synthetic.main.activity_deliverydetails.*

class DeliveryDetailsActivity:BaseActivity(){

    private var mStatus=0

    override val layoutId: Int
        get() = R.layout.activity_deliverydetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.homeonedetails_title),"",false)
            onBack {
                finish()
            }
        }
    }

}