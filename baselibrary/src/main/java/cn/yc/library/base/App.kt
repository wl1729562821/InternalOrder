package cn.yc.library.base

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStore
import android.content.Context
import android.content.Intent
import android.support.multidex.MultiDexApplication
import android.util.Log
import android.widget.Toast
import cn.yc.library.R
import cn.yc.library.bean.JsonBean
import cn.yc.library.ui.activity.messages.MessagesActivity
import cn.yc.library.ui.vm.AppViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.Gson
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.entity.UMessage
import com.zhy.autolayout.config.AutoLayoutConifg
import org.json.JSONArray


open class App:MultiDexApplication(){

    private val mViewModelStore=ViewModelStore()
    private var mViewModelProvider:ViewModelProvider?=null

    fun getViewModelProvider()=mViewModelProvider

    private var mVm: AppViewModel?=null
    fun getAppViewModel():AppViewModel?=mVm

    override fun onCreate() {
        super.onCreate()
        mViewModelProvider=ViewModelProvider(mViewModelStore,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this))
        mVm= ViewModelProvider.AndroidViewModelFactory(this).create(AppViewModel::class.java)

        AutoLayoutConifg.getInstance().useDeviceSize().init(this)
        Fresco.initialize(this)

        UMConfigure.init(this,"5b923f75f43e4825720001a6","Umeng", UMConfigure.DEVICE_TYPE_PHONE,"e4f08b0f42687a147a90c84a1c8f5c63")
        UMConfigure.setLogEnabled(true)
        PushAgent.getInstance(this)?.apply {
            //通知免打扰模式
            //setNoDisturbMode(7,0,23,0)
            register(object : IUmengRegisterCallback {
                override fun onFailure(p0: String?, p1: String?) {
                    Log.e("App","onFailure $p0 $p1")
                }

                override fun onSuccess(p0: String?) {
                    Log.e("App","onSuccess $p0")
                }
            })
            setNotificationClickHandler { context, uMessage ->
                when(getAppViewModel()?.mMessages){
                    2->{
                        getAppViewModel()?.setRequestCode(1413)
                    }
                    1->{
                        Toast.makeText(this@App,getString(R.string.heihei_tv67), Toast.LENGTH_SHORT).show()
                    }
                    else->{
                        startActivity(Intent(this@App, MessagesActivity::class.java))
                    }
                }
                Log.e("App","handleMessage ${uMessage.custom} ${uMessage.alias} ${uMessage.text}")
            }
            messageHandler = object : UmengMessageHandler(){
                override fun dealWithNotificationMessage(p0: Context?, p1: UMessage?) {
                    //调用super则会走通知展示流程，不调用super则不展示通知
                    super.dealWithNotificationMessage(p0, p1)
                }
            }
        }
    }

    private fun parseData(result: String): ArrayList<JsonBean> {//Gson 解析
        val detail = ArrayList<JsonBean>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean::class.java)
                detail.add(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return detail
    }
}