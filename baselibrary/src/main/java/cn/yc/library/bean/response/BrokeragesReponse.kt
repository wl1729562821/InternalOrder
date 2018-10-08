package cn.yc.library.bean.response

class BrokeragesReponse {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-09-04 15:34:25
     * dataSet : [{"orderType":0,"houseId":86,"orderId":1,"payTime":null,"orderStatus":0,"payWay":0,"remark":null,"brokerageId":1,"houseName":"龙岗区坂田 3 室","createTime":"2018-08-30 17:46:27","houseCode":"HC_UXtME3hJnpOV5dFJo2gWJx8er7wNDqW8","orderCode":"OC_mtTS7665naMI86QRkD","payStatus":0},{"orderType":0,"houseId":87,"orderId":2,"payTime":null,"orderStatus":0,"payWay":0,"remark":null,"brokerageId":2,"houseName":"迪丽热巴7室","createTime":"2018-08-31 12:41:30","houseCode":"HC_ArM6UfeUMUCUcBxCaVBN5BJBanuKwjv8","orderCode":"OC_56JFGgjGLmABeSzkDC","payStatus":0},{"orderType":0,"houseId":88,"orderId":3,"payTime":null,"orderStatus":1,"payWay":0,"remark":null,"brokerageId":3,"houseName":"西丽大学城","createTime":"2018-08-31 12:46:27","houseCode":"HC_KrEzSBj6GMWVniTJKt6wVPJmuxj7vzsq","orderCode":"OC_wQbTeDM37BoqhtfPaE","payStatus":0}]
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
         * orderType : 0
         * houseId : 86
         * orderId : 1
         * payTime : null
         * orderStatus : 0
         * payWay : 0
         * remark : null
         * brokerageId : 1
         * houseName : 龙岗区坂田 3 室
         * createTime : 2018-08-30 17:46:27
         * houseCode : HC_UXtME3hJnpOV5dFJo2gWJx8er7wNDqW8
         * orderCode : OC_mtTS7665naMI86QRkD
         * payStatus : 0
         */

        var orderType: Int = 0
        var houseId: Int = 0
        var orderId: Int = 0
        var payTime: Any? = null
        var orderStatus: Int = 0
        var payWay: Int = 0
        var remark: Any? = null
        var brokerageId: Int = 0
        var houseName: String? = null
        var createTime: String? = null
        var houseCode: String? = null
        var orderCode: String? = null
        var payStatus: Int = 0
    }
}
