package cn.yc.library.bean.response

class UserInfoResponse {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-01 20:15:44
     * dataSet : [{"gold":0,"city":"深圳市","mobile":"15012940205",getString(R.string.fwlx_type1):"罗湖区","userId":11,"username":"wenbin"}]
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var pageInfo: Any? = null
    var dataSet: List<DataSetBean>? = null

    override fun toString(): String {
        return "UserInfoResponse{" +
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
         * gold : 0
         * city : 深圳市
         * mobile : 15012940205
         * community : 罗湖区
         * userId : 11
         * username : wenbin
         */

        var userLogo: String? = null
        var gold: Int = 0
        var city: String? = null
        var mobile: String? = null
        var community: String? = null
        var userId: Int = 0
        var username: String? = null

        override fun toString(): String {
            return "DataSetBean{" +
                    "gold=" + gold +
                    ", city='" + city + '\''.toString() +
                    ", mobile='" + mobile + '\''.toString() +
                    ", community='" + community + '\''.toString() +
                    ", userId=" + userId +
                    ", username='" + username + '\''.toString() +
                    '}'.toString()
        }
    }
}
