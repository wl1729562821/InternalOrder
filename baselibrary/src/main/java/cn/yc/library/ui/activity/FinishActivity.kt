package cn.yc.library.ui.activity

import android.os.Bundle
import cn.yc.library.R
import cn.yc.library.base.BaseActivity

class FinishActivity:BaseActivity(){

    override val layoutId: Int
        get() =R.layout.activity_finish

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }

}