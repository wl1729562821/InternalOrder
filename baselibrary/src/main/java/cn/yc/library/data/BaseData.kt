package cn.yc.library.data

import android.app.Application
import cn.yc.library.bean.response.BaseResponse

open class BaseData {
    var context:Application?=null

    protected var mNext: (BaseResponse<String>) -> Unit = { }
    protected var mError: Throwable.()->Unit= {}
    private var mInit:()->Unit={}

    open fun initData(init:()->Unit){
        mInit=init
    }

    open fun onError(error:Throwable.()->Unit){
        mError=error
    }

    open fun onNext(onSuccess: (BaseResponse<String>) -> Unit){
        mNext=onSuccess
    }
}