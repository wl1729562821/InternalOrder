package cn.yc.library.data

import cn.yc.library.R
import cn.yc.library.bean.UserBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.listener.BaseListener
import cn.yc.library.service.LoginService
import cn.yc.library.utils.saveLogin

class LoginData:BaseData(){

    private var mName:String?=null
    private var mPwd:String?=null

    fun setParameter(name:String,pwd:String){
        mName=name
        mPwd=pwd
    }

    fun onSend(){
        when {
            mName?.isNotEmpty()==false -> mError(Throwable(context?.getString(R.string.app_tv4)))
            mPwd?.isNotEmpty()==false -> mError(Throwable(context?.getString(R.string.app_tv5)))
            else -> {
                val service= LoginService(context!!)
                val listener=object : BaseListener {
                    override fun onError(code: Int, throwable: Throwable) {
                        mError(throwable)
                    }

                    override fun <T> onNext(dat: T) {
                        val baseResponse= BaseResponse<String>()
                        if(dat is LoginResponse && dat.dataSet?.isNotEmpty()==true && dat.result==1002){
                            baseResponse.message=dat.message
                            baseResponse.code=200
                        }else{
                            baseResponse.code=404
                            baseResponse.message=context?.getString(R.string.app_tv6)?:""
                        }
                        if(baseResponse.code==200){
                            context?.saveLogin(UserBean(mName?:"",mPwd?:""))
                            mNext(baseResponse)
                        }else{
                            mError(Throwable(baseResponse.message))
                        }
                    }
                }
                service.setListener(listener)
                service.login(mName?:"",mPwd?:"")
            }
        }
    }
}