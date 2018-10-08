package cn.yc.library.ui.activity.user

import android.os.Bundle
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.UserBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.getLogin
import cn.yc.library.utils.saveLogin
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import kotlinx.android.synthetic.main.activity_setpwd.*

class SetPwdActivity:BaseActivity(){

    private var mVm: LoginViewModel?=null
    private var mPassword=""

    override val layoutId: Int
        get() = R.layout.activity_setpwd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.heihei_tv44),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        bt?.setOnClickListener {
            val regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,216}$".toRegex()
            val msg=when{
                pwd1?.editableText?.toString()?.isNullOrBlank() == true -> getString(R.string.heihei_tv38)
                pwd1?.editableText?.toString()?.trim()?.length ?: 0 < 6 -> getString(R.string.heihei_tv39)
                pwd2?.editableText?.toString()?.isNullOrBlank() == true -> getString(R.string.heihei_tv38)
                pwd2?.editableText?.toString()?.trim()?.length ?: 0 < 6 -> getString(R.string.heihei_tv39)
                pwd1?.editableText?.toString()!=pwd2?.editableText?.toString()->getString(R.string.heihei_tv45)
                else-> "correct"
            }
            if(msg=="correct"){
                mDialog?.show()
                request()
            }else{
                showMessage(msg)
            }
        }
    }

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        ((dat as BaseResponse<*>).data as? LoginResponse)?.apply {
            PromptDialog(this@SetPwdActivity,build = PromptDialog.DialogBuild().apply {
                content=getString(R.string.heihei_tv46)
                mListener=object : DialogListener {
                    override fun dismiss() {
                        application?.getLogin()?.also {
                            application?.saveLogin(UserBean(it.name,mPassword))
                        }
                        setResult(10)
                        finish()
                    }
                }
            }).show()
            return
        }
        mDialog?.dismiss()
    }

    private fun request(){
        mVm=mVm?:getViewModel()
        mPassword=pwd1?.editableText?.toString()?:""
        mVm?.setPassword(pwd1?.editableText?.toString()?:"")
    }

}