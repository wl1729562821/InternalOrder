package cn.yc.library.bean.response

class BaseResponse <T>{

    var code:Int=0
    var message:String=""
    var messageId=0
    var data:T?=null
    override fun toString(): String {
        return "BaseResponse(code=$code, message='$message', data=$data)"
    }

}