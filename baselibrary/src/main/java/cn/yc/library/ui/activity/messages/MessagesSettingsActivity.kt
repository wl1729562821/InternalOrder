package cn.yc.library.ui.activity.messages

import android.os.Bundle
import android.view.KeyEvent
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.bean.response.MessagesSettingsResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.checkZh
import cn.yc.library.utils.getApp
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.OrderViewModel
import kotlinx.android.synthetic.main.activity_msg_settings.*

class MessagesSettingsActivity : BaseActivity() {

    private var mVM: OrderViewModel? = null
    private var mResponseData = MessagesSettingsResponse.DataSetBean()
    private var mUpdate=false

    private var mList = hashMapOf<Int, Boolean>().apply {
        put(1, false)
        put(2, false)
        put(3, false)
        put(4, false)
    }

    override val layoutId: Int
        get() = R.layout.activity_msg_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVM = getViewModel()
        toolbarInit {
            mToolbar = toolbar
            data = ToolbarBean(getString(R.string.heihei_tv13), "", false).apply {
            }
            onBack {
                if(mResponseData.userName?.isNotBlank() == true && mUpdate){
                    update()
                }else{
                    finish()
                }
            }
        }
        setClick(false)
        sb1.setOnCheckedChangeListener { view, isChecked ->
            mUpdate=true
        }
        sb2.setOnCheckedChangeListener { view, isChecked ->
            mUpdate=true
        }
        sb3.setOnCheckedChangeListener { view, isChecked ->
            mUpdate=true
        }
        sb4.setOnCheckedChangeListener { view, isChecked ->
            mUpdate=true
        }

        sb2_parent.setOnClickListener {
            if(mResponseData?.userName?.isNullOrEmpty()!=false){
                showMessage(R.string.heihei_tv66)
                return@setOnClickListener
            }
            showMessage(if(checkZh()){
                "该功能暂未开放"
            }else{
                "This feature is not yet open"
            })
        }
        sb3_parent.setOnClickListener {
            if(mResponseData?.userName?.isNullOrEmpty()!=false){
                showMessage(R.string.heihei_tv66)
                return@setOnClickListener
            }
            showMessage(if(checkZh()){
                "该功能暂未开放"
            }else{
                "This feature is not yet open"
            })
        }
        sb4_parent.setOnClickListener {
            if(mResponseData?.userName?.isNullOrEmpty()!=false){
                showMessage(R.string.heihei_tv66)
                return@setOnClickListener
            }
            showMessage(if(checkZh()){
                "该功能暂未开放"
            }else{
                "This feature is not yet open"
            })
        }

        request()
        refresh_layout.setOnRefreshListener {
            request()
        }
    }

    private fun setClick(click:Boolean){
        sb1.isEnabled=click
        sb2.isEnabled=click
        sb3.isEnabled=click
        sb4.isEnabled=click
    }

    private fun request() {
        refresh_layout.isRefreshing = true
        mVM?.getMessagSettings()
    }

    override fun onError(code: Int, throwable: Throwable) {
        refresh_layout.isRefreshing = false
        mDialog?.dismiss()
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        mDialog?.dismiss()
        refresh_layout.isRefreshing = false
        (dat as? BaseResponse<*>)?.data?.apply {
            (this as? MessagesSettingsResponse)?.apply {
                val phone=getApp()?.getAppViewModel()?.mPhone?:""
                if (dataSet!=null && result==0) {
                    refresh_layout.isEnabled = false
                    setClick(true)
                    mResponseData = dataSet!!
                    showMessage(R.string.request_success)
                    mResponseData.apply {
                        openCode?.split(",")?.map {
                            it.toInt()
                        }?.forEach {
                            if (mList.containsKey(it)) {
                                mList[it] = true
                                when (it) {
                                    1 -> sb1.isChecked = true
                                    //2 -> sb2.isChecked = true
                                    //3 -> sb3.isChecked = true
                                    //4 -> sb4.isChecked = true
                                }
                            }
                        }
                    }
                }else if(phone.isNotBlank()){
                    mResponseData.userName=phone
                    mResponseData.id=""
                    setClick(true)
                }
                else {
                    showMessage(R.string.heihei_tv66)
                }
                sb2.isEnabled=false
                sb3.isEnabled=false
                sb4.isEnabled=false
                return
            }
            (this as? LoginResponse)?.apply {
                showMessage(message)
                if(result==0){
                    finish()
                }else{

                }
            }
        }
    }

    private fun update():Boolean{
        if(mResponseData.userName?.isNotBlank() == true && mUpdate){
            mDialog = PromptDialog(this, PromptDialog.DialogBuild().apply {
                type = 1
            })
            mDialog?.show()
            mList[1] = sb1.isChecked
            mList[2] = sb2.isChecked
            mList[3] = sb3.isChecked
            mList[4] = sb4.isChecked
            val getCode: (Int) -> String = {
                if (mList[it] == true) {
                    "$it,"
                } else {
                    ""
                }
            }
            //var code = "${getCode(1)}${getCode(2)}${getCode(3)}${getCode(4)}"
            var code = if(mList[1]==true){
                "1"
            }else{
                ""
            }
            mVM?.setMessagSettings(mResponseData.userName
                    ?: "", code.trim(), mResponseData.id.toString())
            return true
        }
        return false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && update()) {
            return true
        }
        return super.onKeyUp(keyCode, event)
    }
}