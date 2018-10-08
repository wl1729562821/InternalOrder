package com.ysgj.order.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent
import cn.qqtheme.framework.picker.SinglePicker
import cn.yc.library.bean.AdapterBean
import cn.yc.library.bean.CameraBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.ListingConfigResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.vm.AppViewModel
import cn.yc.library.utils.*
import com.facebook.drawee.view.SimpleDraweeView
import com.ysgj.order.R
import com.ysgj.order.R.id.*
import com.ysgj.order.base.BaseActivity
import kotlinx.android.synthetic.main.activity_casa.*

class CasaActivity : BaseActivity() {

    private var mIndex = "0"
    private var mValue = ""

    private var mImgIndex = 0

    private val mWeekList = arrayListOf<String>().apply {
        add("Sun")
        add("Mon")
        add("Tue")
        add("Wed")
        add("Thur")
        add("Fri")
        add("Sat")
    }
    private var mCheckMap = hashMapOf<Int, Boolean>()

    private var mCameraBean = CameraBean()
    private var mDateIndex=0

    private var mImgUri = ""
    private var mImgUri2 = ""
    private var mImgUri3 = ""
    private var mImgUri4 = ""

    private var mHeaderIndex=0

    private var mAllData= hashMapOf<Int,Boolean>().apply {
        put(0,false)
        put(1,false)
        put(2,false)
        put(3,false)
        put(4,false)
        put(5,false)
        put(6,false)
    }

    private var mData= hashMapOf<String,HashMap<String,Boolean>>().apply {
        mWeekList.forEach {
            put(it,hashMapOf<String,Boolean>().apply {
                put("9:00",false)
                put("10:00",false)
                put("11:00",false)
                put("12:00",false)
                put("13:00",false)
                put("14:00",false)
                put("15:00",false)
                put("16:00",false)
                put("17:00",false)
                put("18:00",false)
                put("19:00",false)
                put("20:00",false)
            })
        }
    }

    private val mHousesStatus= arrayListOf<ListingConfigResponse.DataSetBean.ItemsBean>()
    private var mAppViewModel: AppViewModel?=null
    fun setHeaderHidd() {
        mList = getList()
        initMap()
        refreshUI()
    }

    private fun init() {
        mList = getList()
        initMap()
        refreshUI()
    }

    private var mList = arrayListOf<AdapterBean>()

    private var isCustomerServiceRelation=""

    private var mSelectedData= arrayListOf<Boolean>().apply {
        add(false)
        add(false)
        add(false)
        add(false)
        add(false)
        add(false)
        add(false)
    }
    private var mDateList1=ArrayList<AdapterBean.AdapterData>().apply {
        add(AdapterBean.AdapterData().apply {
            title = "9:00"
            color = Color.parseColor("#3c3c3c")
            checkHeader = true
        })
        add(AdapterBean.AdapterData().apply {
            checkHeader = true
            title = "10:00"
            color = Color.parseColor("#3c3c3c")
        })
        add(AdapterBean.AdapterData().apply {
            title = "11:00"
            checkHeader = true
            color = Color.parseColor("#3c3c3c")
        })
        add(AdapterBean.AdapterData().apply {
            title = "12:00"
            color = Color.parseColor("#3c3c3c")
        })
        add(AdapterBean.AdapterData().apply {
            title = "13:00"
            color = Color.parseColor("#3c3c3c")
        })
        add(AdapterBean.AdapterData().apply {
            title = "14:00"
            color = Color.parseColor("#3c3c3c")
        })
        add(AdapterBean.AdapterData().apply {
            title = "15:00"
            color = Color.parseColor("#3c3c3c")
        })
        add(AdapterBean.AdapterData().apply {
            title = "16:00"
            color = Color.parseColor("#3c3c3c")
        })
        add(AdapterBean.AdapterData().apply {
            title = "17:00"
            color = Color.parseColor("#3c3c3c")
        })
        add(AdapterBean.AdapterData().apply {
            title = "18:00"
            color = Color.parseColor("#3c3c3c")

        })
        add(AdapterBean.AdapterData().apply {
            title = "19:00"
            color = Color.parseColor("#3c3c3c")
        })
        add(AdapterBean.AdapterData().apply {
            title = "20:00"
            color = Color.parseColor("#3c3c3c")
        })
    }

    private fun getList(): ArrayList<AdapterBean> {
        mDateIndex = 0

        return arrayListOf<AdapterBean>().apply {
            add(AdapterBean(0))
            add(AdapterBean(1, false))
            add(AdapterBean(2))
        }
    }

    private fun initMap() {
        mCheckMap.clear()
        mCheckMap.apply {
            put(0, true)
            put(1, false)
            put(2, false)
            put(3, false)
            put(4, false)
            put(5, false)
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_casa

    private fun refreshUI() {
        xz.setOnClickListener {
            showPicker<String>(arrayListOf<String>().apply {
                mHousesStatus?.filter { it.itemValue?.isNotBlank()==true }.forEach {
                    add(it.itemValue.toString())
                }
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                mIndex=mHousesStatus[index].id.toString()
                mValue = mHousesStatus[index].itemValue.toString()
                if (index == 0) {
                    init()
                } else {
                    setHeaderHidd()
                }
            })
        }
        xz.text = mValue
        when(mIndex){
            "20074"->{
                header.visibility = View.GONE
                header1.visibility = View.VISIBLE
            }
            "20075"->{//出租状态
                header.visibility = View.VISIBLE
                header1.visibility = View.GONE
                img_parent.visibility=View.VISIBLE
            }
            "20076"->{
                header.visibility = View.GONE
                header1.visibility = View.GONE
            }
        }
        header1_check_partent.setOnClickListener {
            mCheckMap[4] = true
            header1_check.setBackgroundResource(R.mipmap.check_selected)
            mCheckMap[5] = false
            header1_check1.setBackgroundResource(R.drawable.check_bg)
        }
        header1_check1_parent.setOnClickListener {
            mCheckMap[5] = true
            mCheckMap[4] = false
            header1_check1.setBackgroundResource(R.mipmap.check_selected)
            header1_check.setBackgroundResource(R.drawable.check_bg)
        }
        header_check_parent.setOnClickListener {
            if (mCheckMap[0] == false) {
                phone.isEnabled = true
                name.isEnabled = true
                mCheckMap[1] = false
                header_check1.setBackgroundResource(R.drawable.check_bg)
                mCheckMap[0] = true
                header_check.setBackgroundResource(R.mipmap.check_selected)
            }
        }
        header_check1_parent.setOnClickListener {
            if (mCheckMap[1] == false) {
                phone.isEnabled = false
                name.isEnabled = false
                mCheckMap[0] = false
                header_check.setBackgroundResource(R.drawable.check_bg)
                mCheckMap[1] = true
                header_check1.setBackgroundResource(R.mipmap.check_selected)
            }
        }
        footer_check_img_parent.setOnClickListener {
            if (mCheckMap[2] == false) {
                isCustomerServiceRelation="0"
                mAllData[mHeaderIndex]=true
                footer_check_img.setBackgroundResource(R.mipmap.check_selected)
                mDateList1.forEach {
                    mData[mWeekList[mHeaderIndex]]?.put(it.title,true)
                }
                mViewList.forEach {tv->
                    tv.setTextColor(Color.parseColor("#FFFFFF"))
                    tv.setBackgroundResource(R.drawable.casa_bg_selected)
                }
                mCheckMap[3] = false
                footer_check_img1.setBackgroundResource(R.drawable.check_bg)
            }
        }
        footer_check_img1_parent.setOnClickListener {
            if (mCheckMap[3] == false) {
                isCustomerServiceRelation="1"
                mCheckMap[2]=true
                mDateList1.forEach {
                    mData[mWeekList[mHeaderIndex]]?.put(it.title,false)
                }
                mAllData[mHeaderIndex]=false
                mViewList.forEach {tv->
                    tv.setTextColor(Color.parseColor("#3c3c3c"))
                    tv.setBackgroundResource(R.drawable.casa_bg2)
                }
                footer_check_img1.setBackgroundResource(R.mipmap.check_selected)
                mCheckMap[3] = true
            }
        }
        refreshHeader()
    }

    private fun refreshHeader(){
        (0..6).forEach {
            (header_tv_parent[it] as TextView)?.apply {
                setTextColor(if(mHeaderIndex==it){
                    Color.parseColor("#00b66a")
                }else{
                    Color.parseColor("#3c3c3c")
                })
                setOnClickListener {_->
                    mHeaderIndex=it
                    refreshHeader()
                }
            }
        }
        Log.e(TAG,"refreshHeader $mData")
        Log.e(TAG,"refreshHeader $mAllData")
        (0 until mViewList.size).forEach {index->
            val tv=mViewList[index]
            tv.text=mDateList1[index].title
            if(mData[mWeekList[mHeaderIndex]]?.get(mDateList1[index].title)==true){
                tv.setTextColor(Color.parseColor("#FFFFFF"))
                tv.setBackgroundResource(R.drawable.casa_bg_selected)
            }else{
                tv.setTextColor(Color.parseColor("#3c3c3c"))
                tv.setBackgroundResource(R.drawable.casa_bg2)
            }
            tv.setOnClickListener {
                if (isCustomerServiceRelation=="1"){
                    return@setOnClickListener
                }
                val selected=mData[mWeekList[mHeaderIndex]]?.get(mDateList1[index].title)?:false
                mData[mWeekList[mHeaderIndex]]?.put(mDateList1[index].title,!selected)
                if(!selected){
                    tv.setTextColor(Color.parseColor("#FFFFFF"))
                    tv.setBackgroundResource(R.drawable.casa_bg_selected)
                }else{
                    tv.setTextColor(Color.parseColor("#3c3c3c"))
                    tv.setBackgroundResource(R.drawable.casa_bg2)
                }
                if( mData[mWeekList[mHeaderIndex]]?.none { !it.value }==true){
                    mAllData[mHeaderIndex]=true
                    footer_check_img.setBackgroundResource(R.mipmap.check_selected)
                    footer_check_img1.setBackgroundResource(R.drawable.check_bg)
                }else{
                    mAllData[mHeaderIndex]=false
                    footer_check_img1.setBackgroundResource(R.mipmap.check_selected)
                    footer_check_img.setBackgroundResource(R.drawable.check_bg)
                }
            }
        }
        mAllData[mHeaderIndex]= mData[mWeekList[mHeaderIndex]]?.none { !it.value }==true
        if(mAllData[mHeaderIndex]==true){
            footer_check_img.setBackgroundResource(R.mipmap.check_selected)
            footer_check_img1.setBackgroundResource(R.drawable.check_bg)
        }else{
            footer_check_img1.setBackgroundResource(if(isCustomerServiceRelation=="1"){
                R.mipmap.check_selected
            }else{
                R.drawable.check_bg
            })
            footer_check_img.setBackgroundResource(R.drawable.check_bg)
        }
    }

    private val mViewList= arrayListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAppViewModel=getApp()?.getAppViewModel()
        mAppViewModel?.mHousesStatus?.value?.dataSet?.items?.forEach {
            mHousesStatus.add(it)
        }
        for (i in 0 until parenthehe.childCount) {
            val childParent = parenthehe.getChildAt(i) as LinearLayout
            for (x in 0 until childParent.childCount) {
                (childParent.getChildAt(x) as? TextView)?.apply {
                    mViewList.add(this)
                }
            }
        }
        toolbarInit {
            mToolbar = toolbar
            data = ToolbarBean(getString(R.string.casa_tv1), "", false).apply {
            }
            onBack {
                finish()
            }
        }
        var n = intent.getStringExtra("name")
        var p = intent.getStringExtra("phone")
        name.setBlankText(n)
        phone.setBlankText(p)
        mImgUri = intent.getStringExtra("img1")
        mImgUri2 = intent.getStringExtra("img2")
        mImgUri3 = intent.getStringExtra("img3")
        mImgUri4 = intent.getStringExtra("img4")
        setFresco(mImgUri, img_parent.getChildAt(0) as SimpleDraweeView)
        setFresco(mImgUri2, img_parent.getChildAt(1) as SimpleDraweeView)
        setFresco(mImgUri3, img_parent.getChildAt(2) as SimpleDraweeView)
        setFresco(mImgUri4, img_parent.getChildAt(3) as SimpleDraweeView)
        val haveKey=intent.getStringExtra("haveKey")
        if(haveKey=="0"){
            mCheckMap[5] = true
            header1_check1.setBackgroundResource(R.mipmap.check_selected)
            mCheckMap[4] = false
            header1_check.setBackgroundResource(R.drawable.check_bg)
        }else if(haveKey=="1"){
            mCheckMap[4] = true
            header1_check.setBackgroundResource(R.mipmap.check_selected)
            mCheckMap[5] = false
            header1_check1.setBackgroundResource(R.drawable.check_bg)
        }
        val onClick: () -> Unit = {
            showPicker(arrayListOf<String>().apply {
                add(getString(R.string.pz))
                add(getString(R.string.xc))
                add(getString(R.string.heihei_tv19))
            }, SinglePicker.OnItemPickListener<String> { index, item ->
                if (index == 1) {
                    RxGalleryFinalApi
                            .openRadioSelectImage(this, object : RxBusResultDisposable<ImageRadioResultEvent>() {
                                override fun onEvent(t: ImageRadioResultEvent?) {
                                    val view = img_parent.getChildAt(mImgIndex) as SimpleDraweeView
                                    when (mImgIndex) {
                                        0 -> {
                                            mImgUri = t?.result?.originalPath ?: ""
                                        }
                                        1 -> {
                                            mImgUri2 = t?.result?.originalPath ?: ""
                                        }
                                        2 -> {
                                            mImgUri3 = t?.result?.originalPath ?: ""
                                        }
                                        3 -> {
                                            mImgUri4 = t?.result?.originalPath ?: ""
                                        }
                                    }
                                    setFresco(t?.result?.originalPath ?: "", view)
                                }
                            }, true)
                } else if(index==0){
                    camearPermissions()
                }else{
                    val view = img_parent.getChildAt(mImgIndex) as SimpleDraweeView
                    view.setActualImageResource(R.mipmap.upload_default)
                    when (mImgIndex) {
                        0 -> {
                            mImgUri =  ""
                        }
                        1 -> {
                            mImgUri2 =""
                        }
                        2 -> {
                            mImgUri3 = ""
                        }
                        3 -> {
                            mImgUri4 = ""
                        }
                    }

                }
            })
        }

        form_img1.setOnClickListener {
            mImgIndex = 0
            onClick()
        }

        form_img2.setOnClickListener {
            mImgIndex = 1
            onClick()
        }
        for (i in 0 until img_parent.childCount) {
            img_parent.getChildAt(i).setOnClickListener {
                mImgIndex = i
                mSelectedData[i]=true
                onClick()
            }
        }
        isCustomerServiceRelation=intent.getStringExtra("isCustomerServiceRelation")

        bt.setOnClickListener {
            if(mHousesStatus.none { it.id.toString()==mIndex }){
                showMessage(R.string.edit_error22)
                return@setOnClickListener
            }
            var n = name.editableText.toString()
            var p = phone.editableText.toString()
            val list= arrayListOf<String>().apply {
                if(checkZh()){
                    add("租客姓名不能为空")
                    add("租客电话不能为空")
                    add("租客电话格式错误")
                }else{
                    add("Tenant name cannot be empty")
                    add("Tenant phone cannot be empty")
                    add("Tenant phone format error")
                }
            }
            if(mIndex=="20075" && mCheckMap[0]==true){
                val regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-9]))\\d{8}$".toRegex()
                when{
                    n.isNullOrBlank()->{
                        showMessage(list[0])
                        return@setOnClickListener
                    }
                    p.isNullOrBlank()->{
                        showMessage(list[1])
                        return@setOnClickListener
                    }
                    !p.matches(regex)->{
                        showMessage(list[2])
                        return@setOnClickListener
                    }
                }
            }else{
                n=""
                p=""
            }
            setResult(1012, Intent().apply {
                Log.e(TAG,"mIndex: $mIndex")
                putExtra("housingStatus", mIndex)
                if (mIndex == "20075") { //出租状态
                    putExtra("img1", mImgUri)
                    putExtra("img2", mImgUri2)
                    putExtra("img3", mImgUri3)
                    putExtra("img4", mImgUri4)
                }
                putExtra("value", mValue)
                putExtra("name", n)
                putExtra("phone", p)
                putExtra("advanceReservationDay", tq.editableText.toString())
                val ys = when (mIndex) {
                    "20074" -> {//空房状态下
                        if (mCheckMap[4] == true) {//原意交钥匙的话给平台，说明钥匙在业务员
                            "1"
                        } else {
                            "0"
                        }
                    }
                    else -> "-1"
                }
                putExtra("ys", ys)
                var appointmentLookTime = "${mWeekList[mDateIndex]} "
                when {
                    mAllData.none { !it.value } -> //如果全是true说明全选
                        appointmentLookTime="Sun;Mon;Tue;Wed;Thur;Fri;Sat"
                    else -> {
                        appointmentLookTime=""
                        mWeekList.forEach {key->
                            mDateList1.forEach {
                                //如果全是false
                                if(mData[key]?.none { it.value }==true){

                                }else{
                                    //不包含的话才给他加上
                                    if(appointmentLookTime.isNullOrBlank()){
                                        appointmentLookTime+=key
                                    }else{
                                        if(!appointmentLookTime.contains(key)){
                                            appointmentLookTime=appointmentLookTime.substring(0,appointmentLookTime.length-1)
                                            appointmentLookTime+=";$key"
                                        }
                                    }
                                    if(mData[key]?.get(it.title)==true){
                                        appointmentLookTime+=" ${it.title},"
                                    }
                                }
                            }
                        }
                    }
                }

                Log.e(TAG, "点击 $appointmentLookTime")
                putExtra("isCustomerServiceRelation", isCustomerServiceRelation)
                putExtra("appointmentLookTime", appointmentLookTime)
            })
            finish()
        }

        initMap()
        mList = getList()

        val housingStatus = intent.getStringExtra("housingStatus")
        mHousesStatus.forEach {
            if(housingStatus==it.id.toString()){
                mValue=it.itemValue.toString()
                mIndex=it.id.toString()
            }
        }
        if(mIndex=="0"){
            mIndex="20075"
            mHousesStatus.filter { it.id.toString()==mIndex }.forEach {
                mValue=it.itemValue?:""
            }
        }
        if(mIndex=="20075"){
            init()
        }else{
            setHeaderHidd()
        }
        var time=intent.getStringExtra("appointmentLookTime")
        if(time.isNotBlank() && time.substring(time.length-1,time.length)==";"){
            time=time.substring(0,time.length-1).replace(" ","")
        }
        Log.e(TAG,"呵呵1 $time")
        val list=time.split1Empty(";")
        list.forEach {
            Log.e(TAG,"time all $it")
            when{
                it.contains("Sun")->{
                    val value=it.replace("Sun","")
                    Log.e(TAG,"time sun $value")
                    if(value.split(",").none { it.isNotBlank() }){
                        mDateList1.forEach {
                            mData["Sun"]?.put(it.title,true)
                        }
                    }else{
                        value.split(",").filter { it.isNotBlank() }.forEach {
                            mData["Sun"]?.put(it.trim(),true)
                            Log.e(TAG,"time sun ${it.trim()}")
                        }
                    }

                }
                it.contains("Mon")->{
                    val value=it.replace("Mon","")
                    Log.e(TAG,"time mon $value")
                    if(value.split(",").none { it.isNotBlank() }){
                        mDateList1.forEach {
                            mData["Mon"]?.put(it.title,true)
                        }
                    }else{
                        value.split(",").filter { it.isNotBlank() }.forEach {
                            mData["Mon"]?.put(it.trim(),true)
                            Log.e(TAG,"time mon ${it.trim()}")
                        }
                    }
                }
                it.contains("Tue")->{
                    val value=it.replace("Tue","")
                    Log.e(TAG,"time tue $value")
                    if(value.split(",").none { it.isNotBlank() }){
                        mDateList1.forEach {
                            mData["Tue"]?.put(it.title,true)
                        }
                    }else{
                        value.split(",").filter { it.isNotBlank() }.forEach {
                            mData["Tue"]?.put(it.trim(),true)
                            Log.e(TAG,"time tue ${it.trim()}")
                        }
                    }
                }
                it.contains("Wed")->{
                    val value=it.replace("Wed","")
                    Log.e(TAG,"time wed $value")
                    if(value.split(",").none { it.isNotBlank() }){
                        mDateList1.forEach {
                            mData["Wed"]?.put(it.title,true)
                        }
                    }else{
                        value.split(",").filter { it.isNotBlank() }.forEach {
                            mData["Wed"]?.put(it.trim(),true)
                            Log.e(TAG,"time wed ${it.trim()}")
                        }
                    }
                }
                it.contains("Thur")->{
                    val value=it.replace("Thur","")
                    Log.e(TAG,"time thur $value")
                    if(value.split(",").none { it.isNotBlank() }){
                        mDateList1.forEach {
                            mData["Thur"]?.put(it.title,true)
                        }
                    }else{
                        value.split(",").filter { it.isNotBlank() }.forEach {
                            mData["Thur"]?.put(it.trim(),true)
                            Log.e(TAG,"thur ${it.trim()}")
                        }
                    }
                }
                it.contains("Fri")->{
                    val value=it.replace("Fri","")
                    Log.e(TAG,"time fri $value")
                    if(value.split(",").none { it.isNotBlank() }){
                        mDateList1.forEach {
                            mData["Fri"]?.put(it.title,true)
                        }
                    }else{
                        value.split(",").filter { it.isNotBlank() }.forEach {
                            mData["Fri"]?.put(it.trim(),true)
                            Log.e(TAG,"time fri ${it.trim()}")
                        }
                    }
                }
                it.contains("Sat")->{
                    val value=it.replace("Sat","")
                    Log.e(TAG,"time sat $value")
                    if(value.split(",").none { it.isNotBlank() }){
                        mDateList1.forEach {
                            mData["Sat"]?.put(it.title,true)
                        }
                    }else{
                        value.split(",").filter { it.isNotBlank() }.forEach {
                            mData["Sat"]?.put(it.trim(),true)
                            Log.e(TAG,"time sat ${it.trim()}")
                        }
                    }
                }
            }
        }
        Log.e(TAG,"isCustomerServiceRelation=$isCustomerServiceRelation")
        //如果是客服联系
        if(isCustomerServiceRelation=="1"){
            mCheckMap[2]=false
            mCheckMap[3] =true
            footer_check_img.setBackgroundResource(R.drawable.check_bg)
            footer_check_img1.setBackgroundResource(R.mipmap.check_selected)
        }else{
            //mCheckMap[2]=true
            mCheckMap[2]=false
            mCheckMap[3] =false
            footer_check_img.setBackgroundResource(R.drawable.check_bg)
            footer_check_img1.setBackgroundResource(R.drawable.check_bg)
            //footer_check_img1.setBackgroundResource(R.mipmap.check_selected)
        }
        refreshUI()
    }

    private fun setSelector(selector: Boolean) {
        for (i in 0 until mDateList1.size) {
            mDateList1[i].selected=selector
        }
        (0 until mSelectedData.size).forEach {
            mSelectedData[it]=selector
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DataConfig.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            val view = img_parent.getChildAt(mImgIndex) as SimpleDraweeView
            when (mImgIndex) {
                0 -> {
                    mImgUri = mCameraBean.imgPath
                }
                1 -> {
                    mImgUri2 = mCameraBean.imgPath
                }
                2 -> {
                    mImgUri3 = mCameraBean.imgPath
                }
                3 -> {
                    mImgUri4 = mCameraBean.imgPath
                }
            }
            setFresco(mCameraBean.imgPath, view)
        }
    }

    override fun <T> onNext(dat: T) {
        if (dat is CameraBean) {
            mCameraBean = dat
        }
    }
}