package com.ysgj.order.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import cn.finalteam.rxgalleryfinal.utils.EmptyViewUtils.showMessage
import cn.qqtheme.framework.picker.DateTimePicker
import cn.qqtheme.framework.picker.SinglePicker
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.request.ListingInfoRequest
import cn.yc.library.bean.request.ListingRequest
import cn.yc.library.bean.request.OrderSetting
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.FileResponse
import cn.yc.library.bean.response.ListingDidateResponse
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.checkZh
import cn.yc.library.utils.setBlankText
import cn.yc.library.utils.setStringRes
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.model.LatLngBounds
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import kotlinx.android.synthetic.main.activity_zdyd.*
import kotlinx.android.synthetic.main.item_zdyd1.view.*
import kotlinx.android.synthetic.main.item_zdyd2.view.*
import java.net.InetAddress

class RespuestaautomáticaActivity : BaseActivity() {

    private var mViewModel: LoginViewModel?=null
    private var mType=0

    private var mMinPrice="0.0"

    private var mCheckMapParent2=false

    private var mCheckMapParent=false

    private var mRequest= ListingRequest()
    private var mImgRequest= ListingInfoRequest()

    private var mVm: OrderViewModel?=null

    private var mImgFileType=0
    private var mSettingList= arrayListOf<OrderSetting>()
    private val mSetting=OrderSetting()

    private val mImageList= arrayListOf<String>()
    private val mSuccessImageList= arrayListOf<String>()
    private var mInitRequest=ListingInfoRequest()

    private var mPlaceDetectionClient: PlaceDetectionClient?=null
    private var mGeoDataClient: GeoDataClient?=null
    private var mFusedLocationProviderClient: FusedLocationProviderClient?=null

    private val mDefaultHandler=DefaultHandler()
    private var mCheckNetwork=false

    internal inner class DefaultHandler: Handler(){
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

    private val mCheckMap0= hashMapOf<Int,Boolean>().apply {
        for(i in 0..3){
            put(i,false)
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_zdyd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGeoDataClient= Places.getGeoDataClient(this,null)
        mPlaceDetectionClient= Places.getPlaceDetectionClient(this,null)
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        mDialog=mDialog?: PromptDialog(this, PromptDialog.DialogBuild().apply {
            type=1
        })
        mType=intent.getIntExtra("type",0)
        val json=intent.getStringExtra("json")
        val initJson=intent.getStringExtra("initJson")
        mInitRequest=Gson().fromJson(initJson,ListingInfoRequest::class.java)
        val imgJson=intent.getStringExtra("imgJson")
        mImgRequest=Gson().fromJson(imgJson,ListingInfoRequest::class.java)
        mRequest=Gson().fromJson(json,ListingRequest::class.java)
        mSetting.houseId=mRequest.houseId
        val getPath:(String,String)->String={updatePath,path->
            //如果他们一样说明没有修改图片 如果传过来的值是空的说明这个图片是选填的不是必须
            when {
                updatePath==path -> "add_xx1_xx1"
                updatePath.isNullOrBlank() -> "add_xx_xx"
                else -> updatePath
            }
        }
        //业主
        if (mRequest.applicantType == "0") {
            mImageList.add(getPath(mImgRequest.imgList_1[0],mRequest.pocImg1))
            mImageList.add(getPath(mImgRequest.imgList_1[1],mRequest.pocImg2))
            mImageList.add(getPath(mImgRequest.imgList_1[2],mRequest.pocImg3))

            mImageList.add(getPath(mImgRequest.imgList_2[0],mRequest.reoPassportImg1))
            mImageList.add(getPath(mImgRequest.imgList_2[1],mRequest.reoPassportImg2))
            mImageList.add(getPath(mImgRequest.imgList_2[2],mRequest.reoPassportImg3))

            if (mRequest.leaseType == "0") {
                mImageList.add(getPath(mImgRequest.imgList_3[0],mRequest.rentAuthorizationSignImg1))
                mImageList.add(getPath(mImgRequest.imgList_3[1],mRequest.rentAuthorizationSignImg2))
                mImageList.add(getPath(mImgRequest.imgList_3[2],mRequest.rentAuthorizationSignImg1))
                mImgFileType=0
            } else {//出售
                mImgFileType=1
                mImageList.add(getPath(mImgRequest.imgList_3[0],mRequest.formaConfirmImg1))
                mImageList.add(getPath(mImgRequest.imgList_3[1],mRequest.formaConfirmImg2))
                mImageList.add(getPath(mImgRequest.imgList_3[2],mRequest.formaConfirmImg3))
            }
        } else {//poa
            mImageList.add(getPath(mImgRequest.imgList_1[0],mRequest.mandataryCopiesImg1))
            mImageList.add(getPath(mImgRequest.imgList_1[1],mRequest.mandataryCopiesImg2))
            mImageList.add(getPath(mImgRequest.imgList_1[2],mRequest.mandataryCopiesImg3))
            mImageList.add(getPath(mImgRequest.imgList_1[3],mRequest.mandataryCopiesImg4))
            mImageList.add(getPath(mImgRequest.imgList_1[4],mRequest.mandataryCopiesImg5))
            mImageList.add(getPath(mImgRequest.imgList_1[5],mRequest.mandataryCopiesImg6))
            mImageList.add(getPath(mImgRequest.imgList_1[6],mRequest.mandataryCopiesImg7))
            mImageList.add(getPath(mImgRequest.imgList_1[7],mRequest.mandataryCopiesImg8))
            mImageList.add(getPath(mImgRequest.imgList_1[8],mRequest.mandataryCopiesImg9))
            mImageList.add(getPath(mImgRequest.imgList_1[9],mRequest.mandataryCopiesImg10))

            mImageList.add(getPath(mImgRequest.imgList_2[0],mRequest.mandataryPassportImg1))
            mImageList.add(getPath(mImgRequest.imgList_2[1],mRequest.mandataryPassportImg2))
            mImageList.add(getPath(mImgRequest.imgList_2[2],mRequest.mandataryPassportImg3))

            mImageList.add(getPath(mImgRequest.imgList_3[0],mRequest.mandataryVisaImg1))
            mImageList.add(getPath(mImgRequest.imgList_3[0],mRequest.mandataryVisaImg2))
            mImageList.add(getPath(mImgRequest.imgList_3[0],mRequest.mandataryVisaImg3))

            mImageList.add(getPath(mImgRequest.imgList_4[0],mRequest.mandataryIdcardImg1))
            mImageList.add(getPath(mImgRequest.imgList_4[1],mRequest.mandataryIdcardImg2))
            mImageList.add(getPath(mImgRequest.imgList_4[2],mRequest.mandataryIdcardImg3))
            mImageList.add(getPath(mImgRequest.imgList_4[3],mRequest.mandataryIdcardImg4))

            mImageList.add(getPath(mImgRequest.imgList_5[0],mRequest.pocImg1))
            mImageList.add(getPath(mImgRequest.imgList_5[1],mRequest.pocImg2))
            mImageList.add(getPath(mImgRequest.imgList_5[2],mRequest.pocImg3))

            mImageList.add(getPath(mImgRequest.imgList_6[0],mRequest.reoPassportImg1))
            mImageList.add(getPath(mImgRequest.imgList_6[1],mRequest.reoPassportImg2))
            mImageList.add(getPath(mImgRequest.imgList_6[2],mRequest.reoPassportImg3))

            // 出租
            if (mRequest.leaseType == "0") {
                mImageList.add(getPath(mImgRequest.imgList_7[0],mRequest.rentAuthorizationSignImg1))
                mImageList.add(getPath(mImgRequest.imgList_7[1],mRequest.rentAuthorizationSignImg2))
                mImageList.add(getPath(mImgRequest.imgList_7[2],mRequest.rentAuthorizationSignImg3))
                mImgFileType=2
            } else {//出售
                mImgFileType=3
                mImageList.add(getPath(mImgRequest.imgList_7[0],mRequest.formaConfirmImg1))
                mImageList.add(getPath(mImgRequest.imgList_7[1],mRequest.formaConfirmImg2))
                mImageList.add(getPath(mImgRequest.imgList_7[2],mRequest.formaConfirmImg3))
            }
        }
        //出租情况下才会有合同
        mImageList.add(getPath(mImgRequest.imgList_8[0],mRequest.houseRentContractImg1))
        mImageList.add(getPath(mImgRequest.imgList_8[1],mRequest.houseRentContractImg2))
        mImageList.add(getPath(mImgRequest.imgList_8[2],mRequest.houseRentContractImg3))
        mImageList.add(getPath(mImgRequest.imgList_8[3],mRequest.houseRentContractImg4))
        if(mRequest.leaseType=="1"){
            mImageList.add(getPath(mImgRequest.imgList_10[0],mRequest.houseHoldImg1))
            mImageList.add(getPath(mImgRequest.imgList_10[1],mRequest.houseHoldImg2))
            mImageList.add(getPath(mImgRequest.imgList_10[2],mRequest.houseHoldImg3))
            mImageList.add(getPath(mImgRequest.imgList_10[3],mRequest.houseHoldImg4))
            mImageList.add(getPath(mImgRequest.imgList_10[4],mRequest.houseHoldImg5))
            mImageList.add(getPath(mImgRequest.imgList_10[5],mRequest.houseHoldImg6))
            mImageList.add(getPath(mImgRequest.imgList_10[6],mRequest.houseHoldImg7))
            mImageList.add(getPath(mImgRequest.imgList_10[7],mRequest.houseHoldImg8))
            mImageList.add(getPath(mImgRequest.imgList_10[8],mRequest.houseHoldImg9))
            mImageList.add(getPath(mImgRequest.imgList_10[9],mRequest.houseHoldImg10))
        }

        mMinPrice=intent.getStringExtra("price")
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.app_tv9),"",false).apply {
                settingTitle=getString(R.string.app_tv10)
            }
            onBack {
                finish()
            }
            onSetting {
                //mSetting.refresh()
                mRequest.setting=""
                mRequestLocation=false
                requestImg()
            }
        }

        val addItem:()->Unit={
            mSettingList.add(OrderSetting().apply {
                //houseId=mRequest.houseId
                isOpen="0"
                houseRentPrice=mMinPrice
                type=mType
                if(type==1){
                    payType="0"
                    hasExpectApprove="1"
                }
            })
            if(mType==0){
                zdyd_parent.addView(LayoutInflater.from(this).inflate(R.layout.item_zdyd1,null,false))
            }else{
                zdyd_parent.addView(LayoutInflater.from(this).inflate(R.layout.item_zdyd2,null,false))
            }
        }
        if(mType!=0){
            add_bt.visibility=View.GONE
            parent0.setStringRes(R.string.app_tv40)
        }
        addItem()
        add_bt.setOnClickListener {
            if(mSettingList.size<3){
                addItem()
                if(mSettingList.size==3){
                    add_bt.visibility=View.GONE
                }
                refreshUI(mSettingList.size-1)
            }
        }
        refreshUI(0)
        bt.setOnClickListener {
            mSettingList.forEach {
                if (it.type==1) {
                    if(it.isOpen == "1" && it.houseRentPrice.isNullOrBlank()){
                        showMessage(getString(R.string.houses_tv44))
                        return@setOnClickListener
                    }
                }else{
                    if(it.isOpen=="1"){
                        when {
                            it.beginRentDate.isNullOrBlank() -> {
                                showMessage(getString(R.string.edit_error25))
                                return@setOnClickListener
                            }
                            it.rentTime == "0" -> {
                                showMessage(R.string.zpsc)
                                return@setOnClickListener
                            }
                            it.houseRentPrice.isNullOrBlank() -> {
                                showMessage(R.string.srzj)
                                return@setOnClickListener
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
            val gson= GsonBuilder().registerTypeAdapter(OrderSetting::class.java,
                    object : TypeAdapter<OrderSetting>(){
                        override fun read(jsonReader: JsonReader?): OrderSetting {
                            val setting= OrderSetting()
                            jsonReader?.apply {
                                beginObject()
                                while (jsonReader.hasNext()) {
                                    when(jsonReader.nextName()){
                                        "beginRentDate"->setting.beginRentDate=jsonReader.nextString()
                                        "hasExpectApprove"->setting.hasExpectApprove=jsonReader.nextString()
                                        "houseId"->setting.houseId=jsonReader.nextString()
                                        "houseRentPrice"->setting.houseRentPrice=jsonReader.nextString()
                                        "id"->setting.id=jsonReader.nextString()
                                        "isOpen"->setting.isOpen=jsonReader.nextString()
                                        "payNode"->setting.payNode=jsonReader.nextString()
                                        "payType"->setting.payType=jsonReader.nextString()
                                        "rentTime"->setting.rentTime=jsonReader.nextString()
                                        "startRentDat"->setting.startRentDate=jsonReader.nextString()
                                    }
                                }
                                endObject()
                            }
                            return setting
                        }

                        override fun write(out: JsonWriter?, value: OrderSetting?) {
                            out?.apply {
                                beginObject()
                                if (value?.beginRentDate?.isNullOrEmpty() == false) {
                                    name("beginRentDate").value(value?.beginRentDate)
                                }
                                if (value?.hasExpectApprove?.isNullOrEmpty() == false) {
                                    name("hasExpectApprove").value(value?.hasExpectApprove)
                                }
                                if (value?.houseId?.isNullOrEmpty() == false) {
                                    name("houseId").value(value?.houseId)
                                }
                                if (value?.houseRentPrice?.isNullOrEmpty() == false) {
                                    name("houseRentPrice").value(value?.houseRentPrice)
                                }
                                if (value?.id?.isNullOrEmpty() == false) {
                                    name("id").value(value?.id)
                                }
                                if (value?.isOpen?.isNullOrEmpty() == false) {
                                    name("isOpen").value(value?.isOpen)
                                }
                                if (value?.payNode?.isNullOrEmpty() == false) {
                                    name("payNode").value(value?.payNode)
                                }
                                if (value?.payType?.isNullOrEmpty() == false) {
                                    name("payType").value(value?.payType)
                                }
                                if (value?.rentTime?.isNullOrEmpty() == false) {
                                    name("rentTime").value(value?.rentTime)
                                }
                                if (value?.startRentDate?.isNullOrEmpty() == false) {
                                    name("startRentDate").value(value?.startRentDate)
                                }
                                endObject()
                            }
                        }
                    }).create()
            val json=gson.toJson(mSettingList)
            mRequest.setting=json
            mRequestLocation=false
            requestImg()
        }
        getBaseViewModel<BaseViewModel>()?.setRequestCode(11107)
    }

    private fun refreshUI(index: Int){
        val data=mSettingList[index]
        val setText:(TextView)->Unit={
            it.setStringRes(when(index){
                1->R.string.zdyd_tv2
                2->R.string.zdyd_tv3
                else->R.string.zdyd_tv1
            })
        }
        zdyd_parent.getChildAt(index).apply {
            if(data.type==0){
                if(index==0){
                    header1_line.visibility=View.GONE
                }
                setText(title_tv)
                switch_button1.also {
                    zdsj1.isEnabled=false
                    it.isChecked=false
                    it.setOnCheckedChangeListener { view, isChecked ->
                        data.isOpen=if(isChecked){
                            "1"
                        }else{
                            "0"
                        }
                        if(data.isOpen=="1"){
                            zdsj1.isEnabled=true
                            qzrq1.setBlankText(data.beginRentDate)
                            qzrq1.setOnClickListener {
                                if(data.isOpen=="0"){
                                    return@setOnClickListener
                                }
                                showYearMonthDayPicker(DateTimePicker.OnYearMonthDayTimePickListener { year, month, day, hour, minute ->
                                    val value = "$year-$month-$day 00:00:00"
                                    data.beginRentDate = value
                                    qzrq1.text = data.beginRentDate
                                })
                            }
                            if(data.rentTime.isNotBlank()){
                                sc1.text = "${data.rentTime}${getString(R.string.heihei_tv17)}"
                            }
                            sc1.setOnClickListener {
                                if(data.isOpen=="0"){
                                    return@setOnClickListener
                                }
                                showPicker(arrayListOf<String>().apply {
                                    for (i in 1..10) {
                                        add("$i")
                                    }
                                }, SinglePicker.OnItemPickListener<String> { index, item ->
                                    sc1.text = "$item ${getString(R.string.heihei_tv17)}"
                                    data.rentTime = "$item"
                                })
                            }
                            zfjd1.text = when(data.payNode) {
                                "1" -> getString(R.string.paynode_type1)
                                "2" -> getString(R.string.paynode_type2)
                                "4" -> getString(R.string.paynode_type3)
                                "6" -> getString(R.string.paynode_type4)
                                "12" -> getString(R.string.paynode_type5)
                                else -> ""
                            }
                            zfjd1.setOnClickListener {
                                if(data.isOpen=="0"){
                                    return@setOnClickListener
                                }
                                showPicker(arrayListOf<String>().apply {
                                    add(getString(R.string.paynode_type1))
                                    add(getString(R.string.paynode_type2))
                                    add(getString(R.string.paynode_type3))
                                    add(getString(R.string.paynode_type4))
                                    add(getString(R.string.paynode_type5))
                                }, SinglePicker.OnItemPickListener<String> { index, item ->
                                    data.payNode = when(index){
                                        1->"2"
                                        2->"4"
                                        3->"6"
                                        4->"12"
                                        else->"1"
                                    }
                                    zfjd1.text = item
                                })
                            }
                            zdsj1.setBlankText(data.houseRentPrice)
                            zdsj1.addTextChangedListener(object : TextWatcher {
                                override fun afterTextChanged(s: Editable?) {

                                }

                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                                }

                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                    data.houseRentPrice=s?.trim()?.toString()?:""
                                }
                            })
                        }else{
                            val price1=data.houseRentPrice
                            zdsj1.setText("")
                            zfjd1.text = ""
                            sc1.text = ""
                            zdsj1.isEnabled=false
                            qzrq1.text = ""
                            data.houseRentPrice=price1
                        }
                    }
                }
            }else{
                if(index==0){
                    header_line.visibility=View.GONE
                }
                setText(title_tv2)
                switch_button.also {
                    it.isChecked=false
                    price.isEnabled=false
                    it.setOnCheckedChangeListener { view, isChecked ->
                        data.isOpen=if(isChecked){
                            "1"
                        }else{
                            "0"
                        }
                        if(data.isOpen=="1"){
                            price.isEnabled=true
                            val refreshPay:()->Unit={
                                if(data.payType=="0"){
                                    parent3.visibility=View.GONE
                                    dk_img.isChecked=false
                                    xj_img.isChecked=true
                                }else{
                                    parent3.visibility=View.VISIBLE
                                    dk_img.isChecked=true
                                    xj_img.isChecked=false
                                }
                            }
                            val refreshYsp:()->Unit={
                                if(data.hasExpectApprove=="0"){
                                    yes_img.isChecked=true
                                    no_img.isChecked=false
                                }else{
                                    yes_img.isChecked=false
                                    no_img.isChecked=true
                                }
                            }
                            if(data.hasExpectApprove.isNullOrBlank()){
                                data.hasExpectApprove="1"
                            }
                            if(data.payType.isNullOrBlank()){
                                data.payType="1"
                            }
                            refreshYsp()
                            refreshPay()
                            xj_img.setOnClickListener {
                                if(data.isOpen=="0" || data.payType=="0"){
                                    return@setOnClickListener
                                }
                                data.payType=if(data.payType=="0"){
                                    "1"
                                }else{
                                    "0"
                                }
                                refreshPay()

                            }
                            dk_img.setOnClickListener {
                                if(data.isOpen=="0" || data.payType=="1"){
                                    return@setOnClickListener
                                }
                                data.payType=if(data.payType=="0"){
                                    "1"
                                }else{
                                    "0"
                                }
                                refreshPay()

                            }
                            yes_img.setOnClickListener {
                                if(data.isOpen=="0" || data.hasExpectApprove=="1"){
                                    return@setOnClickListener
                                }
                                data.hasExpectApprove=if(data.hasExpectApprove=="0"){
                                    "1"
                                }else{
                                    "0"
                                }
                                refreshYsp()
                            }
                            no_img.setOnClickListener {
                                if(data.isOpen=="0" || data.hasExpectApprove=="0"){
                                    return@setOnClickListener
                                }
                                data.hasExpectApprove=if(data.hasExpectApprove=="0"){
                                    "1"
                                }else{
                                    "0"
                                }
                                refreshYsp()
                            }
                            price.setBlankText(data.houseRentPrice)
                            price.addTextChangedListener(object :TextWatcher{
                                override fun afterTextChanged(s: Editable?) {

                                }

                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                                }

                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                    data.houseRentPrice=s?.trim()?.toString()?:""
                                }
                            })
                        }
                        else{
                            xj_img.isChecked=false
                            dk_img.isChecked=false
                            yes_img.isChecked=false
                            no_img.isChecked=false
                            val price1=data.houseRentPrice
                            price.text = null
                            price.isEnabled=false
                            data.houseRentPrice=price1
                        }
                    }
                }
            }
        }
    }

    private fun getImg(index:Int):Int{
        return when(mType){

            1->{
                if(mCheckMap0[index]==true){
                    R.mipmap.check_selected
                }else{
                    R.drawable.check_bg
                }
            }
            else->R.drawable.check_bg
        }
    }

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        if(dat is String && dat=="location"){
            mPlaceDetectionClient?.getCurrentPlace(null)?.addOnCompleteListener {
                if(it==null || !it.isSuccessful || it.result==null || it.result?.count?:0<=0) {
                    mDialog?.dismiss()
                    showMessage(R.string.location_error)
                    return@addOnCompleteListener
                }
                (it.result as? PlaceLikelihoodBufferResponse)?.apply {
                    mDialog?.show()
                    val data=get(0)
                    mVm?.requestLocation(mRequest.taskId,"${data.place?.latLng?.longitude?:0.0}",
                            "${data.place?.latLng?.latitude?:0.0}","${data?.place?.address?:""}")
                }
            }
            return
        }
        //到达地点成功
        ((dat as? BaseResponse<*>)?.data as? LoginResponse)?.apply {
            if(result==0){
                mRequestLocation=true
                mDialog?.show()
                requestImg()
            }else{
                mDialog?.dismiss()
                showMessage(message)
            }
            return
        }
        //上传图片成功
        if(dat is FileResponse && dat.fid.isNotBlank()){
            Log.e(TAG,"onNext ${mImageList.size}  ${mSuccessImageList.size}")
            mSuccessImageList.add(dat.fid)
            mImageList.removeAt(0)
            requestImg()
            return
        }
        ((dat as BaseResponse<*>).data as? ListingDidateResponse)?.apply {
            mDialog?.dismiss()
            if(dat.code==0){
                showMessage(R.string.app_tv11)
                //通知界面更新
                getBaseViewModel<LoginViewModel>()?.setRequestCode(1001)
                setResult(1033)
                finish()
                return
            }
            showMessage(dat.message)
        }
        mDialog?.dismiss()
    }

    private var mRequestLocation=false
    private fun requestLocation(){
        mDialog?.show()
        val t=object :Thread(){
            override fun run() {
                mCheckNetwork= InetAddress.getByName("www.google.com").isReachable(6000)
                mDefaultHandler.sendEmptyMessage(2)
            }
        }
        t.start()
    }

    private fun requestImg(){
        Log.e(TAG,"requestImg ${mImageList.size} ${mSuccessImageList.size}")
        mDialog?.show()
        if(mImageList.isEmpty()){
            request()
            return
        }
        mVm=getViewModel()
        //如果是add说明没有修改图片存在的
        if(mImageList[0]=="add_xx_xx" || mImageList[0]=="add_xx1_xx1" || mImageList[0]=="add_x_x"){
            mSuccessImageList.add(mImageList[0])
            mImageList.removeAt(0)
            requestImg()
        }else{
            mVm?.uploadImg(mImageList[0])
        }
        return
        if(!mRequestLocation){
            requestLocation()
        }else{
            mDialog?.show()
            if(mImageList.isEmpty()){
                request()
                return
            }
            mVm=getViewModel()
            //如果是add说明没有修改图片存在的
            if(mImageList[0]=="add_xx_xx" || mImageList[0]=="add_xx1_xx1" || mImageList[0]=="add_x_x"){
                mSuccessImageList.add(mImageList[0])
                mImageList.removeAt(0)
                requestImg()
            }else{
                mVm?.uploadImg(mImageList[0])
            }
        }
    }

    private fun request(){
        if (mRequest.applicantType == "0") {
            mRequest.pocImg1=mSuccessImageList[0]
            mRequest.pocImg2=mSuccessImageList[1]
            mRequest.pocImg3=mSuccessImageList[2]

            mRequest.reoPassportImg1=mSuccessImageList[3]
            mRequest.reoPassportImg2=mSuccessImageList[4]
            mRequest.reoPassportImg3=mSuccessImageList[5]

        }else{
            mRequest.mandataryCopiesImg1=mSuccessImageList[0]
            mRequest.mandataryCopiesImg2=mSuccessImageList[1]
            mRequest.mandataryCopiesImg3=mSuccessImageList[2]
            mRequest.mandataryCopiesImg4=mSuccessImageList[3]
            mRequest.mandataryCopiesImg5=mSuccessImageList[4]
            mRequest.mandataryCopiesImg6=mSuccessImageList[5]
            mRequest.mandataryCopiesImg7=mSuccessImageList[6]
            mRequest.mandataryCopiesImg8=mSuccessImageList[7]
            mRequest.mandataryCopiesImg9=mSuccessImageList[8]
            mRequest.mandataryCopiesImg10=mSuccessImageList[9]

            mRequest.mandataryPassportImg1=mSuccessImageList[10]
            mRequest.mandataryPassportImg1=mSuccessImageList[11]
            mRequest.mandataryPassportImg1=mSuccessImageList[12]

            mRequest.mandataryVisaImg1=mSuccessImageList[13]
            mRequest.mandataryVisaImg2=mSuccessImageList[14]
            mRequest.mandataryVisaImg3=mSuccessImageList[15]

            mRequest.mandataryIdcardImg1=mSuccessImageList[16]
            mRequest.mandataryIdcardImg2=mSuccessImageList[17]
            mRequest.mandataryIdcardImg3=mSuccessImageList[18]
            mRequest.mandataryIdcardImg4=mSuccessImageList[19]

            mRequest.pocImg1=mSuccessImageList[20]
            mRequest.pocImg2=mSuccessImageList[21]
            mRequest.pocImg3=mSuccessImageList[22]

            mRequest.reoPassportImg1=mSuccessImageList[23]
            mRequest.reoPassportImg2=mSuccessImageList[24]
            mRequest.reoPassportImg3=mSuccessImageList[25]
        }
        var index=0
        when(mImgFileType){
            0->{
                mRequest.rentAuthorizationSignImg1=mSuccessImageList[6]
                mRequest.rentAuthorizationSignImg2=mSuccessImageList[7]
                mRequest.rentAuthorizationSignImg3=mSuccessImageList[8]
                index=9
            }
            1->{
                mRequest.formaConfirmImg1=mSuccessImageList[6]
                mRequest.formaConfirmImg2=mSuccessImageList[7]
                mRequest.formaConfirmImg3=mSuccessImageList[8]
                index=9
            }
            2->{
                mRequest.rentAuthorizationSignImg1=mSuccessImageList[26]
                mRequest.rentAuthorizationSignImg2=mSuccessImageList[27]
                mRequest.rentAuthorizationSignImg3=mSuccessImageList[28]
                index=29
            }
            3->{
                mRequest.formaConfirmImg1=mSuccessImageList[26]
                mRequest.formaConfirmImg2=mSuccessImageList[27]
                mRequest.formaConfirmImg3=mSuccessImageList[28]
                index=29
            }
        }
        mRequest.houseRentContractImg1=mSuccessImageList[index]
        index++
        mRequest.houseRentContractImg2=mSuccessImageList[index]
        index++
        mRequest.houseRentContractImg3=mSuccessImageList[index]
        index++
        mRequest.houseRentContractImg4=mSuccessImageList[index]
        index++
        if(mRequest.leaseType=="1"){
            mRequest.houseHoldImg1=mSuccessImageList[index]
            index++
            mRequest.houseHoldImg2=mSuccessImageList[index]
            index++
            mRequest.houseHoldImg3=mSuccessImageList[index]
            index++
            mRequest.houseHoldImg4=mSuccessImageList[index]
            index++
            mRequest.houseHoldImg5=mSuccessImageList[index]
            index++
            mRequest.houseHoldImg6=mSuccessImageList[index]
            index++
            mRequest.houseHoldImg7=mSuccessImageList[index]
            index++
            mRequest.houseHoldImg8=mSuccessImageList[index]
            index++
            mRequest.houseHoldImg9=mSuccessImageList[index]
            index++
            mRequest.houseHoldImg10=mSuccessImageList[index]
            index++
        }
        mDialog?.show()
        mVm=getViewModel()
        mVm?.uploadListing(mRequest)
    }
}
