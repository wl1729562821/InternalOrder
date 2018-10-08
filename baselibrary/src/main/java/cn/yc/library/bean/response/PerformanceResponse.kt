package cn.yc.library.bean.response

class PerformanceResponse {

    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-07-27 16:35:06
     * dataSet : {"uploadSuccessNum":0,"arriveCustomerNum":1,"uploadHouseNum":2,"transferOrderNum":1}
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var dataSet: DataSetBean? = null
    var pageInfo: Any? = null
    override fun toString(): String {
        return "PerformanceResponse{" +
                "result=" + result +
                ", message='" + message + '\''.toString() +
                ", token=" + token +
                ", systemTime='" + systemTime + '\''.toString() +
                ", dataSet=" + dataSet +
                ", pageInfo=" + pageInfo +
                '}'.toString()
    }

    class DataSetBean {
        /**
         * uploadSuccessNum : 0
         * arriveCustomerNum : 1
         * uploadHouseNum : 2
         * transferOrderNum : 1
         */

        var uploadSuccessNum: Int = -1
        var arriveCustomerNum: Int = 0
        var uploadHouseNum: Int = 0
        var transferOrderNum: Int = 0
    }
}
