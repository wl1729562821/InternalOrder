package cn.yc.library.service

import android.content.Context
import android.util.Log
import cn.yc.library.R
import cn.yc.library.api.CookieManger
import cn.yc.library.api.OrderApi
import cn.yc.library.bean.PageBean
import cn.yc.library.bean.TransferOrderResponse
import cn.yc.library.bean.request.CancelReservationRequest
import cn.yc.library.bean.request.ListingRequest
import cn.yc.library.bean.request.TransferOrderRequest
import cn.yc.library.bean.response.*
import cn.yc.library.data.PageData
import cn.yc.library.listener.BaseListener
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.utils.addFormDataPart1
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class OrderService(listener: BaseListener?, context: Context) : BaseService(listener, context){

    fun getOutGainDispatchOrder(pageBean: PageBean) {
        val api = mRetrofit.create(OrderApi::class.java)
        val call = api.getOutGainDispatchOrder(HashMap<String, String>().apply {
            put("pageIndex", "${pageBean.pageIndex}")
            put("pageSize", "${pageBean.pageSize}")
            put("languageVersion", "0")
        })
        call.enqueue(object : Callback<DeliveryOrderListBean> {
            override fun onFailure(call: Call<DeliveryOrderListBean>?, t: Throwable?) {
                mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                        Throwable(mContext.getString(R.string.request_error4)))
            }

            override fun onResponse(c: Call<DeliveryOrderListBean>?, response: Response<DeliveryOrderListBean>?) {
                val body = response?.body()
                Log.e("Activity", "getOutGainDispatchOrder onResponse $body")
                val check=checkLogin(body?.result,object : BaseListener {
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getOutGainDispatchOrder(pageBean)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<DeliveryOrderListBean>().apply {
                        data = body
                        code = if (pageBean.pull) {
                            DataConfig.REFRESH_PULL
                        } else {
                            DataConfig.REFRESH_MORE
                        }
                    })
                } else {
                    val msg=body?.message?:mContext.getString(R.string.request_error4)
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(msg))
                }
            }
        })
    }

    fun checkQRCode(path:String) {
        val mRetrofit= Retrofit.Builder()
                //.baseUrl("http://192.168.0.120:8080/")
                .baseUrl("") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .client(OkHttpClient().newBuilder().apply {
                    cookieJar(CookieManger(mContext))
                    //addInterceptor(AddCookiesInterceptor(mContext))
                    //addInterceptor(SaveCookiesInterceptor(mContext))
                    connectTimeout(10, TimeUnit.SECONDS)
                    readTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(10, TimeUnit.SECONDS)
                }.build())
                .build()
        val api = mRetrofit.create(OrderApi::class.java)
        val call = api.checkQRCode(path)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                        Throwable(mContext.getString(R.string.request_error4)))
            }

            override fun onResponse(c: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                val body = response?.body()
                Log.e("Activity", "checkQRCode onResponse $body")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        checkQRCode(path)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(body)
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getHomeOneDetails(taskId: String, applyId: String, poolId: String) {
        Log.e("service","getHomeOneDetails $taskId $applyId $poolId")
        val api = mRetrofit.create(OrderApi::class.java)
        val call = api.getHomeOneDetails(HashMap<String, String>().apply {
            put("taskId", taskId)
            put("applyId", applyId)
            put("poolId", poolId)
        })
        call.enqueue(object : Callback<HomeOneDetailsBean> {
            override fun onFailure(call: Call<HomeOneDetailsBean>?, t: Throwable?) {
                mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                        Throwable(mContext.getString(R.string.request_error4)))
            }

            override fun onResponse(c: Call<HomeOneDetailsBean>?, response: Response<HomeOneDetailsBean>?) {
                val body = response?.body()
                Log.e("Activity", "getOutGainDispatchOrder onResponse $body")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getHomeOneDetails(taskId, applyId, poolId)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.dataSet != null) {
                    mListener?.onNext(BaseResponse<HomeOneDetailsBean>().apply {
                        data = body
                        code=DataConfig.REFRESH_SUCCESS
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getHousesList() {
        val api = mRetrofit.create(OrderApi::class.java)
        val call = api.getHousesList(HashMap<String, String>().apply {
            put("pageIndex", "1")
            put("pageSize", "10")
            put("languageVersion", "0")
        })
        call.enqueue(object : Callback<HousesListBean> {
            override fun onFailure(call: Call<HousesListBean>?, t: Throwable?) {
                //mListener?.onError(Throwable(t?.message?:""))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<HousesListBean>?, response: Response<HousesListBean>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                //mListener?.onNext(loginResponse)
            }
        })
    }

    fun updateTransferOrder(request: TransferOrderRequest) {
        Log.e("Activity","updateTransferOrder $request")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart1("ids", request.ids)
                    addFormDataPart1("remark", request.remark)
                    addFormDataPart1("taskType", request.taskType)
                    //addFormDataPart1("times", request.times)
                    addFormDataPart1("proofPic1",request.proofPic1)
                    addFormDataPart1("proofPic2",request.proofPic2)
                    addFormDataPart1("proofPic3",request.proofPic3)
                    addFormDataPart1("proofPic4",request.proofPic4)
                    addFormDataPart1("proofPic5",request.proofPic5)
                    addFormDataPart1("transferOrderReason", request.transferOrderReason)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.updateTransferOrder(requestBody)
        call.enqueue(object : Callback<TransferOrderResponse> {
            override fun onFailure(call: Call<TransferOrderResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<TransferOrderResponse>?, response:
            Response<TransferOrderResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        updateTransferOrder(request)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true) {
                    mListener?.onNext(BaseResponse<TransferOrderResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }
    fun cancelReservation(request: CancelReservationRequest) {
        Log.e("Activity","updateTransferOrder $request")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart1("cancelType", request.cancelType)
                    addFormDataPart1("feedbackDesc", request.feedbackDesc)
                    addFormDataPart1("feedbackType", request.feedbackType)
                    addFormDataPart1("taskId", request.taskId)
                    addFormDataPart1("taskType", request.taskType)
                    addFormDataPart1("proofPic1",request.proofPic1)
                    addFormDataPart1("proofPic2",request.proofPic2)
                    addFormDataPart1("proofPic3",request.proofPic3)
                    addFormDataPart1("proofPic4",request.proofPic4)
                    addFormDataPart1("proofPic5",request.proofPic5)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.cancelReservation(requestBody)
        call.enqueue(object : Callback<TransferOrderBean> {
            override fun onFailure(call: Call<TransferOrderBean>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<TransferOrderBean>?, response: Response<TransferOrderBean>?) {
                val body = response?.body()
                Log.e("service","onResponse $body")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        cancelReservation(request)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<TransferOrderBean>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getHomeTwo(pageData: PageData) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("pageIndex", "${pageData.pageIndex}")
                    addFormDataPart("pageSize", "${pageData.pageSize}")
                    addFormDataPart("languageVersion", "0")
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getHomeTwo(requestBody)
        call.enqueue(object : Callback<HomeTwoResponse> {
            override fun onFailure(call: Call<HomeTwoResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<HomeTwoResponse>?, response: Response<HomeTwoResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getHomeTwo(pageData)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<HomeTwoResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getMessageList() {
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getMessageList()
        call.enqueue(object : Callback<MessageListResponse> {
            override fun onFailure(call: Call<MessageListResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<MessageListResponse>?, response: Response<MessageListResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getMessageList()
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<MessageListResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getMessagHistory(msgCode:String) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart1("msgCode", msgCode)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getMessagHistory(requestBody)
        call.enqueue(object : Callback<HistoryMessagesResponse> {
            override fun onFailure(call: Call<HistoryMessagesResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<HistoryMessagesResponse>?, response: Response<HistoryMessagesResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getMessagHistory(msgCode)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<HistoryMessagesResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getMessagType(type:String) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart1("type",type)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getMessagType(requestBody)
        call.enqueue(object : Callback<MessagesTypeResponse> {
            override fun onFailure(call: Call<MessagesTypeResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<MessagesTypeResponse>?, response: Response<MessagesTypeResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getMessagType(type)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<MessagesTypeResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getMessagSettings() {
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getMessagSettings()
        call.enqueue(object : Callback<MessagesSettingsResponse> {
            override fun onFailure(call: Call<MessagesSettingsResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<MessagesSettingsResponse>?, response: Response<MessagesSettingsResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getMessagSettings()
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true) {
                    mListener?.onNext(BaseResponse<MessagesSettingsResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun setMessagSettings(userName:String,openCode:String,id:String) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart1("userName",userName)
                    addFormDataPart1("openCode",openCode)
                    addFormDataPart1("id",id)
                    addFormDataPart1("platform","2")
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.setMessagSettings(requestBody)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }
            override fun onResponse(c: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        setMessagSettings(userName,openCode,id)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true) {
                    mListener?.onNext(BaseResponse<LoginResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun checkReach(houseId:String,lon:String,lat:String) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart1("houseId",houseId)
                    addFormDataPart1("lon",lon)
                    addFormDataPart1("lat",lat)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.checkReach(requestBody)
        call.enqueue(object : Callback<ReachResponse> {
            override fun onFailure(call: Call<ReachResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<ReachResponse>?, response: Response<ReachResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        checkReach(houseId,lon,lat)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true) {
                    mListener?.onNext(BaseResponse<ReachResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getHomeThree(pageData: PageData) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("pageIndex", pageData.pageIndex.toString())
                    addFormDataPart("pageSize", pageData.pageSize.toString())
                    addFormDataPart("languageVersion", "0")
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getHomeThree(requestBody)
        call.enqueue(object : Callback<HomeThreeResponse> {
            override fun onFailure(call: Call<HomeThreeResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<HomeThreeResponse>?, response: Response<HomeThreeResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getHomeThree(pageData)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<HomeThreeResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getHomeFour() {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("pageIndex", "1")
                    addFormDataPart("pageSize", "200")
                    addFormDataPart("languageVersion", "0")
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getHomeFour(requestBody)
        call.enqueue(object : Callback<HomeFourResponse> {
            override fun onFailure(call: Call<HomeFourResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<HomeFourResponse>?, response: Response<HomeFourResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getHomeFour()
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true || body?.result==0 ||body?.dataSet?.isNotEmpty()==true) {
                    mListener?.onNext(BaseResponse<HomeFourResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getDeliveryOrder() {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("pageIndex", "1")
                    addFormDataPart("pageSize", "200")
                    addFormDataPart("languageVersion", "0")
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getDeliveryOrder(requestBody)
        call.enqueue(object : Callback<DeliveryOrderResponse> {
            override fun onFailure(call: Call<DeliveryOrderResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<DeliveryOrderResponse>?, response: Response<DeliveryOrderResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getDeliveryOrder()
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<DeliveryOrderResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun grabOrder(id:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("poolId", id)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.grabOrder(requestBody)
        call.enqueue(object : Callback<TransferOrderBean> {
            override fun onFailure(call: Call<TransferOrderBean>?, t: Throwable?) {
                mListener?.onError(20,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<TransferOrderBean>?, response: Response<TransferOrderBean>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        grabOrder(id)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<TransferOrderBean>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(20,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getAttendance(){
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getAttendance()
        call.enqueue(object : Callback<AttendanceResponse> {
            override fun onFailure(call: Call<AttendanceResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<AttendanceResponse>?, response: Response<AttendanceResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getAttendance()
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<AttendanceResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun updatePwd(oldPwd:String,pwd:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("oldPassword",oldPwd)
                    addFormDataPart("password",pwd)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.updatePwd(requestBody)
        call.enqueue(object : Callback<UpdatePwdResponse> {
            override fun onFailure(call: Call<UpdatePwdResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<UpdatePwdResponse>?, response: Response<UpdatePwdResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        updatePwd(oldPwd,pwd)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<UpdatePwdResponse>().apply {
                        data = body
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }
    fun update(time:String,address:String,poolId:String){
        Log.e("Service","update $time $address $poolId")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart1("poolId",poolId)
                    addFormDataPart1("appointmentMeetPlace",address)
                    addFormDataPart1("estimatedTime",time)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.update(requestBody)
        call.enqueue(object : Callback<UpdateResponse> {
            override fun onFailure(call: Call<UpdateResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<UpdateResponse>?, response: Response<UpdateResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        update(time,address,poolId)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<UpdateResponse>().apply {
                        data = body
                        code=221
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getBrokerages(pageIndex:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("type","2")
                    addFormDataPart("pageIndex",pageIndex)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getBrokerages(requestBody)
        call.enqueue(object : Callback<BrokeragesReponse> {
            override fun onFailure(call: Call<BrokeragesReponse>?, t: Throwable?) {
                mListener?.onError(1011,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<BrokeragesReponse>?, response: Response<BrokeragesReponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getBrokerages(pageIndex)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(body)
                } else {
                    val msg=mContext.getString(R.string.request_error3)
                    mListener?.onError(0,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getKey(){
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getKeys()
        call.enqueue(object : Callback<KeyResponse> {
            override fun onFailure(call: Call<KeyResponse>?, t: Throwable?) {
                mListener?.onError(1011,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<KeyResponse>?, response: Response<KeyResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getKey()
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<KeyResponse>().apply {
                        data = body
                    })
                } else {
                    val msg=body?.message?:"Loading failed, please try again later"
                    mListener?.onError(1011,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun checkQrcode(houseId:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("houseId",houseId)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.checkQrcode(requestBody)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        checkQrcode(houseId)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<LoginResponse>().apply {
                        data = body
                    })
                } else {
                    val msg=body?.message?:"Loading failed, please try again later"
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }
    fun getCheckQrcode(houseId:String,houseCode:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("houseId",houseId)
                    addFormDataPart("houseCode",houseCode)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getCheckQrcode(requestBody)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getCheckQrcode(houseId,houseCode)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<LoginResponse>().apply {
                        data = body
                    })
                } else {
                    val msg=body?.message?:"Loading failed, please try again later"
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getListingDidate(applyId:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("applyId",applyId)
                    addFormDataPart("applyId",applyId)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getListingDidate(requestBody)
        call.enqueue(object : Callback<ListingDidateResponse> {
            override fun onFailure(call: Call<ListingDidateResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<ListingDidateResponse>?, response: Response<ListingDidateResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getListingDidate(applyId)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<ListingDidateResponse>().apply {
                        data = body
                        messageId=if(body?.dataSet!=null){
                            R.string.request_success
                        }else{
                            R.string.request_empty_success
                        }
                    })
                } else {
                    val msg=mContext.getString(R.string.request_error)
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getTwoDetails(applyId:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("houseId",applyId)
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getTwoDidate(requestBody)
        call.enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<UploadResponse>?, response: Response<UploadResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getTwoDetails(applyId)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<UploadResponse>().apply {
                        data = body
                        messageId=if(body?.dataSet!=null){
                            R.string.request_success
                        }else{
                            R.string.request_empty_success
                        }
                    })
                } else {
                    val msg=mContext.getString(R.string.request_error)
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getCity(){
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getCity()
        call.enqueue(object : Callback<CityResponse> {
            override fun onFailure(call: Call<CityResponse>?, t: Throwable?) {
                mListener?.onError(4,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<CityResponse>?, response: Response<CityResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getCity()
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<CityResponse>().apply {
                        data = body
                        messageId=if(body?.dataSet!=null){
                            R.string.request_success
                        }else{
                            R.string.request_empty_success
                        }
                    })
                } else {
                    val msg=mContext.getString(R.string.request_error)
                    mListener?.onError(4,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getListingConfigResponse(type:String){
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.getListingConfigResponse(type)
        call.enqueue(object : Callback<ListingConfigResponse> {
            override fun onFailure(call: Call<ListingConfigResponse>?, t: Throwable?) {
                mListener?.onError(4,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<ListingConfigResponse>?, response: Response<ListingConfigResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getListingConfigResponse(type)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<ListingConfigResponse>().apply {
                        data = body
                        messageId=if(body?.dataSet!=null){
                            R.string.request_success
                        }else{
                            R.string.request_empty_success
                        }
                    })
                } else {
                    val msg=mContext.getString(R.string.request_error)
                    mListener?.onError(4,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun uploadListing(request: ListingRequest){
        Log.e("service","uploadListing $request")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart1("taskId",request.taskId)//是//任务ID
                    addFormDataPart1("languageVersion",request.languageVersion)//是//语言版本（0：中文，1：英文）默认为0
                    addFormDataPart1("applicantType",request.applicantType)//是//申请人类型：0业主 1 poa
                    addFormDataPart1("leaseType",request.leaseType)//是//预约类型（0：出租，1：出售）
                    //addFormDataPart1("applyType",request.applyType)//是//申请类型（0：自主完善，1：联系客服上传，2：业务员上传）
                    //addFormDataPart1("houseHoldImg",request.houseHoldImg)//是//房屋类型（0：出租，1：出售）
                    addFormDataPart1("housingTypeDictcode",request.housingTypeDictcode)//是//房屋类型 ，查询数据字典
                    addFormDataPart1("city",request.city)//是//城市
                    //addFormDataPart1("applyId",request.applyId)
                    addFormDataPart1("community",request.community)//是//社区
                    addFormDataPart1("subCommunity",request.subCommunity)//是//子社区
                    //addFormDataPart1("property","hhre")//是//项目
                    addFormDataPart1("address",request.address)//是//房源所在地址
                    addFormDataPart1("longitude",request.longitude)//是//经度
                    addFormDataPart1("latitude",request.latitude)//是//纬度
                    addFormDataPart1("phoneNumber",request.phoneNumber)//是//手机号
                    addFormDataPart1("houseId",request.houseId)
                    addFormDataPart1("houseSelfContainedDictcode",request.houseSelfContainedDictcode)
                    addFormDataPart1("villageName",request.villageName)//是//小区名称
                    addFormDataPart1("buildingName",request.buildingName)//是//楼名/别墅名
                    addFormDataPart1("houseUnitNo",request.houseUnitNo)//是//单元号
                    addFormDataPart1("houseFloor",request.houseFloor)//是//楼层
                    addFormDataPart1("roomName",request.roomName)//是//门牌号
                    addFormDataPart1("houseAcreage",request.houseAcreage)//是//房屋面积
                    addFormDataPart1("parkingSpace",request.parkingSpace)//是//停车位
                    addFormDataPart1("bathroomNum",request.bathroomNum)//是//浴室数量
                    addFormDataPart1("bedroomNum",request.bedroomNum)//是//卧室数量
                    addFormDataPart1("houseDecorationDictcode",request.houseDecorationDictcode)//是//房屋装修 0：带家具，1：不带家具
                    addFormDataPart1("houseConfigDictcode",request.houseConfigDictcode)//是//房源配置，ids
                    addFormDataPart1("housingStatus",request.housingStatus)//是//房屋状态（0：空房，1：出租，2：自住，3：准现房）
                    //出租情况下房源状态默认是现房1
                    addFormDataPart1("isPromissoryBuild",if(request.leaseType=="0"){ "1"}else{
                        "${request.isPromissoryBuild}"
                    })//是//房源状态：0>期房，1>现房
                    //addFormDataPart("contacts",request.contacts)//是//联系人
                    if(request.email.isNotBlank()){
                        addFormDataPart1("email",request.email)//是//邮箱
                    }
                    if(request.facebook.isNotBlank()){
                        addFormDataPart1("facebook",request.facebook)//是//facebook
                    }
                    if(request.twitter.isNotBlank()){
                        addFormDataPart1("twitter",request.twitter)//是//twitter
                    }
                    if(request.instagram.isNotBlank()){
                        addFormDataPart1("instagram",request.instagram)//是//instagram
                    }
                    addFormDataPart("appointmentLookTime",request.appointmentLookTime)
                    addFormDataPart1("houseRent",request.houseRent)//是//期望租金/或出售价
                    addFormDataPart1("minHouseRent",request.minHouseRent)//是//最低租金/或出售价
                    addFormDataPart1("setting",request.setting)//是//自动应答参数
                    if(request.leaseType=="0"){//出租
                        addFormDataPart1("startRentDate",request.startRentDate)//是//起租日期
                        addFormDataPart1("payNode",request.payNode)//是//支付节点 , 1....12/月
                    }else{
                        addFormDataPart1("plotNumber",request.plotNumber)
                        addFormDataPart1("titleDeedNumber",request.titleDeedNumber)
                        addFormDataPart1("propertyNumber",request.propertyNumber)
                        addFormDataPart1("masterDevelpoerName",request.masterDevelpoerName)
                        addFormDataPart1("typeOfArea",request.typeOfArea)

                        addFormDataPart1("houseHoldImg1",request.houseHoldImg1)
                        addFormDataPart1("houseHoldImg2",request.houseHoldImg2)
                        addFormDataPart1("houseHoldImg3",request.houseHoldImg3)
                        addFormDataPart1("houseHoldImg4",request.houseHoldImg4)
                        addFormDataPart1("houseHoldImg5",request.houseHoldImg5)
                        addFormDataPart1("houseHoldImg6",request.houseHoldImg6)
                        addFormDataPart1("houseHoldImg7",request.houseHoldImg7)
                        addFormDataPart1("houseHoldImg8",request.houseHoldImg8)
                        addFormDataPart1("houseHoldImg9",request.houseHoldImg9)
                        addFormDataPart1("houseHoldImg10",request.houseHoldImg10)
                        addFormDataPart1("isHouseLoan",request.isHouseLoan)//是//是否有房贷：0>无，1>有
                    }
                    addFormDataPart1("pocImg1",request.pocImg1)
                    addFormDataPart1("pocImg2",request.pocImg2)
                    addFormDataPart1("pocImg3",request.pocImg3)

                    addFormDataPart1("reoPassportImg1",request.reoPassportImg1)
                    addFormDataPart1("reoPassportImg2",request.reoPassportImg2)
                    addFormDataPart1("reoPassportImg3",request.reoPassportImg3)
                    //poa
                    if(request.applicantType=="1"){
                        addFormDataPart1("mandataryCopiesImg1",request.mandataryCopiesImg1)
                        addFormDataPart1("mandataryCopiesImg2",request.mandataryCopiesImg2)
                        addFormDataPart1("mandataryCopiesImg3",request.mandataryCopiesImg3)
                        addFormDataPart1("mandataryCopiesImg4",request.mandataryCopiesImg4)
                        addFormDataPart1("mandataryCopiesImg5",request.mandataryCopiesImg5)
                        addFormDataPart1("mandataryCopiesImg6",request.mandataryCopiesImg6)
                        addFormDataPart1("mandataryCopiesImg7",request.mandataryCopiesImg7)
                        addFormDataPart1("mandataryCopiesImg8",request.mandataryCopiesImg8)
                        addFormDataPart1("mandataryCopiesImg9",request.mandataryCopiesImg9)
                        addFormDataPart1("mandataryCopiesImg10",request.mandataryCopiesImg10)

                        addFormDataPart1("mandataryPassportImg1",request.mandataryPassportImg1)
                        addFormDataPart1("mandataryPassportImg2",request.mandataryPassportImg2)
                        addFormDataPart1("mandataryPassportImg3",request.mandataryPassportImg3)

                        addFormDataPart1("mandataryVisaImg1",request.mandataryVisaImg1)
                        addFormDataPart1("mandataryVisaImg2",request.mandataryVisaImg2)
                        addFormDataPart1("mandataryVisaImg3",request.mandataryVisaImg3)

                        addFormDataPart1("mandataryIdcardImg1",request.mandataryIdcardImg1)
                        addFormDataPart1("mandataryIdcardImg2",request.mandataryIdcardImg2)
                        addFormDataPart1("mandataryIdcardImg3",request.mandataryIdcardImg3)
                        addFormDataPart1("mandataryIdcardImg4",request.mandataryIdcardImg4)

                        if(request.leaseType=="0"){
                            addFormDataPart1("rentAuthorizationSignImg1",request.rentAuthorizationSignImg1)
                            addFormDataPart1("rentAuthorizationSignImg2",request.rentAuthorizationSignImg2)
                            addFormDataPart1("rentAuthorizationSignImg3",request.rentAuthorizationSignImg3)
                        }else{
                            addFormDataPart1("formaConfirmImg1",request.formaConfirmImg1)
                            addFormDataPart1("formaConfirmImg2",request.formaConfirmImg2)
                            addFormDataPart1("formaConfirmImg3",request.formaConfirmImg3)
                        }

                    }else{
                        //出租
                        if(request.leaseType=="0"){
                            addFormDataPart1("rentAuthorizationSignImg1",request.rentAuthorizationSignImg1)
                            addFormDataPart1("rentAuthorizationSignImg2",request.rentAuthorizationSignImg2)
                            addFormDataPart1("rentAuthorizationSignImg3",request.rentAuthorizationSignImg3)
                        }else{
                            addFormDataPart1("formaConfirmImg1",request.formaConfirmImg1)
                            addFormDataPart1("formaConfirmImg2",request.formaConfirmImg2)
                            addFormDataPart1("formaConfirmImg3",request.formaConfirmImg3)
                        }
                    }
                    addFormDataPart1("isCustomerServiceRelation",request.isCustomerServiceRelation)//是//预约时间设置，是否客服联系（0：否，1：是）
                    addFormDataPart1("advanceReservationDay",request.advanceReservationDay)//是//提前几天预约（默认为0）
                    addFormDataPart1("haveKey",request.haveKey)//是//是否有钥匙：0>无,1有（有选择是否交钥匙时才传值，否则该参数不用传）
                    addFormDataPart1("rentCustomerName",request.rentCustomerName)//是//租客姓名
                    addFormDataPart1("rentCustomerPhone",request.rentCustomerPhone)//是//租客电话
                    addFormDataPart1("houseRentContractImg1",request.houseRentContractImg1)//是//房屋租赁合同图片
                    addFormDataPart1("houseRentContractImg2",request.houseRentContractImg2)//是//房屋租赁合同图片
                    addFormDataPart1("houseRentContractImg3",request.houseRentContractImg3)//是//房屋租赁合同图片
                    addFormDataPart1("houseRentContractImg4",request.houseRentContractImg4)//是//房屋租赁合同图片
                    addFormDataPart1("bargainHouseDate",request.bargainHouseDate)//是//预计交房日期
                    addFormDataPart1("remarks",request.remarks)//是//备注
                    addFormDataPart1("applyId",request.applyId)
                    request.isAutoAnswer=if(request.setting.isNotBlank()){
                        "1"
                    }else{
                        "0"
                    }
                    addFormDataPart1("isAutoAnswer",request.isAutoAnswer)//是//自动应答开关设置（0：关，1：开）
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.uploadListing(requestBody)
        call.enqueue(object : Callback<ListingDidateResponse> {
            override fun onFailure(call: Call<ListingDidateResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<ListingDidateResponse>?, response: Response<ListingDidateResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        uploadListing(request)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<ListingDidateResponse>().apply {
                        data = body
                        message="Request success"
                    })
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun updateListing(request:ListingRequest){
        Log.e("service","uploadListing $request")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    //addFormDataPart1("taskId",request.taskId)//是//任务ID
                    addFormDataPart1("id",request.houseId)
                    addFormDataPart1("languageVersion",request.languageVersion)//是//语言版本（0：中文，1：英文）默认为0
                    //addFormDataPart1("applicantType",request.applicantType)//是//申请人类型：0业主 1 poa
                    addFormDataPart1("leaseType",request.leaseType)//是//预约类型（0：出租，1：出售）
                    //addFormDataPart1("applyType",request.applyType)//是//申请类型（0：自主完善，1：联系客服上传，2：业务员上传）
                    //addFormDataPart1("houseHoldImg",request.houseHoldImg)//是//房屋类型（0：出租，1：出售）
                    addFormDataPart1("housingTypeDictcode",request.housingTypeDictcode)//是//房屋类型 ，查询数据字典
                    addFormDataPart1("city",request.city)//是//城市
                    //addFormDataPart1("applyId",request.applyId)
                    addFormDataPart1("community",request.community)//是//社区
                    addFormDataPart1("subCommunity","hehe")//是//子社区
                    addFormDataPart1("property","hhre")//是//项目
                    addFormDataPart1("address",request.address)//是//房源所在地址
                    addFormDataPart1("longitude",request.longitude)//是//经度
                    addFormDataPart1("latitude",request.latitude)//是//纬度
                    addFormDataPart1("phoneNumber",request.phoneNumber)//是//手机号
                    addFormDataPart1("villageName",request.villageName)//是//小区名称
                    addFormDataPart1("buildingName",request.buildingName)//是//楼名/别墅名
                    addFormDataPart1("houseUnitNo",request.houseUnitNo)//是//单元号
                    addFormDataPart1("houseFloor",request.houseFloor)//是//楼层
                    addFormDataPart1("roomName",request.roomName)//是//门牌号
                    addFormDataPart1("houseSelfContainedDictcode",request.houseSelfContainedDictcode)
                    addFormDataPart1("houseAcreage",request.houseAcreage)//是//房屋面积
                    addFormDataPart1("parkingSpace",request.parkingSpace)//是//停车位
                    addFormDataPart1("bathroomNum",request.bathroomNum)//是//浴室数量
                    addFormDataPart1("bedroomNum",request.bedroomNum)//是//卧室数量
                    addFormDataPart1("houseDecorationDictcode",request.houseDecorationDictcode)//是//房屋装修 0：带家具，1：不带家具
                    addFormDataPart1("houseConfigDictcode",request.houseConfigDictcode)//是//房源配置，ids
                    addFormDataPart1("housingStatus",request.housingStatus)//是//房屋状态（0：空房，1：出租，2：自住，3：准现房）
                    addFormDataPart1("isPromissoryBuild","${request.isPromissoryBuild}")//是//房源状态：0>期房，1>现房
                    addFormDataPart1("applyId",request.applyId)
                    //addFormDataPart("contacts",request.contacts)//是//联系人
                    if(request.email.isNotBlank()){
                        addFormDataPart1("email",request.email)//是//邮箱
                    }
                    if(request.facebook.isNotBlank()){
                        addFormDataPart1("facebook",request.facebook)//是//facebook
                    }
                    if(request.twitter.isNotBlank()){
                        addFormDataPart1("twitter",request.twitter)//是//twitter
                    }
                    if(request.instagram.isNotBlank()){
                        addFormDataPart1("instagram",request.instagram)//是//instagram
                    }
                    addFormDataPart("appointmentLookTime",request.appointmentLookTime)
                    addFormDataPart1("houseRent",request.houseRent)//是//期望租金/或出售价
                    addFormDataPart1("minHouseRent",request.minHouseRent)//是//最低租金/或出售价
                    addFormDataPart1("setting",request.setting)//是//自动应答参数
                    if(request.leaseType=="0"){//出租
                        addFormDataPart1("startRentDate",request.startRentDate)//是//起租日期
                        addFormDataPart1("payNode",request.payNode)//是//支付节点 , 1....12/月
                    }else{
                        addFormDataPart1("plotNumber",request.plotNumber)
                        addFormDataPart1("titleDeedNumber",request.titleDeedNumber)
                        addFormDataPart1("propertyNumber",request.propertyNumber)
                        addFormDataPart1("masterDevelpoerName",request.masterDevelpoerName)
                        addFormDataPart1("typeOfArea",request.typeOfArea)

                        addFormDataPart1("houseHoldImg1",request.houseHoldImg1)
                        addFormDataPart1("houseHoldImg2",request.houseHoldImg2)
                        addFormDataPart1("houseHoldImg3",request.houseHoldImg3)
                        addFormDataPart1("houseHoldImg4",request.houseHoldImg4)
                        addFormDataPart1("houseHoldImg5",request.houseHoldImg5)
                        addFormDataPart1("houseHoldImg6",request.houseHoldImg6)
                        addFormDataPart1("houseHoldImg7",request.houseHoldImg7)
                        addFormDataPart1("houseHoldImg8",request.houseHoldImg8)
                        addFormDataPart1("houseHoldImg9",request.houseHoldImg9)
                        addFormDataPart1("houseHoldImg10",request.houseHoldImg10)
                        addFormDataPart1("isHouseLoan",request.isHouseLoan)//是//是否有房贷：0>无，1>有
                    }
                    addFormDataPart1("pocImg1",request.pocImg1)
                    addFormDataPart1("pocImg2",request.pocImg2)
                    addFormDataPart1("pocImg3",request.pocImg3)

                    addFormDataPart1("reoPassportImg1",request.reoPassportImg1)
                    addFormDataPart1("reoPassportImg2",request.reoPassportImg2)
                    addFormDataPart1("reoPassportImg3",request.reoPassportImg3)
                    //poa
                    if(request.applicantType=="1"){
                        addFormDataPart1("mandataryCopiesImg1",request.mandataryCopiesImg1)
                        addFormDataPart1("mandataryCopiesImg2",request.mandataryCopiesImg2)
                        addFormDataPart1("mandataryCopiesImg3",request.mandataryCopiesImg3)
                        addFormDataPart1("mandataryCopiesImg4",request.mandataryCopiesImg4)
                        addFormDataPart1("mandataryCopiesImg5",request.mandataryCopiesImg5)
                        addFormDataPart1("mandataryCopiesImg6",request.mandataryCopiesImg6)
                        addFormDataPart1("mandataryCopiesImg7",request.mandataryCopiesImg7)
                        addFormDataPart1("mandataryCopiesImg8",request.mandataryCopiesImg8)
                        addFormDataPart1("mandataryCopiesImg9",request.mandataryCopiesImg9)
                        addFormDataPart1("mandataryCopiesImg10",request.mandataryCopiesImg10)

                        addFormDataPart1("mandataryPassportImg1",request.mandataryPassportImg1)
                        addFormDataPart1("mandataryPassportImg2",request.mandataryPassportImg2)
                        addFormDataPart1("mandataryPassportImg3",request.mandataryPassportImg3)

                        addFormDataPart1("mandataryVisaImg1",request.mandataryVisaImg1)
                        addFormDataPart1("mandataryVisaImg2",request.mandataryVisaImg2)
                        addFormDataPart1("mandataryVisaImg3",request.mandataryVisaImg3)

                        addFormDataPart1("mandataryIdcardImg1",request.mandataryIdcardImg1)
                        addFormDataPart1("mandataryIdcardImg2",request.mandataryIdcardImg2)
                        addFormDataPart1("mandataryIdcardImg3",request.mandataryIdcardImg3)
                        addFormDataPart1("mandataryIdcardImg4",request.mandataryIdcardImg4)

                        if(request.leaseType=="0"){
                            addFormDataPart1("rentAuthorizationSignImg1",request.rentAuthorizationSignImg1)
                            addFormDataPart1("rentAuthorizationSignImg2",request.rentAuthorizationSignImg2)
                            addFormDataPart1("rentAuthorizationSignImg3",request.rentAuthorizationSignImg3)
                        }else{
                            addFormDataPart1("formaConfirmImg1",request.formaConfirmImg1)
                            addFormDataPart1("formaConfirmImg2",request.formaConfirmImg2)
                            addFormDataPart1("formaConfirmImg3",request.formaConfirmImg3)
                        }

                    }else{
                        //出租
                        if(request.leaseType=="0"){
                            addFormDataPart1("rentAuthorizationSignImg1",request.rentAuthorizationSignImg1)
                            addFormDataPart1("rentAuthorizationSignImg2",request.rentAuthorizationSignImg2)
                            addFormDataPart1("rentAuthorizationSignImg3",request.rentAuthorizationSignImg3)
                        }else{
                            addFormDataPart1("formaConfirmImg1",request.formaConfirmImg1)
                            addFormDataPart1("formaConfirmImg2",request.formaConfirmImg2)
                            addFormDataPart1("formaConfirmImg3",request.formaConfirmImg3)
                        }
                    }
                    addFormDataPart1("isCustomerServiceRelation",request.isCustomerServiceRelation)//是//预约时间设置，是否客服联系（0：否，1：是）
                    addFormDataPart1("advanceReservationDay",request.advanceReservationDay)//是//提前几天预约（默认为0）
                    addFormDataPart1("rentCustomerName",request.rentCustomerName)//是//租客姓名
                    addFormDataPart1("rentCustomerPhone",request.rentCustomerPhone)//是//租客电话
                    addFormDataPart1("houseRentContractImg1",request.houseRentContractImg1)//是//房屋租赁合同图片
                    addFormDataPart1("houseRentContractImg2",request.houseRentContractImg2)//是//房屋租赁合同图片
                    addFormDataPart1("houseRentContractImg3",request.houseRentContractImg3)//是//房屋租赁合同图片
                    addFormDataPart1("houseRentContractImg4",request.houseRentContractImg4)//是//房屋租赁合同图片
                    addFormDataPart1("haveKey",request.haveKey)//是//是否有钥匙：0>无,1有（有选择是否交钥匙时才传值，否则该参数不用传）
                    addFormDataPart1("bargainHouseDate",request.bargainHouseDate)//是//预计交房日期
                    addFormDataPart1("remarks",request.remarks)//是//备注

                    request.isAutoAnswer=if(request.setting.isNotBlank()){
                        "1"
                    }else{
                        "0"
                    }
                    addFormDataPart1("isAutoAnswer",request.isAutoAnswer)//是//自动应答开关设置（0：关，1：开）
                }
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call = api.updateListing(requestBody)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        updateListing(request)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(body)
                } else {
                    val msg=body?.message?:"Loading failed, please try again later"
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun uploadImg(filePath:File){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            val file1=filePath//File(filePath)
            addFormDataPart("submitFile", file1.name,
                    RequestBody.create(MediaType.parse("image/*"), file1))
        }.build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call= api.uploadImg(requestBody)
        mCall=call
        call.enqueue(object : Callback<FileResponse> {
            override fun onFailure(call: Call<FileResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<FileResponse>?, response: Response<FileResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                (body?.state as? Int)?.apply {
                    val check=checkLogin(this,object :BaseListener{
                        override fun onError(code: Int, throwable: Throwable) {
                            startLoginActivity()
                        }

                        override fun <T> onNext(dat: T) {
                            uploadImg(filePath)
                        }
                    })
                    if(check){
                        return
                    }
                }
                if (response?.isSuccessful == true && body?.state=="0") {
                    mListener?.onNext(body)
                } else {
                    val msg=body?.message?:"Loading failed, please try again later"
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }}
        })
    }

    fun requestLocation(id:String,longitude:String,latitude:String,location:String) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            addFormDataPart1("id", id)
            addFormDataPart1("longitude", longitude)
            addFormDataPart1("latitude", latitude)
            addFormDataPart1("location", location)
        }.build()
        val api = mRetrofit.create(OrderApi::class.java)
        val call = api.arrivalsLocation(requestBody)
        mCall = call
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(0, Throwable(t?.message ?: mContext.getString(R.string.request_error3)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<LoginResponse>?,
                                    response: Response<LoginResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.isSuccessful} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        requestLocation(id,longitude,latitude,location)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true) {
                    val baseResponse = BaseResponse<LoginResponse>()
                    baseResponse.data = body
                    mListener?.onNext(baseResponse)
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun showQrcode(id:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            addFormDataPart("houseId",id)
        }.build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call= api.showQrCode(requestBody)
        mCall=call
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<LoginResponse>?,
                                    response: Response<LoginResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.isSuccessful} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        showQrcode(id)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true) {
                    val baseResponse=BaseResponse<LoginResponse>()
                    baseResponse.data=body
                    baseResponse.code=body?.result?:-1
                    baseResponse.message=body?.message?:""
                    mListener?.onNext(baseResponse)
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }}
        })
    }

    fun complete(taskId:String,holdContractPic:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            addFormDataPart1("taskId",taskId)
            addFormDataPart1("holdContractPic",holdContractPic)
        }.build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call= api.complete(requestBody)
        mCall=call
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<LoginResponse>?,
                                    response: Response<LoginResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.isSuccessful} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        complete(taskId,holdContractPic)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true) {
                    val baseResponse=BaseResponse<LoginResponse>()
                    baseResponse.data=body
                    baseResponse.code=body?.result?:-1
                    baseResponse.message=body?.message?:""
                    mListener?.onNext(baseResponse)
                } else {
                    val msg="Loading failed, please try again later"
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }}
        })
    }

    fun requestLocation(location:String){
        val mRetrofit= Retrofit.Builder()
                //.baseUrl("http://192.168.0.120:8080/")
                .baseUrl("https://maps.googleapis.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .client(OkHttpClient().newBuilder().apply {
                    cookieJar(CookieManger(mContext))
                    //addInterceptor(AddCookiesInterceptor(mContext))
                    //addInterceptor(SaveCookiesInterceptor(mContext))
                    connectTimeout(10, TimeUnit.SECONDS)
                    readTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(10, TimeUnit.SECONDS)
                }.build())
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val call= api.requestLocation(hashMapOf<String,String>().apply {
            put("language","zh_cn")
            put("key","AIzaSyCgrbyTtFjN4fc8rSPoACe-HGJmlsKZZ8s")
            put("latlng",location)
        })
        mCall=call
        call.enqueue(object : Callback<LocationResponse> {
            override fun onFailure(call: Call<LocationResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.location_error)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<LocationResponse>?,
                                    response: Response<LocationResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.isSuccessful} ${call?.request()?.url()?.toString()}")

                val baseResponse = BaseResponse<LocationResponse>()
                baseResponse.message = "Request success"
                baseResponse.data = body ?: LocationResponse()
                mListener?.onNext(baseResponse)
            }}
        )
    }

    fun getGaodeLocation(location:String){
        val mRetrofit= Retrofit.Builder()
                .baseUrl("https://restapi.amap.com/v3/")
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .client(OkHttpClient().newBuilder().apply {
                    cookieJar(CookieManger(mContext))
                    //addInterceptor(AddCookiesInterceptor(mContext))
                    //addInterceptor(SaveCookiesInterceptor(mContext))
                    connectTimeout(10, TimeUnit.SECONDS)
                    readTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(10, TimeUnit.SECONDS)
                }.build())
                .build()
        val api=mRetrofit.create(OrderApi::class.java)
        val key="9798de5b06464f25a9874feee2bc7946"
        val call= api.getGaodeLocation("9798de5b06464f25a9874feee2bc7946",
                location)
        mCall=call
        call.enqueue(object : Callback<GaodeLocationResponse> {
            override fun onFailure(call: Call<GaodeLocationResponse>?, t: Throwable?) {
                mListener?.onError(0,Throwable(mContext.getString(R.string.location_error)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<GaodeLocationResponse>?,
                                    response: Response<GaodeLocationResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.isSuccessful} ${call?.request()?.url()?.toString()}")

                if (response?.isSuccessful == true && body?.status=="1") {
                    val baseResponse=BaseResponse<GaodeLocationResponse>()
                    baseResponse.message="Request success"
                    baseResponse.data=body
                    mListener?.onNext(baseResponse)
                } else {
                    mListener?.onError(DataConfig.HOMEONE_ERROR_CODE,
                            Throwable(mContext.getString(R.string.location_error)))
                }}
        })
    }
}