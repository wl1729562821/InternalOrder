package cn.yc.library.bean.response

class KeyResponse {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-02 15:31:14
     * dataSet : [{"houseMainImg":null,"houseName":"民乐 书香门弟","houseId":31,"address":"Dubai Al Barari Al Barari Villas Bromellia","houseRent":7600000,"city":"Dubai","houseKeyId":175,"houseCode":"HC_ZACNDWYN20180719152733311",getString(R.string.fwlx_type1):"Al Barari","subCommunity":"Al Barari Villas","houseAcreage":125.63}]
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var pageInfo: Any? = null
    var dataSet: List<DataSetBean>? = null

    override fun toString(): String {
        return "KeyResponse{" +
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
         * houseMainImg : null
         * houseName : 民乐 书香门弟
         * houseId : 31
         * address : Dubai Al Barari Al Barari Villas Bromellia
         * houseRent : 7600000
         * city : Dubai
         * houseKeyId : 175
         * houseCode : HC_ZACNDWYN20180719152733311
         * community : Al Barari
         * subCommunity : Al Barari Villas
         * houseAcreage : 125.63
         */

        var houseMainImg: String? = null
        var houseName: String? = null
        var houseId: Int = 0
        var address: String? = null
        var houseRent: Int = 0
        var city: String? = null
        var houseKeyId: Int = 0
        var houseCode: String? = null
        var community: String? = null
        var subCommunity: String? = null
        var houseAcreage: Double = 0.toDouble()

        var check=false

        override fun toString(): String {
            return "DataSetBean{" +
                    "houseMainImg=" + houseMainImg +
                    ", houseName='" + houseName + '\''.toString() +
                    ", houseId=" + houseId +
                    ", address='" + address + '\''.toString() +
                    ", houseRent=" + houseRent +
                    ", city='" + city + '\''.toString() +
                    ", houseKeyId=" + houseKeyId +
                    ", houseCode='" + houseCode + '\''.toString() +
                    ", community='" + community + '\''.toString() +
                    ", subCommunity='" + subCommunity + '\''.toString() +
                    ", houseAcreage=" + houseAcreage +
                    '}'.toString()
        }
    }
}
