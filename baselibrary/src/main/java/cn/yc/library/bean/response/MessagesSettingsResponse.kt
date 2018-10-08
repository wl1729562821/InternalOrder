package cn.yc.library.bean.response

class MessagesSettingsResponse {

    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-27 13:03:07
     * dataSet : [{"createBy":null,"openCode":"1,2,3","createTime":"2018-08-27 12:42:32","updateBy":null,"updateTime":"2018-08-27 12:42:38","id":1,"userName":"13712339282","isDel":0}]
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var pageInfo: Any? = null
    var dataSet: DataSetBean? = null
    override fun toString(): String {
        return "MessagesSettingsResponse{" +
                "result=" + result +
                ", message='" + message + '\''.toString() +
                ", token=" + token +
                ", systemTime='" + systemTime + '\''.toString() +
                ", pageInfo=" + pageInfo +
                ", dataSet=" + dataSet +
                '}'.toString()
    }

    class DataSetBean {
        /**
         * createBy : null
         * openCode : 1,2,3
         * createTime : 2018-08-27 12:42:32
         * updateBy : null
         * updateTime : 2018-08-27 12:42:38
         * id : 1
         * userName : 13712339282
         * isDel : 0
         */

        var createBy: Any? = null
        var openCode: String? = null
        var createTime: String? = null
        var updateBy: Any? = null
        var updateTime: String? = null
        var id=""
        var userName: String? = null
        var isDel: Int = 0
        override fun toString(): String {
            return "DataSetBean(createBy=$createBy, openCode=$openCode, createTime=$createTime, updateBy=$updateBy, updateTime=$updateTime, id=$id, userName=$userName, isDel=$isDel)"
        }
    }
}
