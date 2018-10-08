package cn.yc.library.bean.response

class HomeFourResponse {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-04 19:22:07
     * dataSet : [{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1323406,"houseId":10,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":35,"closeTime":"2018-07-21 15:15:48","orderCode":"SCO_CZJSTHEV20180719151552501","closeTimeCountDown":-1224379,"isOpenOrder":0,"taskId":431,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1323406,"houseId":11,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":36,"closeTime":"2018-07-21 15:16:49","orderCode":"SCO_HUSWXQFW20180719151648160","closeTimeCountDown":-1224318,"isOpenOrder":0,"taskId":432,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1323406,"houseId":12,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":37,"closeTime":"2018-07-21 15:27:34","orderCode":"SCO_QMPFXIJF20180719152734866","closeTimeCountDown":-1223673,"isOpenOrder":0,"taskId":433,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1323406,"houseId":10,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":35,"closeTime":"2018-07-21 15:15:48","orderCode":"SCO_CZJSTHEV20180719151552501","closeTimeCountDown":-1224379,"isOpenOrder":0,"taskId":434,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1323406,"houseId":11,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":36,"closeTime":"2018-07-21 15:16:49","orderCode":"SCO_HUSWXQFW20180719151648160","closeTimeCountDown":-1224318,"isOpenOrder":0,"taskId":435,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1323406,"houseId":12,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":37,"closeTime":"2018-07-21 15:27:34","orderCode":"SCO_QMPFXIJF20180719152734866","closeTimeCountDown":-1223673,"isOpenOrder":0,"taskId":436,"contacts":"Mr Liu"},{"estimatedTime":"2018-08-03 11:45:21","estimatedTimeCountDown":-113806,"houseId":13,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":2,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":38,"closeTime":"2018-08-05 15:29:46","orderCode":"SCO_MSJFTDUZ20180719152945897","closeTimeCountDown":72458,"isOpenOrder":0,"taskId":437,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1323406,"houseId":12,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":37,"closeTime":"2018-07-21 15:27:34","orderCode":"SCO_QMPFXIJF20180719152734866","closeTimeCountDown":-1223673,"isOpenOrder":0,"taskId":438,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1323406,"houseId":10,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":35,"closeTime":"2018-07-21 15:15:48","orderCode":"SCO_CZJSTHEV20180719151552501","closeTimeCountDown":-1224379,"isOpenOrder":0,"taskId":440,"contacts":"Mr Liu"},{"estimatedTime":"2018-07-20 11:45:21","estimatedTimeCountDown":-1323406,"houseId":11,"appointmentMeetPlace":"深圳市南山区","isTransferOrder":0,"isFinished":1,"taskType":0,"leaseType":0,"phoneNumber":"15200302270","poolId":36,"closeTime":"2018-07-21 15:16:49","orderCode":"SCO_HUSWXQFW20180719151648160","closeTimeCountDown":-1224318,"isOpenOrder":0,"taskId":441,"contacts":"Mr Liu"}]
     * pageInfo : {"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"total":20,"pages":2,"list":null,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2],"navigateFirstPage":1,"navigateLastPage":2}
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var pageInfo: PageInfoBean? = null
    var dataSet: List<DataSetBean>? = null

    override fun toString(): String {
        return "HomeFourResponse{" +
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
         * total : 20
         * pages : 2
         * list : null
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2]
         * navigateFirstPage : 1
         * navigateLastPage : 2
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
         * estimatedTime : 2018-07-20 11:45:21
         * estimatedTimeCountDown : -1323406
         * houseId : 10
         * appointmentMeetPlace : 深圳市南山区
         * isTransferOrder : 0
         * isFinished : 1
         * taskType : 0
         * leaseType : 0
         * phoneNumber : 15200302270
         * poolId : 35
         * closeTime : 2018-07-21 15:15:48
         * orderCode : SCO_CZJSTHEV20180719151552501
         * closeTimeCountDown : -1224379
         * isOpenOrder : 0
         * taskId : 431
         * contacts : Mr Liu
         */

        var ownerMoble:String?=null
        var ownerName:String?=null
        var estimatedTime: String? = null
        var estimatedTimeCountDown: Int = 0
        var houseId: Int = 0
        var appointmentMeetPlace: String? = null
        var isTransferOrder: Int = 0
        var isFinished: Int = 0
        var taskType: Int = 0
        var leaseType: Int = 0
        var phoneNumber: String? = null
        var poolId: Int = 0
        var closeTime: String? = null
        var orderCode: String? = null
        var closeTimeCountDown: Int = 0
        var isOpenOrder: Int = 0
        var taskId: Int = 0
        var contacts: String? = null
        override fun toString(): String {
            return "DataSetBean(ownerMoble=$ownerMoble, ownerName=$ownerName, estimatedTime=$estimatedTime, estimatedTimeCountDown=$estimatedTimeCountDown, houseId=$houseId, appointmentMeetPlace=$appointmentMeetPlace, isTransferOrder=$isTransferOrder, isFinished=$isFinished, taskType=$taskType, leaseType=$leaseType, phoneNumber=$phoneNumber, poolId=$poolId, closeTime=$closeTime, orderCode=$orderCode, closeTimeCountDown=$closeTimeCountDown, isOpenOrder=$isOpenOrder, taskId=$taskId, contacts=$contacts)"
        }
    }
}
