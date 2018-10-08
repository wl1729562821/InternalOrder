package cn.yc.library.bean.response

class MessageListResponse {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-09-05 19:27:49
     * dataSet : [{"createBy":15,"alert":"4..22版本发布了","createTime":"2018-09-07 15:03:56","updateBy":null,"details":{"sysMsg":"1.任意外获人员可抢任意区域外获任务单;2.系统分配外获任务单可以给任意外获人员;3.区域长低优先级被系统分配外派任务单，例如：找不到外获业务员了;4.派单池的派单规则，每一分钟派单一次"},"updateTime":null,"id":3,"userName":"","isDel":0,"msgCode":1}]
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
         * createBy : 15
         * alert : 4..22版本发布了
         * createTime : 2018-09-07 15:03:56
         * updateBy : null
         * details : {"sysMsg":"1.任意外获人员可抢任意区域外获任务单;2.系统分配外获任务单可以给任意外获人员;3.区域长低优先级被系统分配外派任务单，例如：找不到外获业务员了;4.派单池的派单规则，每一分钟派单一次"}
         * updateTime : null
         * id : 3
         * userName :
         * isDel : 0
         * msgCode : 1
         */

        var createBy: Int = 0
        var alert: String? = null
        var createTime: String? = null
        var updateBy: Any? = null
        var details: DetailsBean? = null
        var updateTime: Any? = null
        var id: Int = 0
        var userName: String? = null
        var isDel: Int = 0
        var msgCode: Int = 0
        var title=""

        class DetailsBean {
            /**
             * sysMsg : 1.任意外获人员可抢任意区域外获任务单;2.系统分配外获任务单可以给任意外获人员;3.区域长低优先级被系统分配外派任务单，例如：找不到外获业务员了;4.派单池的派单规则，每一分钟派单一次
             */

            var sysMsg: String? = null
        }
    }

}
