package com.ysgj.order.ui.activity.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import cn.qqtheme.framework.entity.City
import cn.qqtheme.framework.entity.County
import cn.qqtheme.framework.entity.Province
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.CameraBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.request.ListingInfoRequest
import cn.yc.library.bean.request.ListingRequest
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.ListingConfigResponse
import cn.yc.library.bean.response.UploadResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.ItemClickListener
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.vm.AppViewModel
import cn.yc.library.utils.*
import cn.yc.library.vm.OrderViewModel
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.ui.adapter.ZdydAdapter
import kotlinx.android.synthetic.main.activity_twoinfo.*
import kotlinx.android.synthetic.main.item_zdyd1.view.*
import kotlinx.android.synthetic.main.item_zdyd2.view.*
import java.util.*

class HomeTwoInfoActivity :BaseActivity(), OnMapReadyCallback {

    internal inner class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }
    private val mDefaultHandler=DefaultHandler()
    private var mAdapter:ZdydAdapter?=null

    private var mRequest = ListingInfoRequest()
    private var mInitRequest= ListingInfoRequest()
    private var mVm: OrderViewModel? = null

    private var mCameraBean = CameraBean()
    private var mAppliId = ""

    private var mImgIndex = 0
    private var mColumnsIndex=0

    private val mCityList= arrayListOf<Province>()
    private var mAppViewModel: AppViewModel?=null
    private var mPropertyTypeList= arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()
    private val mHousesStatus= arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()
    private val mHousesType=arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()

    private var mHahsDataMap= hashMapOf<Int,Boolean>().apply {
        put(0,false)
        put(1,false)
        put(2,false)
        put(3,false)
        put(4,false)
    }

    private val mHttpRequest = ListingRequest()
    private val mImgData= hashMapOf<Int, ListingInfoRequest>().apply {
        put(0, ListingInfoRequest())
        put(1, ListingInfoRequest())
        put(2, ListingInfoRequest())
        put(3, ListingInfoRequest())
    }

    private lateinit var mMap: GoogleMap

    override val layoutId: Int
        get() = R.layout.activity_twoinfo

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e(TAG,"onMapReady $googleMap")
        mMap = googleMap
    }

    private fun setLeaseType(index:Int){
        Log.e(TAG,"setLease Type $index ${mRequest.type}")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAppViewModel=getApp()?.getAppViewModel()
        val title = intent.getStringExtra("title")
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        listing_tv.isEnabled=false
        mHttpRequest.applyId=intent.getStringExtra("applyId")
        mAppliId = intent.getStringExtra("applyId")
        Log.e(TAG,"applyid=$mAppliId")
        toolbarInit {
            mToolbar = toolbar
            data = ToolbarBean(getString(R.string.toolbar_title3), "", false)
            onBack {
                finish()
            }
        }
        mAdapter= ZdydAdapter(this@HomeTwoInfoActivity,object : ItemClickListener {
            override fun <T> onClcik(data: T) {
            }
        })
        bt.setOnClickListener {
            if(!mRequestSuccess){
                showMessage(R.string.heihei_tv66)
                return@setOnClickListener
            }
            startActivityForResult(Intent(this,HomeTwoEditActivity::class.java).apply {
                putExtra("applyId",mAppliId)
                putExtra("title",getString(R.string.toolbar_title3))
            },102)
        }
        refresh_layout.setOnRefreshListener {
            if(mRequestSuccess){
                return@setOnRefreshListener
            }
            requestData()
        }
        getBaseViewModel<BaseViewModel>()?.setRequestCode(11107)
    }

    override fun onResume() {
        super.onResume()
        if(mHahsDataMap[4]==false){
            mHahsDataMap[4]=true
            requestData()
        }
    }

    private fun requestData(){
        refresh_layout.isRefreshing=true
        mVm=getViewModel()
        mVm?.getTwoDetails(mAppliId)
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
                listing_view.isEnabled=false
                mHttpRequest.startRentDate=mResponseData.dataSet?.houses?.beginRentDate.toString()
                mHttpRequest.payNode=mResponseData.dataSet?.houses?.payNode.toString()
                mHttpRequest.isHouseLoan=mResponseData.dataSet?.houses?.isHouseLoan.toString()
                mHttpRequest.startRentDate=mResponseData.dataSet?.houses?.beginRentDate?:""

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
                imgone_parent.getChildAt(i).visibility= View.VISIBLE
            }
            img_parent1_tv.text = getString(R.string.houses_tv8)
            img_parent2_tv.text = getString(R.string.houses_tv9)
            img_parent3_tv.text = getString(R.string.houses_tv10)
            img_parent4_tv.text = getString(R.string.houses_tv11)
            img_parent5_tv.text = getString(R.string.houses_tv12)
            img_parent6_tv.text = getString(R.string.houses_tv13)
            img_parent1.visibility= View.VISIBLE
            img_parent2.visibility= View.VISIBLE
            img_parent3.visibility= View.VISIBLE
            img_parent4.visibility= View.VISIBLE
            img_parent5.visibility= View.VISIBLE
            img_parent6.visibility= View.VISIBLE
            img_parent7.visibility= View.VISIBLE
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
                img_parent4_tv2.visibility= View.VISIBLE

                zddy_title.text = getString(R.string.hehe1)
                zddy_child1_tv.text = getString(R.string.hehe2)
                zddy_child2_tv.text = getString(R.string.hehe3)
                zddy_child4_tv.text = getString(R.string.hehe4)
                zddy_child4_tv1.visibility = View.VISIBLE
                zddy_child5_tv1.visibility = View.VISIBLE
                zddy_child5_tv.text = getString(R.string.hehe5)
                fyxx_parent.visibility= View.GONE
            } else {
                sjxq.visibility = View.VISIBLE
                zddy.visibility = View.GONE
                img_parent7_tv2.visibility = View.GONE
                img_parent4_tv2.visibility= View.VISIBLE

                img_parent7_tv.text = getString(R.string.houses_tv14)
                img_parent7_tv2.visibility= View.VISIBLE
                fyxx_parent.visibility= View.VISIBLE

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
                img_parent4_tv2.visibility= View.GONE
                fd.text = if(mHttpRequest.isHouseLoan=="1"){
                    getString(R.string.yes)
                }else{
                    getString(R.string.no)
                }
                fyxx_parent.visibility= View.VISIBLE
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

                fyxx_parent.visibility= View.GONE
            }

        }
        if(mRequest.lastType!=mRequest.type){
            zdyd_root.removeAllViews()
        }
        mRequest.lastType=mRequest.type
        //mAdapter?.refresh(mRequest.type,mAppliId)
        var index=0
        val size=mAdapter?.mList?.size?:0
        mAdapter?.mList?.forEach {
            if(zdyd_root.childCount<size){
                zdyd_root.addView(LayoutInflater.from(this).inflate(
                        if(mRequest.type==0){
                            R.layout.item_zdyd1
                        }else{
                            R.layout.item_zdyd2
                        },null,false))
            }
            zdyd_root.getChildAt(index).apply {
                refreshZdyd(this,index,mRequest.type)
            }
            index++
        }
        if (mRequest.type == 1) {
            img_parent8.visibility = View.VISIBLE
        } else {
            img_parent8.visibility = View.GONE
        }

        if(mRequest.type==1){
            img_parent8.visibility= View.VISIBLE
        }else{
            img_parent8.visibility= View.GONE
        }

        ////出售情况下户型图
        if(mRequest.type==1){
            for(i in 0 until mRequest.imgList_10.size){
                setFresco(mRequest.imgList_10[i],img8_parent.getChildAt(i) as SimpleDraweeView,false)
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "onActivity $requestCode $resultCode $mCameraBean")
        if(requestCode==102 && resultCode==1033){
            finish()
            return
        }
        if(resultCode==1012 && requestCode== DataConfig.ACTIVITY_REQUEST_CODE_CASA){
            data?.apply {
                mHttpRequest.housingStatus=getStringExtra("housingStatus")
                mHttpRequest.isCustomerServiceRelation=getStringExtra("isCustomerServiceRelation")
                //如果选择的状态是出租，那么拿取合同图片
                if(mHttpRequest.housingStatus=="0"){
                    mRequest.fwzpImg1=getStringExtra("img1")
                    mRequest.fwzpImg2=getStringExtra("img2")
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
                    "0"->{
                        val name=getStringExtra("name")
                        val phone=getStringExtra("phone")
                        if(name.isNotBlank()){
                            mHttpRequest.rentCustomerName=name
                        }
                        if(phone.isNotBlank()){
                            mHttpRequest.rentCustomerPhone=phone
                        }
                    }
                    "1"->{

                    }
                    "2"->{
                        if(ys!="-1"){
                            mHttpRequest.haveKey=ys
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
    private var mResponseData= UploadResponse()

    override fun <T> onNext(dat: T) {
        Log.e(TAG, "onnext $dat")
        ((dat as? BaseResponse<*>)?.data as? UploadResponse)?.apply {
            mResponseData=this

            mRequestSuccess=true
            mAppViewModel?.mHousesStatus?.value?.dataSet?.items?.forEach {
                mHousesStatus.add(it)
            }
            mAppViewModel?.mHousesType?.value?.dataSet?.items?.forEach {
                mHousesType.add(it)
            }
            val list=getApp()?.getAppViewModel()?.mHousesType?.value?.dataSet?.items?: arrayListOf()
            mPropertyTypeList=list as ArrayList<ListingConfigResponse.DataSetBean.ItemsBean>
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
            refresh_layout.isEnabled=false
            dataSet?.also {
                it.houses?.apply {
                    Log.e(TAG, "onnext $minHouseRent $houseRent")
                    listing_tv?.text = if(applicantType==0){
                        getString(R.string.houses_tv7)
                    }else{
                        "POA"
                    }
                    fwgl.setBlankText(phoneNumber)
                    zdzj.setBlankText("$minHouseRent")
                    qwzj.setBlankText("$houseRent")
                    sc.setBlankText(beginRentDate)
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
                    mHttpRequest.appointmentLookTime=appointmentLookTime?:""
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
                    yx.text = email.toString()
                    fb.text = facebook.toString()
                    tw.text = twitter.toString()
                    `is`.text = "$instagram"
                    dkxx.text = plotNumber
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
                    cqzs_ed.text = titleDeedNumber
                    cqbh.text = propertyNumber
                    dckfs.text = masterDevelpoerName
                    zdsj.text = "$minHouseRent"
                    qwsj.text = "$houseRent"
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
                mAdapter?.refresh(it.autoReplyList?: arrayListOf(),it.houses?.leaseType?:0)
                mHttpRequest.city=it.houses?.city?:""
                mHttpRequest.community=it.houses?.community?:""
                mHttpRequest.subCommunity=it.houses?.subCommunity?:""
                address.text ="${it.houses?.city}${it.houses?.community}${it.houses?.subCommunity}"
                address_tv.text = it.houses?.address.toString()
                initImg()
                setLeaseType(mResponseData.dataSet?.houses?.leaseType?:-1)
            }
            showMessage(dat.messageId)
            return
        }
        if (dat is CameraBean) {
            mCameraBean = dat
        }else if(dat is String && dat=="location"){
            mMap?.apply {
                mLocation=true
                setMyLocationEnabled(true)
                mMap.setOnMyLocationChangeListener {
                    Log.e(TAG,"onLocationChanged ${it.latitude}")
                }
            }
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

    private fun refreshZdyd(view: View, index: Int,type:Int) {
        view.apply {
            if(type==0){
                val data = mAdapter!!.mList[0]
                switch_button1.isChecked=data.isOpen == 1
                switch_button1.isEnabled=false
                qzrq1.text = data.beginRentDate
                title_tv.setStringRes(when(index){
                    1-> R.string.zdyd_tv2
                    2->R.string.zdyd_tv3
                    else-> R.string.zdyd_tv1
                })
                if(data.rentTime>0){
                    sc1.text = "${data.rentTime}${getString(R.string.heihei_tv17)}"
                }
                zfjd1.text =  when("${data.payNode}") {
                    "1" -> getString(R.string.paynode_type1)
                    "2" -> getString(R.string.paynode_type2)
                    "4" -> getString(R.string.paynode_type3)
                    "6" -> getString(R.string.paynode_type4)
                    "12" -> getString(R.string.paynode_type5)
                    else -> ""
                }
                zdsj1.isEnabled = data.isOpen == 1
                zdsj1.setText(data.houseRentPrice)
            }else{
                val data = mAdapter!!.mList[index]
                switch_button.isChecked=data.isOpen == 1
                switch_button.isEnabled=false
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
                price.isEnabled = false
                price.setText(data.houseRentPrice.toString())
            }
        }
    }
}