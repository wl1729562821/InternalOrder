package cn.yc.library.ui.activity.qrcode

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.KeyResponse
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.data.recyclerviewInit
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.ItemClickListener
import cn.yc.library.ui.adapter.KeyAdapter
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import kotlinx.android.synthetic.main.activity_key.*

class KeyActivity:BaseActivity(){

    private var mVm: OrderViewModel?=null
    private var mKeyAdapter: KeyAdapter?=null
    private val mHnadler=DefaultHandler()

    private var mSelected=false

    private var mBitmap:Bitmap?=null

    internal inner class DefaultHandler:Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what==0){
                mBitmap?.apply {
                    qrcode.setImageBitmap(this)
                }
            }else{
                mVm?.showQrcode(mSuccessQrList[0].houseId.toString())
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_key

    private var mCheck=false
    private var mStart=false

    private var mQrList= arrayListOf<KeyResponse.DataSetBean>()
    private var mSuccessQrList= arrayListOf<KeyResponse.DataSetBean>()
    private var mAdd=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVm=getViewModel()
        mDialog= PromptDialog(this@KeyActivity, PromptDialog.DialogBuild().apply {
            type=1
        })
        qx?.setOnClickListener {
            qx?.setBackgroundResource(if(mCheck){
                R.drawable.check_bg
            }else{
                R.mipmap.check_selected
            })
            mCheck=!mCheck
            mKeyAdapter?.setSelected(mCheck)
        }
        plsm.setOnClickListener {
            if(mKeyAdapter?.getSelectedData()?.isEmpty() != false){
                showMessage(R.string.app_tv15)
            }else{
                mQrList.clear()
                mHnadler.removeMessages(1)
                mSuccessQrList.clear()
                mKeyAdapter?.getSelectedData()?.forEach {
                    mQrList.add(it)
                }
                request()
            }
        }
        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {
            //扫码获取成功
            if(it?.requestCode==121){
                //获取数据刷新之前设置二维码空
                qrcode.setBackgroundResource(R.mipmap.upload_default)
                mHnadler.removeMessages(1)
                mAdd=true
                getKeys()
            }
        }
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.five_tv5),"",true).apply {
                this.settingsDrawable=R.mipmap.sm
            }
            onBack {
                finish()
            }
            onSetting {
                if(!mStart){
                    mStart=true
                    camearPermissionsNext()
                }
            }
        }
        qx.setOnClickListener {
            if(mSelected){
                mSelected=false
                qx.setBackgroundResource(R.drawable.check_bg)
            }else{
                mSelected=true
                qx.setBackgroundResource(R.mipmap.check_selected)
            }
            mKeyAdapter?.setSelected(mSelected)
        }
        recyclerviewInit {
            mRv=key_rv
            mKeyAdapter=KeyAdapter(this@KeyActivity,object : ItemClickListener {
                override fun <T> onClcik(data: T) {
                    if(data is KeyResponse.DataSetBean){
                        mHnadler.removeMessages(1)
                        mQrList.clear()
                        mSuccessQrList.clear()
                        mQrList.add(data)
                        qrcode.setBackgroundResource(R.mipmap.upload_default)
                        request()
                        return
                    }
                    val selected=data as Boolean
                    if(selected){
                        qx.setBackgroundResource(R.mipmap.check_selected)
                    }else{
                        qx.setBackgroundResource(R.drawable.check_bg)
                    }
                }
            })
            mAdapter=mKeyAdapter
        }

        refresh_layout?.setOnRefreshListener {
            getKeys()
        }

        getKeys()
    }

    override fun onError(code: Int, throwable: Throwable) {
        refresh_layout?.isRefreshing=false
        mDialog?.dismiss()
        mStart=false
        if(code!=1011){
            request()
        }
        mAdd=false
    }

    override fun <T> onNext(dat: T) {
        refresh_layout?.isRefreshing=false
        mDialog?.dismiss()
        ((dat as? BaseResponse<*>)?.data as? LoginResponse)?.apply {
            if(result==0 or 1105){
                //如果可以展示就继续
                if(mTimeStart){
                    request()
                    return
                }
                mHnadler.post {
                    mSuccessQrList.add(mQrList[0])
                    mQrList.removeAt(0)
                    request()
                }
            }else{
                //如果不可以展示就删除当前的
                if(mTimeStart){
                    mKeyAdapter?.remove(mSuccessQrList[0].houseId)
                    mSuccessQrList.removeAt(0)
                    request()
                    return
                }
                mQrList.removeAt(0)
                request()
            }
        }
        if(dat is String && dat=="camear"){
            mStart=false
            startActivity(Intent(this@KeyActivity, QrcodeActivity::class.java))
            return
        }
        ((dat as? BaseResponse<*>)?.data as? KeyResponse)?.dataSet?.apply {
            if(mAdd){
                mAdd=false
                mKeyAdapter?.addItem(this)
                if(mKeyAdapter?.getCheckAll()==true){
                    qx.setBackgroundResource(R.mipmap.check_selected)
                }else{
                    qx.setBackgroundResource(R.drawable.check_bg)
                }
                request()
            }else{
                mKeyAdapter?.refresh(this)
            }
        }
    }

    private fun getKeys(){
        refresh_layout?.isRefreshing=true
        mVm?.getKey()
    }

    private var mTimeStart=false

    private fun request(){
        if(mQrList.isNotEmpty()){
            mTimeStart=false
            mDialog?.show()
            mVm?.showQrcode(mQrList[0].houseId.toString())
        }else{
            mDialog?.dismiss()
            if(mSuccessQrList.isNotEmpty()){
                mTimeStart=true
                mHnadler.post {
                    mBitmap= QRCodeEncoder.
                            syncEncodeQRCode("http://120.77.220.25:8080/interior/salesman/get/house/key?houseId" +
                                    "=${mSuccessQrList[0].houseId}&houseCode=${mSuccessQrList[0].houseCode}",
                                    BGAQRCodeUtil.dp2px(this@KeyActivity, 150f), Color.parseColor("#ff0000"))
                    mHnadler.sendEmptyMessage(0)
                }
                mHnadler.sendEmptyMessageDelayed(1,3000)
            }else{
                qrcode.setBackgroundResource(R.mipmap.upload_default)
            }
        }

    }
}