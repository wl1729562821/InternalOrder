package cn.yc.library.data

class PageData:BaseData(){

    var pageSize:Int=200
    var pageIndex:Int=1
    var requestModel=0 //0:刷新 1：加载更多

    var hasNextPage=true

    var pull=true


    fun init(){
        pageSize=200
        pageIndex=1
        requestModel=0
    }

    init {

    }
}