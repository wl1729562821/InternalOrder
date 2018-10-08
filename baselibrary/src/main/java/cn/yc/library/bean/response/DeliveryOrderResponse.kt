package cn.yc.library.bean.response

class DeliveryOrderResponse {

    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-04 19:40:40
     * dataSet : [{"estimatedTime":"2018-07-20 11:45:21","taskType":0,"leaseType":0,"houseId":10,"phoneNumber":"15200302270","poolId":35,"orderCode":"SCO_CZJSTHEV20180719151552501","appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskId":426,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","taskType":0,"leaseType":0,"houseId":11,"phoneNumber":"15200302270","poolId":36,"orderCode":"SCO_HUSWXQFW20180719151648160","appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskId":427,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","taskType":0,"leaseType":0,"houseId":12,"phoneNumber":"15200302270","poolId":37,"orderCode":"SCO_QMPFXIJF20180719152734866","appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskId":428,"contacts":"Mr Liu"},{"estimatedTime":"2018-08-03 11:45:21","taskType":0,"leaseType":0,"houseId":13,"phoneNumber":"15200302270","poolId":38,"orderCode":"SCO_MSJFTDUZ20180719152945897","appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":2,"taskId":429,"contacts":"Mr Liu"},{"estimatedTime":"2018-08-03 11:45:21","taskType":0,"leaseType":0,"houseId":13,"phoneNumber":"15200302270","poolId":38,"orderCode":"SCO_MSJFTDUZ20180719152945897","appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":2,"taskId":430,"contacts":"Mr Liu"}]
     * pageInfo : {"pageNum":1,"pageSize":10,"size":5,"startRow":1,"endRow":5,"total":5,"pages":1,"list":null,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var pageInfo: PageInfoBean? = null
    var dataSet: List<DataSetBean>? = null

    override fun toString(): String {
        return "DeliveryOrderResponse{" +
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
         * size : 5
         * startRow : 1
         * endRow : 5
         * total : 5
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
            return "PageInfoBean{" +
                    "pageNum=" + pageNum +
                    ", pageSize=" + pageSize +
                    ", size=" + size +
                    ", startRow=" + startRow +
                    ", endRow=" + endRow +
                    ", total=" + total +
                    ", pages=" + pages +
                    ", list=" + list +
                    ", prePage=" + prePage +
                    ", nextPage=" + nextPage +
                    ", isFirstPage=" + isIsFirstPage +
                    ", isLastPage=" + isIsLastPage +
                    ", hasPreviousPage=" + isHasPreviousPage +
                    ", hasNextPage=" + isHasNextPage +
                    ", navigatePages=" + navigatePages +
                    ", navigateFirstPage=" + navigateFirstPage +
                    ", navigateLastPage=" + navigateLastPage +
                    ", navigatepageNums=" + navigatepageNums +
                    '}'.toString()
        }
    }

    class DataSetBean {
        /**
         * estimatedTime : 2018-07-20 11:45:21
         * taskType : 0
         * leaseType : 0
         * houseId : 10
         * phoneNumber : 15200302270
         * poolId : 35
         * orderCode : SCO_CZJSTHEV20180719151552501
         * appointmentMeetPlace : 深圳市南山区
         * isTransferOrder : 0
         * isFinished : 1
         * taskId : 426
         * contacts : Mr Liu
         */

        var estimatedTime: String? = null
        var taskType: Int = 0
        var leaseType: Int = 0
        var houseId: Int = 0
        var phoneNumber: String? = null
        var poolId: Int = 0
        var orderCode: String? = null
        var appointmentMeetPlace: String? = null
        var isTransferOrder: Int = 0
        var isFinished: Int = 0
        var taskId: Int = 0
        var contacts: String? = null
    }
}
