package cn.yc.library.bean.request

import cn.yc.library.R
import cn.yc.library.ui.config.DataConfig

class RequestBean{
    var requestCode= DataConfig.REQUEST_LOAD
    var request=false

    var requestMessage= R.string.request_empty_success

    var data:Any?=null

    constructor()

    constructor(code:Int){
        requestCode=code
    }
}