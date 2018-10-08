package cn.yc.library.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.net.Uri
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import cn.yc.library.R
import cn.yc.library.base.App
import cn.yc.library.bean.DialogDataBean
import cn.yc.library.bean.UserBean
import cn.yc.library.listener.BaseListener
import cn.yc.library.ui.view.dialog.PhoneDialog
import com.facebook.common.util.UriUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.common.RotationOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.github.promeg.pinyinhelper.Pinyin
import com.google.gson.Gson
import io.reactivex.annotations.Nullable
import okhttp3.MultipartBody
import java.util.*


fun Application.saveLogin(userBean: UserBean){
    val sp=getSharedPreferences("config",Context.MODE_PRIVATE)
    val json=Gson().toJson(userBean)
    sp.edit().putString("login",json).commit()
}

fun Context.getLogin():UserBean?{
    val sp=getSharedPreferences("config",Context.MODE_PRIVATE)
    val json=sp.getString("login",null)
    return  if(json==null){
        null
    }else{
        Gson().fromJson(json,UserBean::class.java)
    }
}

fun String.split1Empty(s:String):ArrayList<String>{
    var nextInt=indexOf(s)
    val list= arrayListOf<String>()
    list.add(substring(0,nextInt))
    var current=0
    while (nextInt!=-1){
        val start=nextInt
        nextInt=indexOf(s,start+1)
        if(nextInt!=-1){
            list.add(substring(start+1,nextInt))
            current=nextInt
        }
    }
    list.add(substring(current+1,length))
    list.forEach {
        Log.e("Utils","splitEmpty $it")
    }
    return list
}

fun Application.getLogin():UserBean?{
    val sp=getSharedPreferences("config",Context.MODE_PRIVATE)
    val json=sp.getString("login",null)
    return  if(json==null){
        null
    }else{
        Gson().fromJson(json,UserBean::class.java)
    }
}

fun saveCookie(context: Context,data:HashSet<String>){
    var i=0
    val sp=context.getSharedPreferences("config",Context.MODE_PRIVATE)
    sp.edit().remove("Socket")
    sp.edit().putStringSet("Socket",data).commit()
}

fun getCookit(context: Context):HashSet<String>{
    val sp=context.getSharedPreferences("config",Context.MODE_PRIVATE)
    return sp.getStringSet("Socket",hashSetOf<String>()).toHashSet()
}

fun Activity.showMessage(msg:String?){
    if(msg?.isNullOrBlank()==true){
        return
    }
    msg?.apply {
        Toast.makeText(this@showMessage,msg,Toast.LENGTH_SHORT).show()
    }
}
fun Fragment.showMessage(msg:Int){
    activity?.apply {
        Toast.makeText(this,resources.getString(msg),Toast.LENGTH_SHORT).show()
    }
}
fun Context.getActivityNname():String{
    val activityManager =getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    val list = activityManager?.getRunningTasks(1)
    if (list?.isNotEmpty()==true) {
        val componentName = list[0].topActivity

        //包名
        val packName = componentName.packageName//packName=com.dr.dr_testappmanager

        //包名+类名，这是是我们需要的
        val className = componentName.className//className=com.dr.dr_testappmanager.MainActivity

        val nameStr = componentName.toString()//ComponentInfo{com.dr.dr_testappmanager/com.dr.dr_testappmanager.MainActivity}


        return className

    }
    return ""
}
fun Activity.showMessage(msg:Int){
    Toast.makeText(this,resources.getString(msg),Toast.LENGTH_SHORT).show()
}
fun Fragment.showMessage(msg:String?){
    if(msg?.isNullOrBlank()==true){
        return
    }
    activity?.apply {
        Toast.makeText(this,msg?:"",Toast.LENGTH_SHORT).show()
    }
}

@SuppressLint("MissingPermission")
fun Activity.startPhone(phoneNum:String){
    val intent = Intent(Intent.ACTION_CALL)
    val data = Uri.parse("tel:$phoneNum")
    intent.data = data
    startActivity(intent)
}

fun Activity.startPhoneDialog(phone:String){
    var dialog: PhoneDialog?=null
    val listener=object : BaseListener {
        override fun <T> onNext(dat: T) {
            val p=dat as String
            if(p.isNotEmpty()){
                dialog?.dismiss()
                startPhone(p)
            }else{
                showMessage(resources.getString(R.string.phone_error))
            }
        }

        override fun onError(code: Int, throwable: Throwable) {}
    }
    dialog= PhoneDialog (this, DialogDataBean().apply {
        content=phone
        this.phone=phone
        title=resources.getString(R.string.customerservice)
        mListener=listener
    })
    dialog?.show()
}

fun Activity.startPhoneDialog(data:DialogDataBean){
    var dialog: PhoneDialog?=null
    val listener=object : BaseListener {
        override fun <T> onNext(dat: T) {
            val p=dat as String
            if(data.phone.isNotEmpty()){
                dialog?.dismiss()
                startPhone(p)
            }else{
                showMessage(resources.getString(R.string.phone_error))
            }
        }

        override fun onError(code: Int, throwable: Throwable) {}
    }
    data.mListener=listener
    dialog= PhoneDialog (this,data)
    dialog?.show()
}

fun Activity.setFresco(imgPath:String?,img:View){
    setFresco(imgPath,img,true)
}

fun Activity.setFresco(imgPath: String?,img: View,empty:Boolean){
    Log.e("Activity","setFresco $imgPath")
    if((imgPath== null || imgPath.isNullOrBlank() || imgPath=="add_x_x")&& empty){
        return
    }
    var path=imgPath
    if(path?.contains("group")==true && !path.contains("http://")){
        path="http://120.77.220.25/$path"
    }
    Log.e("Activity","setFresco $path")
    (img as? SimpleDraweeView)?.also { img->
        if(path?.contains("http://")==true||path?.contains("htts://")==true){
            val hierarchy = if(img.hierarchy==null){
                GenericDraweeHierarchyBuilder(resources).build()
            }else{
                img.hierarchy
            }
            hierarchy.actualImageScaleType=object : ScalingUtils.ScaleType{
                override fun getTransform(outTransform: Matrix?, parentBounds: Rect?, childWidth: Int, childHeight: Int, focusX: Float, focusY: Float): Matrix {
                    val parentW=(parentBounds?.width()?:0).toFloat()
                    val parentH=(parentBounds?.height()?:0).toFloat()
                    val parentLeft=parentBounds?.left?:0
                    val parentTop=parentBounds?.top?:0
                    val scaleX=(parentW/childWidth)
                    val scaleY=(parentH/childHeight)
                    val scale=Math.max(scaleX, scaleY)
                    Log.e("Activity","setFresco gettranform pw=$parentW ph=$parentH scalex=$scaleX scaley=$scaleY scale=$scale height=$childHeight  width=$childWidth  $outTransform")
                    val dx = parentLeft + (parentW - childWidth * scale) * 0.5
                    val dy = parentTop + (parentH - childHeight * scale) * 0.5
                    outTransform?.setScale(scaleX, scaleY)
                    //outTransform?.postTranslate(dx.toFloat()+0.5f,dy.toFloat() + 0.5f)
                    Log.e("Activity","setFresco gettranform ${outTransform?.toString()} scale1=$scale  scale=${scale.toFloat()} x=${dx.toFloat()} y=${dy.toFloat()}")
                    return outTransform?: Matrix()
                }
            }
            if(img.hierarchy==null){
                img.hierarchy=hierarchy
            }
            img.setImageURI(path)
            return
        }
        val uri = Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(path)
                .build()
        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setLocalThumbnailPreviewsEnabled(true)
                .setResizeOptions(ResizeOptions(400, 400))
                .build()
        val controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(img.controller)
                .setControllerListener(object : BaseControllerListener<ImageInfo>() {
                    override fun onFinalImageSet(id: String?, @Nullable imageInfo: ImageInfo?, @Nullable anim: Animatable?) {
                        if (imageInfo == null) {
                            return
                        }
                        val vp = img.layoutParams
                        vp.width = 400
                        vp.height = 400
                        img.requestLayout()
                    }
                })
                .build()
        img.controller = controller
        img?.setBackgroundColor(Color.TRANSPARENT)
    }
}

fun SimpleDraweeView.setPath(imgPath: String?){
    Log.e("Activity","setFresco $imgPath")
    var path=imgPath
    if(path?.contains("group")==true && !path.contains("http://")){
        path="http://120.77.220.25/$path"
    }
    Log.e("Activity","setFresco $path")
    setImageURI(path)
}


fun MultipartBody.Builder.addFormDataPart1(name:String,value:String){
    Log.e("Utils","addFormDataPart1 $name $value ${value=="add_x_x"}")
    if(value=="add_x_x" || value=="add_xx_xx" || value=="add_xx1_xx1"){
        return
    }
    if(value.isNotBlank()){
        addFormDataPart(name,value)
    }
}

fun Activity.getApp(): App?{
    return application as? App
}

fun TextView.setBusinessType(type:Int){
    if(type==0){
        text=context.getString(R.string.rent)
        setTextColor(Color.parseColor("#00b660"))
        setBackgroundResource(R.drawable.round5)
    }else{
        text=context.getString(R.string.sell)
        setBackgroundResource(R.drawable.round6)
        setTextColor(Color.parseColor("#f39700"))
    }
}

fun Activity.setAlpha(alpha:Float){
    val params = window.attributes
    params.alpha = alpha
    window.attributes = params
}

operator fun ViewGroup.get(index:Int):View?{
    return if(childCount>index){
        getChildAt(index)
    }else{
        null
    }
}
operator fun LinearLayout.get(index:Int):View?{
    return if(childCount>index){
        getChildAt(index)
    }else{
        null
    }
}
fun TextView.setStringRes(res:Int){
    text = context.getString(res)
}
fun TextView.setBlankText(value:String?){
    if(value?.isNotBlank()==true){
        text=value
    }
}
fun TextView.setEmptyText(value:String?){
    if(text.isNullOrEmpty() || text.isNullOrBlank()){
        text = value
    }
}
fun EditText.setBlankText(value:String?){
    if(value?.isNotBlank()==true){
        setText(value)
    }
}
fun getTime(year:Int,month:Int):Int{
    val a = Calendar.getInstance()
    a.set(Calendar.YEAR, year)
    a.set(Calendar.MONTH, month - 1)
    a.set(Calendar.DATE, 1)
    a.set(Calendar.DATE, 1);
    a.roll(Calendar.DATE, -1);
    //a.roll(Calendar.DATE, -1)
    //a.set(Calendar.DAY_OF_WEEK,1)
    Log.e("Util","荷藕和 ${a.get(Calendar.DAY_OF_WEEK)}")
    return a.get(Calendar.DATE)
}

fun getWeek(year:Int,month:Int):Int{
    val a = Calendar.getInstance()
    a.set(Calendar.YEAR, year)
    a.set(Calendar.MONTH, month - 1)
    a.set(Calendar.DATE, 1)
    return a.get(Calendar.DAY_OF_WEEK)-1
}
fun Activity.checkZh():Boolean{
    val locale = resources.configuration.locale
    val language = locale.language
    return language.endsWith("zh")
}

fun dip2px(context:Context,dpValue:Int):Float{
    val scale = context.resources.displayMetrics.density
    return dpValue * scale
}

fun getPingYin(inputString:String):String{
    return Pinyin.toPinyin(inputString, "")
}