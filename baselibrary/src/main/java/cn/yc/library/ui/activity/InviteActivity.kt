package cn.yc.library.ui.activity

import android.os.Bundle
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.data.toolbarInit
import kotlinx.android.synthetic.main.activity_invite.*

class InviteActivity:BaseActivity(){

    override val layoutId: Int
        get() = R.layout.activity_invite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.five_tv11),"",false).apply {
            }
            onBack {
                finish()
            }
        }
    }
}