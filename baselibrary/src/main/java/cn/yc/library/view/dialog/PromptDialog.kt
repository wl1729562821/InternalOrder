package cn.yc.library.ui.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import cn.yc.library.R
import cn.yc.library.listener.DialogListener
import cn.yc.library.utils.setStringRes
import kotlinx.android.synthetic.main.dialog_load.view.*
import kotlinx.android.synthetic.main.dialog_prompt.view.*



class PromptDialog(val mCtx:Context,val build:DialogBuild= DialogBuild()):Dialog(mCtx,build.style){

    private var mType=0

    constructor(context: Context,type:Int):this(context){
        mType=type
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = View.inflate(context,build.layout, null)
        setContentView(view)
        Log.e("Activity","show1 ${build.type}")
        if(build.type==1){
            Log.e("Activity","show1 $this")
            view.lv_circularring?.apply {
                Log.e("Activity","show $this")
                //setViewColor(Color.argb(160, 0, 0, 0))
                setBarColor(Color.WHITE)
                startAnim(1000)
            }
            if(build.load>0){
                view.load_tv.setStringRes(build.load)
            }
        }else{
            if(build.title!="Tips"){
                view.title?.text=build.title
            }
            view.content?.text=build.content
            view.ok?.setStringRes(build.bt)
            view?.ok?.setOnClickListener {
                dismiss()
            }
        }

        setCanceledOnTouchOutside(false)

        val win = window
        val lp:WindowManager.LayoutParams= win.attributes
        val width=if(build.type==0){
            (mCtx.resources.displayMetrics.widthPixels*0.67).toInt()
        }else{
            (mCtx.resources.displayMetrics.widthPixels*0.65).toInt()
        }
        lp.width =width
        win.attributes = lp
    }

    class DialogBuild{

        var mListener: DialogListener?=null
        var load= R.string.load
        var title="Tips"
        var bt=R.string.app_tv32
        var content="The password is successfully modified, and you will be automatically redirected to the login page."
        var type=0
        set(value) {
            if(value==0){
                style=R.style.Dialog
                layout=R.layout.dialog_prompt
            }else{
                style=R.style.LoadDialog
                layout=R.layout.dialog_load
            }
            field=value
        }

        var style=R.style.Dialog

        var layout=R.layout.dialog_prompt
    }

    override fun dismiss() {
        super.dismiss()
        build.mListener?.dismiss()
    }

    override fun show() {
        super.show()
        build.mListener?.show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.e("Dialog","onkeyDown")
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}