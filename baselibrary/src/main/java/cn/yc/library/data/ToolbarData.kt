package cn.yc.library.data

import android.view.View
import cn.yc.library.R
import cn.yc.library.bean.ToolbarBean
import kotlinx.android.synthetic.main.toolbar.view.*

fun toolbarInit(toolbar:ToolbarData.()->Unit){
    ToolbarData().apply {
        toolbar()
        init()
    }
}

class ToolbarData {

    private var back: () -> Unit={}
    private var setting: () -> Unit={}

    var mToolbar:View?=null

    var data: ToolbarBean?=null

    fun onBack(onBack: () -> Unit) {
        back=onBack
    }

    fun onSetting(onSuccess: () -> Unit){
        setting=onSuccess
    }

    fun init(){
        mToolbar?.apply {
            data?.also {
                toolbar_title?.text=it.title
                val vis=if(it.backTitle.isNotEmpty()){
                    View.GONE
                }else{
                    View.VISIBLE
                }
                if(it.back){
                    toolbar_back_img?.visibility=vis
                    toolbar_back?.apply {
                        visibility=if(it.backTitle.isNotEmpty()){
                            View.VISIBLE
                        }else{
                            View.GONE
                        }
                        text=it.backTitle
                    }
                    back_bt.setOnClickListener {
                        back()
                    }
                }else{
                    toolbar_back_img?.visibility=View.GONE
                    toolbar_back?.visibility=View.GONE
                }
                toolbar_setting?.apply {
                    visibility=if(it.setting){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }
                    if(data?.settingsDrawable!=-1){
                        setBackgroundResource(data?.settingsDrawable?: R.mipmap.home_one_message)
                    }
                }
                if(it.setting || it.settingTitle.isNotBlank()){
                    setting_bt.setOnClickListener {
                        setting()
                    }
                }
                toolbar_setting_title?.apply {
                    visibility=if(it.settingTitle.isNotBlank()){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }
                    text=it.settingTitle
                }
            }
        }
    }

}