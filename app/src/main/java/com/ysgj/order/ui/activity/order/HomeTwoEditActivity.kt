package com.ysgj.order.ui.activity.order

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent
import cn.qqtheme.framework.entity.City
import cn.qqtheme.framework.entity.County
import cn.qqtheme.framework.entity.Province
import cn.qqtheme.framework.picker.AddressPicker
import cn.qqtheme.framework.picker.DateTimePicker
import cn.qqtheme.framework.picker.SinglePicker
import cn.yc.library.bean.CameraBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.request.ListingInfoRequest
import cn.yc.library.bean.request.ListingRequest
import cn.yc.library.bean.request.OrderSetting
import cn.yc.library.bean.response.*
import cn.yc.library.data.LocationData
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.ui.vm.AppViewModel
import cn.yc.library.utils.*
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.ui.activity.CasaActivity
import com.ysgj.order.ui.adapter.ZdydAdapter
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_twoinfo_edit.*
import kotlinx.android.synthetic.main.item_zdyd1.view.*
import kotlinx.android.synthetic.main.item_zdyd2.view.*
import java.net.InetAddress
import java.util.*

class HomeTwoEditActivity : BaseActivity(), OnMapReadyCallback {

    private var mList = arrayListOf<UploadResponse.DataSetBean.AutoReplyListBean>()
    private var mCheckNetwork=false

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

    private val mCityList= arrayListOf<Province>()
    private var mPropertyTypeList= arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()
    private val mHousesStatus= arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()
    private val mHousesType=arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()

    private var mAppViewModel: AppViewModel?=null

    private val mDefaultHandler = DefaultHandler()
    private var mAdapter: ZdydAdapter? = null

    private var mRequest = ListingInfoRequest()
    private var mInitRequest = ListingInfoRequest()
    private var mVm: OrderViewModel? = null

    private var mCameraBean = CameraBean()
    private var mAppliId = ""

    private var mImgIndex = 0
    private var mColumnsIndex = 0

    private var mScroll = false

    private var mSelectorIndex = -1

    private var mImgFileType = 0
    private var mSettingList = arrayListOf<OrderSetting>()
    private val mSetting = OrderSetting()

    private val mImageList = arrayListOf<String>()
    private val mSuccessImageList = arrayListOf<String>()

    private var mHahsDataMap = hashMapOf<Int, Boolean>().apply {
        put(0, false)
        put(1, false)
        put(2, false)
        put(3, false)
        put(4,false)
    }

    private val mHttpRequest = ListingRequest()
    private val mImgData = hashMapOf<Int, ListingInfoRequest>().apply {
        put(0, ListingInfoRequest())
        put(1, ListingInfoRequest())
        put(2, ListingInfoRequest())
        put(3, ListingInfoRequest())
    }

    private var mPlaceDetectionClient: PlaceDetectionClient?=null
    private var mGeoDataClient: GeoDataClient?=null
    private var mFusedLocationProviderClient: FusedLocationProviderClient?=null

    private var mLocationData:LocationData?=null

    private lateinit var mMap: GoogleMap

    override val layoutId: Int
        get() = R.layout.activity_twoinfo_edit

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e(TAG, "onMapReady $googleMap")
        mMap = googleMap
    }

    private fun setLeaseType(index: Int) {
        Log.e(TAG, "setLease Type $index ${mRequest.type}")
        if (index == 0) {
            mHttpRequest.leaseType = "0"
            if (mRequest.type == 1 || mRequest.type == -1) {
                mRequest.type = 0
                mResponseData.dataSet?.houses?.leaseType = 0
                check_img.setBackgroundResource(R.mipmap.check_selected)
                check_img1.setBackgroundResource(R.drawable.check_bg)
                refreshUI()
            }
        } else {
            mHttpRequest.leaseType = "1"
            if (mRequest.type == 0 || mRequest.type == -1) {
                mRequest.type = 1
                mResponseData.dataSet?.houses?.leaseType = 1
                check_img1.setBackgroundResource(R.mipmap.check_selected)
                check_img.setBackgroundResource(R.drawable.check_bg)
                refreshUI()
            }
        }
    }

    private fun setListingStatus(index: Int) {
        mHttpRequest.isPromissoryBuild = index
        fyzt.text = getString(if (index == 0) {
            R.string.fyzt_type1
        } else {
            R.string.fyzt_type2
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLocationData=LocationData().apply {
            initData(this@HomeTwoEditActivity)
        }

        mAppViewModel=getApp()?.getAppViewModel()
        val title = intent.getStringExtra("title")
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getBaseViewModel<OrderViewModel>()
        mGeoDataClient= Places.getGeoDataClient(this,null)
        mPlaceDetectionClient= Places.getPlaceDetectionClient(this,null)
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        location_test.setOnClickListener {
            mHttpRequest.address=if(checkZh()){
                "房源测试地址"
            }else{
                "property test address"
            }
            mHttpRequest.latitude="25.1885043 "
            mHttpRequest.longitude="55.2981322"
            address_tv.setText(mHttpRequest.address)
        }
        location_bt.setOnClickListener {
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
        mHttpRequest.applyId = intent.getStringExtra("applyId")
        qylx.setOnClickListener {
            showPicker(arrayListOf<String>().apply {
                add("Free Hold")
                add("Lease Hold")
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                qylx.text = item
                mHttpRequest.typeOfArea = index.toString()
            })
        }
        dw.setOnClickListener {
            if (mLocation) {
                locationTask()
            }
        }
        mAppliId = intent.getStringExtra("applyId")
        toolbarInit {
            mToolbar = toolbar
            data = ToolbarBean(title, "", false)
            onBack {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(mHahsDataMap[4]==false){
            mHahsDataMap[4]=true
            selector?.setOnClickListener {
                showPicker(arrayListOf<String>().apply {
                    add("POA")
                    add(getString(R.string.houses_tv7))
                }, SinglePicker.OnItemPickListener<String> { index, item ->
                    listing_tv?.text = item
                    if (index == 1) {
                        mResponseData.dataSet?.houses?.applicantType = 0
                        mHttpRequest.applicantType = "0"
                    } else {
                        mResponseData.dataSet?.houses?.applicantType = 1
                        mHttpRequest.applicantType = "1"
                    }
                    initImg()
                    refreshUI()
                })
            }
            fwlx.setOnClickListener {
                mSelectorIndex = 0
                showPicker<String>(arrayListOf<String>().apply {
                    mHousesType.filter { it.itemValue?.isNotBlank()==true }.forEach {
                        add(it.itemValue.toString())
                    }
                }, SinglePicker.OnItemPickListener<String> { index, item ->
                    fwlx.text=item
                    mHttpRequest.housingTypeDictcode=mHousesType[index].id.toString()
                })
            }
            fwzt.setOnClickListener {
                startActivityForResult(
                        Intent(this, CasaActivity::class.java).apply {
                            putExtra("img1", mRequest.imgList_8[0])
                            putExtra("img2", mRequest.imgList_8[1])
                            putExtra("img3", mRequest.imgList_8[2])
                            putExtra("img4", mRequest.imgList_8[3])
                            putExtra("name",mHttpRequest.rentCustomerName)
                            putExtra("phone",mHttpRequest.rentCustomerPhone)
                            putExtra("isCustomerServiceRelation",mHttpRequest.isCustomerServiceRelation)
                            putExtra("appointmentLookTime", mHttpRequest.appointmentLookTime)
                            putExtra("housingStatus", mHttpRequest.housingStatus)
                            putExtra("haveKey", mHttpRequest.haveKey)

                        }, DataConfig.ACTIVITY_REQUEST_CODE_CASA)
            }
            fyzt.setOnClickListener {
                showPicker(arrayListOf<String>().apply {
                    add(getString(R.string.fyzt_type1))
                    add(getString(R.string.fyzt_type2))
                }, SinglePicker.OnItemPickListener<String> { index, item ->
                    setListingStatus(index)
                })
            }
            zxqk.setOnClickListener {
                showPicker(arrayListOf<String>().apply {
                    add(getString(R.string.zxqk_type1))
                    add(getString(R.string.zxqk_type2))
                    add(getString(R.string.zxqk_type3))
                }, SinglePicker.OnItemPickListener<String> { index, item ->
                    zxqk.text = item
                    mHttpRequest.houseDecorationDictcode= when (index) {
                        0 -> "0"
                        1 -> "1"
                        else -> "100"
                    }
                })
            }
            wssl.setOnClickListener {
                showPicker(arrayListOf<String>().apply {
                    for (i in 1..10) {
                        add("$i")
                    }
                    add(getString(R.string.houses_tv60))
                }, SinglePicker.OnItemPickListener<String> { index, item ->
                    mHttpRequest.bedroomNum=if(index<=9){
                        item
                    }else{
                        "100"
                    }
                    wssl.text=item
                })
            }
            yssl.setOnClickListener {
                showPicker(arrayListOf<String>().apply {
                    for (i in 1..10) {
                        add("$i")
                    }
                }, SinglePicker.OnItemPickListener<String> { index, item ->
                    yssl.text = "$item ${getString(R.string.ge)}"
                    mHttpRequest.bathroomNum = item
                })
            }
            fd.setOnClickListener {
                showPicker(arrayListOf<String>().apply {
                    add(getString(R.string.no))
                    add(getString(R.string.yes))
                }, SinglePicker.OnItemPickListener<String> { index, item ->
                    mHttpRequest.isHouseLoan = index.toString()
                    fd.text = item
                })
            }
            cw.setOnClickListener {
                showPicker(arrayListOf<String>().apply {
                    add(getString(R.string.no))
                    add(getString(R.string.yes))
                }, SinglePicker.OnItemPickListener<String> { index, item ->
                    mHttpRequest.parkingSpace=item
                    cw.text=item
                })
            }
            check_img?.setOnClickListener {
                mImgData[getDataIndex()] = mRequest
                mHttpRequest.leaseType = "0"
                if (mRequest.type == 1 || mRequest.type == -1) {
                    mRequest.type = 0
                    mResponseData.dataSet?.houses?.leaseType = 0
                    check_img.setBackgroundResource(R.mipmap.check_selected)
                    check_img1.setBackgroundResource(R.drawable.check_bg)
                    initImg()
                    refreshUI()
                }
            }
            check_img1?.setOnClickListener {
                mImgData[getDataIndex()] = mRequest
                mHttpRequest.leaseType = "1"
                if (mRequest.type == 0 || mRequest.type == -1) {
                    mRequest.type = 1
                    mResponseData.dataSet?.houses?.leaseType = 1
                    check_img1.setBackgroundResource(R.mipmap.check_selected)
                    check_img.setBackgroundResource(R.drawable.check_bg)
                    initImg()
                    refreshUI()
                }
            }
            val setImgClick: (Int, Int) -> Unit = { index, columnsIndex ->
                mImgIndex = index
                mColumnsIndex = columnsIndex
                setImg()
            }

            for (i in 0 until imgone_parent.childCount) {
                imgone_parent.getChildAt(i).setOnClickListener {
                    setImgClick(0, i)
                }
            }
            for (i in 0 until imgtwo_parent.childCount) {
                imgtwo_parent.getChildAt(i).setOnClickListener {
                    setImgClick(1, i)
                }
            }
            for (i in 0 until img3_parent.childCount) {
                img3_parent.getChildAt(i).setOnClickListener {
                    setImgClick(2, i)
                }
            }
            for (i in 0 until img4_parent.childCount) {
                img4_parent.getChildAt(i).setOnClickListener {
                    setImgClick(3, i)
                }
            }
            for (i in 0 until img5_parent.childCount) {
                img5_parent.getChildAt(i).setOnClickListener {
                    setImgClick(4, i)
                }
            }
            for (i in 0 until img6_parent.childCount) {
                img6_parent.getChildAt(i).setOnClickListener {
                    setImgClick(5, i)
                }
            }
            for (i in 0 until img7_parent.childCount) {
                img7_parent.getChildAt(i).setOnClickListener {
                    setImgClick(6, i)
                }
            }
            for (i in 0 until img8_parent.childCount) {
                img8_parent.getChildAt(i).setOnClickListener {
                    setImgClick(7, i)
                }
            }

            bt.setOnClickListener {
                if(!mRequestSuccess){
                    showMessage(R.string.heihei_tv66)
                    return@setOnClickListener
                }
                upload()
            }
            requestData()
            refresh_layout.setOnRefreshListener {
                if (mRequestSuccess) {
                    return@setOnRefreshListener
                }
                requestData()
            }
            address.setOnClickListener {
                showCityPicker(mCityList, AddressPicker.OnAddressPickListener { province, city, county ->
                    mHttpRequest.city=city.areaName
                    mHttpRequest.community=county.areaName
                    address.text="${province.areaName} ${city.areaName} ${county.areaName}"
                })
            }
        }
    }

    private fun requestData() {
        refresh_layout.isRefreshing=true
        mVm = getViewModel()
        mVm?.getTwoDetails(mAppliId)
    }

    private var mRequestInit = false

    private fun initImg(){
        val setPath:(String?)->String={
            //如果图片是空的就给他设置成add_x_x
            if(it?.isNotBlank()==true){
                it
            }else{
                "add_x_x"
            }
        }
        mResponseData.dataSet?.credentials?.apply {
            if(!mRequestInit){
                listener.setListingData(mResponseData.dataSet?.houses?.houseConfigDictcode?:"")
                listener.setHousesData(mResponseData.dataSet?.houses?.houseSelfContainedDictcode?:"")
                mHttpRequest.payNode=mResponseData.dataSet?.houses?.payNode.toString()
                mHttpRequest.isHouseLoan=mResponseData.dataSet?.houses?.isHouseLoan.toString()
                fd.text = if(mHttpRequest.isHouseLoan=="1"){
                    getString(R.string.yes)
                }else{
                    getString(R.string.no)
                }
                mHttpRequest.payNode="${mResponseData.dataSet?.houses?.payNode}"

                mHttpRequest.houseRentContractImg1=setPath(houseRentContractImg1)
                mHttpRequest.houseRentContractImg2=setPath(houseRentContractImg2)
                mHttpRequest.houseRentContractImg3=setPath(houseRentContractImg3)
                mHttpRequest.houseRentContractImg4=setPath(houseRentContractImg4)
                mRequest.imgList_8[0]=mHttpRequest.houseRentContractImg1
                mRequest.imgList_8[1]=mHttpRequest.houseRentContractImg2
                mRequest.imgList_8[2]=mHttpRequest.houseRentContractImg3
                mRequest.imgList_8[3]=mHttpRequest.houseRentContractImg4
            }
            if(mResponseData.dataSet?.houses?.leaseType==1){
                //出售情况下户型图
                mHttpRequest.houseHoldImg1=setPath(houseHoldImg1)
                mHttpRequest.houseHoldImg2=setPath(houseHoldImg2)
                mHttpRequest.houseHoldImg3=setPath(houseHoldImg3)
                mHttpRequest.houseHoldImg4=setPath(houseHoldImg4)
                mHttpRequest.houseHoldImg5=setPath(houseHoldImg5)
                mHttpRequest.houseHoldImg6=setPath(houseHoldImg6)
                mHttpRequest.houseHoldImg7=setPath(houseHoldImg7)
                mHttpRequest.houseHoldImg8=setPath(houseHoldImg8)
                mHttpRequest.houseHoldImg9=setPath(houseHoldImg9)
                mHttpRequest.houseHoldImg10=setPath(houseHoldImg10)
                mRequest.imgList_10[0]=mHttpRequest.houseHoldImg1
                mRequest.imgList_10[1]=mHttpRequest.houseHoldImg2
                mRequest.imgList_10[2]=mHttpRequest.houseHoldImg3
                mRequest.imgList_10[3]=mHttpRequest.houseHoldImg4
                mRequest.imgList_10[4]=mHttpRequest.houseHoldImg5
                mRequest.imgList_10[5]=mHttpRequest.houseHoldImg6
                mRequest.imgList_10[6]=mHttpRequest.houseHoldImg7
                mRequest.imgList_10[7]=mHttpRequest.houseHoldImg8
                mRequest.imgList_10[8]=mHttpRequest.houseHoldImg9
                mRequest.imgList_10[9]=mHttpRequest.houseHoldImg10
            }
            if (applicantType == 1) {//poa
                mHttpRequest.mandataryCopiesImg1=setPath(mandataryCopiesImg1)
                mHttpRequest.mandataryCopiesImg2=setPath(mandataryCopiesImg2)
                mHttpRequest.mandataryCopiesImg3=setPath(mandataryCopiesImg3)
                mHttpRequest.mandataryCopiesImg4=setPath(mandataryCopiesImg4)
                mHttpRequest.mandataryCopiesImg5=setPath(mandataryCopiesImg5)
                mHttpRequest.mandataryCopiesImg6=setPath(mandataryCopiesImg6)
                mHttpRequest.mandataryCopiesImg7=setPath(mandataryCopiesImg7)
                mHttpRequest.mandataryCopiesImg8=setPath(mandataryCopiesImg8)
                mHttpRequest.mandataryCopiesImg9=setPath(mandataryCopiesImg9)
                mHttpRequest.mandataryCopiesImg10=setPath(mandataryCopiesImg10)
                mRequest.imgList_1[0] = mHttpRequest.mandataryCopiesImg1
                mRequest.imgList_1[1] = mHttpRequest.mandataryCopiesImg2
                mRequest.imgList_1[2] = mHttpRequest.mandataryCopiesImg3
                mRequest.imgList_1[3] = mHttpRequest.mandataryCopiesImg4
                mRequest.imgList_1[4] = mHttpRequest.mandataryCopiesImg5
                mRequest.imgList_1[5] = mHttpRequest.mandataryCopiesImg6
                mRequest.imgList_1[6] = mHttpRequest.mandataryCopiesImg7
                mRequest.imgList_1[7] = mHttpRequest.mandataryCopiesImg8
                mRequest.imgList_1[8] = mHttpRequest.mandataryCopiesImg9
                mRequest.imgList_1[9] = mHttpRequest.mandataryCopiesImg10

                //被委托人的护照
                mHttpRequest.mandataryPassportImg1=setPath(mandataryPassportImg1)
                mHttpRequest.mandataryPassportImg2=setPath(mandataryPassportImg2)
                mHttpRequest.mandataryPassportImg3=setPath(mandataryPassportImg3)
                mRequest.imgList_2[0]=mHttpRequest.mandataryPassportImg1
                mRequest.imgList_2[1]=mHttpRequest.mandataryPassportImg2
                mRequest.imgList_2[2]=mHttpRequest.mandataryPassportImg3

                //被委托人的签证
                mHttpRequest.mandataryVisaImg1=setPath(mandataryVisaImg1)
                mHttpRequest.mandataryVisaImg2=setPath(mandataryVisaImg2)
                mHttpRequest.mandataryVisaImg3=setPath(mandataryVisaImg3)
                mRequest.imgList_3[0]=mHttpRequest.mandataryVisaImg1
                mRequest.imgList_3[1]=mHttpRequest.mandataryVisaImg2
                mRequest.imgList_3[2]=mHttpRequest.mandataryVisaImg3

                //被委托人的id卡
                mHttpRequest.mandataryIdcardImg1=setPath(mandataryIdcardImg1)
                mHttpRequest.mandataryIdcardImg2=setPath(mandataryIdcardImg2)
                mHttpRequest.mandataryIdcardImg3=setPath(mandataryIdcardImg3)
                mHttpRequest.mandataryIdcardImg4=setPath(mandataryIdcardImg4)
                mRequest.imgList_4[0]=mHttpRequest.mandataryIdcardImg1
                mRequest.imgList_4[1]=mHttpRequest.mandataryIdcardImg2
                mRequest.imgList_4[2]=mHttpRequest.mandataryIdcardImg3
                mRequest.imgList_4[3]=mHttpRequest.mandataryIdcardImg4

                //房屋产权证明
                mHttpRequest.pocImg1=setPath(pocImg1)
                mHttpRequest.pocImg2=setPath(pocImg2)
                mHttpRequest.pocImg3=setPath(pocImg3)
                mRequest.imgList_5[0]=mHttpRequest.pocImg1
                mRequest.imgList_5[1]=mHttpRequest.pocImg2
                mRequest.imgList_5[2]=mHttpRequest.pocImg3

                //房屋产权人护照照片
                mHttpRequest.reoPassportImg1=setPath(reoPassportImg1)
                mHttpRequest.reoPassportImg2=setPath(reoPassportImg2)
                mHttpRequest.reoPassportImg3=setPath(reoPassportImg3)
                mRequest.imgList_6[0]=mHttpRequest.reoPassportImg1
                mRequest.imgList_6[1]=mHttpRequest.reoPassportImg2
                mRequest.imgList_6[2]=mHttpRequest.reoPassportImg3

                //出租
                if(mResponseData.dataSet?.houses?.leaseType==0){
                    mHttpRequest.rentAuthorizationSignImg1=setPath(rentAuthorizationSignImg1)
                    mHttpRequest.rentAuthorizationSignImg2=setPath(rentAuthorizationSignImg2)
                    mHttpRequest.rentAuthorizationSignImg3=setPath(rentAuthorizationSignImg3)
                    mRequest.imgList_7[0]=mHttpRequest.rentAuthorizationSignImg1
                    mRequest.imgList_7[1]=mHttpRequest.rentAuthorizationSignImg2
                    mRequest.imgList_7[2]=mHttpRequest.rentAuthorizationSignImg3

                    mImgData[0]=mRequest
                    mHahsDataMap[0]=true
                }else{
                    mHttpRequest.formaConfirmImg3=setPath(formaConfirmImg3)
                    mHttpRequest.formaConfirmImg2=setPath(formaConfirmImg2)
                    mHttpRequest.formaConfirmImg1=setPath(formaConfirmImg1)
                    mRequest.imgList_7[0]=mHttpRequest.formaConfirmImg1
                    mRequest.imgList_7[1]=mHttpRequest.formaConfirmImg2
                    mRequest.imgList_7[2]=mHttpRequest.formaConfirmImg3
                    mImgData[1]=mRequest
                    mHahsDataMap[1]=true
                }
            }else{
                //房屋产权证明
                mHttpRequest.pocImg1=setPath(pocImg1)
                mHttpRequest.pocImg2=setPath(pocImg2)
                mHttpRequest.pocImg3=setPath(pocImg3)
                mRequest.imgList_1[0]=mHttpRequest.pocImg1
                mRequest.imgList_1[1]=mHttpRequest.pocImg2
                mRequest.imgList_1[2]=mHttpRequest.pocImg3

                //房屋产权人护照照片
                mHttpRequest.reoPassportImg1=setPath(reoPassportImg1)
                mHttpRequest.reoPassportImg2=setPath(reoPassportImg2)
                mHttpRequest.reoPassportImg3=setPath(reoPassportImg3)
                mRequest.imgList_2[0]=mHttpRequest.reoPassportImg1
                mRequest.imgList_2[1]=mHttpRequest.reoPassportImg2
                mRequest.imgList_2[2]=mHttpRequest.reoPassportImg3

                //出租
                if(mResponseData.dataSet?.houses?.leaseType==0){
                    mHttpRequest.rentAuthorizationSignImg1=setPath(rentAuthorizationSignImg1)
                    mHttpRequest.rentAuthorizationSignImg2=setPath(rentAuthorizationSignImg2)
                    mHttpRequest.rentAuthorizationSignImg3=setPath(rentAuthorizationSignImg3)
                    mRequest.imgList_3[0]=mHttpRequest.rentAuthorizationSignImg1
                    mRequest.imgList_3[1]=mHttpRequest.rentAuthorizationSignImg2
                    mRequest.imgList_3[2]=mHttpRequest.rentAuthorizationSignImg3

                    mImgData[2]=mRequest
                    mHahsDataMap[2]=true
                }else{
                    mHttpRequest.formaConfirmImg3=setPath(formaConfirmImg3)
                    mHttpRequest.formaConfirmImg2=setPath(formaConfirmImg2)
                    mHttpRequest.formaConfirmImg1=setPath(formaConfirmImg1)
                    mRequest.imgList_3[0]=mHttpRequest.formaConfirmImg1
                    mRequest.imgList_3[1]=mHttpRequest.formaConfirmImg2
                    mRequest.imgList_3[2]=mHttpRequest.formaConfirmImg3

                    mImgData[3]=mRequest
                    mHahsDataMap[3]=true
                }
            }
        }
        mRequestInit=true
        mInitRequest=mRequest
    }

    private fun getDataIndex(): Int {
        return if (mHttpRequest.applicantType == "1") {
            //如果是出租
            if (mRequest.type == 0) 0 else {
                1
            }
        } else {
            //如果是出租
            if (mRequest.type == 0) {
                2
            } else {
                3
            }
        }
    }

    private fun refreshUI() {
        Log.e(TAG, "refreshUI ${mRequest.toString()}")
        Log.e(TAG, "refreshUI ${getDataIndex()}")
        if(mResponseData.dataSet?.houses?.leaseType==0){
            fyzt_parent.visibility=View.GONE
        }else{
            fyzt_parent.visibility=View.VISIBLE
        }
        //如果是POA且出租
        if (mHttpRequest.applicantType == "1") {
            for (i in 0 until imgone_parent.childCount) {
                imgone_parent.getChildAt(i).visibility = View.VISIBLE
            }
            img_parent1_tv.text = getString(R.string.houses_tv8)
            img_parent2_tv.text = getString(R.string.houses_tv9)
            img_parent3_tv.text = getString(R.string.houses_tv10)
            img_parent4_tv.text = getString(R.string.houses_tv11)
            img_parent5_tv.text = getString(R.string.houses_tv12)
            img_parent6_tv.text = getString(R.string.houses_tv13)
            img_parent1.visibility = View.VISIBLE
            img_parent2.visibility = View.VISIBLE
            img_parent3.visibility = View.VISIBLE
            img_parent4.visibility = View.VISIBLE
            img_parent5.visibility = View.VISIBLE
            img_parent6.visibility = View.VISIBLE
            img_parent7.visibility = View.VISIBLE
            if (mRequest.type == 0) {
                zddy_child1.visibility = View.VISIBLE
                zddy_child2.visibility = View.VISIBLE
                zddy_child3.visibility = View.GONE
                zddy_child3_line.visibility = View.GONE
                zddy_child4.visibility = View.VISIBLE
                zddy_child5.visibility = View.VISIBLE

                img_parent7_tv.text = getString(R.string.houses_tv2_4)

                sjxq.visibility = View.GONE
                zddy.visibility = View.VISIBLE
                img_parent7_tv.text = getString(R.string.houses_tv2_4)
                img_parent4_tv2.visibility = View.VISIBLE

                zddy_title.text = getString(R.string.hehe1)
                zddy_child1_tv.text = getString(R.string.hehe2)
                zddy_child2_tv.text = getString(R.string.hehe3)
                zddy_child4_tv.text = getString(R.string.hehe4)
                zddy_child4_tv1.visibility = View.VISIBLE
                zddy_child5_tv1.visibility = View.VISIBLE
                zddy_child5_tv.text = getString(R.string.hehe5)
                fyxx_parent.visibility = View.GONE
            } else {
                sjxq.visibility = View.VISIBLE
                zddy.visibility = View.GONE
                img_parent7_tv2.visibility = View.GONE
                img_parent4_tv2.visibility = View.VISIBLE

                img_parent7_tv.text = getString(R.string.houses_tv14)
                img_parent7_tv2.visibility = View.VISIBLE
                fyxx_parent.visibility = View.VISIBLE

            }
        } else {
            img_parent1.visibility = View.VISIBLE
            img_parent2.visibility = View.VISIBLE
            img_parent3.visibility = View.VISIBLE
            img_parent4.visibility = View.GONE
            img_parent5.visibility = View.GONE
            img_parent6.visibility = View.GONE
            img_parent7.visibility = View.GONE
            for (i in 0 until imgone_parent.childCount) {
                imgone_parent.getChildAt(i).visibility = if (i <= 2) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            //如果业主出售
            if (mRequest.type == 1) {
                sjxq.visibility = View.VISIBLE
                zddy.visibility = View.GONE
                img_parent1_tv.text = getString(R.string.houses_tv12)
                img_parent2_tv.text = getString(R.string.houses_tv13)
                img_parent3_tv.text = getString(R.string.houses_tv14)
                img_parent4_tv.text = getString(R.string.houses_tv15)
                img_parent4_tv2.visibility = View.GONE

                fd.text = if (mHttpRequest.isHouseLoan == "1") {
                    mHttpRequest.isHouseLoan = "1"
                    getString(R.string.yes)
                } else {
                    mHttpRequest.isHouseLoan = "0"
                    getString(R.string.no)
                }
                fyxx_parent.visibility = View.VISIBLE
            } else {
                sjxq.visibility = View.GONE
                zddy.visibility = View.VISIBLE
                zddy_child3.visibility = View.GONE
                zddy_child3_line.visibility = View.GONE
                img_parent1_tv.text = getString(R.string.houses_tv12)
                img_parent2_tv.text = getString(R.string.houses_tv13)
                img_parent3_tv.text = getString(R.string.houses_tv2_4)

                zddy_title.text = getString(R.string.hehe1)
                zddy_child1_tv.text = getString(R.string.hehe2)
                zddy_child2_tv.text = getString(R.string.hehe3)
                zddy_child4_tv.text = getString(R.string.hehe4)
                zddy_child4_tv1.visibility = View.VISIBLE
                zddy_child5_tv1.visibility = View.VISIBLE
                zddy_child4.visibility = View.VISIBLE
                zddy_child5.visibility = View.VISIBLE
                zddy_child5_tv.text = getString(R.string.hehe5)

                fyxx_parent.visibility = View.GONE
            }

        }
        var index = 0
        val size = mList.size
        if(mRequest.lastType!=mRequest.type){
            zdyd_root.removeAllViews()
        }
        mRequest.lastType=mRequest.type
        mList.forEach {
            if (zdyd_root.childCount < size) {
                zdyd_root.addView(LayoutInflater.from(this).inflate(
                        if (mRequest.type == 0) {
                            R.layout.item_zdyd1
                        } else {
                            R.layout.item_zdyd2
                        }, null, false))
            }
            zdyd_root.getChildAt(index).apply {
                refreshZdyd(this, index, mRequest.type)
            }
            index++
        }
        if (mRequest.type == 1) {
            img_parent8.visibility = View.VISIBLE
        } else {
            img_parent8.visibility = View.GONE
        }

        qzrq.setOnClickListener {
            showPicker(arrayListOf<String>().apply {
                add(getString(R.string.paynode_type1))
                add(getString(R.string.paynode_type2))
                add(getString(R.string.paynode_type3))
                add(getString(R.string.paynode_type4))
                add(getString(R.string.paynode_type5))
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                mHttpRequest.payNode=when(index){
                    1->"2"
                    2->"4"
                    3->"6"
                    4->"12"
                    else->"1"
                }
                qzrq.text = item
            })
        }
        sc.setOnClickListener {
            showYearMonthDayPicker(DateTimePicker.OnYearMonthDayTimePickListener { year, month, day, hour, minute ->
                val value = "$year-$month-$day 00:00:00"
                mHttpRequest.startRentDate = value
                sc.text = value
            })
        }

        ////出售情况下户型图
        if (mRequest.type == 1) {
            for (i in 0 until mRequest.imgList_10.size) {
                setFresco(mRequest.imgList_10[i], img8_parent.getChildAt(i) as SimpleDraweeView, false)
            }
        }
        if (mHttpRequest.applicantType == "1") {//poa
            for (i in 0 until mRequest.imgList_1.size) {
                setFresco(mRequest.imgList_1[i], imgone_parent.getChildAt(i) as SimpleDraweeView, false)
            }
            //被委托人的护照
            setFresco(mRequest.imgList_2[0], imgtwo_parent.getChildAt(0), false)
            setFresco(mRequest.imgList_2[1], imgtwo_parent.getChildAt(1), false)
            setFresco(mRequest.imgList_2[2], imgtwo_parent.getChildAt(2) as SimpleDraweeView, false)

            //被委托人的签证
            setFresco(mRequest.imgList_3[0], img3_parent.getChildAt(0), false)
            setFresco(mRequest.imgList_3[1], img3_parent.getChildAt(1), false)
            setFresco(mRequest.imgList_3[2], img3_parent.getChildAt(2), false)


            //被委托人的id卡
            setFresco(mRequest.imgList_4[0], img4_parent.getChildAt(0), false)
            setFresco(mRequest.imgList_4[1], img4_parent.getChildAt(1), false)
            setFresco(mRequest.imgList_4[2], img4_parent.getChildAt(2) as SimpleDraweeView, false)
            setFresco(mRequest.imgList_4[3], img4_parent.getChildAt(3), false)

            //房屋产权证明
            setFresco(mRequest.imgList_5[0], img5_parent.getChildAt(0), false)
            setFresco(mRequest.imgList_5[1], img5_parent.getChildAt(1), false)
            setFresco(mRequest.imgList_5[2], img5_parent.getChildAt(2), false)

            //房屋产权人护照照片
            setFresco(mRequest.imgList_6[0], img6_parent.getChildAt(0), false)
            setFresco(mRequest.imgList_6[1], img6_parent.getChildAt(1), false)
            setFresco(mRequest.imgList_6[2], img6_parent.getChildAt(2), false)

            setFresco(mRequest.imgList_7[0], img7_parent.getChildAt(0), false)
            setFresco(mRequest.imgList_7[1], img7_parent.getChildAt(1), false)
            setFresco(mRequest.imgList_7[2], img7_parent.getChildAt(2), false)
        } else {
            //房屋产权证明
            setFresco(mRequest.imgList_1[0], imgone_parent.getChildAt(0), false)
            setFresco(mRequest.imgList_1[1], imgone_parent.getChildAt(1), false)
            setFresco(mRequest.imgList_1[2], imgone_parent.getChildAt(2) as SimpleDraweeView, false)

            //房屋产权人护照照片
            setFresco(mRequest.imgList_2[0], imgtwo_parent.getChildAt(0), false)
            setFresco(mRequest.imgList_2[1], imgtwo_parent.getChildAt(1), false)
            setFresco(mRequest.imgList_2[2], imgtwo_parent.getChildAt(2) as SimpleDraweeView, false)

            setFresco(mRequest.imgList_3[0], img3_parent.getChildAt(0), false)
            setFresco(mRequest.imgList_3[1], img3_parent.getChildAt(1), false)
            setFresco(mRequest.imgList_3[2], img3_parent.getChildAt(2) as SimpleDraweeView, false)

        }
    }

    private fun setImg() {
        showPicker(arrayListOf<String>().apply {
            add(getString(R.string.pz))
            add(getString(R.string.xc))
            add(getString(R.string.heihei_tv19))
        }, SinglePicker.OnItemPickListener<String> { index, item ->
            if (index == 1) {
                RxGalleryFinalApi
                        .openRadioSelectImage(this, object : RxBusResultDisposable<ImageRadioResultEvent>() {
                            override fun onEvent(it: ImageRadioResultEvent?) {
                                Log.e(TAG, "选中了图片路径：${it?.result?.cropPath}; index=$mImgIndex;" + it?.result?.originalPath)
                                it?.also {
                                    mImageList.clear()
                                    mSuccessImageList.clear()
                                    when (mImgIndex) {
                                        0 -> {
                                            mRequest.imgList_1[mColumnsIndex] = it.result.originalPath
                                            Log.e(TAG, "图片 ${mRequest.imgList_1[mColumnsIndex]} ${imgone_parent.getChildAt(mColumnsIndex)}")
                                            val view = imgone_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            setFresco(it.result.originalPath, view)
                                        }
                                        1 -> {
                                            val view = imgtwo_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_2[mColumnsIndex] = it.result.originalPath
                                            setFresco(it.result.originalPath, view)
                                        }
                                        2 -> {
                                            val view = img3_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_3[mColumnsIndex] = it.result.originalPath
                                            setFresco(it.result.originalPath, view)
                                        }
                                        3 -> {
                                            val view = img4_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_4[mColumnsIndex] = it.result.originalPath
                                            setFresco(it.result.originalPath, view)
                                        }
                                        4 -> {
                                            val view = img5_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_5[mColumnsIndex] = it.result.originalPath
                                            setFresco(it.result.originalPath, view)
                                        }
                                        5 -> {
                                            val view = img6_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_6[mColumnsIndex] = it.result.originalPath
                                            setFresco(it.result.originalPath, view)
                                        }
                                        6 -> {
                                            val view = img7_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_7[mColumnsIndex] = it.result.originalPath
                                            setFresco(it.result.originalPath, view)
                                        }
                                        7 -> {
                                            val view = img8_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_10[mColumnsIndex] = it.result.originalPath
                                            setFresco(it.result.originalPath, view)
                                        }

                                    }
                                }
                            }
                        }, true)
            } else if(index==0){
                camearPermissions()
            }else{
                val setBack:(View)->Unit={
                    (it as? SimpleDraweeView)?.setActualImageResource(R.mipmap.upload_default)
                }
                when (mImgIndex) {
                    0->{
                        mRequest.imgList_1[mColumnsIndex]="add_x_x"
                        setBack(imgone_parent.getChildAt(mColumnsIndex))
                    }
                    1 -> {
                        setBack(imgtwo_parent.getChildAt(mColumnsIndex))
                        mRequest.imgList_2[mColumnsIndex]="add_x_x"
                    }
                    2 -> {
                        setBack(img3_parent.getChildAt(mColumnsIndex))
                        mRequest.imgList_3[mColumnsIndex]="add_x_x"
                    }
                    3 -> {
                        setBack(img4_parent.getChildAt(mColumnsIndex))
                        mRequest.imgList_4[mColumnsIndex]="add_x_x"
                    }
                    4 -> {
                        setBack(img5_parent.getChildAt(mColumnsIndex))
                        mRequest.imgList_5[mColumnsIndex]="add_x_x"
                    }
                    5 -> {
                        setBack(img6_parent.getChildAt(mColumnsIndex))
                        mRequest.imgList_6[mColumnsIndex]="add_x_x"
                    }
                    6 -> {
                        setBack(img7_parent.getChildAt(mColumnsIndex))
                        mRequest.imgList_7[mColumnsIndex]="add_x_x"
                    }
                    7->{
                        setBack(img8_parent.getChildAt(mColumnsIndex))
                        mRequest.imgList_10[mColumnsIndex]="add_x_x"
                    }
                }
            }
        })
    }

    private fun setKey(key:Int){
        mHttpRequest.haveKey=key.toString()
        ysgs.text=getString(if(key==0){
            R.string.heihei_tv73
        }else{
            R.string.heihei_tv74
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "onActivity $requestCode $resultCode $mCameraBean")
        if(requestCode==10 && resultCode== Activity.RESULT_OK){
            PlacePicker.getPlace(data, this)?.apply {
                mHttpRequest.address="$name"
                mHttpRequest.latitude="${latLng.latitude}"
                mHttpRequest.longitude="${latLng.longitude}"
                address_tv.setText(mHttpRequest.address)
            }
            return
        }
        if (resultCode == 1033) {
            setResult(1033)
            finish()
            return
        }
        if(resultCode==1012 && requestCode==DataConfig.ACTIVITY_REQUEST_CODE_CASA){
            data?.apply {
                mHttpRequest.housingStatus=getStringExtra("housingStatus")
                mHttpRequest.isCustomerServiceRelation=getStringExtra("isCustomerServiceRelation")
                //如果选择的状态是出租，那么拿取合同图片
                if(mHttpRequest.housingStatus=="20075"){
                    mRequest.imgList_8[0] = getStringExtra("img1")
                    mRequest.imgList_8[1] = getStringExtra("img2")
                    mRequest.imgList_8[2] = getStringExtra("img3")
                    mRequest.imgList_8[3] = getStringExtra("img4")
                }else{
                    mRequest.fwzpImg1=""
                    mRequest.fwzpImg2=""
                }
                fwzt.text=getStringExtra("value")
                val ys=getStringExtra("ys")
                val advanceReservationDay=getStringExtra("advanceReservationDay")
                if(advanceReservationDay.isNotBlank()){
                    mHttpRequest.advanceReservationDay=advanceReservationDay
                }
                mHttpRequest.appointmentLookTime=getStringExtra("appointmentLookTime")
                when(mHttpRequest.housingStatus){
                    "20075"->{
                        val name=getStringExtra("name")
                        val phone=getStringExtra("phone")
                        if(name.isNotBlank()){
                            mHttpRequest.rentCustomerName=name
                        }
                        if(phone.isNotBlank()){
                            mHttpRequest.rentCustomerPhone=phone
                        }
                    }
                    "20074"->{
                        if(ys!="-1"){
                            mHttpRequest.haveKey=ys
                            setKey(ys.toInt())
                        }
                    }
                }

            }
        }
        if (requestCode == DataConfig.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            mCameraBean.imgUri?.apply {
                Log.e(TAG, "裁剪 ${mCameraBean.imgUri}  ${mCameraBean.imgPath}")
                mImageList.clear()
                mSuccessImageList.clear()
                when (mImgIndex) {
                    0 -> {
                        mRequest.imgList_1[mColumnsIndex] = mCameraBean.imgPath
                        val view = imgone_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        setFresco(mCameraBean.imgPath, view)
                    }
                    1 -> {
                        val view = imgtwo_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_2[mColumnsIndex] = mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath, view)
                    }
                    2 -> {
                        val view = img3_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_3[mColumnsIndex] = mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath, view)
                    }
                    3 -> {
                        val view = img4_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_4[mColumnsIndex] = mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath, view)
                    }
                    4 -> {
                        val view = img5_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_5[mColumnsIndex] = mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath, view)
                    }
                    5 -> {
                        val view = img6_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_6[mColumnsIndex] = mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath, view)
                    }
                    6 -> {
                        val view = img7_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_7[mColumnsIndex] = mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath, view)
                    }
                    7 -> {
                        val view = img8_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_10[mColumnsIndex] = mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath, view)
                    }
                }
            }
        } else if (requestCode == 11 && resultCode == AutoLayoutActivity.RESULT_OK) {

        }
    }

    private var mLocation = false
    private var mRequestSuccess = false
    private var mResponseData = UploadResponse()

    override fun <T> onNext(dat: T) {
        Log.e(TAG, "onnext $dat")
        if(dat is String && dat=="location"){
            mMap?.isMyLocationEnabled = true
            mMap?.uiSettings?.isMyLocationButtonEnabled = true
            mDialog?.show()
            mPlaceDetectionClient?.getCurrentPlace(null)?.addOnCompleteListener {
                if(it==null || !it.isSuccessful || it.result==null || it.result?.count?:0<=0) {
                    mDialog?.dismiss()
                    showMessage(R.string.location_error)
                    return@addOnCompleteListener
                }
                (it.result as? PlaceLikelihoodBufferResponse)?.apply {
                    mDialog?.dismiss()
                    val data=get(0)
                    val builder= PlacePicker.IntentBuilder()
                    builder.setLatLngBounds(LatLngBounds(data.place.latLng,data.place.latLng))
                    startActivityForResult(builder.build(this@HomeTwoEditActivity),10)
                }
            }
            return
        }
        (dat as? LoginResponse)?.apply {
            mDialog?.dismiss()
            if (dat.result == 0) {
                showMessage(R.string.request_success2)
                //通知界面更新
                getBaseViewModel<LoginViewModel>()?.setRequestCode(1002)
                setResult(1033)
                finish()
                return
            }
            showMessage(dat.message)
            return
        }
        if (dat is FileResponse && dat.fid.isNotBlank()) {
            Log.e(TAG, "onNext ${mImageList.size}  ${mSuccessImageList.size}")
            mSuccessImageList.add(dat.fid)
            mImageList.removeAt(0)
            requestImg()
            return
        }
        ((dat as? BaseResponse<*>)?.data as? UploadResponse)?.apply {
            mResponseData = this
            mRequestSuccess = true
            refresh_layout.isEnabled=false
            mAppViewModel?.mHousesStatus?.value?.dataSet?.items?.forEach {
                mHousesStatus.add(it)
            }
            mAppViewModel?.mHousesType?.value?.dataSet?.items?.forEach {
                mHousesType.add(it)
            }
            mCityList.apply {
                mAppViewModel?.mCity?.value?.dataSet?.filter { it.cityNameCn?.isNotBlank()==true }?.forEach {
                    add(Province(it.cityNameCn).apply {
                        cities= arrayListOf<City>().apply {
                            it.sub?.filter { it.cityNameCn?.isNotBlank()==true }?.forEach {childIt->
                                add(City(childIt.cityNameCn).apply {
                                    this.counties= arrayListOf<County>().apply {
                                        childIt.sub?.filter { it.cityNameCn?.isNotBlank()==true }?.forEach {
                                            add(County(it.cityNameCn))
                                        }
                                    }
                                })
                            }
                        }
                    })
                }
            }
            val list=getApp()?.getAppViewModel()?.mHousesType?.value?.dataSet?.items?: arrayListOf()
            mPropertyTypeList=list as ArrayList<ListingConfigResponse.DataSetBean.ItemsBean>
            refresh_layout.isEnabled = false
            dataSet?.also {
                it.houses?.apply {
                    Log.e(TAG, "onnext ${applicantType}")
                    listing_tv?.text = if(applicantType==0){
                        getString(R.string.houses_tv7)
                    }else{
                        "POA"
                    }
                    mHttpRequest.rentCustomerName=rentCustomerName?:""
                    mHttpRequest.rentCustomerPhone=rentCustomerPhone?:""
                    fwgl.setBlankText(phoneNumber)
                    zdzj.setBlankText("$minHouseRent")
                    qwzj.setBlankText("$houseRent")
                    mHttpRequest.applicantType = applicantType.toString()
                    mHttpRequest.housingTypeDictcode=housingTypeDictcode?:""
                    mHousesType.filter { it.id.toString()==mHttpRequest.housingTypeDictcode}.forEach {
                        fwlx.text=it.itemValue
                    }
                    mHttpRequest.isCustomerServiceRelation="$isCustomerServiceRelation"
                    mHttpRequest.appointmentLookTime=
                    if(mHttpRequest.isCustomerServiceRelation=="1"){
                        ""
                    }else{
                        appointmentLookTime?:""
                    }
                    mHttpRequest.startRentDate=beginRentDate?:""
                    lm.setText(buildingName.toString())
                    lc.setText(houseFloor.toString())
                    mph.setText(roomName.toString())
                    mj.setText(houseAcreage.toString())
                    mHttpRequest.bedroomNum=bedroomNum.toString()
                    wssl.text=if(mHttpRequest.bedroomNum!="100"){
                        mHttpRequest.bedroomNum
                    }else{
                        getString(R.string.houses_tv60)
                    }
                    mHttpRequest.longitude=longitude?:""
                    mHttpRequest.latitude=latitude?:""
                    mHttpRequest.bathroomNum=bathroomNum.toString()
                    yssl.text=mHttpRequest.bathroomNum
                    mHttpRequest.parkingSpace=parkingSpace.toString()
                    cw.text=if(mHttpRequest.parkingSpace.toInt()==1){
                        getString(R.string.yes)
                    }else{
                        getString(R.string.no)
                    }
                    setKey(haveKey)
                    mHttpRequest.houseDecorationDictcode=houseDecorationDictcode.toString()
                    zxqk.text=when(mHttpRequest.houseDecorationDictcode){
                        "0"->getString(R.string.zxqk_type1)
                        "100"->getString(R.string.zxqk_type3)
                        else->getString(R.string.zxqk_type2)
                    }
                    mHttpRequest.housingStatus=housingStatus.toString()
                    mHousesStatus?.forEach {
                        if(it.id==housingStatus){
                            fwzt.text=it.itemValue
                        }
                    }
                    mHttpRequest.isPromissoryBuild=isPromissoryBuild
                    setListingStatus(mHttpRequest.isPromissoryBuild)
                    yx.setText(email.toString())
                    fb.setText(facebook.toString())
                    sc.text=beginRentDate
                    tw.setText(twitter.toString())
                    `is`.setText("${instagram}")
                    dkxx.setText(plotNumber)
                    mHttpRequest.typeOfArea=if(typeOfArea.isNullOrBlank()){
                        "1"
                    }else{
                        typeOfArea
                    }
                    qylx.text=if(typeOfArea=="0"){
                        "Free Hold"
                    }else{
                        "Lease Hold"
                    }
                    mHttpRequest.payNode="$payNode"
                    cqzs_ed.setText(titleDeedNumber)
                    cqbh.setText(propertyNumber)
                    dckfs.setText(masterDevelpoerName)
                    zdsj.setText("$minHouseRent")
                    qwsj.setText("$houseRent")
                    qzrq.text=when(payNode){
                        1->getString(R.string.paynode_type1)
                        2->getString(R.string.paynode_type2)
                        4->getString(R.string.paynode_type3)
                        6->getString(R.string.paynode_type4)
                        12->getString(R.string.paynode_type5)
                        else->""
                    }
                    mHttpRequest.city=city?:""
                    //mHttpRequest.houseId=mResponseData.dataSet?.houses?.id.toString()
                }
                mList.clear()
                it.autoReplyList?.forEach {
                    mList.add(it)
                }
                mHttpRequest.city=it.houses?.city?:""
                mHttpRequest.community=it.houses?.community?:""
                mHttpRequest.subCommunity=it.houses?.subCommunity?:""
                address.text ="${it.houses?.city}${it.houses?.community}${it.houses?.subCommunity}"
                address_tv.setText(it.houses?.address.toString())
                initImg()
                setLeaseType(mResponseData.dataSet?.houses?.leaseType?:-1)
                //refreshUI()
            }
            showMessage(dat.messageId)
            return
        }
        if (dat is CameraBean) {
            mCameraBean = dat
        } else if (dat is String && dat == "location") {
            mMap?.apply {
                mLocation = true
                setMyLocationEnabled(true)
                mMap.setOnMyLocationChangeListener {
                    Log.e(TAG, "onLocationChanged ${it.latitude}")
                }
            }
        }
    }

    private var mBitmap:Bitmap?=null

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        showMessage(throwable.message)
    }

    private fun upload() {
        var imgMsg=""

        //mHttpRequest.mandataryCopiesImg1=mRequest.poiCopyImg1
        //户主
        //val imageList= arrayListOf<String>()
        if (mHttpRequest.applicantType == "0") {
            imgMsg=when{
                mRequest.imgList_1.filter { it.isNotBlank()&&it!="add_x_x" }.isEmpty()->{
                    getString(R.string.edit_error1)
                }
                mRequest.imgList_2.filter { it.isNotBlank()&&it!="add_x_x" }.isEmpty()->{
                    getString(R.string.edit_error2)
                }
                mRequest.imgList_3.filter { it.isNotBlank()&&it!="add_x_x" }.isEmpty()->{
                    getString(if(mHttpRequest.leaseType=="0"){R.string.edit_error3}
                    else{
                        R.string.edit_error4
                    })
                }
                else->""
            }
        } else {//poa
            imgMsg = when {
                mRequest.imgList_1.filter { it.isNotBlank() && it != "add_x_x" }.isEmpty() -> getString(R.string.edit_error5)
                mRequest.imgList_2.filter { it.isNotBlank()&&it!="add_x_x" }.isEmpty()-> getString(R.string.edit_error6)
                mRequest.imgList_3.filter { it.isNotBlank()&&it!="add_x_x" }.isEmpty() -> getString(R.string.edit_error8)
                mRequest.imgList_4.filter { it.isNotBlank()&&it!="add_x_x" }.isEmpty() -> getString(R.string.edit_error7)
                mRequest.imgList_5.filter { it.isNotBlank()&&it!="add_x_x" }.isEmpty() -> getString(R.string.edit_error9)
                mRequest.imgList_6.filter { it.isNotBlank()&&it!="add_x_x" }.isEmpty() -> getString(R.string.edit_error2)
                mRequest.imgList_7.filter { it.isNotBlank()&&it!="add_x_x" }.isEmpty() -> getString(if(mHttpRequest.leaseType=="0"){
                    R.string.edit_error3
                }else{
                    R.string.edit_error10
                })
                else -> ""
            }
        }
        if(imgMsg.isNotBlank()){
            showMessage(imgMsg)
            return
        }
        mHttpRequest.plotNumber=dkxx.editableText.toString()
        mHttpRequest.titleDeedNumber=cqzs_ed.editableText.toString()
        mHttpRequest.propertyNumber=cqbh.editableText.toString()
        mHttpRequest.masterDevelpoerName=dckfs.editableText.toString()
        if(mHttpRequest.leaseType=="1"){
            imgMsg=when{
                dkxx.editableText.isNullOrBlank()->getString(R.string.edit_error32)
                mHttpRequest.typeOfArea.isNullOrBlank()->getString(R.string.edit_error33)
                cqzs_ed.editableText.isNullOrBlank()->getString(R.string.edit_error34)
                cqbh.editableText.isNullOrBlank()->getString(R.string.edit_error35)
                dckfs.editableText.isNullOrBlank()->getString(R.string.edit_error36)
                else->""
            }
        }
        if(imgMsg.isNotBlank()){
            showMessage(imgMsg)
            return
        }
        mHttpRequest.address=address_tv.editableText.toString()
        mHttpRequest.buildingName=lm.editableText.toString()
        mHttpRequest.houseFloor=lc.editableText.toString()
        mHttpRequest.roomName=mph.editableText.toString()
        mHttpRequest.houseAcreage=mj.editableText.toString()
        val none=mCityList.none { it.cities.map {
            if(it.areaName==mHttpRequest.city){
                it.counties.map { it.areaName==mHttpRequest.community }.isNotEmpty()
            }else{
                false
            }
        }.none()}
        val msg = when {
            mHousesType.none { it.id.toString()==mHttpRequest.housingTypeDictcode } -> getString(R.string.edit_error11)
            !none -> getString(R.string.edit_error12)
            address_tv.editableText.isNullOrEmpty() -> getString(R.string.edit_error37)
            lm.editableText.isNullOrEmpty() -> getString(R.string.edit_error14)
            lc.editableText.isNullOrEmpty() -> getString(R.string.edit_error15)
            mph.editableText.isNullOrEmpty() -> getString(R.string.edit_error16)
            mj.editableText.isNullOrEmpty() -> getString(R.string.edit_error17)
            mHttpRequest.bedroomNum.isNullOrEmpty() -> getString(R.string.edit_error18)
            mHttpRequest.bathroomNum.isNullOrEmpty() -> getString(R.string.edit_error19)
            mHttpRequest.parkingSpace.isNullOrEmpty() -> getString(R.string.edit_error20)
            mHttpRequest.houseDecorationDictcode.isNullOrEmpty() -> getString(R.string.edit_error21)
            else -> ""
        }
        if (msg.isNotBlank()) {
            showMessage(msg)
            return
        }
        imgMsg=when(mHttpRequest.isPromissoryBuild){
            0->""
            1->{
                if(mHousesStatus.none { mHttpRequest.housingStatus==it.id.toString() }){
                    getString(R.string.edit_error22)
                }else{
                    ""
                }
            }
            else->getString(R.string.edit_error22)
        }
        if(imgMsg.isNotBlank()){
            showMessage(imgMsg)
            return
        }
        val payNodes= arrayListOf<String>().apply {
            add("1")
            add("2")
            add("4")
            add("6")
            add("12")
        }
        //户主
        if (mHttpRequest.applicantType == "0") {
            //出租
            if (mHttpRequest.leaseType == "0") {
                imgMsg = when {
                    payNodes.none { it==mHttpRequest.payNode } -> getString(R.string.edit_error24)
                    mHttpRequest.startRentDate.isNullOrEmpty() ->getString(R.string.edit_error25)
                    qwzj.editableText.isNullOrEmpty() -> getString(R.string.edit_error26)
                    zdzj.editableText.isNullOrEmpty() -> getString(R.string.edit_error27)
                    else -> ""
                }
                mHttpRequest.houseRent=qwzj.editableText.toString()
                mHttpRequest.minHouseRent=zdzj.editableText.toString()
            } else {//出售
                imgMsg = when {
                    mHttpRequest.isHouseLoan.isNullOrEmpty() -> getString(R.string.edit_error29)
                    qwsj.editableText.isNullOrEmpty() -> getString(R.string.edit_error30)
                    zdsj.editableText.isNullOrEmpty() -> getString(R.string.edit_error31)
                    else -> ""
                }
                mHttpRequest.houseRent=qwsj.editableText.toString()
                mHttpRequest.minHouseRent=zdsj.editableText.toString()
            }
        } else {//poa
            // 出租
            if (mHttpRequest.leaseType== "0") {
                imgMsg = when {
                    payNodes.none { it==mHttpRequest.payNode } -> getString(R.string.edit_error24)
                    mHttpRequest.startRentDate.isNullOrEmpty() ->getString(R.string.edit_error25)
                    qwzj.editableText.isNullOrEmpty() -> getString(R.string.edit_error26)
                    zdzj.editableText.isNullOrEmpty() -> getString(R.string.edit_error27)
                    else -> ""
                }
                mHttpRequest.houseRent=qwzj.editableText.toString()
                mHttpRequest.minHouseRent=zdzj.editableText.toString()
            } else {//出售
                imgMsg = when {
                    mHttpRequest.isHouseLoan.isNullOrEmpty() -> getString(R.string.edit_error29)
                    qwsj.editableText.isNullOrEmpty() -> getString(R.string.edit_error30)
                    zdsj.editableText.isNullOrEmpty() -> getString(R.string.edit_error31)
                    else -> ""
                }
                mHttpRequest.houseRent=qwsj.editableText.toString()
                mHttpRequest.minHouseRent=zdsj.editableText.toString()
            }
        }
        if (imgMsg.isNotBlank()) {
            showMessage(imgMsg)
            return
        }
        if(yx.editableText.isNotBlank()){
            val m="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$".toRegex()
            if(!yx.editableText.matches(m)){
                showMessage(R.string.email_error)
                return
            }
        }
        mAppViewModel?.mCity?.value?.dataSet?.filter { it.cityNameCn==mHttpRequest.property }?.forEach {
            it.sub?.first { it.cityNameCn==mHttpRequest.city }?.sub?.filter {
                it.cityNameCn==mHttpRequest.community }?.forEach {
                mHttpRequest.longitude=it.longitude.toString()
                mHttpRequest.latitude=it.latitude.toString()
            }
        }
        mHttpRequest.facebook=fb.editableText.toString()
        mHttpRequest.twitter=tw.editableText.toString()
        mHttpRequest.instagram=`is`.editableText.toString()
        mHttpRequest.email=yx.editableText.toString()
        mHttpRequest.languageVersion="0"
        mHttpRequest.houseConfigDictcode=listener.getListing()
        mHttpRequest.houseSelfContainedDictcode=listener.getHouses()

        mList.forEach {
            if (mRequest.type==1) {
                if(it.isOpen == 1 && it.houseRentPrice.isNullOrBlank()){
                    showMessage(getString(R.string.houses_tv44))
                    return
                }
            }else{
                if(it.isOpen==1){
                    when {
                        it.beginRentDate.isNullOrBlank() -> {
                            showMessage(getString(R.string.edit_error25))
                            return
                        }
                        it.rentTime == 0 -> {
                            showMessage(R.string.zpsc)
                            return
                        }
                        it.houseRentPrice.isNullOrBlank() -> {
                            showMessage(R.string.srzj)
                            return
                        }
                        else -> {
                        }
                    }
                }
            }
        }
        mDialog = PromptDialog(this, PromptDialog.DialogBuild().apply {
            type = 1
        })
        mDialog?.show()
        val getPath: (String, String) -> String = { updatePath, path ->
            //如果他们一样说明没有修改图片 如果传过来的值是空的说明这个图片是选填的不是必须
            when {
                updatePath == path -> "add_xx1_xx1"
                updatePath.isNullOrBlank() -> "add_xx_xx"
                else -> updatePath
            }
        }
        if (mImageList.size > 0) {
            requestImg()
            return
        }
        //业主
        if (mHttpRequest.applicantType == "0") {

            mImageList.add(getPath(mRequest.imgList_1[0], mHttpRequest.pocImg1))
            mImageList.add(getPath(mRequest.imgList_1[1], mHttpRequest.pocImg2))
            mImageList.add(getPath(mRequest.imgList_1[2], mHttpRequest.pocImg3))

            mImageList.add(getPath(mRequest.imgList_2[0], mHttpRequest.reoPassportImg1))
            mImageList.add(getPath(mRequest.imgList_2[1], mHttpRequest.reoPassportImg2))
            mImageList.add(getPath(mRequest.imgList_2[2], mHttpRequest.reoPassportImg3))

            if (mHttpRequest.leaseType == "0") {
                mImageList.add(getPath(mRequest.imgList_3[0], mHttpRequest.rentAuthorizationSignImg1))
                mImageList.add(getPath(mRequest.imgList_3[1], mHttpRequest.rentAuthorizationSignImg2))
                mImageList.add(getPath(mRequest.imgList_3[2], mHttpRequest.rentAuthorizationSignImg1))
                mImgFileType = 0
            } else {//出售
                mImgFileType = 1
                mImageList.add(getPath(mRequest.imgList_3[0], mHttpRequest.formaConfirmImg1))
                mImageList.add(getPath(mRequest.imgList_3[1], mHttpRequest.formaConfirmImg2))
                mImageList.add(getPath(mRequest.imgList_3[2], mHttpRequest.formaConfirmImg3))
            }
        } else {//poa
            mImageList.add(getPath(mRequest.imgList_1[0], mHttpRequest.mandataryCopiesImg1))
            mImageList.add(getPath(mRequest.imgList_1[1], mHttpRequest.mandataryCopiesImg2))
            mImageList.add(getPath(mRequest.imgList_1[2], mHttpRequest.mandataryCopiesImg3))
            mImageList.add(getPath(mRequest.imgList_1[3], mHttpRequest.mandataryCopiesImg4))
            mImageList.add(getPath(mRequest.imgList_1[4], mHttpRequest.mandataryCopiesImg5))
            mImageList.add(getPath(mRequest.imgList_1[5], mHttpRequest.mandataryCopiesImg6))
            mImageList.add(getPath(mRequest.imgList_1[6], mHttpRequest.mandataryCopiesImg7))
            mImageList.add(getPath(mRequest.imgList_1[7], mHttpRequest.mandataryCopiesImg8))
            mImageList.add(getPath(mRequest.imgList_1[8], mHttpRequest.mandataryCopiesImg9))
            mImageList.add(getPath(mRequest.imgList_1[9], mHttpRequest.mandataryCopiesImg10))

            mImageList.add(getPath(mRequest.imgList_2[0], mHttpRequest.mandataryPassportImg1))
            mImageList.add(getPath(mRequest.imgList_2[1], mHttpRequest.mandataryPassportImg2))
            mImageList.add(getPath(mRequest.imgList_2[2], mHttpRequest.mandataryPassportImg3))

            mImageList.add(getPath(mRequest.imgList_3[0], mHttpRequest.mandataryVisaImg1))
            mImageList.add(getPath(mRequest.imgList_3[0], mHttpRequest.mandataryVisaImg2))
            mImageList.add(getPath(mRequest.imgList_3[0], mHttpRequest.mandataryVisaImg3))

            mImageList.add(getPath(mRequest.imgList_4[0], mHttpRequest.mandataryIdcardImg1))
            mImageList.add(getPath(mRequest.imgList_4[1], mHttpRequest.mandataryIdcardImg2))
            mImageList.add(getPath(mRequest.imgList_4[2], mHttpRequest.mandataryIdcardImg3))
            mImageList.add(getPath(mRequest.imgList_4[3], mHttpRequest.mandataryIdcardImg4))

            mImageList.add(getPath(mRequest.imgList_5[0], mHttpRequest.pocImg1))
            mImageList.add(getPath(mRequest.imgList_5[1], mHttpRequest.pocImg2))
            mImageList.add(getPath(mRequest.imgList_5[2], mHttpRequest.pocImg3))

            mImageList.add(getPath(mRequest.imgList_6[0], mHttpRequest.reoPassportImg1))
            mImageList.add(getPath(mRequest.imgList_6[1], mHttpRequest.reoPassportImg2))
            mImageList.add(getPath(mRequest.imgList_6[2], mHttpRequest.reoPassportImg3))

            // 出租
            if (mHttpRequest.leaseType == "0") {
                mImageList.add(getPath(mRequest.imgList_7[0], mHttpRequest.rentAuthorizationSignImg1))
                mImageList.add(getPath(mRequest.imgList_7[1], mHttpRequest.rentAuthorizationSignImg2))
                mImageList.add(getPath(mRequest.imgList_7[2], mHttpRequest.rentAuthorizationSignImg3))
                mImgFileType = 2
            } else {//出售
                mImgFileType = 3
                mImageList.add(getPath(mRequest.imgList_7[0], mHttpRequest.formaConfirmImg1))
                mImageList.add(getPath(mRequest.imgList_7[1], mHttpRequest.formaConfirmImg2))
                mImageList.add(getPath(mRequest.imgList_7[2], mHttpRequest.formaConfirmImg3))
            }
        }
        //出租情况下才会有合同
        mImageList.add(getPath(mRequest.imgList_8[0], mHttpRequest.houseRentContractImg1))
        mImageList.add(getPath(mRequest.imgList_8[1], mHttpRequest.houseRentContractImg2))
        mImageList.add(getPath(mRequest.imgList_8[2], mHttpRequest.houseRentContractImg3))
        mImageList.add(getPath(mRequest.imgList_8[3], mHttpRequest.houseRentContractImg4))

        if (mHttpRequest.leaseType == "1") {
            mImageList.add(getPath(mRequest.imgList_10[0], mHttpRequest.houseHoldImg1))
            mImageList.add(getPath(mRequest.imgList_10[1], mHttpRequest.houseHoldImg2))
            mImageList.add(getPath(mRequest.imgList_10[2], mHttpRequest.houseHoldImg3))
            mImageList.add(getPath(mRequest.imgList_10[3], mHttpRequest.houseHoldImg4))
            mImageList.add(getPath(mRequest.imgList_10[4], mHttpRequest.houseHoldImg5))
            mImageList.add(getPath(mRequest.imgList_10[5], mHttpRequest.houseHoldImg6))
            mImageList.add(getPath(mRequest.imgList_10[6], mHttpRequest.houseHoldImg7))
            mImageList.add(getPath(mRequest.imgList_10[7], mHttpRequest.houseHoldImg8))
            mImageList.add(getPath(mRequest.imgList_10[8], mHttpRequest.houseHoldImg9))
            mImageList.add(getPath(mRequest.imgList_10[9], mHttpRequest.houseHoldImg10))
        }
        Log.e(TAG, "提交 ${mHttpRequest.houseConfigDictcode}")
        requestImg()
    }

    private fun requestImg() {
        Log.e(TAG, "requestImg ${mImageList.size} ${mSuccessImageList.size}")
        mDialog?.show()
        if (mImageList.isEmpty()) {
            request()
            return
        }
        mVm = getViewModel()
        //如果是add说明没有修改图片存在的
        if (mImageList[0] == "add_xx_xx" || mImageList[0] == "add_xx1_xx1" ||mImageList[0] == "add_x_x") {
            mSuccessImageList.add(mImageList[0])
            mImageList.removeAt(0)
            requestImg()
        } else {
            mVm?.uploadImg(mImageList[0])
        }
    }

    private fun request() {
        if (mHttpRequest.applicantType == "0") {
            mHttpRequest.pocImg1 = mSuccessImageList[0]
            mHttpRequest.pocImg2 = mSuccessImageList[1]
            mHttpRequest.pocImg3 = mSuccessImageList[2]

            mHttpRequest.reoPassportImg1 = mSuccessImageList[3]
            mHttpRequest.reoPassportImg2 = mSuccessImageList[4]
            mHttpRequest.reoPassportImg3 = mSuccessImageList[5]

        } else {
            mHttpRequest.mandataryCopiesImg1 = mSuccessImageList[0]
            mHttpRequest.mandataryCopiesImg2 = mSuccessImageList[1]
            mHttpRequest.mandataryCopiesImg3 = mSuccessImageList[2]
            mHttpRequest.mandataryCopiesImg4 = mSuccessImageList[3]
            mHttpRequest.mandataryCopiesImg5 = mSuccessImageList[4]
            mHttpRequest.mandataryCopiesImg6 = mSuccessImageList[5]
            mHttpRequest.mandataryCopiesImg7 = mSuccessImageList[6]
            mHttpRequest.mandataryCopiesImg8 = mSuccessImageList[7]
            mHttpRequest.mandataryCopiesImg9 = mSuccessImageList[8]
            mHttpRequest.mandataryCopiesImg10 = mSuccessImageList[9]

            mHttpRequest.mandataryPassportImg1 = mSuccessImageList[10]
            mHttpRequest.mandataryPassportImg1 = mSuccessImageList[11]
            mHttpRequest.mandataryPassportImg1 = mSuccessImageList[12]

            mHttpRequest.mandataryVisaImg1 = mSuccessImageList[13]
            mHttpRequest.mandataryVisaImg2 = mSuccessImageList[14]
            mHttpRequest.mandataryVisaImg3 = mSuccessImageList[15]

            mHttpRequest.mandataryIdcardImg1 = mSuccessImageList[16]
            mHttpRequest.mandataryIdcardImg2 = mSuccessImageList[17]
            mHttpRequest.mandataryIdcardImg3 = mSuccessImageList[18]
            mHttpRequest.mandataryIdcardImg4 = mSuccessImageList[19]

            mHttpRequest.pocImg1 = mSuccessImageList[20]
            mHttpRequest.pocImg2 = mSuccessImageList[21]
            mHttpRequest.pocImg3 = mSuccessImageList[22]

            mHttpRequest.reoPassportImg1 = mSuccessImageList[23]
            mHttpRequest.reoPassportImg2 = mSuccessImageList[24]
            mHttpRequest.reoPassportImg3 = mSuccessImageList[25]
        }
        var index = 0
        when (mImgFileType) {
            0 -> {
                mHttpRequest.rentAuthorizationSignImg1 = mSuccessImageList[6]
                mHttpRequest.rentAuthorizationSignImg2 = mSuccessImageList[7]
                mHttpRequest.rentAuthorizationSignImg3 = mSuccessImageList[8]
                index = 9
            }
            1 -> {
                mHttpRequest.formaConfirmImg1 = mSuccessImageList[6]
                mHttpRequest.formaConfirmImg2 = mSuccessImageList[7]
                mHttpRequest.formaConfirmImg3 = mSuccessImageList[8]
                index = 9
            }
            2 -> {
                mHttpRequest.rentAuthorizationSignImg1 = mSuccessImageList[26]
                mHttpRequest.rentAuthorizationSignImg2 = mSuccessImageList[27]
                mHttpRequest.rentAuthorizationSignImg3 = mSuccessImageList[28]
                index = 29
            }
            3 -> {
                mHttpRequest.formaConfirmImg1 = mSuccessImageList[26]
                mHttpRequest.formaConfirmImg2 = mSuccessImageList[27]
                mHttpRequest.formaConfirmImg3 = mSuccessImageList[28]
                index = 29
            }
        }
        mHttpRequest.houseRentContractImg1 = mSuccessImageList[index]
        index++
        mHttpRequest.houseRentContractImg2 = mSuccessImageList[index]
        index++
        mHttpRequest.houseRentContractImg3 = mSuccessImageList[index]
        index++
        mHttpRequest.houseRentContractImg4 = mSuccessImageList[index]
        index++
        if (mHttpRequest.leaseType == "1") {
            mHttpRequest.houseHoldImg1 = mSuccessImageList[index]
            index++
            mHttpRequest.houseHoldImg2 = mSuccessImageList[index]
            index++
            mHttpRequest.houseHoldImg3 = mSuccessImageList[index]
            index++
            mHttpRequest.houseHoldImg4 = mSuccessImageList[index]
            index++
            mHttpRequest.houseHoldImg5 = mSuccessImageList[index]
            index++
            mHttpRequest.houseHoldImg6 = mSuccessImageList[index]
            index++
            mHttpRequest.houseHoldImg7 = mSuccessImageList[index]
            index++
            mHttpRequest.houseHoldImg8 = mSuccessImageList[index]
            index++
            mHttpRequest.houseHoldImg9 = mSuccessImageList[index]
            index++
            mHttpRequest.houseHoldImg10 = mSuccessImageList[index]
            index++
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
        val json = gson.toJson(arrayListOf<OrderSetting>().apply {
            mList.forEach {
                add(OrderSetting().apply {
                    id="${it.id}"
                    houseId="$mAppliId"
                    isOpen="${it.isOpen}"
                    houseRentPrice=it.houseRentPrice
                    //出租
                    if(mResponseData.dataSet?.houses?.leaseType==0){
                        beginRentDate=it.beginRentDate
                        rentTime=it.rentTime.toString()
                    }else{
                        payNode=it.payNode.toString()
                        payType=it.payType.toString()
                    }
                })
            }
        })
        mHttpRequest.setting = json
        Log.e(TAG,"fgvdfvd $json")
        mDialog?.show()
        mVm = getViewModel()
        mHttpRequest.houseId=mAppliId
        mVm?.updateListing(mHttpRequest)
    }

    private fun refreshZdyd(view: View, index: Int, type: Int) {
        view.apply {
            if (type == 0) {
                val data = mList[0]
                title_tv.setStringRes(when(index){
                    1-> R.string.zdyd_tv2
                    2->R.string.zdyd_tv3
                    else-> R.string.zdyd_tv1
                })
                switch_button1.isChecked = data.isOpen == 1
                switch_button1.setOnCheckedChangeListener { view, isChecked ->
                    data.isOpen = if (isChecked) {
                        1
                    } else {
                        0
                    }
                    zdsj1.isEnabled = data.isOpen == 1
                    //mList[index] = data
                }
                qzrq1.text = data.beginRentDate
                qzrq1.setOnClickListener {
                    if (data.isOpen == 1) {
                        showYearMonthDayPicker(DateTimePicker.OnYearMonthDayTimePickListener { year, month, day, hour, minute ->
                            val value = "$year-$month-$day 00:00"
                            data.beginRentDate = value
                            qzrq1.text = data.beginRentDate
                            //mList[index] = data
                        })
                    }
                }
                if(data.rentTime in 1..10){
                    sc1.text = "${data.rentTime}${getString(R.string.heihei_tv17)}"
                }
                sc1.setOnClickListener {
                    if (data.isOpen == 1) {
                        showPicker(arrayListOf<String>().apply {
                            for (i in 1..10) {
                                add("$i")
                            }
                        }, SinglePicker.OnItemPickListener<String> { index, item ->
                            sc1.text = "$item ${getString(R.string.heihei_tv17)}"
                            data.rentTime = "$item".toInt()
                            //mList[index] = data
                        })
                    }
                }
                if (data.payNode > 0) {
                    zfjd1.text = when("${data.payNode}") {
                        "1" -> getString(R.string.paynode_type1)
                        "2" -> getString(R.string.paynode_type2)
                        "4" -> getString(R.string.paynode_type3)
                        "6" -> getString(R.string.paynode_type4)
                        "12" -> getString(R.string.paynode_type5)
                        else -> ""
                    }
                }
                zfjd1.setOnClickListener {
                    if (data.isOpen == 1) {
                        showPicker(arrayListOf<String>().apply {
                            add(getString(R.string.paynode_type1))
                            add(getString(R.string.paynode_type2))
                            add(getString(R.string.paynode_type3))
                            add(getString(R.string.paynode_type4))
                            add(getString(R.string.paynode_type5))
                        }, SinglePicker.OnItemPickListener<String> { index, item ->
                            data.payNode = when(index){
                                1->2
                                2->4
                                3->6
                                4->12
                                else->1
                            }
                            zfjd1.text = item
                        })
                    }
                }
                zdsj1.isEnabled = data.isOpen == 1
                zdsj1.setText(data.houseRentPrice.toString())
                zdsj1.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        data.houseRentPrice = s?.trim().toString()
                        Log.e(TAG, "onTextChanged ${mList[index].houseRentPrice}")
                    }
                })
                return
            }
            val data = mList[index]
            switch_button.isChecked = data.isOpen == 1
            val isClick:()->Unit={
                xj_img.isEnabled = data.isOpen == 1
                dk_img.isEnabled = data.isOpen == 1
                yes_img.isEnabled = data.isOpen == 1
                no_img.isEnabled = data.isOpen == 1
                price.isEnabled = data.isOpen == 1
            }
            isClick()
            switch_button.setOnCheckedChangeListener { view, isChecked ->
                data.isOpen = if (isChecked) {
                    1
                } else {
                    0
                }
                isClick()
                price.isEnabled = data.isOpen == 1
                //mList[index] = data
            }
            if (data.payType == 0) {
                xj_img.setBackgroundResource(R.mipmap.check_selected)
                dk_img.setBackgroundResource(R.drawable.check_bg)
                parent3.visibility = View.GONE
            } else {
                xj_img.setBackgroundResource(R.drawable.check_bg)
                dk_img.setBackgroundResource(R.mipmap.check_selected)
                parent3.visibility = View.VISIBLE
            }
            if (data.hasExpectApprove == 0) {
                yes_img.setBackgroundResource(R.mipmap.check_selected)
                no_img.setBackgroundResource(R.drawable.check_bg)
            } else {
                no_img.setBackgroundResource(R.mipmap.check_selected)
                yes_img.setBackgroundResource(R.drawable.check_bg)
            }
            xj_img.setOnClickListener {
                data.payType = 1
                xj_img.setBackgroundResource(R.mipmap.check_selected)
                dk_img.setBackgroundResource(R.drawable.check_bg)
                parent3.visibility = View.GONE
                //mList[index] = data
            }
            dk_img.setOnClickListener {
                data.payType = 0
                xj_img.setBackgroundResource(R.drawable.check_bg)
                dk_img.setBackgroundResource(R.mipmap.check_selected)
                parent3.visibility = View.VISIBLE
                //mList[index] = data
            }
            yes_img.setOnClickListener {
                data.hasExpectApprove = 0
                yes_img.setBackgroundResource(R.mipmap.check_selected)
                no_img.setBackgroundResource(R.drawable.check_bg)
                //mList[index] = data
            }
            no_img.setOnClickListener {
                data.hasExpectApprove = 1
                no_img.setBackgroundResource(R.mipmap.check_selected)
                yes_img.setBackgroundResource(R.drawable.check_bg)
                //mList[index] = data
            }
            price.isEnabled = data.isOpen == 1
            price.setText(data.houseRentPrice.toString())
            price.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    data.houseRentPrice = s?.trim().toString()
                    Log.e(TAG,"onTextChanged ${mList[index]}")
                    //mList[index] = data
                }
            })
        }
    }
}