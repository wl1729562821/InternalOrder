package cn.yc.library.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import android.util.Log
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.PageBean
import cn.yc.library.bean.request.CancelReservationRequest
import cn.yc.library.bean.request.ListingRequest
import cn.yc.library.bean.request.TransferOrderRequest
import cn.yc.library.bean.response.HomeTwoResponse
import cn.yc.library.data.PageData
import top.zibin.luban.Luban

class OrderViewModel(app:Application): BaseViewModel(app){

    private val mTwoMoreData=MutableLiveData<PagedList<HomeTwoResponse.DataSetBean>>()

    fun getOutGainDispatchOrder(data: PageData){
        Log.e("VM","getOutGainDispatchOrder ${data.hasNextPage}")
        mService.getOutGainDispatchOrder(PageBean(data.pageIndex,data.pageSize))
    }
    fun getHomeOneDetails(taskId:String,applyId:String,poolId:String){
        mService.getHomeOneDetails(taskId,applyId,poolId)
    }

    fun updateTransferOrder(request: TransferOrderRequest){
        mService.updateTransferOrder(request)
    }

    fun cancelReservation(request: CancelReservationRequest){
        mService.cancelReservation(request)
    }

    fun checkQRCode(path:String) {
        mService.checkQRCode(path)
    }
    fun checkReach(houseId:String,lon:String,lat:String) {
        Log.e("avtivity","checkReach $houseId $lon $lat")
        mService.checkReach(houseId,lon,lat)
    }


    fun getHomeTwo(pageData: PageData){
        mService.getHomeTwo(pageData)
    }

    fun getHomeThree(pageData: PageData){
        mService.getHomeThree(pageData)
    }

    fun getHomeFour(){
        mService.getHomeFour()
    }

    fun getDeliveryOrder(){
        mService.getDeliveryOrder()
    }

    fun grabOrder(id:String){
        mService.grabOrder(id)
    }

    fun getAttendance(){
        mService.getAttendance()
    }

    fun updatePwd(oldPwd:String,pwd:String){
        mService.updatePwd(oldPwd,pwd)
    }

    fun update(time:String,address:String,poolId:String){
        mService.update(time,address,poolId)
    }
    fun getKey(){
        mService.getKey()
    }

    fun getBrokerages(pageIndex:String){
        mService.getBrokerages(pageIndex)
    }

    fun getListingDidate(applyId:String){
        mService.getListingDidate(applyId)
    }

    fun getTwoDetails(applyId:String){
        mService.getTwoDetails(applyId)
    }

    fun getListingConfigResponse(type:String){
        mService.getListingConfigResponse(type)
    }
    fun getCity(){
        mService.getCity()
    }

    fun uploadListing(request: ListingRequest){
        mService.uploadListing(request)
    }

    fun showQrcode(id:String){
        mService.showQrcode(id)
    }

    fun updateListing(request: ListingRequest){
        mService.updateListing(request)
    }
    fun uploadImg(filePath:String){
        val file=Luban.with(getApplication()).get(filePath)
        Log.e("ViewModel","uploadImg ${file.absolutePath}")
        mService.uploadImg(file)
    }

    fun requestLocation(id:String,longitude:String,latitude:String,location:String){
        mService.requestLocation(id,longitude,latitude,location)
    }

    fun requestLocation(location:String){
        Log.e("ViewModel","requestLocation $location")
        //mService.requestLocation("22.576577,113.880099")
        mService.requestLocation(location)
    }

    fun getGaodeLocation(location:String){
        mService.getGaodeLocation(location)
    }

    fun complete(taskId:String,holdContractPic:String){
        mService.complete(taskId,holdContractPic)
    }

    fun getMessageList(){
        mService.getMessageList()
    }
    fun getMessagHistory(msgCode:String){
        mService.getMessagHistory(msgCode)
    }
    fun getMessagType(type:String){
        mService.getMessagType(type)
    }
    fun getMessagSettings(){
        mService.getMessagSettings()
    }
    fun setMessagSettings(userName:String,openCode:String,id:String){
        mService.setMessagSettings(userName,openCode,id)
    }
}