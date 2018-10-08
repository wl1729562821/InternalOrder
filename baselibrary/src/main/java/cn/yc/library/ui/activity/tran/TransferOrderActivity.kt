package cn.yc.library.ui.activity.tran

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent
import cn.qqtheme.framework.picker.SinglePicker
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.CameraBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.TransferOrderResponse
import cn.yc.library.bean.request.TransferOrderRequest
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.DeliveryOrderListBean
import cn.yc.library.bean.response.FileResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.adapter.ImageAdapter
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.setFresco
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_transferorder.*


class TransferOrderActivity:BaseActivity(){

    private var mImageAdapter:ImageAdapter?=null

    private var mRequest= TransferOrderRequest()
    private var mViewModel: OrderViewModel?=null
    private var taskId:String=""
    private var poolId:String=""
    private var times=""
    private var mType=-1
    private val mImageList= arrayListOf<String>()
    private val mSuccessImageList= arrayListOf<String>()

    private var mCameraBean = CameraBean()

    private var mImgUri=""
    private var mImgUri2=""
    private var mImgUri3=""
    private var mImgUri4=""
    private var mImgUri5=""

    private var mRefresh=false

    private var mIdList= arrayListOf<DeliveryOrderListBean.DataSetBean>()


    override val layoutId: Int
        get() =R.layout.activity_transferorder

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        Toast.makeText(this,throwable.message,Toast.LENGTH_SHORT).show()
    }

    override fun <T> onNext(dat: T) {
        when(dat){
            is FileResponse ->{
                mImageList.removeAt(0)
                mSuccessImageList.add(dat.fid)
                requestImg()
                return
            }
            is CameraBean->{
                mDialog?.dismiss()
                mCameraBean = dat
                return
            }
        }
        mDialog?.dismiss()
        (dat as? BaseResponse<*>)?.run {
            data as? TransferOrderResponse
        }?.run {
            if(result==0){
                PromptDialog(this@TransferOrderActivity,build = PromptDialog.DialogBuild().apply {
                    content=getString(R.string.app_tv17)
                    mListener=object : DialogListener {
                        override fun dismiss() {
                            setResult(1033)
                            finish()
                        }
                    }
                }).show()
            }else{
                showMessage(R.string.request_error3)
                if(mJsonType!=1){
                    return
                }
                if(mIdList.size>dataSet?.size?:0 && dataSet?.isNotEmpty()==true){
                    mRefresh=true
                }
                mIdList?.clear()
                dataSet?.forEach {data->
                    mIdList.add(DeliveryOrderListBean.DataSetBean().apply {
                        taskId=data
                    })
                }
            }
            return
        }
        showMessage(R.string.request_error_1)
    }

    private fun requestImg(){
        if(mImageList.isEmpty()){
            request()
            return
        }
        mViewModel=mViewModel?:getViewModel()
        mViewModel?.uploadImg(mImageList[0])
    }
    private var mJsonType=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.apply {
            mType=getIntExtra("type",-1)
            val json=getStringExtra("json")
            poolId=getStringExtra("poolId")
            taskId=getStringExtra("taskId")
            times=getStringExtra("times")
            mRequest.times=times
            mRequest.ids=taskId
            mRequest.taskType=getStringExtra("taskType")
        }
        mJsonType=intent.getIntExtra("jsonType",0)
        if(mJsonType==1){
            val json=intent.getStringExtra("json")
            Gson().fromJson(json,DeliveryOrderListBean::class.java)?.dataSet?.forEach {
                mIdList.add(it)
            }
        }
        poi_img_add7.setOnClickListener {
            showPicker(arrayListOf<String>().apply {
                add(getString(R.string.pz))
                add(getString(R.string.xc))
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                if(index==1){
                    RxGalleryFinalApi
                            .openRadioSelectImage(this, object : RxBusResultDisposable<ImageRadioResultEvent>() {
                                override fun onEvent(t: ImageRadioResultEvent?) {
                                    if(mImgUri.isNullOrEmpty()){
                                        form_img1.visibility=View.VISIBLE
                                        mImgUri=t?.result?.originalPath?:""
                                        setFresco(mImgUri, form_img1)
                                    }else if(mImgUri2.isNullOrEmpty()){
                                        mImgUri2=t?.result?.originalPath?:""
                                        form_img2.visibility=View.VISIBLE
                                        setFresco(mImgUri2, form_img2)
                                    }else if(mImgUri3.isNullOrEmpty()){
                                        mImgUri3=t?.result?.originalPath?:""
                                        form_img3.visibility=View.VISIBLE
                                        setFresco(mImgUri3, form_img3)
                                    }else if(mImgUri4.isNullOrEmpty()){
                                        mImgUri4=t?.result?.originalPath?:""
                                        form_img4.visibility=View.VISIBLE
                                        setFresco(mImgUri4, form_img4)
                                    }else if(mImgUri5.isNullOrEmpty()){
                                        mImgUri5=t?.result?.originalPath?:""
                                        form_img5.visibility=View.VISIBLE
                                        setFresco(mImgUri5, form_img5)
                                    }
                                }
                            },true)
                }else{
                    camearPermissions()
                }
            })
        }
        toolbarInit {
            mToolbar=toolbar2
            data= ToolbarBean(getString(R.string.deliverydetails_tv7),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        check(0)
        check1_parent.setOnClickListener {
            check(0)
        }
        check2_parent.setOnClickListener {
            check(1)
        }
        check3_parent.setOnClickListener {
            check(2)
        }
        check4_parent.setOnClickListener {
            check(3)
        }
        value?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                size?.text="${s?.length}/100"
            }
        })
        bt?.setOnClickListener {
            when {
                value?.editableText?.toString()?.isNullOrBlank()==true -> showMessage(R.string.cancelreservation_tv10)
                value?.editableText?.toString()?.length?:0<=9 -> showMessage(R.string.cancelreservation_tv9)
                else -> {
                    mRequest.remark=value?.editableText?.toString()?:""
                    if(mDialog?.isShowing==true){
                        return@setOnClickListener
                    }
                    mDialog= mDialog?:PromptDialog(this,build = PromptDialog.DialogBuild().apply {
                        type=1
                    })
                    mDialog?.show()
                    mImageList.clear()
                    mSuccessImageList.clear()
                    if(mImgUri.isNotBlank()){
                        mImageList.add(mImgUri)
                    }
                    if(mImgUri2.isNotBlank()){
                        mImageList.add(mImgUri2)
                    }
                    if(mImgUri3.isNotBlank()){
                        mImageList.add(mImgUri3)
                    }
                    if(mImgUri4.isNotBlank()){
                        mImageList.add(mImgUri4)
                    }
                    if(mImgUri5.isNotBlank()){
                        mImageList.add(mImgUri5)
                    }
                    requestImg()
                }
            }
        }
        getBaseViewModel<BaseViewModel>()?.setRequestCode(11107)
    }

    private fun request(){
        mViewModel=mViewModel?:getViewModel()
        if(mIdList.isNotEmpty()){
            mRequest.ids=""
        }
        (0 until mIdList.size).forEach {
            if(it==0){
                mRequest.ids+="${mIdList[it].taskId}"
            }else{
                mRequest.ids+=",${mIdList[it].taskId}"
            }
        }
        mViewModel?.apply {
            updateTransferOrder(mRequest)
        }
    }

    private fun check(index:Int){
        check1_img.setBackgroundResource(R.drawable.check_bg)
        check2_img.setBackgroundResource(R.drawable.check_bg)
        check3_img.setBackgroundResource(R.drawable.check_bg)
        check4_img.setBackgroundResource(R.drawable.check_bg)

        when(index){
            0->check1_img.setBackgroundResource(R.mipmap.check_selected)
            1->check2_img.setBackgroundResource(R.mipmap.check_selected)
            2->check3_img.setBackgroundResource(R.mipmap.check_selected)
            3->check4_img.setBackgroundResource(R.mipmap.check_selected)
        }
        mRequest.transferOrderReason="$index"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DataConfig.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            mCameraBean.imgUri?.apply {
                when {
                    mImgUri.isNullOrEmpty() -> {
                        form_img1.visibility=View.VISIBLE
                        mImgUri=mCameraBean.imgPath
                        setFresco(mImgUri, form_img1)
                    }
                    mImgUri2.isNullOrEmpty() -> {
                        mImgUri2=mCameraBean.imgPath
                        form_img2.visibility=View.VISIBLE
                        setFresco(mImgUri2, form_img2)
                    }
                    mImgUri3.isNullOrEmpty() -> {
                        mImgUri3=mCameraBean.imgPath
                        form_img3.visibility=View.VISIBLE
                        setFresco(mImgUri3, form_img3)
                    }
                    mImgUri4.isNullOrEmpty() -> {
                        mImgUri4=mCameraBean.imgPath
                        form_img4.visibility=View.VISIBLE
                        setFresco(mImgUri4, form_img4)
                    }
                    mImgUri5.isNullOrEmpty() -> {
                        mImgUri5=mCameraBean.imgPath
                        form_img5.visibility=View.VISIBLE
                        setFresco(mImgUri5, form_img5)
                    }
                }
            }
        }
    }
    override fun finish() {
        if(mRefresh){
            getBaseViewModel<LoginViewModel>()?.setRequestCode(1333,1001)
        }
        super.finish()
    }
}