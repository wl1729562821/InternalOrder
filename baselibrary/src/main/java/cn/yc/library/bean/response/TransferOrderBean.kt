package cn.yc.library.bean.response

class TransferOrderBean {

    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-07-27 15:51:10
     * dataSet : {"memberMoble":"15200302270","applyId":"1","leaseType":0,"nickName":"杨女士","languageVersion":0,"poolId":"35","appointmentMeetPlace":"深圳市南山区","appointmentDoorTime":"2018-07-28 20:21:58","taskId":"431"}
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var dataSet: DataSetBean? = null
    var pageInfo: Any? = null

    override fun toString(): String {
        return "TransferOrderBean{" +
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
         * memberMoble : 15200302270
         * applyId : 1
         * leaseType : 0
         * nickName : 杨女士
         * languageVersion : 0
         * poolId : 35
         * appointmentMeetPlace : 深圳市南山区
         * appointmentDoorTime : 2018-07-28 20:21:58
         * taskId : 431
         */

        var memberMoble: String? = null
        var applyId: String? = null
        var leaseType: Int = 0
        var nickName: String? = null
        var languageVersion: Int = 0
        var poolId: String? = null
        var appointmentMeetPlace: String? = null
        var appointmentDoorTime: String? = null
        var taskId: String? = null
        override fun toString(): String {
            return "DataSetBean(memberMoble=$memberMoble, applyId=$applyId, leaseType=$leaseType, nickName=$nickName, languageVersion=$languageVersion, poolId=$poolId, appointmentMeetPlace=$appointmentMeetPlace, appointmentDoorTime=$appointmentDoorTime, taskId=$taskId)"
        }
    }
}
