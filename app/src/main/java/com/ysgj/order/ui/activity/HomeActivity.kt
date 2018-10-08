package com.ysgj.order.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.CityResponse
import cn.yc.library.bean.response.ListingConfigResponse
import cn.yc.library.service.cookie.PersistentCookieStore
import cn.yc.library.ui.activity.messages.MessagesActivity
import cn.yc.library.ui.activity.user.LoginActivity
import cn.yc.library.ui.config.ActivityConfig
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.getApp
import cn.yc.library.utils.getLogin
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.base.BaseFragment
import com.ysgj.order.ui.fragment.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity:BaseActivity(){

    private var mViewModel: OrderViewModel?=null

    private var mType=0
    private var mIndex=-1

    private var mRequest=false

    private val mVm by lazy(LazyThreadSafetyMode.NONE){
        getViewModel<OrderViewModel>()
    }

    private val mFragmentList= arrayListOf<BaseFragment>().apply {
        add(HomeOneFragment())
        add(HomeTwoFragment())
        add(HomeThreeFragment())
        add(HomeFourFragment())
        add(HomeFiveFragment())
    }

    override val layoutId: Int
        get() =R.layout.activity_home

    fun getRequest():Boolean{
        return mRequest
    }

    fun setRequest(request:Boolean){
        mViewModel?.setRequest(request)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG,"mvm $mVm")
        PersistentCookieStore(this).removeAll()
        mViewModel=mViewModel?:getViewModel()
        mDialog= PromptDialog(this,PromptDialog.DialogBuild().apply {
            type=1
            load=R.string.initConfig
        })
        mViewModel?.getRequest()?.observeForever {
            mRequest=it?:false
            mType=0
            Log.e(TAG,"onChanged $it $mRequest")
            if(it==true){
                initView()
                mDialog?.show()
                mViewModel?.getCity()
            }
        }
        if(application.getLogin()?.name?.isNotEmpty()==true && application.getLogin()?.password?.isNotEmpty()==true){
            mDialog?.show()
            mViewModel?.apply {
                login {
                    setParameter(application.getLogin()?.name?:"",application.getLogin()?.password?:"")
                    onError {
                        mDialog?.dismiss()
                        Log.e("Activity","请求失败")
                        mViewModel?.setRequest(false)
                        startActivity()
                    }

                    onNext {
                        getApp()?.getAppViewModel()?.mPhone=application.getLogin()?.name?:""
                        Log.e("Activity","请求成功")
                        mViewModel?.setRequest(true)
                    }
                }
            }
        }else{
            mViewModel?.setRequest(false)
            startActivity()
        }
    }

    private var mInit=false

    private fun initView(){
        if(!mInit){
            showFragment(0)
            home_one?.setOnClickListener {
                if(!getRequest()){
                    mIndex=0
                    setRequest(true)
                    return@setOnClickListener
                }
                showFragment(0)
            }
            home_three?.setOnClickListener {
                if(!getRequest()){
                    mIndex=2
                    setRequest(true)
                    return@setOnClickListener
                }
                showFragment(2)
            }
            home_two?.setOnClickListener {
                if(!getRequest()){
                    mIndex=1
                    setRequest(true)
                    return@setOnClickListener
                }
                showFragment(1)
            }
            home_four?.setOnClickListener {
                if(!getRequest()){
                    mIndex=3
                    setRequest(true)
                    return@setOnClickListener
                }
                showFragment(3)
            }
            home_five?.setOnClickListener {
                if(!getRequest()){
                    mIndex=4
                    setRequest(true)
                    return@setOnClickListener
                }
                showFragment(4)
            }
            mInit=true
        }
    }

    //获取派单表数据
    private fun getOutGainDispatchOrder(){
        //getViewModel<OrderViewModel>()?.sendMessage(0)
        getBaseViewModel<LoginViewModel>()?.setRequestCode(1001)
    }

    private fun showFragment(index:Int){
        home_one_img.setBackgroundResource(R.mipmap.home_icon_one)
        home_two_img.setBackgroundResource(R.mipmap.home_icon_two)
        home_five_img.setBackgroundResource(R.mipmap.home_icon_five)
        home_four_img.setBackgroundResource(R.mipmap.home_icon_four)
        home_three_img.setBackgroundResource(R.mipmap.home_icon_three)
        home_one_tv.setTextColor(Color.parseColor("#b9b9b9"))
        home_two_tv.setTextColor(Color.parseColor("#b9b9b9"))
        home_five_tv.setTextColor(Color.parseColor("#b9b9b9"))
        home_four_tv.setTextColor(Color.parseColor("#b9b9b9"))
        home_three_tv.setTextColor(Color.parseColor("#b9b9b9"))
        when(index){
            0->{
                home_one_img.setBackgroundResource(R.mipmap.home_icon_one_selected)
                home_one_tv.setTextColor(Color.parseColor("#00b660"))
            }
            1->{
                home_two_img.setBackgroundResource(R.mipmap.home_icon_two_selected)
                home_two_tv.setTextColor(Color.parseColor("#00b660"))
            }
            2->{
                home_three_img.setBackgroundResource(R.mipmap.home_icon_three_selected)
                home_three_tv.setTextColor(Color.parseColor("#00b660"))
            }
            3->{
                home_four_img.setBackgroundResource(R.mipmap.home_icon_four_selected)
                home_four_tv.setTextColor(Color.parseColor("#00b660"))
            }
            4->{
                home_five_img.setBackgroundResource(R.mipmap.home_icon_five_selected)
                home_five_tv.setTextColor(Color.parseColor("#00b660"))
            }
        }
        val tran=supportFragmentManager.beginTransaction()
        mFragmentList.forEach {
            if(it.isAdded){
                tran.hide(it)
            }
        }
        if(mFragmentList.isNotEmpty() && !mFragmentList[index].isAdded){
            tran.add(R.id.home_fm,mFragmentList[index])
        }
        tran.show(mFragmentList[index]).commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1033 &&resultCode==1033){
            showFragment(1)
            return
        }
        if(requestCode==151 && resultCode== ActivityConfig.LOGIN_RESULT_CODE){
            mViewModel?.setRequest(true)
            getBaseViewModel<LoginViewModel>()?.apply {
                setRequestCode(1002,152,1001,1101)
            }
            return
        }
        if(requestCode==ActivityConfig.LOGIN_REQUEST_CODE ){
            if(resultCode==ActivityConfig.LOGIN_RESULT_CODE){
                mViewModel?.setRequest(true)
            }else{
                finish()
            }
        }
    }

    fun clearData(){
        mFragmentList.forEach {
            it.clearData()
        }
    }

    fun startActivity(){
        startActivityForResult(Intent(this@HomeActivity, LoginActivity::class.java),ActivityConfig.LOGIN_REQUEST_CODE)
    }

    override fun onError(code: Int, throwable: Throwable) {
        if(code==4){
            setRequest(false)
            showMessage(R.string.initConfig_error)
        }
        mDialog?.dismiss()
    }

    override fun <T> onNext(dat: T) {
        ((dat as? BaseResponse<*>)?.apply {
            (data as? CityResponse)?.apply {
                mViewModel?.getListingConfigResponse(mType.toString())
                val able = resources.configuration.locale.country
                if(able != "CN"){
                    val data=this
                    data?.dataSet?.forEach {
                        it.cityNameCn=it.cityNameEn
                        it.sub?.forEach {
                            it.cityNameCn=it.cityNameEn
                            it.sub?.forEach {
                                it.cityNameCn=it.cityNameEn
                            }
                        }
                    }
                }
                getApp()?.getAppViewModel()?.mCity?.value=this
                mType++
                return
            }
            (data as? ListingConfigResponse)?.apply {
                val able = resources.configuration.locale.country
                if(able != "CN"){
                    val data=this
                    data.dataSet?.items?.forEach {
                        it.itemValue=it.itemValueEn
                    }
                }
                when(mType-1){
                    0->{
                        getApp()?.getAppViewModel()?.mListingType?.value=this
                    }
                    1->getApp()?.getAppViewModel()?.mHousesType?.value=this
                    2->getApp()?.getAppViewModel()?.mHousesStatus?.value=this
                    3->getApp()?.getAppViewModel()?.mHousesMatching?.value=this
                }
                if(mType==4){
                    mDialog?.dismiss()
                    showMessage(R.string.initConfig_success)
                    if(mIndex!=-1){
                        showFragment(mIndex)
                    }
                    getOutGainDispatchOrder()
                    return
                }
                mViewModel?.getListingConfigResponse(mType.toString())
                mType++
            }
        })
        if(dat is String){
            if(dat=="phone"){
                startActivity(Intent(this, MessagesActivity::class.java))
            }
        }
    }

}