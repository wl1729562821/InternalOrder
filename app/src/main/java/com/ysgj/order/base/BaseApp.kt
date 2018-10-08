package com.ysgj.order.base

import cn.yc.library.base.App
import com.bugtags.library.Bugtags

class BaseApp :App(){

    override fun onCreate() {
        super.onCreate()

        Bugtags.start("b9c26ef53aff0561c1063b3141d370cf", this, Bugtags.BTGInvocationEventNone)
    }

}