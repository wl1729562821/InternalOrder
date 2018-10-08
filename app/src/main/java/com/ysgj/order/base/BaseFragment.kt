package com.ysgj.order.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.DialogDataBean
import cn.yc.library.listener.BaseListener
import cn.yc.library.listener.DataListener
import cn.yc.library.listener.DialogListener
import cn.yc.library.ui.view.dialog.PromptDialog
import cn.yc.library.utils.getApp
import cn.yc.library.view.DragFloatActionButton
import com.ysgj.order.R
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseFragment:android.support.v4.app.Fragment(), BaseListener, DataListener {

    companion object {
        private const val RC_SMS_PERM = 122
    }

    protected val TAG=javaClass.simpleName

    private var mImage:ImageView?=null

    protected var mDialog: PromptDialog?=null

    protected abstract val layoutId:Int

    protected inline fun <reified T: AndroidViewModel> getViewModel():T?{
        val vm= if(activity==null){
            null
        }else{
            ViewModelProvider.AndroidViewModelFactory(activity!!.application).create(T::class.java)
        }
        if(vm!=null && vm is BaseViewModel){
            vm.setListener(this)
        }
        return vm
    }
    protected inline fun <reified T: AndroidViewModel> getBaseViewModel():T?{
        val vm= if(activity==null){
            null
        }else{
            activity?.getApp()?.getViewModelProvider()?.get(T::class.java)
        }
        if(vm!=null && vm is BaseViewModel){
            vm.setListener(this)
        }
        return vm
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(layoutId,null,false)?:View(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<DragFloatActionButton>(R.id.dial_phone)?.apply {
            setOnClickListener {
                (activity as? BaseActivity)?.smsTask(DialogDataBean().apply {
                    phone = "971 4 565 6182"
                    title = getString(R.string.app_tv3)
                    content = "971 4 565 6182"
                })
            }
            val view=view?.findViewById<SwipeRefreshLayout>(R.id.refresh_layout)
            setListener(object : DialogListener {
                override fun dismiss() {
                    view?.isEnabled=false
                }

                override fun show() {
                    view?.isEnabled=true
                }
            })
        }
    }

    override fun onError(code: Int, throwable: Throwable) {

    }

    override fun <T> onNext(dat: T) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun clearData() {

    }

    override fun refreshData() {
        requestData()
    }

}