package cn.yc.library.bean.request

class TransferOrderRequest {
    var ids:String=""
    var times=""
    var taskType=""
    var transferOrderReason=""
    var remark=""
    var proofPic1=""
    var proofPic2=""
    var proofPic3=""
    var proofPic4=""
    var proofPic5=""
    override fun toString(): String {
        return "TransferOrderRequest(ids='$ids', times='$times', taskType='$taskType', transferOrderReason='$transferOrderReason', remark='$remark', proofPic1='$proofPic1', proofPic2='$proofPic2', proofPic3='$proofPic3', proofPic4='$proofPic4', proofPic5='$proofPic5')"
    }
}