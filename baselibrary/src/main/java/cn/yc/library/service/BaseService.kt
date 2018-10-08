package cn.yc.library.service

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import cn.yc.library.R
import cn.yc.library.api.CookieManger
import cn.yc.library.api.LoginApi
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.listener.BaseListener
import cn.yc.library.ui.activity.user.LoginActivity
import cn.yc.library.ui.config.DataConfig
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseService {

    protected lateinit var mContext:Context

    protected lateinit var mRetrofit:Retrofit

    protected var mCall: Call<*>?=null

    inner class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what==31){
                val intent= Intent(mContext, LoginActivity::class.java)
                intent.putExtra("refresh",true)
                mContext.startActivity(intent)
            }
        }
    }
    private val mDefaultHandler=DefaultHandler()

    private var mGsonAdapter=object : TypeAdapter<String>(){
        override fun read(read: JsonReader?): String{
            return if(read?.peek()== JsonToken.NULL){
                read?.nextNull()
                ""
            }else{
                read?.nextString()?:""
            }
        }

        override fun write(out: JsonWriter?, value: String?) {
            if(value==null){
                out?.nullValue()
            }else{
                out?.value(value)
            }
        }
    }

    constructor(listener: BaseListener?, context: Context) {
        mListener = listener
        mContext=context
        init()
    }

    constructor(context: Context):this(null,context)

    protected fun startLoginActivity(){
        mDefaultHandler.sendEmptyMessageDelayed(31,0)
        mListener?.onError(-1,Throwable(mContext.getString(R.string.heihei_tv71)))
    }

    private fun init() {
        val gson= GsonBuilder().registerTypeAdapter(String::class.java,mGsonAdapter).create()
        mRetrofit=Retrofit.Builder()
                //.baseUrl("http://192.168.0.120:8080/")
                //.baseUrl("http://192.168.0.115:8080")
                .baseUrl("http://120.77.220.25:8080/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create(gson)) //设置数据解析器
                .client(OkHttpClient().newBuilder().apply {
                    cookieJar(CookieManger(mContext))
                    //addInterceptor(AddCookiesInterceptor(mContext))
                    //addInterceptor(SaveCookiesInterceptor(mContext))
                    connectTimeout(10, TimeUnit.SECONDS)
                    readTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(10, TimeUnit.SECONDS)
                }.build())
                .build()
    }

    fun login(name:String,password:String,listener:BaseListener){

        val api=mRetrofit.create(LoginApi::class.java)
        val call=api.login(HashMap<String,String>().apply{
            put("username",name)
            put("password",password)
            put("roleType","0")
        })
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                listener.onError(DataConfig.LOGIN_ERROR_CODE,Throwable(t?.message?:""))
            }

            override fun onResponse(c: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                val body=response?.body()
                Log.e("Activity","headers  ${response?.headers()?.toString()}")
                Log.e("Actctivity","login ${body?.toString()}  $name  $password")
                val loginResponse=if(response?.raw()?.isSuccessful==true && body!=null){
                    body
                }else{
                    LoginResponse(mContext.getString(R.string.app_tv6))
                }
                listener.onNext(loginResponse)
            }
        })
    }

    protected fun checkLogin(code:Int?,listener: BaseListener):Boolean{
        if(code==1001){
            //startLoginActivity()
            listener.onError(DataConfig.REQUEST_LOGIN_ERROR, Throwable(mContext.getString(R.string.heihei_tv71)))
            /*val login=mContext.getLogin()
            if(login?.name?.isNotBlank()==true && login?.password?.isNotBlank()){
                login(login.name,login.password,listener)
            }else{
                listener.onError(DataConfig.REQUEST_LOGIN_ERROR, Throwable(mContext.getString(R.string.request_error)))
            }*/
            return true
        }
        return false
    }


    fun setListener(baseListener: BaseListener) {
        mListener = baseListener
    }

    protected var mListener: BaseListener? = null

    private val mDisposable = CompositeDisposable().apply {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String) {/* Do something */

    }

    fun cancelRequest(){
        mCall?.cancel()
    }
}