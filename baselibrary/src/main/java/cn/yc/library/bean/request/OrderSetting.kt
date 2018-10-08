package cn.yc.library.bean.request

class OrderSetting {
    /**
     * beginRentDate : null
     * hasExpectApprove : 0
     * houseId : 1
     * houseRentPrice : 0
     * id : 0
     * isOpen : 0
     * payNode : 0
     * payType : 0
     * rentTime : 4
     * startRentDate :
     */

    var beginRentDate=""
    var hasExpectApprove=""
    var houseId = ""
    var houseRentPrice=""
    var id=""
    var isOpen=""
    var payNode=""
    var payType=""
    var rentTime=""
    var startRentDate: String? = null

    fun refresh(){
        beginRentDate=""
        hasExpectApprove=""
        houseRentPrice="0"
        id=""
        isOpen="0"
        payNode="0"
        payType="0"
        rentTime="0"
        startRentDate=""
        isOpen="0"
    }

    var type=0

}