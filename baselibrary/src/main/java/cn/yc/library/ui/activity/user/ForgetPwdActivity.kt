package cn.yc.library.ui.activity.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.bean.response.UpdatePwdResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import kotlinx.android.synthetic.main.activity_forgetpwd.*


class ForgetPwdActivity:BaseActivity(){

    private var mPhone=""
    private var mVm: LoginViewModel?=null

    private val mHandler=DefaultHandler()
    private var mTime=60

    internal inner class DefaultHandler: Handler(){
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

    override val layoutId: Int
        get() = R.layout.activity_forgetpwd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialog= PromptDialog(this,build = PromptDialog.DialogBuild().apply {
            type=1
        })
        mPhone=intent.getStringExtra("phone")
        if(mPhone.isNotEmpty()){
            phone.text=mPhone
        }else{
            PromptDialog(this,build =PromptDialog.DialogBuild().apply {
                type=0
                content=getString(R.string.heihei_tv43)
                mListener=object : DialogListener {
                    override fun dismiss() {
                        finish()
                    }
                }
            })
        }
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.five_tv12),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        forgetpwd_bt?.setOnClickListener {
            if(mPhone.isNotEmpty()){
                if(forget_code?.editableText?.isNullOrBlank()==true){
                    showMessage(R.string.heihei_tv40)
                }else{
                    mDialog?.show()
                    request()
                }
            }else{
                showMessage(R.string.heihei_tv41)
            }
        }

        send_code?.setOnClickListener {

            if(mPhone.isNotEmpty()){
                mDialog?.show()
                sendCode()
            }else{
                showMessage(R.string.heihei_tv41)
            }
        }
    }

    private fun setTime(what:Int){
        send_code?.text="\t\t$mTime\t\t"
        if(mTime>0){
            mHandler.sendEmptyMessageDelayed(what,1000)
            mTime--
        }else{
            send_code.isEnabled=true
            mTime=60
            send_code.text = getString(R.string.forgetpwd_tv2)
        }
    }

    private fun sendCode(){
        mVm=mVm?:getViewModel()
        mVm?.sendCode(mPhone)
    }

    private fun request(){
        mVm=mVm?:getViewModel()
        mVm?.checkCode(forget_code?.editableText?.toString()?:"")
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
                }
            }
            (data as? LoginResponse)?.apply{
                startActivityForResult(Intent(this@ForgetPwdActivity, SetPwdActivity::class.java),10)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10 && resultCode==10){
            setResult(11)
            finish()
        }
    }

}