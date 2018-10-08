package cn.yc.library.ui.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationSet
import cn.yc.library.R
import cn.yc.library.bean.DialogDataBean
import cn.yc.library.utils.OptAnimationLoader
import kotlinx.android.synthetic.main.dialog_phone.view.*

class PhoneDialog(val mCtx:Context,val bean:DialogDataBean):Dialog(mCtx, R.style.Dialog){

    private var mType=0
    private var mView:View?=null

    private var mModalInAnim: AnimationSet?=null
    private var mDialogView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = View.inflate(context,R.layout.dialog_phone, null)
        setContentView(view)
        mView=view

        setCanceledOnTouchOutside(false)

        val win = window
        val lp:WindowManager.LayoutParams= win.attributes
        val width=(mCtx.resources.displayMetrics.widthPixels*0.67).toInt()
        lp.width =width
        win.attributes = lp

        view.value_tv?.text=bean.content
        view.dialog_title?.text=bean.title
        view.cancel_bt.setOnClickListener {
            dismiss()
        }
        view.ok_bt.setOnClickListener {
            bean.mListener?.onNext(bean.content)
        }

        mModalInAnim =mModalInAnim?: OptAnimationLoader.loadAnimation(context, R.anim.modal_in) as AnimationSet
        mDialogView=window?.decorView?.findViewById(android.R.id.content)
        mModalInAnim?.also {
            mDialogView?.startAnimation(it)
        }
    }

}