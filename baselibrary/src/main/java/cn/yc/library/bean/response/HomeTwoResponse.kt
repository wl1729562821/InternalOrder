package cn.yc.library.bean.response

class HomeTwoResponse {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-07-30 17:55:39
     * dataSet : [{"houseMainImg":"http://120.77.220.25//pic/121782738.jpg","houseName":"Dubai Al Barari Al Barari Villas Bromellia 3 Bedroom","isCheck":2,"leaseType":0,"address":"Dubai Al Barari Al Barari Villas Bromellia","houseRent":7580000,"orderCode":"SCO_CZJSTHEV20180719151552501","id":1,"houseAcreage":125.63},{"houseMainImg":"http://120.77.220.25//pic/121782738.jpg","houseName":"Dubai Al Barari Al Barari Villas Bromellia 3 Bedroom","isCheck":1,"leaseType":0,"address":"Dubai Al Barari Al Barari Villas Bromellia","houseRent":7580000,"orderCode":"SCO_HUSWXQFW20180719151648160","id":2,"houseAcreage":125.63}]
     * pageInfo : {"pageNum":1,"pageSize":10,"size":2,"startRow":1,"endRow":2,"total":2,"pages":1,"list":null,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var pageInfo: PageInfoBean? = null
    var dataSet: List<DataSetBean>? = null

    override fun toString(): String {
        return "HomeTwoResponse{" +
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
         * houseMainImg : http://120.77.220.25//pic/121782738.jpg
         * houseName : Dubai Al Barari Al Barari Villas Bromellia 3 Bedroom
         * isCheck : 2
         * leaseType : 0
         * address : Dubai Al Barari Al Barari Villas Bromellia
         * houseRent : 7580000
         * orderCode : SCO_CZJSTHEV20180719151552501
         * id : 1
         * houseAcreage : 125.63
         */

        var houseMainImg: String? = null
        var houseName: String? = null
        var isCheck: Int = 0
        var leaseType: Int = 0
        var address: String? = null
        var houseRent: Int = 0
        var orderCode: String? = null
        var houseId: Int = 0
        var houseAcreage: Double = 0.toDouble()
        override fun toString(): String {
            return "DataSetBean(houseMainImg=$houseMainImg, houseName=$houseName, isCheck=$isCheck, leaseType=$leaseType, address=$address, houseRent=$houseRent, orderCode=$orderCode, id=$houseId, houseAcreage=$houseAcreage)"
        }
    }
}
