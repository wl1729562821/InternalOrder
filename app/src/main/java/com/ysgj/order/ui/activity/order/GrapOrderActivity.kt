package com.ysgj.order.ui.activity.order

import android.os.Bundle
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.DialogDataBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.bean.response.HomeThreeResponse
import cn.yc.library.bean.response.TransferOrderBean
import cn.yc.library.data.toolbarInit
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.view.dialog.PhoneDialog
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.vm.LoginViewModel
import cn.yc.library.vm.OrderViewModel
import com.google.gson.Gson
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import kotlinx.android.synthetic.main.activity_graporder.*

class GrapOrderActivity:BaseActivity(){

    private var mData: HomeThreeResponse.DataSetBean?=null
    private var mViewModel: OrderViewModel?=null


    override val layoutId: Int
        get() =R.layout.activity_graporder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialog= PromptDialog(this,PromptDialog.DialogBuild().apply {
            type=1
        })
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.heihei_tv31),"",false)
            onBack {
                finish()
            }
        }
        val json=intent.getStringExtra("json")
        mData=Gson().fromJson(json,HomeThreeResponse.DataSetBean::class.java)
        mData?.apply {
            name.text=this.nickname
            language.text=if(languageVersion==0){
                getString(R.string.language_ch)
            }else{
                getString(R.string.language_en)
            }
            if(leaseType==0){
                deliveryorder_img.text=getString(R.string.rent)
                fy_type.text=getString(R.string.cz)
                deliveryorder_img.setBackgroundResource(R.drawable.round2)
            }else{
                fy_type.text=getString(R.string.cs)
                deliveryorder_img.text=""
                deliveryorder_img.setBackgroundResource(R.mipmap.sell)
            }
            phone_bt.setOnClickListener {
                PhoneDialog(this@GrapOrderActivity, DialogDataBean().also {
                    it.content="${getString(R.string.bddh)}${this.nickname}\n${this.memberMoble}"
                    it.phone="$memberMoble"
                }).show()
            }
            time.text=estimatedTime
            address_tv.text=appointmentMeetPlace
            getBaseViewModel<BaseViewModel>()?.getCurrentTimebean()?.value?.apply {
                get("three_estimatedTimeCountDown")?.addOtherView(endTime)
                get("three_closeTimeCountDown")?.addOtherView(startTime)
            }
            bt.setOnClickListener {
                if(mData==null){
                    return@setOnClickListener
                }
                mDialog?.show()
                request("${mData!!.poolId}")
            }
        }

        getBaseViewModel<BaseViewModel>()?.setRequestCode(11107)
    }

    override fun onError(code: Int, throwable: Throwable) {
        mDialog?.dismiss()
        PromptDialog(this,build = PromptDialog.DialogBuild().apply {
            bt=R.string.bt_qd
            content=getString(R.string.heihei_tv32)
        }).show()
    }

    override fun <T> onNext(dat: T) {
        mDialog?.dismiss()
        ((dat as? BaseResponse<*>)?.data as? TransferOrderBean)?.apply {
            mDialog?.dismiss()
            PromptDialog(this@GrapOrderActivity,build = PromptDialog.DialogBuild().apply {
                content=getString(R.string.heihei_tv33)
                bt=R.string.bt_qd
                mListener=object : DialogListener {
                    override fun dismiss() {
                        //刷新派单表界面和抢单界面
                        getBaseViewModel<LoginViewModel>()?.setRequestCode(152,10010)
                        finish()
                    }
                }
            }).show()
            return
        }
    }

    private fun request(id:String){
        mViewModel=mViewModel?:getViewModel()
        mViewModel?.grabOrder(id)
    }

}