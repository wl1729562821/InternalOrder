package cn.yc.library.ui.activity.user

import android.os.Bundle
import android.os.Handler
import android.os.Message
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.UpdatePwdResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.getLogin
import cn.yc.library.utils.saveLogin
import cn.yc.library.utils.setStringRes
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import kotlinx.android.synthetic.main.activity_updatephone.*

class UpdatePhoneActivity:BaseActivity(){

    private val mHandler=DefaultHandler()
    private var mTime=60

    private var mVm: LoginViewModel?=null

    private var mName=""

    internal inner class DefaultHandler:Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                0->{
                    setTime(1)
                }
                1->{
                    setTime(0)
                }
            }
        }
    }

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        if(code== DataConfig.SENDCODE_ERROR_CODE){
            send_code?.isEnabled=true
            mTime=0
            setTime(0)
            showMessage(throwable.message?:"")
        }else{
            showMessage(throwable.message?:"")
        }
    }

    override fun <T> onNext(dat: T) {
        mDialog?.dismiss()
        (dat as BaseResponse<*>).apply {
            (data as? UpdatePwdResponse).also{
                if(code==0){
                    mTime=60
                    send_code.isEnabled=false
                    setTime(0)
                    showMessage(R.string.heihei_tv42)
                }else{
                    PromptDialog(this@UpdatePhoneActivity,build =PromptDialog.DialogBuild().apply {
                        content=getString(R.string.heihei_tv48)
                        application.getLogin()?.apply {
                            val bean=this
                            bean.name=mName
                            application.saveLogin(bean)
                        }
                        mListener=object : DialogListener {
                            override fun dismiss() {
                                setResult(10)
                                finish()
                            }
                        }
                    }).show()
                }
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_updatephone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialog= PromptDialog(this,build = PromptDialog.DialogBuild().apply {
            type=1
        })
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.heihei_tv47),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        updatephone_bt?.setOnClickListener {
            val regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-9]))\\d{8}$".toRegex()
            if(phone?.editableText?.toString()?.matches(regex)==true){
                if(code?.editableText?.isNotEmpty()==true){
                    request()
                }else{
                    showMessage(R.string.heihei_tv40)
                }
            }else{
                showMessage(R.string.heihei_tv49)
            }
        }
        send_code?.setOnClickListener {
            val regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-9]))\\d{8}$".toRegex()
            if(phone?.editableText?.toString()?.matches(regex)==true){
                mDialog?.show()
                sendCode()
            }else{
                showMessage(R.string.heihei_tv49)
            }
        }
    }

    private fun sendCode(){
        mVm=mVm?:getViewModel()
        mVm?.sendCode(phone?.editableText?.toString()?:"")
    }
    private fun request(){
        mVm=mVm?:getViewModel()
        mName=phone?.editableText?.toString()?:""
        mVm?.updatePhone(phone?.editableText?.toString()?:"",code?.editableText?.toString()?:"")
    }

    private fun setTime(what:Int){
        send_code?.text="\t\t$mTime\t\t"
        if(mTime>0){
            mHandler.sendEmptyMessageDelayed(what,1000)
            mTime--
        }else{
            send_code.isEnabled=true
            mTime=60
            send_code.setStringRes(R.string.forgetpwd_tv2)
        }
    }
}