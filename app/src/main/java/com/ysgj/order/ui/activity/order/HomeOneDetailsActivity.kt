package com.ysgj.order.ui.activity.order

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import cn.qqtheme.framework.picker.DateTimePicker
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.DialogDataBean
import cn.yc.library.bean.TimeBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.request.RequestBean
import cn.yc.library.bean.response.*
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.BaseListener
import cn.yc.library.ui.activity.cancel.CancelReservationActivity
import cn.yc.library.ui.activity.tran.TransferOrderActivity
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.view.dialog.EditDialog
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.checkZh
import cn.yc.library.utils.showMessage
import cn.yc.library.utils.startPhoneDialog
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import kotlinx.android.synthetic.main.activity_deliverydetails.*
import java.net.InetAddress
import java.util.*


class HomeOneDetailsActivity : BaseActivity(), OnMapReadyCallback {

    private var mVm: OrderViewModel? = null

    private var mData = HomeOneDetailsBean.DataSetBean()
    private var mInitData = HomeOneDetailsBean.DataSetBean()

    private var mBean = DeliveryOrderListBean.DataSetBean()

    private var mAddressDialog: EditDialog? = null
    private var mSuccessMsg = ""

    private var mTime = ""
    private var mAddress = ""
    private var mType=-1

    private var mRequest=RequestBean()
    private var mSendLocation=false

    inner class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what==2){
                mDialog?.dismiss()
                Log.e(TAG,"hehe $mCheckNetwork")
                if(!mCheckNetwork){
                    showMessage(R.string.heihei_tv68)
                    return
                }
                val list= arrayListOf<String>().apply {
                    if(checkZh()){
                        add("您未安装google-play服务，无法进行下一步的操作")
                        add("您未安装google地图，无法进行下一步的操作")
                    }else{
                        add("You are not installing the google-play service and cannot proceed to the next step.")
                        add("You don't have google map installed, you can't do the next step")
                    }
                }
                val packageList=packageManager.getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
                when{
                    mData.applyId?.isNullOrBlank()==true->showMessage(getString(R.string.heihei_tv30))
                    GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@HomeOneDetailsActivity)!= ConnectionResult.SUCCESS->showMessage(list[0])
                    //packageList.none { it.packageName=="com.google.android.apps.maps" }->showMessage(list[1])
                    mData?.appointmentDoorTime?.isNullOrBlank()==true->showMessage(R.string.heihei_tv30)
                    else->{
                        setClick(false)
                        mLocation=false
                        bottom_bt3.isEnabled=false
                        mDialog?.show()
                        locationTask()
                    }
                }
            }else{
                //获取最后一次当前位置
                mPlaceDetectionClient?.getCurrentPlace(null)?.addOnCompleteListener {
                    Log.e(TAG,"dfgdgvdf data=$it successs=${it?.isSuccessful}")
                    if(it==null || !it.isSuccessful || it.result==null || it.result?.count?:0<=0){

                        mDialog?.dismiss()
                        showMessage(R.string.location_error)
                        setClick(true)
                        bottom_bt3.isEnabled = true
                        return@addOnCompleteListener
                    }
                    Log.e(TAG,"dfgdgvdf data=$it successs=${it?.isSuccessful}  result${it?.result} COUNT=${it?.result?.count}")
                    Log.e(TAG,"refreshLocation2 ${it.isSuccessful} ${it.result}")
                    (it.result as? PlaceLikelihoodBufferResponse)?.apply {
                        mDialog?.show()
                        mLocation=false
                        val data=get(0)
                        mDdRequest.location="${data?.place?.address?:""}"
                        mDdRequest.id=mBean.taskId.toString()
                        mVm?.requestLocation(mBean.taskId.toString(),"${data.place?.latLng?.longitude?:0.0}",
                                "${data.place?.latLng?.latitude?:0.0}",mDdRequest.location)
                    }
                }
            }
        }
    }
    private val mDefaultHandler=DefaultHandler()

    private var mId=""
    private var mLocation=true

    private var mMap: GoogleMap? = null

    private var mClient:GoogleApiClient?=null
    private var mPlaceDetectionClient: PlaceDetectionClient?=null
    private var mGeoDataClient: GeoDataClient?=null
    private var mFusedLocationProviderClient: FusedLocationProviderClient?=null
    private var mInit=false
    private var mTimeBean=TimeBean()

    private var mCheckNetwork=false

    override fun onMapReady(p0: GoogleMap?) {
        mMap=p0
        locationTask()
    }

    override val layoutId: Int
        get() = R.layout.activity_deliverydetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGeoDataClient=Places.getGeoDataClient(this,null)
        mPlaceDetectionClient=Places.getPlaceDetectionClient(this,null)
        mFusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        getBaseViewModel<BaseViewModel>()?.setRequestCode(11107)
        mClient= GoogleApiClient.Builder(this@HomeOneDetailsActivity)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks{
                    override fun onConnected(p0: Bundle?) {
                    }

                    override fun onConnectionSuspended(p0: Int) {

                    }
                })
                .addOnConnectionFailedListener {

                }
                .addApi(LocationServices.API)
                .build()
    }

    override fun onStart() {
        super.onStart()
        mClient?.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        mClient?.disconnect()
    }

    override fun onResume() {
        super.onResume()
        if(mInit){
            return
        }
        mInit=true
        mDialog = PromptDialog(this, build = PromptDialog.DialogBuild().apply {
            type = 1
        })

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        scroll.viewTreeObserver.addOnScrollChangedListener {
            if (refresh_layout != null) {
                refresh_layout.isEnabled = scroll.scrollY === 0
            }
        }
        mType=intent.getIntExtra("type",-1)
        if(mType==0){
            refresh_layout.isEnabled=false
        }
        mAddressDialog = EditDialog(this@HomeOneDetailsActivity,
                getString(R.string.update_address),EditDialog.EditDialogBuild().apply {
            title=R.string.update_address
        }).apply {
            setListener(object : BaseListener {
                override fun onError(code: Int, throwable: Throwable) {

                }

                override fun <T> onNext(dat: T) {
                    mDialog?.show()
                    mAddress = dat as String
                    request("",mAddress, "${mBean.poolId}")
                }
            })
        }

        toolbarInit {
            mToolbar = toolbar
            data = ToolbarBean(getString(R.string.toolbar_title_homeone), "", false)
            onBack {
                finish()
            }
        }
        bottom_bt2.setOnClickListener {
            if(!mRequest.request){
                showMessage(getString(R.string.heihei_tv30))
                return@setOnClickListener
            }
            if(mTimeBean.startTimeInt<=3200){
                showMessage(R.string.heihei_tv65)
                return@setOnClickListener
            }
            mData?.also {
                if(getBaseViewModel<BaseViewModel>()?.getRequestCode()==11106){
                    return@setOnClickListener
                }
                getBaseViewModel<BaseViewModel>()?.setRequestCode(11106)
                setClick(false)
                val intent = Intent(this@HomeOneDetailsActivity,
                        TransferOrderActivity::class.java).apply {
                    putExtra("applyId", "${mBean.applyId}")
                    putExtra("poolId", "${mBean.poolId}")
                    putExtra("taskId", "${mBean.taskId}")
                    putExtra("times", it.appointmentDoorTime + "")
                    putExtra("taskType", "0")
                    putExtra("type",mType)
                    putExtra("json",Gson().toJson(mBean))
                }
                startActivityForResult(intent,1033)
                setClick(true)
            }
        }
        bottom_bt1.setOnClickListener {
            if(!mRequest.request){
                showMessage(getString(R.string.heihei_tv30))
                return@setOnClickListener
            }

            mData?.also {
                it.taskId="${mBean.taskId}"
                setClick(false)
                val intent = Intent(this@HomeOneDetailsActivity,
                        CancelReservationActivity::class.java).apply {
                    val json = Gson().toJson(it)
                    putExtra("json", json)
                }
                startActivityForResult(intent,1033)
                setClick(true)
            }
        }
        bottom_bt3.setOnClickListener {
            /*mData?.also {
                Log.e(TAG,"haha ${mData.houseId}")
                val intent = Intent(this@HomeOneDetailsActivity,
                        HomeOneNextDetailsActivity::class.java).apply {
                    val json = Gson().toJson(it)
                    putExtra("json", json)
                    putExtra("houseId",mId)
                    putExtra("type",mType)
                }
                startActivityForResult(intent,1033)
            }
            return@setOnClickListener*/
            mDialog?.show()
            val t=object :Thread(){
                override fun run() {
                    mCheckNetwork=InetAddress.getByName("www.google.com").isReachable(6000)
                    mDefaultHandler.sendEmptyMessage(2)
                }
            }
            t.start()
        }

        refresh_layout?.setOnRefreshListener {
            if(mType==1){
                refresh()
            }
        }
        val json = intent.getStringExtra("json")
        val bean = Gson().fromJson(json, DeliveryOrderListBean.DataSetBean::class.java)
        Log.e(TAG, "onCreate $bean  $json")
        bean?.apply {
            mBean = bean
            mData.applyId = applyId?.toString()
            mData.poolId = poolId?.toString()
            mData.taskId = taskId?.toString()
            mId="$houseId"
        }
        phone_bt.setOnClickListener {
            startPhoneDialog(DialogDataBean().apply {
                mData.apply {
                    title = getString(R.string.prompt)
                    content = "${getString(R.string.bddh)} $memberName\n$memberMoble"
                    phone = "$memberMoble"
                }
            })
        }
        update_bt1.setOnClickListener {
            mSuccessMsg = getString(R.string.update_time_success)
            val picker = DateTimePicker(this, DateTimePicker.HOUR_24)
            val calendar = Calendar.getInstance()
//获取系统的日期
//年
            val year = calendar.get(Calendar.YEAR)
//月
            val month = calendar.get(Calendar.MONTH) + 1
//日
            val day = calendar.get(Calendar.DAY_OF_MONTH)
//获取系统时间
//小时
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
//分钟
            val minute = calendar.get(Calendar.MINUTE)
//秒
            val second = calendar.get(Calendar.SECOND)
            picker.setDateRangeStart(year, month, day)
            //picker.setDateRangeEnd(2025, 11, 11)
            picker.setTimeRangeStart(hour, minute)
            //picker.setTimeRangeEnd(20, 30)
            picker.setTopLineColor(-0x66010000)
            picker.setLabelTextColor(-0x10000)
            picker.setDividerColor(-0x10000)
            picker.setOffset(3)
            picker.setTitleTextColor(Color.parseColor("#000000"))
            picker.setLabelTextColor(Color.parseColor("#00b660"))
            picker.setDividerColor(Color.WHITE)
            picker.setSubmitTextColor(Color.parseColor("#000000"))
            picker.setCancelTextColor(Color.parseColor("#000000"))
            picker.setTextColor(Color.parseColor("#000000"))
            picker.setOnDateTimePickListener(DateTimePicker.OnYearMonthDayTimePickListener {
                year, month, day, hour, minute ->
                mDialog?.show()
                val value="$year-$month-$day $hour:$minute:$second"
                mAddress = value
                mAddress=address_tv.text.toString()
                request(value, "","${mBean.poolId}")
            })
            picker.show()
        }
        update_bt2.setOnClickListener {
            mSuccessMsg = getString(R.string.update_address_success)
            mAddressDialog?.show()
        }
        if(mType==1){
            refresh()
        }else{
            mRequest.request=true
            mData.appointmentDoorTime=mBean.estimatedTime
            mData.applyId=mBean.applyId.toString()
            mData.taskId=mBean.taskId.toString()
            mData.poolId=mBean.poolId.toString()
            mData.appointmentMeetPlace=mBean.appointmentMeetPlace
            mData.languageVersion=mBean.languageVersion
            mData.leaseType=mBean.leaseType
            mData.memberMoble=mBean.memberMoble
            mData.memberName=mBean.memberName.toString()
            onLoad()
        }
    }

    private fun onLoad() {
        mData.apply {
            if (leaseType == 0) {
                deliveryorder_img.text = getString(R.string.rent)
                fy_type.text = getString(R.string.cz)
                deliveryorder_img.setBackgroundResource(R.drawable.round2)
            } else {
                fy_type.text = getString(R.string.cs)
                deliveryorder_img.text = getString(R.string.buy)
                deliveryorder_img.setBackgroundResource(R.drawable.round1)
            }
            time.text = appointmentDoorTime
            address_tv.text = appointmentMeetPlace
            name.text = this.memberName?.toString() ?: ""
            getBaseViewModel<BaseViewModel>()?.getCurrentTimebean()?.value?.apply {
                get("estimatedTimeCountDown")?.addOtherView(endTime)
                get("closeTimeCountDown")?.also {
                    mTimeBean=it
                    it.addOtherView(startTime)
                }
            }
            language.text = if (languageVersion == 0) {
                getString(R.string.language_ch)
            } else {
                getString(R.string.language_en)
            }

        }
    }

    private fun request(time: String, address: String, id: String) {
        mVm = mVm ?: getViewModel()
        mVm?.update(time, address, id)
    }

    private fun refresh() {
        mRequest.requestCode= DataConfig.REQUEST_LOAD
        mRequest.request=false
        mVm = mVm ?: getViewModel()
        mData?.apply {
            mVm?.getHomeOneDetails(taskId.toString(), applyId.toString(), poolId.toString())
            return
        }
        refresh_layout.isRefreshing = false
    }

    @SuppressLint("MissingPermission", "RestrictedApi")
    private fun refreshLocation(){
        Log.e(TAG,"refreshLocation1 $mLocation $mFusedLocationProviderClient")
        mMap?.isMyLocationEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = true
        if(mLocation){
            mDialog?.dismiss()
            return
        }
        mDialog?.show()
        val thread=object :Thread(){
            override fun run() {
                val lastLocation=LocationServices.FusedLocationApi.getLastLocation(mClient)
                val list= Geocoder(this@HomeOneDetailsActivity,Locale.getDefault()).getFromLocation(lastLocation.latitude,lastLocation.longitude, 1)
                if(list.isNotEmpty()){
                    list.forEach {
                        Log.e(TAG,"地址 ${it.toString()}")
                    }
                    val sb = StringBuilder()
                    for (i in 0..list[0].maxAddressLineIndex) {
                        val line = list[0].getAddressLine(i)
                        if (line == null) {
                            sb.append("null")
                        } else {
                            sb.append(line)
                        }
                    }
                    mLocation=false
                    mDdRequest.location=sb.toString()
                    mDdRequest.id=mBean.taskId.toString()
                    mVm?.requestLocation(mBean.taskId.toString(),"${list[0].longitude?:0.0}",
                            "${list[0].latitude?:0.0}",mDdRequest.location)
                }else{
                    mDefaultHandler.sendEmptyMessage(3)
                }
            }
        }
        thread.start()
    }

    override fun onError(code: Int, throwable: Throwable) {
        Log.e(TAG,"onError ${throwable.message}")
        if(mRequest.requestCode==DataConfig.REQUEST_LOAD){
            mRequest.request=false
        }
        mLocation=false
        setClick(true)
        bottom_bt3.isEnabled = true
        mDialog?.dismiss()
        refresh_layout.isRefreshing = false
        showMessage(throwable.message)
    }

    private var mDdRequest = LocationRequest()

    @SuppressLint("MissingPermission")
    override fun <T> onNext(dat: T) {
        refresh_layout.isRefreshing = false
        Log.e(TAG,"onnext $dat")
        if(dat is String && dat=="location"){
            refreshLocation()
            return
        }
        (dat as? BaseResponse<*>)?.apply{

            (data as? HomeOneDetailsBean)?.dataSet?.apply {
                mRequest.request=true
                mData = this
                onLoad()
                return
            }
            mDialog?.dismiss()
            (data as? UpdateResponse)?.apply {
                showMessage(mSuccessMsg)
                if (mSuccessMsg == getString(R.string.update_time_success)) {
                    time.text = mTime
                } else {
                    address_tv.text = mAddress
                }
                mAddressDialog?.dismiss()
                refresh()
                return
            }
            (data as? LoginResponse)?.apply {
                if(result==0){
                    mData?.also {
                        Log.e(TAG,"haha ${mData.houseId}")
                        val intent = Intent(this@HomeOneDetailsActivity,
                                HomeOneNextDetailsActivity::class.java).apply {
                            val json = Gson().toJson(it)
                            putExtra("json", json)
                            putExtra("houseId",mId)
                            putExtra("type",mType)
                        }
                        startActivityForResult(intent,1033)
                    }
                    setClick(true)
                }else{
                    showMessage(message)
                    setClick(true)
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1033 && resultCode==1033){
            getBaseViewModel<LoginViewModel>()?.setRequestCode(1001)
            finish()
            return
        }
    }

    class LocationRequest(var id:String,var longitude:String,var latitude:String,var location:String){

        constructor():this("","","","")

        override fun toString(): String {
            return "LocationRequest(id='$id', longitude='$longitude', latitude='$latitude', location='$location')"
        }
    }

    private fun setClick(boolean: Boolean){
        bottom_bt1.isEnabled=boolean
        bottom_bt2.isEnabled=boolean
        bottom_bt3.isEnabled=boolean
    }
}