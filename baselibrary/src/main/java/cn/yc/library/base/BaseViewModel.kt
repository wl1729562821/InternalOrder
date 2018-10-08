package cn.yc.library.base

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.os.Handler
import android.os.Message
import cn.yc.library.bean.TimeBean
import cn.yc.library.bean.request.RequestBean
import cn.yc.library.data.LoginData
import cn.yc.library.listener.BaseListener
import cn.yc.library.service.LoginService
import cn.yc.library.service.OrderService

open class BaseViewModel(app:Application):AndroidViewModel(app),BaseListener{

    private val mCurrentTimeBean=MutableLiveData<HashMap<String,TimeBean>>()

    fun setCurrentTimebean(list:HashMap<String,TimeBean>){
        mCurrentTimeBean.value=null
        mCurrentTimeBean.postValue(list)
    }

    fun getCurrentTimebean()=mCurrentTimeBean

    protected val mRequest=MutableLiveData<Boolean>()
    protected val mRequestModule=MutableLiveData<RequestBean>()

    private inner class DefaultHandler: Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what==1002){
                if(mCodeList.isNotEmpty()){
                    mRequestModule.postValue(RequestBean(mCodeList[0]))
                    mCodeList.removeAt(0)
                    mDefaultHandler.sendEmptyMessageDelayed(1002,1500)
                }
            }
        }
    }
    private var mCodeList= arrayListOf<Int>()
    private val mDefaultHandler=DefaultHandler()

    fun setRequestCode(vararg code:Int){
        mCodeList.clear()
        code.forEach {
            mCodeList.add(it)
        }
        if(mCodeList.isNotEmpty()){
            mRequestModule.postValue(RequestBean(mCodeList[0]))
            mCodeList.removeAt(0)
            mDefaultHandler.sendEmptyMessageDelayed(1002,1500)
        }
        /*code.forEach {
            //mRequestModule.value=RequestBean(it)
            mRequestModule.postValue(RequestBean(it))
        }*/
    }

    fun getRequestCode()=mRequestModule.value?.requestCode?:-1

    fun getRequestModule()=mRequestModule

    fun setRequest(request:Boolean){
        mRequest.postValue(request)
    }

    fun getRequest():MutableLiveData<Boolean>{
        return mRequest
    }

    protected var mListener: BaseListener?=null

    protected val mService= OrderService(this,getApplication())
    protected val mLoginService= LoginService(this,getApplication())

    override fun onError(code: Int, throwable: Throwable) {
    }

    override fun <T> onNext(dat: T) {

    }

    open fun setListener(baseListener: BaseListener){
        mListener=baseListener
        mService.setListener(baseListener)
        mLoginService.setListener(baseListener)
    }

    fun login(init: LoginData.() -> Unit) {
        val wrap = LoginData()
        with(wrap){
            context=getApplication()
            init()
            onSend()
        }
    }

    fun cancelRequest(){
        mService.cancelRequest()
    }

    init {
        getApplication<App>().registerActivityLifecycleCallbacks(object :Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {
                mCurrentTimeBean?.value?.apply {
                    forEach {
                        it.value?.removeOtherView(activity?.javaClass?.simpleName?:"")
                    }
                }
            }

            override fun onActivityPaused(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityStopped(activity: Activity?) {
            }
        })
    }

}