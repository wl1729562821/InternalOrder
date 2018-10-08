package cn.yc.library.api

import cn.yc.library.bean.TransferOrderResponse
import cn.yc.library.bean.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface OrderApi {

    @POST("interior/salesman/getOutGainDispatchOrder")
    fun getOutGainDispatchOrder(@QueryMap data:HashMap<String,String>)
            : Call<DeliveryOrderListBean>

    @POST("interior/salesman/outGainOrderDetail")
    fun getHomeOneDetails(@QueryMap data:HashMap<String,String>)
            : Call<HomeOneDetailsBean>

    @POST("interior/salesman/getUploadedHousingList")
    fun getHousesList(@QueryMap data:HashMap<String,String>): Call<HousesListBean>

    @POST("interior/salesman/batchDispathOrder")
    fun updateTransferOrder(@Body body:RequestBody): Call<TransferOrderResponse>

    @POST("interior/houses/check/position")
    fun checkReach(@Body body:RequestBody): Call<ReachResponse>

    @POST("interior/salesman/customerCancelAppointment")
    fun cancelReservation(@Body body:RequestBody): Call<TransferOrderBean>

    @POST("interior/salesman/customerCancelAppointment")
    fun cancelReservation1(@Body body:RequestBody,@Part file:MultipartBody.Part,
                           @Part file1:MultipartBody.Part,
                           @Part file2:MultipartBody.Part,
                           @Part file3:MultipartBody.Part,
                           @Part file4:MultipartBody.Part): Call<TransferOrderBean>

    @POST("interior/salesman/getUploadedHousingList")
    fun getHomeTwo(@Body body:RequestBody): Call<HomeTwoResponse>

    @POST("interior/salesman/grab/orders/list")
    fun getHomeThree(@Body body:RequestBody): Call<HomeThreeResponse>

    @POST("interior/salesman/contract/orders/list")
    fun getHomeFour(@Body body:RequestBody): Call<HomeFourResponse>

    @POST("interior/msg/getMsgList")
    fun getMessageList():Call<MessageListResponse>

    @POST("interior/msg/getMsgHistory")
    fun getMessagHistory(@Body body:RequestBody):Call<HistoryMessagesResponse>

    @POST("interior/msg/getMsgType")
    fun getMessagType(@Body body:RequestBody):Call<MessagesTypeResponse>

    @POST("interior/msg/getMemMsgSetting")
    fun getMessagSettings():Call<MessagesSettingsResponse>

    @POST("interior/msg/setMemMsgSetting")
    fun setMessagSettings(@Body body:RequestBody):Call<LoginResponse>

    @POST("interior/salesman/contract/orders/records")
    fun getDeliveryOrder(@Body body:RequestBody): Call<DeliveryOrderResponse>

    @POST("interior/salesman/my/brokerages")
    fun getBrokerages(@Body body:RequestBody): Call<BrokeragesReponse>

    @POST("interior/salesman/grab/order/action")
    fun grabOrder(@Body body:RequestBody): Call<TransferOrderBean>

    @POST("interior/salesman/query/attendance")
    fun getAttendance(): Call<AttendanceResponse>

    @POST("interior/admin/password/update")
    fun updatePwd(@Body body:RequestBody): Call<UpdatePwdResponse>

    @POST("interior/salesman/update/pool/info")
    fun update(@Body body:RequestBody): Call<UpdateResponse>

    @POST("interior/salesman/my/keys")
    fun getKeys(): Call<KeyResponse>

    @POST("interior/salesman/check/key/isExpire")
    fun checkQrcode(@Body body:RequestBody): Call<LoginResponse>

    @POST("interior/salesman/get/house/key")
    fun getCheckQrcode(@Body body:RequestBody): Call<LoginResponse>

    @POST("interior/salesman/outGainOrderDetail")
    fun getDeliveryDetails(@Body body:RequestBody): Call<DeliveryDetailsResponse>

    @POST("interior/salesman/getApplicationDetails")
    fun getListingDidate(@Body body:RequestBody): Call<ListingDidateResponse>

    @POST("exterior/get/dict/{type}")
    fun getListingConfigResponse(@Path("type") type:String): Call<ListingConfigResponse>

    @GET("geocode/regeo")
    fun getGaodeLocation(@Query("key") keu:String,
                         @Query("location") location:String): Call<GaodeLocationResponse>

    @POST("{type}")
    fun checkQRCode(@Path("type") type:String):Call<LoginResponse>

    @POST("exterior/support/cities")
    fun getCity(): Call<CityResponse>

    @POST("interior/salesman/getUploadHouseDetail")
    fun getTwoDidate(@Body body:RequestBody): Call<UploadResponse>

    @POST("interior/salesman/uploadHousing")
    fun uploadListing(@Body body:RequestBody): Call<ListingDidateResponse>

    @POST("interior/salesman/updateUploadedHouse")
    fun updateListing(@Body body:RequestBody):Call<LoginResponse>

    @POST("interior/file/upload")
    fun uploadImg(@Body body:RequestBody):Call<FileResponse>

    @POST("interior/salesman/arriveAtCustomer")
    fun arrivalsLocation(@Body body:RequestBody):Call<LoginResponse>

    @POST("maps/api/geocode/json")
    fun requestLocation(@QueryMap body:HashMap<String,String>):Call<LocationResponse>


    @POST("interior/salesman/check/show/qrcode")
    fun showQrCode(@Body body:RequestBody):Call<LoginResponse>

    @POST("interior/salesman/finishedContractTask")
    fun complete(@Body body:RequestBody):Call<LoginResponse>

}