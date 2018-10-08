package com.ysgj.order.ui.activity.order

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent
import cn.qqtheme.framework.entity.City
import cn.qqtheme.framework.entity.County
import cn.qqtheme.framework.entity.Province
import cn.qqtheme.framework.picker.AddressPicker
import cn.qqtheme.framework.picker.DateTimePicker
import cn.qqtheme.framework.picker.SinglePicker
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.CameraBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.request.ListingInfoRequest
import cn.yc.library.bean.request.ListingRequest
import cn.yc.library.bean.request.OrderSetting
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.ListingConfigResponse
import cn.yc.library.bean.response.ListingDidateResponse
import cn.yc.library.bean.response.LoginResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.ui.vm.AppViewModel
import cn.yc.library.utils.*
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
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.ui.activity.CasaActivity
import com.ysgj.order.ui.activity.RespuestaautomáticaActivity
import kotlinx.android.synthetic.main.activity_houses_edit.*
import java.net.InetAddress
import java.util.*


class HousesOrderEditActivity : BaseActivity(), SinglePicker.OnItemPickListener<String>, OnMapReadyCallback {

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
            }else{
                mRequestSuccess=true
            }
        }
    }

    private val mCityList= arrayListOf<Province>()
    private var mPropertyTypeList= arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()
    private val mHousesStatus= arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()
    private val mHousesType=arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()

    private var mAppViewModel: AppViewModel?=null

    private val mDefaultHandler=DefaultHandler()
    private var mCheckNetwork=false

    private var mRequest = ListingInfoRequest()
    private var mInitRequest=ListingInfoRequest()
    private var mVm: OrderViewModel? = null

    private var mCameraBean = CameraBean()
    private var mAppliId = ""

    private var mImgIndex = 0
    private var mColumnsIndex=0
    private var mType = -1

    private var mDelImageView:ImageView?=null

    private var mSelectorIndex = -1
    private var mImgFileType=0

    private var mPlaceDetectionClient: PlaceDetectionClient?=null
    private var mGeoDataClient: GeoDataClient?=null
    private var mFusedLocationProviderClient: FusedLocationProviderClient?=null

    private var mHahsDataMap= hashMapOf<Int,Boolean>().apply {
        put(0,false)
        put(1,false)
        put(2,false)
        put(3,false)
    }

    private val mHttpRequest = ListingRequest()
    private val mImgData= hashMapOf<Int, ListingInfoRequest>().apply {
        put(0,ListingInfoRequest())
        put(1,ListingInfoRequest())
        put(2,ListingInfoRequest())
        put(3,ListingInfoRequest())
    }

    private lateinit var mMap: GoogleMap

    override val layoutId: Int
        get() = R.layout.activity_houses_edit

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun setLeaseType(index:Int){
        if(index==0){
            mHttpRequest.leaseType="0"
            if (mRequest.type == 1||mRequest.type == -1) {
                mRequest.type = 0
                mResponseData.dataSet?.houses?.leaseType=0
                check_img.setBackgroundResource(R.mipmap.check_selected)
                check_img1.setBackgroundResource(R.drawable.check_bg)
                refreshUI()
            }
        }else{
            mHttpRequest.leaseType="1"
            if (mRequest.type == 0 ||mRequest.type == -1) {
                mRequest.type = 1
                mResponseData.dataSet?.houses?.leaseType=1
                check_img1.setBackgroundResource(R.mipmap.check_selected)
                check_img.setBackgroundResource(R.drawable.check_bg)
                refreshUI()
            }
        }
    }

    private fun setListingStatus(index:Int){
        mHttpRequest.isPromissoryBuild=index
        fyzt.text=getString(if(index==0){
            R.string.fyzt_type1
        }else{
            R.string.fyzt_type2
        })
    }

    private var mLocationType=0
    private fun sendLocation(locationType:Int){
        mLocationType=locationType
        mDialog= PromptDialog(this, PromptDialog.DialogBuild().apply {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDelImageView=ImageView(this).apply {

        }

        mAppViewModel=getApp()?.getAppViewModel()
        val settings= OrderSetting()
        settings.refresh()

        val title = intent.getStringExtra("title")
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mType = intent.getIntExtra("type", -1)
        mHttpRequest.applyId=intent.getStringExtra("applyId")
        mHttpRequest.houseId=intent.getStringExtra("houseId")
        Log.e(TAG, "onCreate $mType")
        foter?.visibility = if (mType == 1) {
            View.GONE
        } else {
            View.GONE
        }
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
            sendLocation(0)
        }
        qylx.setOnClickListener {
            showPicker(arrayListOf<String>().apply {
                add("Free Hold")
                add("Lease Hold")
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                qylx.text=item
                mHttpRequest.typeOfArea=index.toString()
            })
        }
        if (mType == 1) {
            foter.visibility = View.GONE
            zddy.visibility = View.GONE
        }
        dw.setOnClickListener {
            if(mLocation){
                locationTask()
            }
        }
        mHttpRequest.taskId=intent.getStringExtra("taskId")
        mAppliId = intent.getStringExtra("applyId")
        toolbarInit {
            mToolbar = toolbar
            data = ToolbarBean(getString(R.string.toolbar_title3), "", false)
            onBack {
                finish()
            }
        }

        selector?.setOnClickListener {
            showPicker(arrayListOf<String>().apply {
                add("POA")
                add(getString(R.string.houses_tv7))
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                listing_tv?.text = item
                if (index == 1) {
                    mResponseData.dataSet?.houses?.applicantType=0
                    mHttpRequest.applicantType = "0"
                } else {
                    mResponseData.dataSet?.houses?.applicantType=1
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
                    Intent(this,CasaActivity::class.java).apply {
                        putExtra("img1",mRequest.imgList_8[0])
                        putExtra("img2",mRequest.imgList_8[1])
                        putExtra("img3",mRequest.imgList_8[2])
                        putExtra("img4",mRequest.imgList_8[3])
                        putExtra("isCustomerServiceRelation",mHttpRequest.isCustomerServiceRelation)
                        putExtra("appointmentLookTime",mHttpRequest.appointmentLookTime)
                        putExtra("housingStatus",mHttpRequest.housingStatus)
                        putExtra("haveKey",mHttpRequest.haveKey)
                        putExtra("name",mHttpRequest.rentCustomerName)
                        putExtra("phone",mHttpRequest.rentCustomerPhone)
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
                zxqk.text=item
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
                yssl.text="$item"
                mHttpRequest.bathroomNum=item
            })
        }
        fd.setOnClickListener {
            showPicker(arrayListOf<String>().apply {
                add(getString(R.string.no))
                add(getString(R.string.yes))
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                mHttpRequest.isHouseLoan=index.toString()
                fd.text=item
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
            mHttpRequest.leaseType="0"
            if (mRequest.type == 1||mRequest.type == -1) {
                mRequest.type = 0
                mResponseData.dataSet?.houses?.leaseType=0
                check_img.setBackgroundResource(R.mipmap.check_selected)
                check_img1.setBackgroundResource(R.drawable.check_bg)
                initImg()
                refreshUI()
            }
        }
        check_img1?.setOnClickListener {
            mImgData[getDataIndex()] = mRequest
            mHttpRequest.leaseType="1"
            if (mRequest.type == 0 ||mRequest.type == -1) {
                mRequest.type = 1
                mResponseData.dataSet?.houses?.leaseType=1
                check_img1.setBackgroundResource(R.mipmap.check_selected)
                check_img.setBackgroundResource(R.drawable.check_bg)
                initImg()
                refreshUI()
            }
        }
        val setImgClick:(Int,Int)->Unit={index,columnsIndex->
            mImgIndex =index
            mColumnsIndex=columnsIndex
            setImg()
        }

        for(i in 0 until imgone_parent.childCount){
            imgone_parent.getChildAt(i).setOnClickListener {
                setImgClick(0,i)
            }
        }
        for(i in 0 until imgtwo_parent.childCount){
            (imgtwo_parent.getChildAt(i) as? SimpleDraweeView)?.apply {
                setOnClickListener {
                    setImgClick(1,i)
                }
            }
        }
        for(i in 0 until img3_parent.childCount){
            img3_parent.getChildAt(i).setOnClickListener {
                setImgClick(2,i)
            }
        }
        for(i in 0 until img4_parent.childCount){
            img4_parent.getChildAt(i).setOnClickListener {
                setImgClick(3,i)
            }
        }
        for(i in 0 until img5_parent.childCount){
            img5_parent.getChildAt(i).setOnClickListener {
                setImgClick(4,i)
            }
        }
        for(i in 0 until img6_parent.childCount){
            img6_parent.getChildAt(i).setOnClickListener {
                setImgClick(5,i)
            }
        }
        for(i in 0 until img7_parent.childCount){
            img7_parent.getChildAt(i).setOnClickListener {
                setImgClick(6,i)
            }
        }
        for(i in 0 until img8_parent.childCount){
            img8_parent.getChildAt(i).setOnClickListener {
                setImgClick(7,i)
            }
        }

        bt.setOnClickListener {
            if(!mRequestSuccess){
                showMessage(R.string.heihei_tv66)
                return@setOnClickListener
            }
            if(mHttpRequest.houseId.isNullOrEmpty() || mHttpRequest.houseId.isNullOrBlank() || !mRequestSuccess){
                showMessage(R.string.request_error_1)
                return@setOnClickListener
            }
            upload()
        }
        requestData()
        refresh_layout.setOnRefreshListener {
            if(mRequestSuccess){
                return@setOnRefreshListener
            }
            requestData()
        }
        address.setOnClickListener {
            //mAppViewModel?.mCity?.value?.dataSet?.
            showCityPicker(mCityList, AddressPicker.OnAddressPickListener { province, city, county ->
                mHttpRequest.city=city.areaName
                mHttpRequest.community=county.areaName
                mHttpRequest.province=province.areaName
                mHttpRequest.subCommunity=county.areaName
                address.text="${province.areaName} ${city.areaName} ${county.areaName}"
            })
        }
        getBaseViewModel<BaseViewModel>()?.setRequestCode(11107)
        refreshUI()
    }

    private fun requestData(){
        mVm=getViewModel()
        mVm?.getListingDidate(mAppliId)
    }

    private var mRequestInit=false

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
                listing_view.setListingData(mResponseData.dataSet?.houses?.houseConfigDictcode?:"")
                listing_view.setHousesData(mResponseData.dataSet?.houses?.houseSelfContainedDictcode?:"")
                mHttpRequest.startRentDate=mResponseData.dataSet?.houses?.beginRentDate.toString()
                mHttpRequest.payNode=mResponseData.dataSet?.houses?.payNode.toString()
                mHttpRequest.isHouseLoan=mResponseData.dataSet?.houses?.isHouseLoan.toString()
                fd.text = if(mHttpRequest.isHouseLoan=="1"){
                    getString(R.string.yes)
                }else{
                    getString(R.string.no)
                }
                qwzj.setText(mResponseData.dataSet?.houses?.houseRent.toString())
                zdzj.setText(mResponseData.dataSet?.houses?.minHouseRent.toString())
                mHttpRequest.startRentDate=mResponseData.dataSet?.houses?.beginRentDate?:""

                mHttpRequest.payNode="${mResponseData.dataSet?.houses?.payNode}"

                sc.setBlankText(mResponseData.dataSet?.houses?.beginRentDate)
                mHttpRequest.houseRentContractImg1=setPath(houseRentContractImg1)
                mHttpRequest.houseRentContractImg2=setPath(houseRentContractImg2)
                mHttpRequest.houseRentContractImg3=setPath(houseRentContractImg3)
                mHttpRequest.houseRentContractImg4=setPath(houseRentContractImg4)
                mRequest.imgList_8[0]=mHttpRequest.houseRentContractImg1
                mRequest.imgList_8[1]=mHttpRequest.houseRentContractImg2
                mRequest.imgList_8[2]=mHttpRequest.houseRentContractImg3
                mRequest.imgList_8[3]=mHttpRequest.houseRentContractImg4
                qzrq.text=when(mHttpRequest.payNode) {
                    "1" -> getString(R.string.paynode_type1)
                    "2" -> getString(R.string.paynode_type2)
                    "4" -> getString(R.string.paynode_type3)
                    "6" -> getString(R.string.paynode_type4)
                    "12" -> getString(R.string.paynode_type5)
                    else -> ""
                }
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

    private fun getDataIndex():Int{
        return if (mHttpRequest.applicantType == "1") {
            //如果是出租
            if(mRequest.type == 0) 0 else{
                1
            }
        } else {
            //如果是出租
            if(mRequest.type == 0){
                2
            }else{
                3
            }
        }
    }

    private fun refreshUI() {
        if(mResponseData.dataSet?.houses?.leaseType==0){
            fyzt_parent.visibility=View.GONE
        }else{
            fyzt_parent.visibility=View.VISIBLE
        }
        //如果是POA且出租
        if (mHttpRequest.applicantType == "1") {
            for(i in 0 until imgone_parent.childCount){
                imgone_parent.getChildAt(i).visibility=View.VISIBLE
            }
            img_parent1_tv.text = getString(R.string.houses_tv8)
            img_parent2_tv.text = getString(R.string.houses_tv9)
            img_parent3_tv.text = getString(R.string.houses_tv10)
            img_parent4_tv.text = getString(R.string.houses_tv11)
            img_parent5_tv.text = getString(R.string.houses_tv12)
            img_parent6_tv.text = getString(R.string.houses_tv13)
            img_parent1.visibility=View.VISIBLE
            img_parent2.visibility=View.VISIBLE
            img_parent3.visibility=View.VISIBLE
            img_parent4.visibility=View.VISIBLE
            img_parent5.visibility=View.VISIBLE
            img_parent6.visibility=View.VISIBLE
            img_parent7.visibility=View.VISIBLE
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
                img_parent4_tv2.visibility=View.VISIBLE

                zddy_title.text = getString(R.string.hehe1)
                zddy_child1_tv.text = getString(R.string.hehe2)
                zddy_child2_tv.text = getString(R.string.hehe3)
                zddy_child4_tv.text = getString(R.string.hehe4)
                zddy_child4_tv1.visibility = View.VISIBLE
                zddy_child5_tv1.visibility = View.VISIBLE
                zddy_child5_tv.text = getString(R.string.hehe5)
                fyxx_parent.visibility=View.GONE
            } else {
                sjxq.visibility = View.VISIBLE
                zddy.visibility = View.GONE
                img_parent7_tv2.visibility = View.GONE
                img_parent4_tv2.visibility=View.VISIBLE

                img_parent7_tv.text = getString(R.string.houses_tv14)
                img_parent7_tv2.visibility=View.VISIBLE
                fyxx_parent.visibility=View.VISIBLE

            }
        } else {
            img_parent1.visibility = View.VISIBLE
            img_parent2.visibility = View.VISIBLE
            img_parent3.visibility = View.VISIBLE
            img_parent4.visibility = View.GONE
            img_parent5.visibility = View.GONE
            img_parent6.visibility = View.GONE
            img_parent7.visibility = View.GONE
            for(i in 0 until imgone_parent.childCount){
                imgone_parent.getChildAt(i).visibility=if(i<=2){
                    View.VISIBLE
                }else{
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
                img_parent4_tv2.visibility=View.GONE
                fd.text = if(mHttpRequest.isHouseLoan=="1"){
                    mHttpRequest.isHouseLoan="1"
                    getString(R.string.yes)
                }else{
                    mHttpRequest.isHouseLoan="0"
                    getString(R.string.no)
                }
                fyxx_parent.visibility=View.VISIBLE
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

                fyxx_parent.visibility=View.GONE
            }

        }

        if(mRequest.type==1){
            img_parent8.visibility=View.VISIBLE
        }else{
            img_parent8.visibility=View.GONE
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
                qzrq.text=item
            })
        }
        sc.setOnClickListener {
            showYearMonthDayPicker(DateTimePicker.OnYearMonthDayTimePickListener { year, month, day, hour, minute ->
                val value = "$year-$month-$day 00:00"
                mHttpRequest.startRentDate=value
                sc.text=value
            })
        }

        ////出售情况下户型图
        if(mRequest.type==1){
            for(i in 0 until mRequest.imgList_10.size){
                setFresco(mRequest.imgList_10[i],img8_parent.getChildAt(i) as SimpleDraweeView,false)
            }
        }
        Log.e(TAG,"刷新 ${mHttpRequest.applicantType} $mRequest")
        if (mHttpRequest.applicantType == "1") {//poa
            for(i in 0 until mRequest.imgList_1.size){
                setFresco(mRequest.imgList_1[i],imgone_parent.getChildAt(i) as SimpleDraweeView,false)
            }
            //被委托人的护照
            setFresco(mRequest.imgList_2[0],imgtwo_parent.getChildAt(0),false)
            setFresco(mRequest.imgList_2[1],imgtwo_parent.getChildAt(1),false)
            setFresco(mRequest.imgList_2[2],imgtwo_parent.getChildAt(2) as SimpleDraweeView,false)

            //被委托人的签证
            setFresco(mRequest.imgList_3[0],img3_parent.getChildAt(0),false)
            setFresco(mRequest.imgList_3[1],img3_parent.getChildAt(1),false)
            setFresco(mRequest.imgList_3[2],img3_parent.getChildAt(2),false)


            //被委托人的id卡
            setFresco(mRequest.imgList_4[0],img4_parent.getChildAt(0),false)
            setFresco(mRequest.imgList_4[1],img4_parent.getChildAt(1),false)
            setFresco(mRequest.imgList_4[2],img4_parent.getChildAt(2) as SimpleDraweeView,false)
            setFresco(mRequest.imgList_4[3],img4_parent.getChildAt(3),false)

            //房屋产权证明
            setFresco(mRequest.imgList_5[0],img5_parent.getChildAt(0),false)
            setFresco(mRequest.imgList_5[1],img5_parent.getChildAt(1),false)
            setFresco(mRequest.imgList_5[2],img5_parent.getChildAt(2),false)

            //房屋产权人护照照片
            setFresco(mRequest.imgList_6[0],img6_parent.getChildAt(0),false)
            setFresco(mRequest.imgList_6[1],img6_parent.getChildAt(1),false)
            setFresco(mRequest.imgList_6[2],img6_parent.getChildAt(2),false)

            setFresco(mRequest.imgList_7[0],img7_parent.getChildAt(0),false)
            setFresco(mRequest.imgList_7[1],img7_parent.getChildAt(1),false)
            setFresco(mRequest.imgList_7[2],img7_parent.getChildAt(2),false)
        }else{
            //房屋产权证明
            setFresco(mRequest.imgList_1[0],imgone_parent.getChildAt(0),false)
            setFresco(mRequest.imgList_1[1],imgone_parent.getChildAt(1),false)
            setFresco(mRequest.imgList_1[2],imgone_parent.getChildAt(2) as SimpleDraweeView,false)

            //房屋产权人护照照片
            setFresco(mRequest.imgList_2[0],imgtwo_parent.getChildAt(0),false)
            setFresco(mRequest.imgList_2[1],imgtwo_parent.getChildAt(1),false)
            setFresco(mRequest.imgList_2[2],imgtwo_parent.getChildAt(2) as SimpleDraweeView,false)

            setFresco(mRequest.imgList_3[0],img3_parent.getChildAt(0),false)
            setFresco(mRequest.imgList_3[1],img3_parent.getChildAt(1),false)
            setFresco(mRequest.imgList_3[2],img3_parent.getChildAt(2) as SimpleDraweeView,false)

        }
    }

    private fun setImg() {
        showPicker(arrayListOf<String>().apply {
            add(getString(R.string.pz))
            add(getString(R.string.xc))
            add(getString(R.string.heihei_tv19))
        }, SinglePicker.OnItemPickListener<String> { index, item ->
            when (index) {
                1 -> RxGalleryFinalApi
                        .openRadioSelectImage(this, object : RxBusResultDisposable<ImageRadioResultEvent>() {
                            override fun onEvent(it: ImageRadioResultEvent?) {
                                it?.also {
                                    when (mImgIndex) {
                                        0->{
                                            mRequest.imgList_1[mColumnsIndex]=it.result.originalPath
                                            Log.e(TAG,"图片 ${mRequest.imgList_1[mColumnsIndex]} ${imgone_parent.getChildAt(mColumnsIndex)}")
                                            val view=imgone_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            setFresco(it.result.originalPath,view)
                                        }
                                        1 -> {
                                            val view=imgtwo_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_2[mColumnsIndex]=it.result.originalPath
                                            setFresco(it.result.originalPath,view)
                                        }
                                        2 -> {
                                            val view=img3_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_3[mColumnsIndex]=it.result.originalPath
                                            setFresco(it.result.originalPath,view)
                                        }
                                        3 -> {
                                            val view=img4_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_4[mColumnsIndex]=it.result.originalPath
                                            setFresco(it.result.originalPath,view)
                                        }
                                        4 -> {
                                            val view=img5_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_5[mColumnsIndex]=it.result.originalPath
                                            setFresco(it.result.originalPath,view)
                                        }
                                        5 -> {
                                            val view=img6_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_6[mColumnsIndex]=it.result.originalPath
                                            setFresco(it.result.originalPath,view)
                                        }
                                        6 -> {
                                            val view=img7_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_7[mColumnsIndex]=it.result.originalPath
                                            setFresco(it.result.originalPath,view)
                                        }
                                        7->{
                                            val view=img8_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                                            mRequest.imgList_10[mColumnsIndex]=it.result.originalPath
                                            setFresco(it.result.originalPath,view)
                                        }
                                    }
                                }
                            }
                        },true)
                0 -> camearPermissions()
                else ->{
                    Log.e(TAG,"删除 $mImgIndex")
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
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "onActivity $requestCode $resultCode $mCameraBean")
        if(resultCode==1033){
            setResult(1033)
            finish()
            return
        }
        if(requestCode==10 && resultCode== Activity.RESULT_OK){
            PlacePicker.getPlace(data, this)?.apply {
                mHttpRequest.address="$name"
                mHttpRequest.latitude="${latLng.latitude}"
                mHttpRequest.longitude="${latLng.longitude}"
                address_tv.setText(mHttpRequest.address)
            }
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
                when (mImgIndex) {
                    0->{
                        mRequest.imgList_1[mColumnsIndex]=mCameraBean.imgPath
                        val view=imgone_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        setFresco(mCameraBean.imgPath,view)
                    }
                    1 -> {
                        val view=imgtwo_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_2[mColumnsIndex]=mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath,view)
                    }
                    2 -> {
                        val view=img3_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_3[mColumnsIndex]=mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath,view)
                    }
                    3 -> {
                        val view=img4_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_4[mColumnsIndex]=mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath,view)
                    }
                    4 -> {
                        val view=img5_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_5[mColumnsIndex]=mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath,view)
                    }
                    5 -> {
                        val view=img6_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_6[mColumnsIndex]=mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath,view)
                    }
                    6 -> {
                        val view=img7_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_7[mColumnsIndex]=mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath,view)
                    }
                    7->{
                        val view=img8_parent.getChildAt(mColumnsIndex) as SimpleDraweeView
                        mRequest.imgList_10[mColumnsIndex]=mCameraBean.imgPath
                        setFresco(mCameraBean.imgPath,view)
                    }
                }
            }
        } else if (requestCode == 11 && resultCode == RESULT_OK) {

        }
    }

    private var mLocation=false
    private var mRequestSuccess=false
    private var mResponseData= ListingDidateResponse()

    override fun <T> onNext(dat: T) {
        Log.e(TAG, "onnext $dat")
        if(dat is String && dat=="location"){
            mPlaceDetectionClient?.getCurrentPlace(null)?.addOnCompleteListener {
                if(it==null || !it.isSuccessful || it.result==null || it.result?.count?:0<=0) {
                    mDialog?.dismiss()
                    showMessage(R.string.location_error)
                    return@addOnCompleteListener
                }
                (it.result as? PlaceLikelihoodBufferResponse)?.apply {
                    val data=get(0)
                    if(mLocationType==0){
                        mDialog?.dismiss()
                        val builder= PlacePicker.IntentBuilder()
                        builder.setLatLngBounds(LatLngBounds(data.place.latLng,data.place.latLng))
                        startActivityForResult(builder.build(this@HousesOrderEditActivity),10)
                    }else{
                        mDialog?.show()
                        mVm?.requestLocation(mHttpRequest.taskId,"${data.place?.latLng?.longitude?:0.0}",
                                "${data.place?.latLng?.latitude?:0.0}","${data?.place?.address?:""}")
                    }
                }
            }
            return
        }
        ((dat as? BaseResponse<*>)?.data as? LoginResponse)?.apply {
            mDialog?.dismiss()
            if(result==0){
                getBaseViewModel<BaseViewModel>()?.setRequestCode(11106)
                val mtype:Int=if(mHttpRequest.leaseType == "0"){
                    0
                }else{
                    1
                }
                startActivityForResult(Intent(this@HousesOrderEditActivity,RespuestaautomáticaActivity::class.java).apply {
                    putExtra("type",mtype)
                    putExtra("price",zdsj.editableText.toString())
                    putExtra("json",Gson().toJson(mHttpRequest))
                    putExtra("imgJson",Gson().toJson(mRequest))
                    putExtra("initJson",Gson().toJson(mInitRequest))
                },1033)
            }else{
                showMessage(message)
            }
            return
        }
        ((dat as? BaseResponse<*>)?.data as? ListingDidateResponse)?.apply {
            Log.e(TAG,"嘿嘿 ${this.phoneNumber} ${this.dataSet?.phoneNumber}")
            fwgl.setBlankText(dataSet?.phoneNumber)
            mHttpRequest.phoneNumber=dataSet?.phoneNumber?:""
            mResponseData=this
            mRequestSuccess=true
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
            dataSet?.also {
                it.houses?.apply {
                    mHttpRequest.rentCustomerName="$rentCustomerName"
                    mHttpRequest.rentCustomerPhone="$rentCustomerPhone"
                    Log.e(TAG, "onnext ${applicantType}")
                    listing_tv?.text = if(applicantType==0){
                        getString(R.string.houses_tv7)
                    }else{
                        "POA"
                    }
                    mHttpRequest.applicantType = applicantType.toString()
                    mHttpRequest.housingTypeDictcode=housingTypeDictcode?:""
                    mHousesType.filter { it.id.toString()==mHttpRequest.housingTypeDictcode}.forEach {
                        fwlx.text=it.itemValue
                    }
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
                    mHttpRequest.isCustomerServiceRelation="$isCustomerServiceRelation"
                    if(mHttpRequest.isCustomerServiceRelation=="1"){
                        mHttpRequest.appointmentLookTime=""
                    }else{
                        mHttpRequest.appointmentLookTime=appointmentLookTime?:""
                    }
                    Log.e(TAG,"dfsvxvx ${mHttpRequest.isCustomerServiceRelation} ${mHttpRequest.appointmentLookTime}")
                    mHttpRequest.longitude=longitude?:""
                    mHttpRequest.latitude=latitude?:""
                    mHttpRequest.bathroomNum=bathroomNum.toString()
                    yssl.text=mHttpRequest.bathroomNum
                    mHttpRequest.parkingSpace=if(parkingSpace!=0 && parkingSpace!=100){
                        "1"
                    }else{
                        "$parkingSpace"
                    }
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
                    tw.setText(twitter.toString())
                    `is`.setText(instagram)
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
                    cqzs_ed.setText(titleDeedNumber)
                    cqbh.setText(propertyNumber)
                    dckfs.setText(masterDevelpoerName)
                    zdsj.setText("$minHouseRent")
                    qwsj.setText("$houseRent")

                    mHttpRequest.city=city?:""
                    //mHttpRequest.houseId=mResponseData.dataSet?.houses?.id.toString()
                }
                mHttpRequest.city=it.houses?.city?:""
                mHttpRequest.community=it.houses?.community?:""
                mHttpRequest.subCommunity=it.houses?.subCommunity?:""
                address.text ="${it.houses?.city}${it.houses?.community}${it.houses?.subCommunity}"
                address_tv.setText(it.houses?.address.toString())

                initImg()
                setLeaseType(mResponseData.dataSet?.houses?.leaseType?:-1)
            }
            showMessage(dat.messageId)
            return
        }
        if (dat is CameraBean) {
            mCameraBean = dat
        }
    }
    private fun setKey(key:Int){
        mHttpRequest.haveKey=key.toString()
        ysgs.text=getString(if(key==0){
            R.string.heihei_tv73
        }else{
            R.string.heihei_tv74
        })
    }

    override fun onError(code: Int, throwable: Throwable) {
        mRequestSuccess=false
        showMessage(throwable.message)
    }

    override fun onItemPicked(index: Int, item: String?) {

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
            mHousesStatus.none { mHttpRequest.housingStatus==it.id.toString() }->getString(R.string.edit_error22)
            else -> ""
        }
        if (msg.isNotBlank()) {
            showMessage(msg)
            return
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
        mHttpRequest.houseConfigDictcode=listing_view.getListing()
        mHttpRequest.houseSelfContainedDictcode=listing_view.getHouses()
        Log.e(TAG,"提交 ${mHttpRequest.houseConfigDictcode}")
        getBaseViewModel<BaseViewModel>()?.setRequestCode(11106)
        val mtype:Int=if(mHttpRequest.leaseType == "0"){
            0
        }else{
            1
        }
        startActivityForResult(Intent(this@HousesOrderEditActivity,RespuestaautomáticaActivity::class.java).apply {
            putExtra("type",mtype)
            putExtra("price",zdsj.editableText.toString())
            putExtra("json",Gson().toJson(mHttpRequest))
            putExtra("imgJson",Gson().toJson(mRequest))
            putExtra("initJson",Gson().toJson(mInitRequest))
        },1033)
        return
        sendLocation(1)

    }
}