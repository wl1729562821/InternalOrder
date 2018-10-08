package cn.yc.library.bean.response

class HomeOneDetailsBean {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-03 21:39:43
     * dataSet : {"memberMoble":"18129922469","applyId":"2","leaseType":1,"languageVersion":0,"poolId":"38","memberName":"覃先生","appointmentMeetPlace":"深圳市南山区","appointmentDoorTime":"2018-08-03 11:45:21","taskId":"439"}
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var dataSet: DataSetBean? = null
    var pageInfo: Any? = null

    class DataSetBean {
        /**
         * memberMoble : 18129922469
         * applyId : 2
         * leaseType : 1
         * languageVersion : 0
         * poolId : 38
         * memberName : 覃先生
         * appointmentMeetPlace : 深圳市南山区
         * appointmentDoorTime : 2018-08-03 11:45:21
         * taskId : 439
         */

        var memberMoble: String? = null
        var applyId: String? = null
        var leaseType: Int = 0
        var languageVersion: Int = 0
        var poolId: String? = null
        var memberName: String? = null
        var appointmentMeetPlace: String? = null
        var appointmentDoorTime: String? = null
        var taskId: String? = null
        var houseId=""
        override fun toString(): String {
            return "DataSetBean(memberMoble=$memberMoble, applyId=$applyId, leaseType=$leaseType, languageVersion=$languageVersion, poolId=$poolId, memberName=$memberName, appointmentMeetPlace=$appointmentMeetPlace, appointmentDoorTime=$appointmentDoorTime, taskId=$taskId, houseId='$houseId')"
        }


    }

    override fun toString(): String {
        return "HomeOneDetailsBean(result=$result, message=$message, token=$token, systemTime=$systemTime, dataSet=$dataSet, pageInfo=$pageInfo)"
    }

}
