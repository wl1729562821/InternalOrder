package cn.yc.library.bean.response

class UpdatePwdResponse {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-07-28 15:24:16
     * dataSet : null
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var dataSet: Any? = null
    var pageInfo: Any? = null

    override fun toString(): String {
        return "UpdatePwdResponse{" +
                "result=" + result +
                ", message='" + message + '\''.toString() +
                ", token=" + token +
                ", systemTime='" + systemTime + '\''.toString() +
                ", dataSet=" + dataSet +
                ", pageInfo=" + pageInfo +
                '}'.toString()
    }
}
