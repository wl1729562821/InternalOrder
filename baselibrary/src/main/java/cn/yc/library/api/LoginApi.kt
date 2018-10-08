package cn.yc.library.api

import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.bean.response.PerformanceResponse
import cn.yc.library.bean.response.UpdatePwdResponse
import cn.yc.library.bean.response.UserInfoResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface LoginApi {
    @POST("interior/admin/login")
    fun login(@QueryMap data:HashMap<String,String>)
            :Call<LoginResponse>
    @POST("interior/admin/validateCode/sms/send")
    fun sendCode(@Body body: RequestBody): Call<UpdatePwdResponse>

    @POST("interior/admin/mobile/update")
    fun updatePhone(@Body body: RequestBody): Call<UpdatePwdResponse>

    @POST("interior/salesman/info")
    fun getUserInfo(): Call<UserInfoResponse>

    @POST("interior/salesman/getOutGainPersonalPerformance")
    fun getPerformance(): Call<PerformanceResponse>

    @POST("interior/admin/password/forget")
    fun forgetPassword(@Body body: RequestBody): Call<LoginResponse>

    @POST("interior/admin/validate/sms/code")
    fun checkCode(@Body body: RequestBody): Call<LoginResponse>

}