package cn.yc.library.listener

interface BaseListener {

    fun <T> onNext(dat:T)

    fun onError(code:Int,throwable: Throwable)

}