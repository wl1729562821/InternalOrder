package cn.yc.library.bean.response

class HomeThreeResponse {

    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-04 18:49:44
     * dataSet : [{"estimatedTime":"2018-08-04 16:19:40","estimatedTimeCountDown":-9004,"orderType":0,"rentCustomerName":"Mr Liu","houseId":15,"contactName":"文先生","languageVersion":0,"appointmentMeetPlace":"深圳市南山区","canRob":0,"rentCustomerPhone":"15200302201","leaseType":0,"phoneNumber":"15200302270","poolId":42,"closeTime":"2018-07-28 16:19:46","versionNo":0,"orderCode":"SCO_SADFDEW20180719152956698","closeTimeCountDown":-613798,"contacts":"Mr Liu"},{"estimatedTime":"2018-08-01 16:00:16","estimatedTimeCountDown":-269368,"orderType":0,"rentCustomerName":"guang","houseId":49,"contactName":"","languageVersion":0,"appointmentMeetPlace":"深圳市福田区","canRob":0,"rentCustomerPhone":"lala","leaseType":1,"phoneNumber":null,"poolId":59,"closeTime":"2018-08-03 16:03:00","versionNo":0,"orderCode":"SCO_KPKBOTGU20180801160259409","closeTimeCountDown":-96404,"contacts":null},{"estimatedTime":"2018-08-01 16:00:16","estimatedTimeCountDown":-269368,"orderType":0,"rentCustomerName":"guang","houseId":50,"contactName":"","languageVersion":0,"appointmentMeetPlace":"深圳市福田区","canRob":0,"rentCustomerPhone":"lala","leaseType":1,"phoneNumber":null,"poolId":60,"closeTime":"2018-08-03 16:03:03","versionNo":0,"orderCode":"SCO_QXDOWBKV20180801160302732","closeTimeCountDown":-96401,"contacts":null},{"estimatedTime":"2018-08-01 16:00:16","estimatedTimeCountDown":-269368,"orderType":0,"rentCustomerName":"guang","houseId":51,"contactName":"","languageVersion":0,"appointmentMeetPlace":"深圳市福田区","canRob":0,"rentCustomerPhone":"lala","leaseType":1,"phoneNumber":null,"poolId":61,"closeTime":"2018-08-03 16:03:06","versionNo":0,"orderCode":"SCO_TULTMKFH20180801160305559","closeTimeCountDown":-96398,"contacts":null},{"estimatedTime":"2018-08-01 16:00:16","estimatedTimeCountDown":-269368,"orderType":0,"rentCustomerName":"guang","houseId":52,"contactName":"","languageVersion":0,"appointmentMeetPlace":"深圳市福田区","canRob":0,"rentCustomerPhone":"lala","leaseType":1,"phoneNumber":null,"poolId":62,"closeTime":"2018-08-03 16:03:10","versionNo":0,"orderCode":"SCO_QTJZHJPM20180801160309337","closeTimeCountDown":-96394,"contacts":null},{"estimatedTime":"2018-08-01 16:00:16","estimatedTimeCountDown":-269368,"orderType":0,"rentCustomerName":"guang","houseId":53,"contactName":"","languageVersion":0,"appointmentMeetPlace":"深圳市福田区","canRob":0,"rentCustomerPhone":"lala","leaseType":1,"phoneNumber":null,"poolId":63,"closeTime":"2018-08-03 16:03:13","versionNo":0,"orderCode":"SCO_FPVRGHQB20180801160312293","closeTimeCountDown":-96391,"contacts":null},{"estimatedTime":"2018-08-01 16:00:16","estimatedTimeCountDown":-269368,"orderType":0,"rentCustomerName":"","houseId":54,"contactName":"","languageVersion":0,"appointmentMeetPlace":"深圳市福田区","canRob":0,"rentCustomerPhone":"","leaseType":0,"phoneNumber":null,"poolId":64,"closeTime":"2018-08-03 16:03:15","versionNo":0,"orderCode":"SCO_OKPFIDWK20180801160315762","closeTimeCountDown":-96389,"contacts":null},{"estimatedTime":"2018-08-01 16:00:16","estimatedTimeCountDown":-269368,"orderType":0,"rentCustomerName":"","houseId":55,"contactName":"","languageVersion":0,"appointmentMeetPlace":"深圳市福田区","canRob":0,"rentCustomerPhone":"","leaseType":1,"phoneNumber":null,"poolId":65,"closeTime":"2018-08-03 16:03:18","versionNo":0,"orderCode":"SCO_OONRLAPG20180801160318399","closeTimeCountDown":-96386,"contacts":null},{"estimatedTime":"2018-08-01 16:00:16","estimatedTimeCountDown":-269368,"orderType":0,"rentCustomerName":"","houseId":56,"contactName":"","languageVersion":0,"appointmentMeetPlace":"深圳市福田区","canRob":0,"rentCustomerPhone":"","leaseType":1,"phoneNumber":null,"poolId":66,"closeTime":"2018-08-03 16:03:21","versionNo":0,"orderCode":"SCO_CSGRLKQT20180801160320463","closeTimeCountDown":-96383,"contacts":null},{"estimatedTime":"2018-08-01 16:00:16","estimatedTimeCountDown":-269368,"orderType":0,"rentCustomerName":"","houseId":57,"contactName":"","languageVersion":0,"appointmentMeetPlace":"深圳市福田区","canRob":0,"rentCustomerPhone":"","leaseType":1,"phoneNumber":null,"poolId":67,"closeTime":"2018-08-03 16:03:24","versionNo":0,"orderCode":"SCO_DJDMMWAU20180801160324932","closeTimeCountDown":-96380,"contacts":null}]
     * pageInfo : {"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"total":23,"pages":3,"list":null,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3],"navigateFirstPage":1,"navigateLastPage":3}
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var pageInfo: PageInfoBean? = null
    var dataSet: List<DataSetBean>? = null

    override fun toString(): String {
        return "HomeThreeResponse{" +
                "result=" + result +
                ", message='" + message + '\''.toString() +
                ", token=" + token +
                ", systemTime='" + systemTime + '\''.toString() +
                ", pageInfo=" + pageInfo +
                ", dataSet=" + dataSet +
                '}'.toString()
    }

    class PageInfoBean {
        /**
         * pageNum : 1
         * pageSize : 10
         * size : 10
         * startRow : 1
         * endRow : 10
         * total : 23
         * pages : 3
         * list : null
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2,3]
         * navigateFirstPage : 1
         * navigateLastPage : 3
         */

        var pageNum: Int = 0
        var pageSize: Int = 0
        var size: Int = 0
        var startRow: Int = 0
        var endRow: Int = 0
        var total: Int = 0
        var pages: Int = 0

        var list: Any? = null
        var prePage: Int = 0
        var nextPage: Int = 0
        var isIsFirstPage: Boolean = false
        var isIsLastPage: Boolean = false
        var isHasPreviousPage: Boolean = false
        var isHasNextPage: Boolean = false
        var navigatePages: Int = 0
        var navigateFirstPage: Int = 0
        var navigateLastPage: Int = 0
        var navigatepageNums: List<Int>? = null
    }

    class DataSetBean {

        /**
         * estimatedTime : 2018-08-04 16:19:40
         * estimatedTimeCountDown : -9004
         * orderType : 0
         * rentCustomerName : Mr Liu
         * houseId : 15
         * contactName : 文先生
         * languageVersion : 0
         * appointmentMeetPlace : 深圳市南山区
         * canRob : 0
         * rentCustomerPhone : 15200302201
         * leaseType : 0
         * phoneNumber : 15200302270
         * poolId : 42
         * closeTime : 2018-07-28 16:19:46
         * versionNo : 0
         * orderCode : SCO_SADFDEW20180719152956698
         * closeTimeCountDown : -613798
         * contacts : Mr Liu
         */

        var estimatedTime: String? = null
        var estimatedTimeCountDown: Int = 0
        var orderType: Int = 0
        var rentCustomerName: String? = null
        var houseId: Int = 0
        var nickname=""
        var memberMoble=""
        var contactName: String? = null
        var languageVersion: Int = 0
        var appointmentMeetPlace: String? = null
        var canRob: Int = 0
        var rentCustomerPhone: String? = null
        var leaseType: Int = 0
        var phoneNumber: String? = null
        var poolId: Int = 0
        var closeTime: String? = null
        var versionNo: Int = 0
        var orderCode: String? = null
        var closeTimeCountDown: Int = 0
        var contacts: String? = null
        override fun toString(): String {
            return "DataSetBean(estimatedTime=$estimatedTime, estimatedTimeCountDown=$estimatedTimeCountDown, orderType=$orderType, rentCustomerName=$rentCustomerName, houseId=$houseId, nickname='$nickname', memberMoble='$memberMoble', contactName=$contactName, languageVersion=$languageVersion, appointmentMeetPlace=$appointmentMeetPlace, canRob=$canRob, rentCustomerPhone=$rentCustomerPhone, leaseType=$leaseType, phoneNumber=$phoneNumber, poolId=$poolId, closeTime=$closeTime, versionNo=$versionNo, orderCode=$orderCode, closeTimeCountDown=$closeTimeCountDown, contacts=$contacts)"
        }
    }
}
