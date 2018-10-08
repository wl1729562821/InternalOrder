package com.ysgj.order.base

import android.view.MotionEvent
import cn.yc.library.listener.BaseListener
import com.bugtags.library.Bugtags

abstract class BaseActivity : cn.yc.library.base.BaseActivity(), BaseListener {
    override fun onPause() {
        super.onPause()
        Bugtags.onPause(this)
    }

    override fun onResume() {
        super.onResume()
        Bugtags.onResume(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Bugtags.onDispatchTouchEvent(this,ev)
        return super.dispatchTouchEvent(ev)
    }
}