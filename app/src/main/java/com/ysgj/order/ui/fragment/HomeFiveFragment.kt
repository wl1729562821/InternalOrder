package com.ysgj.order.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.UserBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.PerformanceResponse
import cn.yc.library.bean.response.UserInfoResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.DialogListener
import cn.yc.library.service.cookie.PersistentCookieStore
import cn.yc.library.ui.activity.AttendanceActivity
import cn.yc.library.ui.activity.InviteActivity
import cn.yc.library.ui.activity.SettlementListActivity
import cn.yc.library.ui.activity.qrcode.KeyActivity
import cn.yc.library.ui.activity.user.LoginActivity
import cn.yc.library.ui.activity.user.UpdatePhoneActivity
import cn.yc.library.ui.activity.user.UpdatePwdActivity
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.getApp
import cn.yc.library.utils.getLogin
import cn.yc.library.utils.saveLogin
import cn.yc.library.utils.showMessage
import cn.yc.library.vm.LoginViewModel
import com.umeng.message.PushAgent
import com.ysgj.order.R
import com.ysgj.order.base.BaseFragment
import com.ysgj.order.ui.activity.HomeActivity
import kotlinx.android.synthetic.main.fragment_home_five.view.*

class HomeFiveFragment:BaseFragment(){

    private var mLogin=true

    private var mVm: LoginViewModel?=null
    private var mData= UserInfoResponse.DataSetBean()
    private var mData1= PerformanceResponse.DataSetBean()

    inner class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what==101){
                activity?.application?.also {
                    PushAgent.getInstance(it)?.apply {
                        addAlias(msg?.obj?.toString()?:"123","outOf",{boolean,value->
                            Log.e(TAG,"addAlias ${msg?.obj} $boolean $value")
                        })
                    }
                }
            }
        }
    }
    private val mDefaultHandler=DefaultHandler()

    override val layoutId: Int
        get() =R.layout.fragment_home_five

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarInit {
            mToolbar=view?.toolbar
            data= ToolbarBean(getString(R.string.home_atv_tv5),"",false).apply {
                back=false
            }
        }
        getBaseViewModel<LoginViewModel>()?.getRequestModule()?.observeForever {
            if(it?.requestCode==1101){
                request()
            }
        }
        view?.home_five_updatepwd?.setOnClickListener {
            startActivityForResult(Intent(activity, UpdatePwdActivity::class.java).apply {
                putExtra("phone",mData.mobile)
            },12)
        }
        view?.home_five_invite?.setOnClickListener {
            startActivity(Intent(activity, InviteActivity::class.java))
        }
        view?.home_five_updatephone?.setOnClickListener {
            startActivityForResult(Intent(activity, UpdatePhoneActivity::class.java),10)
        }
        view?.home_five_child1_img?.setOnClickListener {
            startActivity(Intent(activity,SettlementListActivity::class.java))
        }
        view?.home_five_child2_img?.setOnClickListener {
            startActivity(Intent(activity, AttendanceActivity::class.java))
        }
        view?.home_five_child3_img?.setOnClickListener {
            startActivity(Intent(activity, KeyActivity::class.java))
        }
        view?.exit?.setOnClickListener {
            activity?.application?.saveLogin(UserBean("",""))
            PromptDialog(activity!!,build = PromptDialog.DialogBuild().apply {
                content=getString(R.string.heihei_tv24)
                title=getString(R.string.prompt)
                mListener=object : DialogListener {
                    override fun dismiss() {
                        PersistentCookieStore(activity).removeAll()
                        (activity as? HomeActivity)?.apply {
                            PushAgent.getInstance(activity!!.application)?.apply {
                                deleteAlias(mData.mobile?:"123","outOf",{boolean,value->
                                    Log.e(TAG,"delAlias ${mData.mobile} $boolean $value")
                                })
                            }
                            clearData()
                            startActivityForResult(Intent(activity, LoginActivity::class.java), 151)
                        }
                    }
                }
            }).show()
        }
        view?.refresh_layout?.setOnRefreshListener {
            if(mLogin){
                request()
            }else{
                login()
            }
        }
        if(mLogin){
            request()
        }else{
            login()
        }
    }

    override fun onError(code: Int, throwable: Throwable) {
        view?.refresh_layout?.isRefreshing=false
        showMessage(throwable.message)
    }

    override fun <T> onNext(dat: T) {
        (dat as BaseResponse<*>)?.apply {
            (data as? UserInfoResponse)?.run {
                dataSet?.get(0)?:UserInfoResponse.DataSetBean()
            }?.apply {
                mData=this
                getPerformance()
                return
            }
            (data as? PerformanceResponse)?.dataSet?.also {
                view?.refresh_layout?.isRefreshing=false
                mData.apply {
                    activity?.getApp()?.getAppViewModel()?.mPhone=mobile?:""
                    PushAgent.getInstance(activity!!.application)?.apply {
                        deleteAlias(mobile?:"123","outOf",{boolean,value->
                            Log.e(TAG,"delAlias $mobile $boolean $value")
                        })
                        mDefaultHandler.sendMessageDelayed(Message().apply {
                            what=101
                            obj=mobile
                        },2000)

                    }

                    view?.home_five_name?.text=username
                    view?.jf_tv?.text="$gold ${getString(R.string.heihei_tv25)}"
                    view?.home_five_location?.text="${getString(R.string.heihei_tv26)}: $community"
                    view?.home_five_phone?.text=mobile

                    view?.home_five_avatar?.also {
                        it.setImageURI(this.userLogo)
                    }
                }
                view?.child_1?.text="${it.arriveCustomerNum}${getString(R.string.heihei_tv27)}"
                view?.child_2?.text="${it.uploadHouseNum}${getString(R.string.heihei_tv27)}"
                view?.child_3?.text="${it.uploadSuccessNum}${getString(R.string.heihei_tv27)}"
                view?.child_4?.text="${it.transferOrderNum}${getString(R.string.heihei_tv27)}"
                mData1=it
                return
            }
        }
        view?.refresh_layout?.isRefreshing=false
    }

    private fun request(){
        mData1=PerformanceResponse.DataSetBean()
        view?.refresh_layout?.isRefreshing=true
        mVm=mVm?:getViewModel()
        mVm?.getUserInfo()
    }

    private fun getPerformance(){
        mVm=mVm?:getViewModel()
        mVm?.getPerformance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10 && resultCode==10){
            mLogin=false
            login()
        }else if(requestCode==12 && resultCode==12){//设置密码需要重新登录
            mLogin=false
            login()
        }
    }

    private fun login(){
        activity?.application?.getLogin()?.also {
            mVm?.login {
                setParameter(it.name,it.password)
                onError {
                    mLogin=false
                }
                onNext {
                    mLogin=true
                    request()
                }
            }
        }
    }
}