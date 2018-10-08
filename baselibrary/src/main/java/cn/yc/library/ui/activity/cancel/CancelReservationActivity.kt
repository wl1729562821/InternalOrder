package cn.yc.library.ui.activity.cancel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent
import cn.qqtheme.framework.picker.SinglePicker
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.CameraBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.request.CancelReservationRequest
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.FileResponse
import cn.yc.library.bean.response.HomeOneDetailsBean
import cn.yc.library.bean.response.TransferOrderBean
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.adapter.ImageAdapter
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.setFresco
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.OrderViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_cancelreservation.*


class CancelReservationActivity : BaseActivity() {

    private var mImageAdapter: ImageAdapter? = null

    private var mRequest = CancelReservationRequest()
    private var mViewModel: OrderViewModel? = null

    private var mData: HomeOneDetailsBean.DataSetBean? = null

    private val mImageList = arrayListOf<String>()
    private val mSuccessImageList = arrayListOf<String>()

    private var mCameraBean = CameraBean()

    private var mImgUri = ""
    private var mImgUri2 = ""
    private var mImgUri3 = ""
    private var mImgUri4 = ""
    private var mImgUri5 = ""

    private var mImgIndex = 0

    override val layoutId: Int
        get() = R.layout.activity_cancelreservation

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        when (dat) {
            is FileResponse -> {
                mImageList.removeAt(0)
                mSuccessImageList.add(dat.fid)
                requestImg()
                return
            }
            is CameraBean -> {
                mDialog?.dismiss()
                mCameraBean = dat
                return
            }
        }
        mDialog?.dismiss()
        (dat as? BaseResponse<*>)?.run {
            data as? TransferOrderBean
        }?.run {
            showMessage(R.string.request_success3)
            setResult(1033)
            finish()
            return
        }
        showMessage(R.string.request_error3)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onClick: () -> Unit = {
            showPicker(arrayListOf<String>().apply {
                add(getString(R.string.pz))
                add(getString(R.string.xc))
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                if (index == 1) {
                    RxGalleryFinalApi
                            .openRadioSelectImage(this, object : RxBusResultDisposable<ImageRadioResultEvent>() {
                                override fun onEvent(t: ImageRadioResultEvent?) {
                                    when {
                                        mImgUri.isNullOrEmpty() -> {
                                            form_img1.visibility = View.VISIBLE
                                            mImgUri = t?.result?.originalPath ?: ""
                                            setFresco(mImgUri, form_img1)
                                        }
                                        mImgUri2.isNullOrEmpty() -> {
                                            mImgUri2 = t?.result?.originalPath ?: ""
                                            form_img2.visibility = View.VISIBLE
                                            setFresco(mImgUri2, form_img2)
                                        }
                                        mImgUri3.isNullOrEmpty() -> {
                                            mImgUri3 = t?.result?.originalPath ?: ""
                                            form_img3.visibility = View.VISIBLE
                                            setFresco(mImgUri3, form_img3)
                                        }
                                        mImgUri4.isNullOrEmpty() -> {
                                            mImgUri4 = t?.result?.originalPath ?: ""
                                            form_img4.visibility = View.VISIBLE
                                            setFresco(mImgUri4, form_img4)
                                        }
                                        mImgUri5.isNullOrEmpty() -> {
                                            mImgUri5 = t?.result?.originalPath ?: ""
                                            form_img5.visibility = View.VISIBLE
                                            setFresco(mImgUri5, form_img5)
                                        }
                                    }
                                }
                            }, true)
                } else {
                    camearPermissions()
                }
            })
        }

        poi_img_add7.setOnClickListener {
            onClick()
        }
        intent.run {
            val json = getStringExtra("json")
            mData = Gson().fromJson(json, HomeOneDetailsBean.DataSetBean::class.java)
            mData
        }?.apply {
            mRequest.cancelType = "0"
            mRequest.taskType = "0"
            mRequest.taskId = "$taskId"

        }

        toolbarInit {
            mToolbar = toolbar2
            data = ToolbarBean(getString(R.string.deliverydetails_tv6), "", false).apply {
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
        check5_parent.setOnClickListener {
            check(4)
        }
        check6_parent.setOnClickListener {
            check(5)
        }
        value?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                size?.text = "${s?.length}/100"
            }
        })
        bt?.setOnClickListener {
            if (value?.editableText?.trim()?.length?:0>=10) {
                mRequest.feedbackDesc = value?.editableText?.toString() ?: ""
                if (mDialog?.isShowing == true) {
                    return@setOnClickListener
                }
                mDialog = mDialog ?: PromptDialog(this, build = PromptDialog.DialogBuild().apply {
                    type = 1
                })
                mDialog?.show()
                mImageList.clear()
                mSuccessImageList.clear()
                if (mImgUri.isNotBlank()) {
                    mImageList.add(mImgUri)
                }
                if (mImgUri2.isNotBlank()) {
                    mImageList.add(mImgUri2)
                }
                if (mImgUri3.isNotBlank()) {
                    mImageList.add(mImgUri3)
                }
                if (mImgUri4.isNotBlank()) {
                    mImageList.add(mImgUri4)
                }
                if (mImgUri5.isNotBlank()) {
                    mImageList.add(mImgUri5)
                }
                requestImg()
            } else {
                showMessage(R.string.cancelreservation_tv9)
            }
        }
    }

    private fun requestImg() {
        if (mImageList.isEmpty()) {
            request()
            return
        }
        mViewModel = mViewModel ?:getViewModel()
        mViewModel?.uploadImg(mImageList[0])
    }

    private fun request() {
        mViewModel = mViewModel ?: getViewModel()
        mImageAdapter?.getList()?.apply {
            /*val file=File(get(0))
            if(file.exists()){
                mRequest.proofPic1=get(0)
            }
            if(size>=2){
                val file=File(get(1))
                if(file.exists()){
                    mRequest.proofPic2=get(1)
                }
            }
            if(size>=3){
                val file=File(get(2))
                if(file.exists()){
                    mRequest.proofPic3=get(2)
                }
            }
            if(size>=4){
                val file=File(get(3))
                if(file.exists()){
                    mRequest.proofPic4=get(3)
                }
            }
            if(size>=5){
                val file=File(get(4))
                if(file.exists()){
                    mRequest.proofPic5=get(4)
                }
            }
            if(size==6){
                val file=File(get(3))
                if(file.exists()){
                    mRequest.proofPic4=get(3)
                }
            }*/
        }
        if (mSuccessImageList.size > 0) {
            mRequest.proofPic1 = mSuccessImageList[0]
        }
        if (mSuccessImageList.size > 1) {
            mRequest.proofPic2 = mSuccessImageList[1]
        }
        if (mSuccessImageList.size > 2) {
            mRequest.proofPic3 = mSuccessImageList[2]
        }
        if (mSuccessImageList.size > 3) {
            mRequest.proofPic4 = mSuccessImageList[3]
        }
        if (mSuccessImageList.size > 4) {
            mRequest.proofPic5 = mSuccessImageList[4]
        }
        mViewModel?.apply {
            cancelReservation(mRequest)
        }
    }

    private fun check(index: Int) {
        check1_img.setBackgroundResource(R.drawable.check_bg)
        check2_img.setBackgroundResource(R.drawable.check_bg)
        check3_img.setBackgroundResource(R.drawable.check_bg)
        check4_img.setBackgroundResource(R.drawable.check_bg)
        check5_img.setBackgroundResource(R.drawable.check_bg)
        check6_img.setBackgroundResource(R.drawable.check_bg)

        when (index) {
            0 -> check1_img.setBackgroundResource(R.mipmap.check_selected)
            1 -> check2_img.setBackgroundResource(R.mipmap.check_selected)
            2 -> check3_img.setBackgroundResource(R.mipmap.check_selected)
            3 -> check4_img.setBackgroundResource(R.mipmap.check_selected)
            4 -> check5_img.setBackgroundResource(R.mipmap.check_selected)
            5 -> check6_img.setBackgroundResource(R.mipmap.check_selected)
        }
        mRequest.feedbackType = "$index"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DataConfig.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            mCameraBean.imgUri?.apply {
                if (mImgUri.isNullOrEmpty()) {
                    form_img1.visibility = View.VISIBLE
                    mImgUri = mCameraBean.imgPath
                    setFresco(mImgUri, form_img1)
                } else if (mImgUri2.isNullOrEmpty()) {
                    mImgUri2 = mCameraBean.imgPath
                    form_img2.visibility = View.VISIBLE
                    setFresco(mImgUri2, form_img2)
                } else if (mImgUri3.isNullOrEmpty()) {
                    mImgUri3 = mCameraBean.imgPath
                    form_img3.visibility = View.VISIBLE
                    setFresco(mImgUri3, form_img3)
                } else if (mImgUri4.isNullOrEmpty()) {
                    mImgUri4 = mCameraBean.imgPath
                    form_img4.visibility = View.VISIBLE
                    setFresco(mImgUri4, form_img4)
                } else if (mImgUri5.isNullOrEmpty()) {
                    mImgUri5 = mCameraBean.imgPath
                    form_img5.visibility = View.VISIBLE
                    setFresco(mImgUri5, form_img5)
                }
            }
        }
    }
}