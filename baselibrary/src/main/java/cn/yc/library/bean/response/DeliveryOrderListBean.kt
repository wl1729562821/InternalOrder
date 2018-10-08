package cn.yc.library.bean.response

class DeliveryOrderListBean {

    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-03 15:50:39
     * dataSet : [{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1224318,"memberMoble":"15012940201","houseId":11,"address":"Dubai Al Barari Al Barari Villas Bromellia","languageVersion":0,"memberName":null,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":0,"applyId":1,"taskType":0,"leaseType":0,"poolId":36,"closeTime":"2018-07-21 15:16:49","orderCode":"SCO_HUSWXQFW20180719151648160","closeTimeCountDown":-1125230,"taskId":432,"memberId":4443},{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1224318,"memberMoble":"18129922469","houseId":10,"address":null,"languageVersion":0,"memberName":"覃先生","appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":0,"applyId":2,"taskType":0,"leaseType":1,"poolId":35,"closeTime":"2018-07-21 15:15:48","orderCode":"SCO_CZJSTHEV20180719151552501","closeTimeCountDown":-1125291,"taskId":443,"memberId":4444}]
     * pageInfo : {"pageNum":1,"pageSize":10,"size":2,"startRow":1,"endRow":2,"total":2,"pages":1,"list":null,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var pageInfo: PageInfoBean? = null
    var dataSet: List<DataSetBean>? = null

    override fun toString(): String {
        return "AXDXCVX{" +
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
         * size : 2
         * startRow : 1
         * endRow : 2
         * total : 2
         * pages : 1
         * list : null
         * prePage : 0
         * nextPage : 0
         * isFirstPage : true
         * isLastPage : true
         * hasPreviousPage : false
         * hasNextPage : false
         * navigatePages : 8
         * navigatepageNums : [1]
         * navigateFirstPage : 1
         * navigateLastPage : 1
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
        override fun toString(): String {
            return "PageInfoBean(pageNum=$pageNum, pageSize=$pageSize, size=$size, startRow=$startRow, endRow=$endRow, total=$total, pages=$pages, list=$list, prePage=$prePage, nextPage=$nextPage, isIsFirstPage=$isIsFirstPage, isIsLastPage=$isIsLastPage, isHasPreviousPage=$isHasPreviousPage, isHasNextPage=$isHasNextPage, navigatePages=$navigatePages, navigateFirstPage=$navigateFirstPage, navigateLastPage=$navigateLastPage, navigatepageNums=$navigatepageNums)"
        }
    }

    class DataSetBean {
        /**
         * estimatedTime : 2018-07-20 11:45:21
         * estimatedTimeCountDown : -1224318
         * memberMoble : 15012940201
         * houseId : 11
         * address : Dubai Al Barari Al Barari Villas Bromellia
         * languageVersion : 0
         * memberName : null
         * appointmentMeetPlace : 深圳市南山区
         * isTransferOrder : 0
         * isFinished : 0
         * applyId : 1
         * taskType : 0
         * leaseType : 0
         * poolId : 36
         * closeTime : 2018-07-21 15:16:49
         * orderCode : SCO_HUSWXQFW20180719151648160
         * closeTimeCountDown : -1125230
         * taskId : 432
         * memberId : 4443
         */

        var estimatedTime: String? = null
        var estimatedTimeCountDown: Int = 0
        var memberMoble: String? = null
        var houseId: Int = 0
        var address: String? = null
        var languageVersion: Int = 0
        var memberName: Any? = null
        var appointmentMeetPlace: String? = null
        var isTransferOrder: Int = 0
        var isFinished: Int = 0
        var applyId: Int = 0
        var taskType: Int = 0
        var leaseType: Int = 0
        var poolId: Int = 0
        var closeTime: String? = null
        var orderCode: String? = null
        var closeTimeCountDown: Int = 0
        var taskId: Int = 0
        var memberId: Int = 0
        override fun toString(): String {
            return "DataSetBean(estimatedTime=$estimatedTime, estimatedTimeCountDown=$estimatedTimeCountDown, memberMoble=$memberMoble, houseId=$houseId, address=$address, languageVersion=$languageVersion, memberName=$memberName, appointmentMeetPlace=$appointmentMeetPlace, isTransferOrder=$isTransferOrder, isFinished=$isFinished, applyId=$applyId, taskType=$taskType, leaseType=$leaseType, poolId=$poolId, closeTime=$closeTime, orderCode=$orderCode, closeTimeCountDown=$closeTimeCountDown, taskId=$taskId, memberId=$memberId)"
        }


    }
}
