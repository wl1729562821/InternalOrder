package cn.yc.library.ui.activity.user

import android.os.Bundle
import android.view.KeyEvent
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.config.ActivityConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.checkZh
import cn.yc.library.utils.getApp
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : BaseActivity() {

    private var mViewModel: LoginViewModel?=null

    override val layoutId: Int
        get() = R.layout.activity_main

    private var mLogin=false
    private var mRefresh=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getApp()?.getAppViewModel()?.mMessages=1
        mDialog= PromptDialog(this,build = PromptDialog.DialogBuild().apply {
            type=1
        })
        mRefresh=intent.getBooleanExtra("refresh",false)
        login_title.text=if(checkZh()){
            "外获登录"
        }else{
            "OutofLogin"
        }
        login_bt?.setOnClickListener {
            login(login_name?.editableText?.toString()?:"",
                    login_pwd?.editableText?.toString()?:"")
        }
    }
    /**
     * 登录
     */
    private fun login(name:String,pwd:String){
        mDialog?.show()
        mViewModel=mViewModel?:getViewModel()
        mViewModel?.apply {
            login {
                setParameter(name,pwd)
                onError {
                    mDialog?.dismiss()
                    showMessage(message)
                }

                onNext {
                    mDialog?.dismiss()
                    if(mRefresh){
                        finish()
                        return@onNext
                    }
                    PromptDialog(this@LoginActivity,build = PromptDialog.DialogBuild().apply {
                        content=getString(R.string.app_tv18)
                        title=getString(R.string.prompt)
                        mListener=object : DialogListener {
                            override fun dismiss() {
                                mLogin=true
                                getApp()?.getAppViewModel()?.mPhone=name
                                setResult(ActivityConfig.LOGIN_RESULT_CODE)
                                getApp()?.getAppViewModel()?.mMessages=2
                                finish()
                            }
                        }
                    }).show()
                }
            }
        }
    }

    override fun finish() {
        if(!mLogin){
            getApp()?.getAppViewModel()?.mMessages=2
            setResult(1022)
        }
        super.finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            //startActivity(Intent(this,FinishActivity::class.java))
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
