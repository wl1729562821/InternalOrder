package cn.yc.library.ui.activity.user

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.UpdatePwdResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.OrderViewModel
import kotlinx.android.synthetic.main.activity_updatepwd.*

class UpdatePwdActivity:BaseActivity(){

    private var mViewModel: OrderViewModel?=null
    private var mCheckInputPassword=true
    private var mCheckInputPassword1=true
    private var mPhone=""

    private var mSetPassword=false

    override val layoutId: Int
        get() = R.layout.activity_updatepwd

    override fun <T> onNext(dat: T) {
        mDialog?.dismiss()
        var msg=getString(R.string.heihei_tv34)
        ((dat as BaseResponse<*>).data as? UpdatePwdResponse)?.apply {
            Log.e(TAG,"onNext $this")
            msg=getString(R.string.app_tv31)
        }
        PromptDialog(this,build =PromptDialog.DialogBuild().apply {
            content=msg
            mListener=object : DialogListener {
                override fun dismiss() {
                    finish()
                }
            }
        }).show()
    }

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        Toast.makeText(this,throwable.message,Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPhone=intent.getStringExtra("phone")
        mDialog= PromptDialog(this,PromptDialog.DialogBuild().apply {
            type=1
        })
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.five_tv12),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        forgetpwd?.setOnClickListener {

            startActivityForResult(Intent(this,ForgetPwdActivity::class.java).putExtra("phone",mPhone),11)
        }

        bt1.setOnClickListener {
            if(mCheckInputPassword){
                pwd_ed?.inputType= EditorInfo.TYPE_CLASS_TEXT
            }else{
                pwd_ed?.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            mCheckInputPassword=!mCheckInputPassword
        }
        bt2.setOnClickListener {
            Log.e("Activity","$mCheckInputPassword1")
            if(mCheckInputPassword1){
                pwd1_ed?.inputType= EditorInfo.TYPE_CLASS_TEXT
            }else{
                pwd1_ed?.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            mCheckInputPassword1=!mCheckInputPassword1
        }
        updatepwd_bt?.setOnClickListener {
            /*val regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,216}$".toRegex()
            val msg=when{
                oldpwd_ed?.editableText?.toString()?.isNullOrBlank()==true->"Old password cannot be empty"
                pwd_ed?.editableText?.toString()?.matches(regex)==false->"Wrong password format"
                pwd1_ed?.editableText?.toString()?.matches(regex)==false->"Confirm password format error"
                pwd1_ed?.editableText?.toString()!=pwd1_ed?.editableText?.toString()->"New password and confirmation password are inconsistent"
                else-> "正确"
            }*/
            val msg=when {
                oldpwd_ed?.editableText?.toString()?.isNullOrBlank() == true -> getString(R.string.heihei_tv37)
                pwd_ed?.editableText?.toString()?.isNullOrBlank() == true -> getString(R.string.heihei_tv38)
                pwd_ed?.editableText?.toString()?.trim()?.length ?: 0 < 6 -> getString(R.string.heihei_tv39)
                pwd1_ed?.editableText?.toString()?.isNullOrBlank() == true -> getString(R.string.heihei_tv38)
                pwd1_ed?.editableText?.toString()?.trim()?.length ?: 0 < 6 -> getString(R.string.heihei_tv39)
                else->"正确"
            }
            if(msg=="正确"){
                mDialog?.show()
                request()
            }else{
                showMessage(msg)
            }
        }
    }

    private fun request(){
        mViewModel=mViewModel?:getViewModel()
        mViewModel?.updatePwd(oldpwd_ed.editableText.toString(),
                pwd_ed.editableText.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==11 && resultCode==11){
            mSetPassword=true
        }
    }

    override fun finish() {
        if(mSetPassword){
            setResult(12)
        }
        super.finish()
    }

}