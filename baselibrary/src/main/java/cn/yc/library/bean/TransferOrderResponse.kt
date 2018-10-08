package cn.yc.library.bean

class TransferOrderResponse {
    /**
     * result : 0
     * message : 账号未注册
     * token : null
     * systemTime : 2018-08-01 17:02:20
     * dataSet : null
     * pageInfo : null
     */

    var result: Int = 0
    var message: String = ""
    var token: String = ""
    var systemTime: String = ""
    var dataSet: List<Int>? = null
    var pageInfo: String? = null

    constructor(msg:String){
        message=msg
    }

    constructor()

    override fun toString(): String {
        return "LoginResponse(result=$result, message='$message', token='$token', systemTime='$systemTime', dataSet=$dataSet, pageInfo=$pageInfo)"
    }
}