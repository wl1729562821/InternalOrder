package cn.yc.library.ui.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import cn.yc.library.R
import cn.yc.library.listener.BaseListener
import cn.yc.library.utils.setStringRes
import kotlinx.android.synthetic.main.dialog_edit.*
import kotlinx.android.synthetic.main.dialog_edit.view.*

class EditDialog(val mCtx:Context,val title:String,val data:EditDialogBuild):Dialog(mCtx, R.style.Dialog){
    private var mListener: BaseListener?=null

    fun setListener(listener:BaseListener){
        mListener=listener
    }

    private var mType=0
    private var mView:View?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = View.inflate(context,R.layout.dialog_edit, null)
        setContentView(view)
        mView=view
        view?.apply {
            title_tv?.setStringRes(data.title)
            ok_bt?.setStringRes(data.ok)
            cancel_bt?.setStringRes(data.cancel)
        }
        setCanceledOnTouchOutside(false)
        val win = window
        val lp:WindowManager.LayoutParams= win.attributes
        val width=(mCtx.resources.displayMetrics.widthPixels*0.67).toInt()
        lp.width =width
        win.attributes = lp
        view.title_tv?.text=title
        view?.cancel_bt?.setOnClickListener {
            dismiss()
        }
        view?.ok_bt?.setOnClickListener {
            if(edit?.editableText?.isNotEmpty()==true){
                mListener?.onNext(edit?.editableText?.toString())
            }else{
                Toast.makeText(context,context.getString(data.hint),Toast.LENGTH_SHORT).show()
            }
        }
    }

    class EditDialogBuild{
        var title=R.string.update_address
        var cancel=R.string.cancel
        var ok=R.string.bt_qd
        var hint=R.string.heihei_tv72
    }
    override fun dismiss() {
        super.dismiss()
    }

    override fun show() {
        super.show()
    }
}