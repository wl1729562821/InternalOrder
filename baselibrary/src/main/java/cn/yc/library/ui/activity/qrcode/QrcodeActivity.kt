package cn.yc.library.ui.activity.qrcode

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.finalteam.rxgalleryfinal.utils.EmptyViewUtils.showMessage
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.request.RequestBean
import cn.yc.library.bean.response.KeyResponse
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import kotlinx.android.synthetic.main.activity_qrcode.*

class QrcodeActivity:BaseActivity(), QRCodeView.Delegate{

    private var mVm: OrderViewModel?=null

    inner class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what==1){
                finish()
                zxingview?.apply {

                    //tartCamera()
                    //startSpotAndShowRect()
                }
            }
        }
    }
    private val mDefaultHandler=DefaultHandler()

    override val layoutId: Int
        get() = R.layout.activity_qrcode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialog= PromptDialog(this,PromptDialog.DialogBuild().apply {
            type=1
        })
        mVm=getViewModel()
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.app_tv13),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        zxingview.setDelegate(this)

    }

    override fun onScanQRCodeOpenCameraError() {

    }

    private var mRequestData= KeyResponse.DataSetBean()
    override fun onScanQRCodeSuccess(result: String?) {
        Log.e(TAG,"onScanQrCode $result")
        zxingview?.apply {
            if(result?.isNotBlank()==true){
                if(result?.contains("http:") && result?.contains("houseId")
                        && result?.contains("houseCode")){
                    stopCamera()
                    mDialog?.show()
                    //mDefaultHandler.sendEmptyMessageDelayed(1,2000)
                    mVm?.checkQRCode(result)
                    return
                }
                //showMessage("扫描成功$result")
            }
        }
        start()
    }

    override fun onStart() {
        super.onStart()
        start()
    }

    private fun start(){
        zxingview?.apply {
            stopSpot()
            startCamera()
            startSpotAndShowRect()
            zxingview.startSpot()
            scanBoxView.isOnlyDecodeScanBoxArea = false
        }
    }

    override fun onStop() {
        zxingview?.stopCamera()
        super.onStop()
    }

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        showMessage(throwable.message)
        start()
    }

    override fun <T> onNext(dat: T) {
        mDialog?.dismiss()
        (dat as? LoginResponse)?.apply {
            Log.e(TAG,"onNext $dat")
            mDialog= PromptDialog(this@QrcodeActivity,PromptDialog.DialogBuild().apply {
                type=0
                bt=R.string.bt_qd
                content=getString(R.string.heihei_tv29)
                mListener=object : DialogListener {
                    override fun dismiss() {
                        getBaseViewModel<LoginViewModel>()?.
                                getRequestModule()?.postValue(RequestBean().apply {
                            requestCode=121
                        })
                        mDefaultHandler.sendEmptyMessageDelayed(1,1000)
                    }
                }
            })
            mDialog?.show()
        }
    }

    override fun onDestroy() {
        zxingview?.onDestroy()
        super.onDestroy()
    }

}