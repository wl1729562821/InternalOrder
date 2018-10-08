package cn.yc.library.base

import android.Manifest
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import cn.qqtheme.framework.entity.Province
import cn.qqtheme.framework.picker.AddressPicker
import cn.qqtheme.framework.picker.DatePicker
import cn.qqtheme.framework.picker.DateTimePicker
import cn.qqtheme.framework.picker.SinglePicker
import cn.yc.library.R
import cn.yc.library.bean.CameraBean
import cn.yc.library.bean.DialogDataBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.listener.BaseListener
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.config.DataConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.getApp
import cn.yc.library.utils.showMessage
import cn.yc.library.utils.startPhoneDialog
import cn.yc.library.view.DragFloatActionButton
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.zhy.autolayout.AutoLayoutActivity
import io.reactivex.annotations.NonNull
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*


abstract class BaseActivity : AutoLayoutActivity(), BaseListener {

    companion object {
        private const val RC_SMS_PERM = 122
        private const val RC_SMS_LOCATION = 125
        private const val RC_CAMERA= 123
        private const val RC_READ=124
        private const val RC_READ_PHONE=126
        private const val RC_CAMERA_NEXT = 129
    }

    protected val TAG = javaClass.simpleName

    protected abstract val layoutId: Int

    protected var mDialog: PromptDialog? = null
    private var mRefresh:SwipeRefreshLayout?=null

    private var mActivityLoadDialog:PromptDialog?=null

    inline fun <reified T : AndroidViewModel> getBaseViewModel(): T?{
        return getApp()?.getViewModelProvider()?.get(T::class.java)?.apply {
            if (this is BaseViewModel) {
                setVMListener(this)
            }
        }
    }
    inline fun <reified T : AndroidViewModel> getViewModel(): T?{
        return ViewModelProvider.AndroidViewModelFactory(application).create(T::class.java).apply {
            if (this is BaseViewModel) {
                setVMListener(this)
            }
        }
    }

    fun setVMListener(vm:BaseViewModel){
        vm.setListener(object :BaseListener{
            override fun onError(code: Int, throwable: Throwable) {
                mRefresh?.isRefreshing=false
                this@BaseActivity.onError(code,throwable)
            }

            override fun <T> onNext(dat: T) {
                this@BaseActivity.onNext(dat)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setContentView(layoutId)
        mRefresh=findViewById(R.id.refresh_layout)
        findViewById<View>(R.id.dial_phone)?.apply {
            (this as? DragFloatActionButton)?.apply {
                setOnClickListener {
                    smsTask(DialogDataBean().apply {
                        phone = "971 4 565 6182"
                        title = getString(R.string.kfdh)
                        content = "971 4 565 6182"
                    })
                }
                setListener(object : DialogListener {
                    override fun dismiss() {
                        mRefresh?.isEnabled=false
                    }

                    override fun show() {
                        mRefresh?.isEnabled=true
                    }
                })
            }
        }

        getBaseViewModel<BaseViewModel>()?.getRequestModule()?.observeForever {
            if(it?.requestCode==11106){
                mEnable=true
            }else if(it?.requestCode==11107){
                mEnable=false
            }
        }
    }

    override fun onError(code: Int, throwable: Throwable) {

    }

    override fun <T> onNext(dat: T) {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    fun smsTask(bean: DialogDataBean){
        smsTask(BaseResponse<DialogDataBean>().apply {
            data=bean
        })
    }

    private var mData: DialogDataBean? = null
    @AfterPermissionGranted(RC_SMS_PERM)
    fun smsTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE)) {
            // Have permission, do the thing!
            startPhoneDialog(mData?.phone?:"")
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, "请求打电话权限",
                    RC_SMS_PERM, Manifest.permission.CALL_PHONE)
        }
    }

    @AfterPermissionGranted(RC_READ_PHONE)
    fun phoneTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
            // Have permission, do the thing!
            onNext(mMsg)
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, "请求电话权限",
                    RC_READ_PHONE, Manifest.permission.READ_PHONE_STATE)
        }
    }

    private var mMsg=""
    fun phoneTask(msg:String){
        mMsg=msg
        phoneTask()
    }

    @AfterPermissionGranted(RC_SMS_LOCATION)
    fun locationTask() {
        Log.e(TAG,"locationTask ${EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)}")
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Have permission, do the thing!
            onNext("location")
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, "请求定位权限",
                    RC_SMS_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun  smsTask(data: BaseResponse<DialogDataBean>){
        mData = data.data
        smsTask()
    }

    @AfterPermissionGranted(RC_CAMERA_NEXT)
    fun camearPermissionsNext() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            onNext("camear")
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, "照相权限",
                    RC_CAMERA_NEXT, Manifest.permission.CAMERA)
        }
    }

    @AfterPermissionGranted(RC_READ)
    fun readPermissions() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!
            openCamear()
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, "照相权限",
                    RC_READ, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    @AfterPermissionGranted(RC_CAMERA)
    fun camearPermissions() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            readPermissions()
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, "照相权限",
                    RC_CAMERA, Manifest.permission.CAMERA)
        }
    }

    fun onPermissionsGranted(requestCode: Int, @NonNull perms: List<String>) {
        when (requestCode) {
            RC_SMS_PERM -> mData?.apply {
                startPhoneDialog(this)
            }
            RC_CAMERA -> readPermissions()
            RC_READ -> {
                openCamear()
            }
            RC_CAMERA_NEXT->onNext("camear")
            RC_SMS_LOCATION->{
                onNext("location")
            }
            RC_READ_PHONE->{
                onNext(mMsg)
            }
        }
    }

    private var mSinglePicker:SinglePicker<*>?=null
    fun <T> showPicker(list: List<T>, listener: SinglePicker.OnItemPickListener<T>) {
        if(mSinglePicker?.isShowing==true){
            return
        }
        mSinglePicker=SinglePicker<T>(this, list).apply {
            setCanceledOnTouchOutside(false)
            //selectedIndex = 1
            setCycleDisable(true)
            setOnItemPickListener(listener)
            //setTopLineColor(-0x66010000)
            //setLabelTextColor(-0x10000)
            //setDividerColor(Color.WHITE)
            setOffset(3)
            setSubmitTextSize(20)
            setTextSize(20)
            setTitleTextColor(Color.parseColor("#000000"))
            setLabelTextColor(Color.parseColor("#00b660"))
            setDividerColor(Color.WHITE)
            setSubmitTextColor(Color.parseColor("#000000"))
            setCancelTextColor(Color.parseColor("#000000"))
            setTextColor(Color.parseColor("#000000"))
        }
        mSinglePicker?.show()
    }
    private var mDateTimePicker:DateTimePicker?=null
    fun showTimePicker(listener: DateTimePicker.OnYearMonthDayTimePickListener){
        if(mDateTimePicker?.isShowing ==true){
            return
        }
        mDateTimePicker =mDateTimePicker?: DateTimePicker(this, DateTimePicker.HOUR_24).apply {
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
            setDateRangeStart(year, month, day)
            //setDateRangeEnd(2025, 11, 11)
            setTimeRangeStart(hour, minute)
            //setTimeRangeEnd(20, 30)
            setTopLineColor(-0x66010000)
            setLabelTextColor(-0x10000)
            setDividerColor(-0x10000)
            setOffset(3)
            setTitleTextColor(Color.parseColor("#000000"))
            setLabelTextColor(Color.parseColor("#00b660"))
            setDividerColor(Color.WHITE)
            setSubmitTextColor(Color.parseColor("#000000"))
            setCancelTextColor(Color.parseColor("#000000"))
            setTextColor(Color.parseColor("#000000"))
        }
        mDateTimePicker?.setOnDateTimePickListener(listener)
        mDateTimePicker?.show()
    }
    private var mYearMonthDayPicker:DateTimePicker?=null
    fun showYearMonthDayPicker(listener: DateTimePicker.OnYearMonthDayTimePickListener){
        if(mYearMonthDayPicker?.isShowing ==true){
            return
        }
        mYearMonthDayPicker =mYearMonthDayPicker?: DatePicker(this, DateTimePicker.YEAR_MONTH_DAY).apply {
            val calendar = Calendar.getInstance()
//获取系统的日期
//年
            val year = calendar.get(Calendar.YEAR)
//月
            val month = calendar.get(Calendar.MONTH) + 1
//日
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            setDateRangeStart(year, month, day)
            //setDateRangeEnd(2025, 11, 11)
            //setTimeRangeStart(hour, minute)
            //setTimeRangeEnd(20, 30)
            setTopLineColor(-0x66010000)
            setLabelTextColor(-0x10000)
            setDividerColor(-0x10000)
            setOffset(3)
            setTitleTextColor(Color.parseColor("#000000"))
            setLabelTextColor(Color.parseColor("#00b660"))
            setDividerColor(Color.WHITE)
            setSubmitTextColor(Color.parseColor("#000000"))
            setCancelTextColor(Color.parseColor("#000000"))
            setTextColor(Color.parseColor("#000000"))
        }
        mYearMonthDayPicker?.setOnDateTimePickListener(listener)
        mYearMonthDayPicker?.show()
    }

    private var mPickerAddress:OptionsPickerView<*>?=null
    fun showPickerAddress(listener:OnOptionsSelectListener) {
        if(mPickerAddress?.isShowing==true){
            return
        }
        mPickerAddress =mPickerAddress?:OptionsPickerBuilder(this,listener)
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#f5f5f5"))
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.parseColor("#000000"))
                .setSubmitColor(Color.parseColor("#000000"))
                .setTextColorCenter(Color.parseColor("#00b660"))
                .setDividerColor(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                //.setLabels("省", "市", "区")
                .setBackgroundId(0x00000000) //设置外部遮罩颜色
                .build<Any>()
        (application as? App)?.apply {
            //mPickerAddress?.setPicker(options1Items, options2Items, options3Items)
            //mPickerAddress?.show()
        }
    }

    private var mAddressPicker:AddressPicker?=null
    fun showCityPicker(list:ArrayList<Province>,listener:AddressPicker.OnAddressPickListener){
        if(mAddressPicker?.isShowing==true){
            return
        }
        mAddressPicker=AddressPicker(this,list).apply {
            setTextSize(20)
            setDividerColor(Color.LTGRAY)
            setCanceledOnTouchOutside(false)
            //selectedIndex = 1
            setCycleDisable(true)
            setOffset(3)
            setSubmitTextSize(20)
            setTextSize(20)
            setTitleTextColor(Color.parseColor("#000000"))
            setLabelTextColor(Color.parseColor("#00b660"))
            setDividerColor(Color.WHITE)
            setSubmitTextColor(Color.parseColor("#000000"))
            setCancelTextColor(Color.parseColor("#000000"))
            setTextColor(Color.parseColor("#000000"))
            setOnAddressPickListener(listener)
        }
        mAddressPicker?.show()
    }
    fun showPickerOptions(list:ArrayList<String>,childList:ArrayList<ArrayList<String>>,listener:OnOptionsSelectListener) {
        val picker = OptionsPickerBuilder(this,listener)
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#f5f5f5"))
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.parseColor("#000000"))
                .setSubmitColor(Color.parseColor("#000000"))
                .setTextColorCenter(Color.parseColor("#00b660"))
                .setDividerColor(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                //.setLabels("省", "市", "区")
                .setBackgroundId(0x00000000) //设置外部遮罩颜色
                .build<Any>()
        (application as? App)?.apply {
            picker.setPicker(list as List<Any>?, childList as List<MutableList<Any>>?)
            picker.show()
        }
    }

    fun openCamear() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        val currentapiVersion = Build.VERSION.SDK_INT
        val bean = CameraBean()
        bean.requestResultCode = DataConfig.REQUEST_CAMERA
        val outputImage = File(Environment.getExternalStorageDirectory().absolutePath
                + "/test/" + System.currentTimeMillis() + ".jpg")
        bean.imgPath = outputImage.absolutePath
        outputImage.parentFile.mkdirs()
        if (currentapiVersion < 24) {
            val imageUri = Uri.fromFile(outputImage)
            bean.imgUri = imageUri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        } else {
            val contentValues = ContentValues(1)
            contentValues.put(MediaStore.Images.Media.DATA, outputImage.absolutePath)
            //检查是否有存储权限，以免崩溃
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                showMessage(R.string.app_tv2)
                return
            }
            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            bean.imgUri = imageUri
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, bean.requestResultCode)
        onNext(bean)
    }

    private var mEnable=false
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(mEnable){
            return true
        }
        return super.dispatchTouchEvent(ev)
    }
}