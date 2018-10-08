package cn.yc.library.bean.response

class MessagesTypeResponse {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-27 13:01:47
     * dataSet : [{"zhCn":"系统消息","code":1,"enUs":"systemMessage","id":1,"type":"1,2,3,4"},{"zhCn":"客服回复","code":2,"enUs":"keFuHuiFu","id":2,"type":"1,2,3,4"},{"zhCn":"房源推荐","code":3,"enUs":"fangYuanTuiJian","id":3,"type":"1"}]
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var pageInfo: Any? = null
    var dataSet: List<DataSetBean>? = null

    class DataSetBean {
        /**
         * zhCn : 系统消息
         * code : 1
         * enUs : systemMessage
         * id : 1
         * type : 1,2,3,4
         */

        var zhCn: String? = null
        var code: Int = 0
        var enUs: String? = null
        var id: Int = 0
        var type: String? = null
    }

    override fun toString(): String {
        return "MessagesTypeResponse(result=$result, message=$message, token=$token, systemTime=$systemTime, pageInfo=$pageInfo, dataSet=$dataSet)"
    }
}
