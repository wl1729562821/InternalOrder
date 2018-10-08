package cn.yc.library.service

import android.content.Context
import android.util.Log
import cn.yc.library.R
import cn.yc.library.api.LoginApi
import cn.yc.library.bean.response.*
import cn.yc.library.listener.BaseListener
import cn.yc.library.ui.config.DataConfig
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginService(listener: BaseListener?, context: Context):BaseService(listener,context){

    constructor(context: Context):this(null,context)

    fun login(name:String,password:String){

        val api=mRetrofit.create(LoginApi::class.java)
        val call=api.login(HashMap<String,String>().apply{
            put("username",name)
            put("password",password)
            put("roleType","0")
        })
        call.enqueue(object :Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(DataConfig.LOGIN_ERROR_CODE,Throwable(mContext.getString(R.string.request_error4)))
            }

            override fun onResponse(c: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                val body=response?.body()
                Log.e("Activity","headers  ${response?.headers()?.toString()}")
                Log.e("Actctivity","login ${body?.toString()}  $name  $password")
                val loginResponse=if(response?.raw()?.isSuccessful==true && body!=null){
                    body
                }else{
                    LoginResponse(body?.message?:mContext.getString(R.string.app_tv6))
                }
                mListener?.onNext(loginResponse)
            }
        })
    }

    fun sendCode(phone:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("areaCode","86")
                    addFormDataPart("mobile",phone)
                }
                .build()
        val api=mRetrofit.create(LoginApi::class.java)
        val call = api.sendCode(requestBody)
        call.enqueue(object : Callback<UpdatePwdResponse> {
            override fun onFailure(call: Call<UpdatePwdResponse>?, t: Throwable?) {
                mListener?.onError(DataConfig.SENDCODE_ERROR_CODE,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<UpdatePwdResponse>?, response: Response<UpdatePwdResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()} $phone")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        sendCode(phone)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<UpdatePwdResponse>().apply {
                        data = body
                        this.code=0
                    })
                } else {
                    val msg=body?.message?:"Failed to get verification code, please try again later"
                    mListener?.onError(DataConfig.SENDCODE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun updatePhone(phone:String,code:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("areaCode","86")
                    addFormDataPart("mobile",phone)
                    addFormDataPart("validateCode",code)
                }
                .build()
        val api=mRetrofit.create(LoginApi::class.java)
        val call = api.updatePhone(requestBody)
        call.enqueue(object : Callback<UpdatePwdResponse> {
            override fun onFailure(call: Call<UpdatePwdResponse>?, t: Throwable?) {
                mListener?.onError(DataConfig.UPDATEPHONE_ERROR_CODE,Throwable(mContext.getString(R.string.request_error4)))
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
                        updatePhone(phone,code)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<UpdatePwdResponse>().apply {
                        data = body
                        this.code=1
                    })
                } else {
                    val msg=body?.message?:"Failed to modify mobile number, please try again later"
                    mListener?.onError(DataConfig.UPDATEPHONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getUserInfo(){
        val api=mRetrofit.create(LoginApi::class.java)
        val call = api.getUserInfo()
        call.enqueue(object : Callback<UserInfoResponse> {
            override fun onFailure(call: Call<UserInfoResponse>?, t: Throwable?) {
                mListener?.onError(DataConfig.UPDATEPHONE_ERROR_CODE,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<UserInfoResponse>?, response: Response<UserInfoResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getUserInfo()
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<UserInfoResponse>().apply {
                        data = body
                    })
                } else {
                    val msg=body?.message?:"Loading failed, please try again later"
                    mListener?.onError(DataConfig.UPDATEPHONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun getPerformance(){
        val api=mRetrofit.create(LoginApi::class.java)
        val call = api.getPerformance()
        call.enqueue(object : Callback<PerformanceResponse> {
            override fun onFailure(call: Call<PerformanceResponse>?, t: Throwable?) {
                mListener?.onError(DataConfig.UPDATEPHONE_ERROR_CODE,Throwable(mContext.getString(R.string.request_error4)))
                Log.e("Activity", "onFailure ${t?.toString()}")
            }

            override fun onResponse(c: Call<PerformanceResponse>?, response: Response<PerformanceResponse>?) {
                val body = response?.body()
                Log.e("Activity", "onResponse ${body?.toString()}  ${response?.code()} ${call?.request()?.url()?.toString()}")
                val check=checkLogin(body?.result,object :BaseListener{
                    override fun onError(code: Int, throwable: Throwable) {
                        startLoginActivity()
                    }

                    override fun <T> onNext(dat: T) {
                        getPerformance()
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<PerformanceResponse>().apply {
                        data = body
                    })
                } else {
                    val msg=body?.message?:"Loading failed, please try again later"
                    mListener?.onError(DataConfig.UPDATEPHONE_ERROR_CODE,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun forgetPassword(pwd:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("password",pwd)
                }
                .build()
        val api=mRetrofit.create(LoginApi::class.java)
        val call = api.forgetPassword(requestBody)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(1002,Throwable(mContext.getString(R.string.request_error4)))
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
                        forgetPassword(pwd)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<LoginResponse>().apply {
                        data = body
                        this.code=1002
                    })
                } else {
                    val msg=body?.message?:"Loading failed, please try again later"
                    mListener?.onError(1002,
                            Throwable(body?.message?:mContext.getString(R.string.request_error4)))
                }
            }
        })
    }

    fun checkCode(code:String){
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("smsCode",code)
                }
                .build()
        val api=mRetrofit.create(LoginApi::class.java)
        val call = api.checkCode(requestBody)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                mListener?.onError(1,Throwable(mContext.getString(R.string.request_error4)))
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
                        checkCode(code)
                    }
                })
                if(check){
                    return
                }
                if (response?.isSuccessful == true && body?.result==0) {
                    mListener?.onNext(BaseResponse<LoginResponse>().apply {
                        data = body
                        this.code=1
                    })
                } else {
                    val msg=body?.message?:mContext.getString(R.string.app_tv7)
                    mListener?.onError(1,
                            Throwable(msg))
                }
            }
        })
    }


}