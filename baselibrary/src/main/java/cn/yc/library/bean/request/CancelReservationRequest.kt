package cn.yc.library.bean.request

class CancelReservationRequest {
    var taskId:String=""
    var taskType=""
    var cancelType=""
    var feedbackType=""
    var feedbackDesc=""
    var proofPic1=""
    var proofPic2=""
    var proofPic3=""
    var proofPic4=""
    var proofPic5=""
    override fun toString(): String {
        return "CancelReservationRequest(taskId='$taskId', taskType='$taskType', cancelType='$cancelType', feedbackType='$feedbackType', feedbackDesc='$feedbackDesc', proofPic1='$proofPic1', proofPic2='$proofPic2', proofPic3='$proofPic3', proofPic4='$proofPic4', proofPic5='$proofPic5')"
    }
}