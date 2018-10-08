package cn.yc.library.vm

import android.app.Application
import cn.yc.library.base.BaseViewModel

class LoginViewModel(app:Application):BaseViewModel(app){

    fun sendCode(phone:String){
        mLoginService.sendCode(phone)
    }

    fun updatePhone(phone:String,code:String){
        mLoginService.updatePhone(phone,code)
    }

    fun getUserInfo(){
        mLoginService.getUserInfo()
    }

    fun getPerformance(){
        mLoginService.getPerformance()
    }

    fun checkCode(code:String){
        mLoginService.checkCode(code)
    }

    fun setPassword(pwd:String){
        mLoginService.forgetPassword(pwd)
    }
}