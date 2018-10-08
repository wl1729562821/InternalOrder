package com.ysgj.order.ui.activity.order

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent
import cn.qqtheme.framework.picker.SinglePicker
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.CameraBean
import cn.yc.library.bean.DialogDataBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.FileResponse
import cn.yc.library.bean.response.HomeOneDetailsBean
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.activity.cancel.CancelReservationActivity
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.setFresco
import cn.yc.library.utils.showMessage
import cn.yc.library.utils.startPhoneDialog
import cn.yc.library.vm.OrderViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse
import com.google.android.gms.location.places.Places
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import kotlinx.android.synthetic.main.activity_deliverydetailsnext.*
import java.net.InetAddress

class HomeOneNextDetailsActivity:BaseActivity(){

    private var mData: HomeOneDetailsBean.DataSetBean?=null
    private var type=-1

    private var mImg=""
    private var mHouseId=""

    private var mVm: OrderViewModel?=null

    private var mFile=""

    private var mPlaceDetectionClient: PlaceDetectionClient?=null
    private var mGeoDataClient: GeoDataClient?=null
    private var mFusedLocationProviderClient: FusedLocationProviderClient?=null

    private var mCheckNetwork=false
    private val mDefaultHandler = DefaultHandler()
    internal inner class DefaultHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what==2){
                if(mCheckNetwork){
                    locationTask()
                }else{
                    mDialog?.dismiss()
                    showMessage(R.string.request_error4)
                }
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_deliverydetailsnext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGeoDataClient= Places.getGeoDataClient(this,null)
        mPlaceDetectionClient= Places.getPlaceDetectionClient(this,null)
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        mVm=getViewModel()
        phone_bt.setOnClickListener {
            startPhoneDialog(DialogDataBean().apply {
                mData?.apply {
                    title = getString(R.string.prompt)
                    content = "${getString(R.string.bddh)} $memberName\n$memberMoble"
                    phone = "$memberMoble"
                }
            })
        }
        mDialog= PromptDialog(this,PromptDialog.DialogBuild().apply {
            type=1
        })
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.toolbar_title_homeone),"",false)
            onBack {
                finish()
            }
        }

        type=intent.getIntExtra("type",-1)
        Log.e(TAG,"onCreate $type")
        img_parent?.visibility=if(type==1){
            View.GONE
        }else{
            View.VISIBLE
        }
        img_parent?.setOnClickListener {
            showPicker(arrayListOf<String>().apply {
                add(getString(R.string.pz))
                add(getString(R.string.xc))
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                if (index == 1) {
                    RxGalleryFinalApi
                            .openRadioSelectImage(this, object : RxBusResultDisposable<ImageRadioResultEvent>() {
                                override fun onEvent(it: ImageRadioResultEvent?) {
                                    mFile=it?.result?.originalPath?:""
                                    setFresco(it?.result?.originalPath,img)
                                }
                            }, true)
                } else {
                    camearPermissions()
                }
            })
        }
        val json=intent.getStringExtra("json")
        mData=Gson().fromJson(json, HomeOneDetailsBean.DataSetBean::class.java)
        Log.e(TAG,"json=$json")
        mHouseId=intent.getStringExtra("houseId")
        mData?.apply {
            if(leaseType==0){
                deliveryorder_img.text=getString(R.string.rent)
                fy_type.text=getString(R.string.cz)
                deliveryorder_img.setBackgroundResource(R.drawable.round2)
            }else{
                fy_type.text=getString(R.string.cs)
                deliveryorder_img.text=getString(R.string.buy)
                deliveryorder_img.setBackgroundResource(R.drawable.round1)
            }
            time.text=this.appointmentDoorTime
            address_tv.text=appointmentMeetPlace
            name.text=this.memberName?.toString()?:""
            getBaseViewModel<BaseViewModel>()?.getCurrentTimebean()?.
                    value?.get("estimatedTimeCountDown")?.addOtherView(endTime)

            language.text=if(languageVersion==0){
                getString(R.string.language_ch)
            }else{
                getString(R.string.language_en)
            }
        }
        if(type==0){
            scfy.text=getString(R.string.wc)
        }
        scfy.setOnClickListener {
            Log.e(TAG,"startActivity $type")
            if(type==0){
                if(mFile.isNullOrBlank()){
                    showMessage(R.string.hehe_3)
                }else{
                    requestImg()
                }
                return@setOnClickListener
            }
            getBaseViewModel<BaseViewModel>()?.setRequestCode(11106)
            val mType=type
            mData?.also {
                Log.e(TAG,"hdfggvf ${it.houseId}")
                startActivityForResult(Intent(this@HomeOneNextDetailsActivity,HousesOrderEditActivity::class.java).apply {
                    putExtra("title","Improve listing information")
                    putExtra("applyId","${it.applyId}")
                    putExtra("type",mType)
                    putExtra("houseId",mHouseId)
                    putExtra("taskId",mData?.taskId?.toString())
                    putExtra("applyId",mData?.applyId?.toString())
                },1033)
            }
            return@setOnClickListener
            mDialog= PromptDialog(this,PromptDialog.DialogBuild().apply {
                type=1
            })
            mDialog?.show()
            val t=object :Thread(){
                override fun run() {
                    mCheckNetwork= InetAddress.getByName("www.google.com").isReachable(6000)
                    mDefaultHandler.sendEmptyMessage(2)
                }
            }
            t.start()
        }
        bottom_bt1.setOnClickListener {
            mData?.also {
                val intent= Intent(this,
                        CancelReservationActivity::class.java).apply {
                    val json=Gson().toJson(it)
                    putExtra("json",json)
                }
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DataConfig.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            mFile=mCameraBean.imgPath
            return
        }
        if(resultCode==1033){
            setResult(1033)
            finish()
        }
    }

    private var mCameraBean = CameraBean()

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        showMessage(throwable.message)
    }

    private var mDdRequest = HomeOneDetailsActivity.LocationRequest()
    @SuppressLint("MissingPermission", "RestrictedApi")
    private fun refreshLocation(){
        mDialog?.show()
        //获取最后一次当前位置
        mPlaceDetectionClient?.getCurrentPlace(null)?.addOnCompleteListener {
            if(it==null || !it.isSuccessful || it.result==null || it.result?.count?:0<=0){
                mDialog?.dismiss()
                showMessage(R.string.location_error)
                return@addOnCompleteListener
            }
            Log.e(TAG,"refreshLocation2 ${it.isSuccessful} ${it.result}")
            (it.result as? PlaceLikelihoodBufferResponse)?.apply {
                mDialog?.show()
                val data=get(0)
                mDdRequest.location="${data?.place?.address?:""}"
                mDdRequest.id="${mData?.taskId}"
                mVm?.requestLocation(mDdRequest.id,"${data.place?.latLng?.longitude?:0.0}",
                        "${data.place?.latLng?.latitude?:0.0}",mDdRequest.location)
            }
        }
    }

    override fun <T> onNext(dat: T) {
        if(dat is String && dat=="location"){
            refreshLocation()
            return
        }
        ((dat as? BaseResponse<*>)?.data as? LoginResponse)?.apply {
            mDialog?.dismiss()
            if(result==0){
                getBaseViewModel<BaseViewModel>()?.setRequestCode(11106)
                val mType=type
                mData?.also {
                    Log.e(TAG,"hdfggvf ${it.houseId}")
                    startActivityForResult(Intent(this@HomeOneNextDetailsActivity,HousesOrderEditActivity::class.java).apply {
                        putExtra("title","Improve listing information")
                        putExtra("applyId","${it.applyId}")
                        putExtra("type",mType)
                        putExtra("houseId",mHouseId)
                        putExtra("taskId",mData?.taskId?.toString())
                        putExtra("applyId",mData?.applyId?.toString())
                    },1033)
                }
            }else{
                showMessage(message)
            }
            return
        }
        (dat as? BaseResponse<*>)?.apply {
            (data as? LoginResponse)?.apply {
                mDialog?.dismiss()
                if(result==0){
                    setResult(1033)
                    finish()
                    showMessage(R.string.request_success3)
                }else{
                    showMessage(dat.message)
                }
            }
            return
        }
        if (dat is CameraBean) {
            mCameraBean = dat
        }else if(dat is FileResponse && dat.fid.isNotBlank()){
            mImg=dat.fid
            requestImg()
        }
    }

    private fun requestImg(){
        mDialog=mDialog?: PromptDialog(this,PromptDialog.DialogBuild().apply {
            type=1
        })
        mDialog?.show()
        mVm=getViewModel()
        if(mImg.isNullOrBlank()){
            mVm?.uploadImg(mFile)
        }else{
            mVm?.complete(mData!!.taskId.toString(),mImg)
        }
    }
}