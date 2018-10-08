package cn.yc.library.ui.adapter

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.finalteam.rxgalleryfinal.bean.MediaBean
import cn.yc.library.R
import cn.yc.library.listener.ItemClickListener
import cn.yc.library.utils.setFresco
import kotlinx.android.synthetic.main.item_img.view.*

class ImageAdapter(val mAtv: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mLaouy = LayoutInflater.from(mAtv)
    private var mListener:ItemClickListener?=null

    fun setListener(listener: ItemClickListener){
        mListener=listener
    }

    fun getList():ArrayList<String>{
        return mList
    }

    private val mList = arrayListOf<String>().apply {
        add("add")
    }

    fun addItem(imgs:List<MediaBean>){
        imgs.forEach {
            if(mList.size<6){
                mList.add(it.originalPath)
            }
        }
        if(mList.size>=6){
            mList.removeAt(0)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeOneViewHolder(mLaouy.inflate(R.layout.item_img, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("Activity","onBindViewHolder ${mList[position]}")
        holder.itemView?.apply {
            if (mList[position] == "add") {
                item_img_parenr?.setBackgroundColor(Color.parseColor("#ebebeb"))
                item_img?.visibility=View.VISIBLE
                item_img1?.visibility=View.INVISIBLE
                item_img?.setBackgroundResource(R.mipmap.houses_add)
                setOnClickListener {
                    mListener?.onClcik(position)
                }
            } else {
                item_img1?.visibility=View.VISIBLE
                item_img?.visibility=View.INVISIBLE
                mAtv.setFresco(mList[position],item_img1)
                /*val uri = Uri.Builder()
                        .scheme(UriUtil.LOCAL_FILE_SCHEME)
                        .path(mList[position])
                        .build()
                val request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setRotationOptions(RotationOptions.autoRotate())
                        .setLocalThumbnailPreviewsEnabled(true)
                        .setResizeOptions(ResizeOptions(400, 400))
                        .build()
                val controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(item_img1.controller)
                        .setControllerListener(object : BaseControllerListener<ImageInfo>() {
                            override fun onFinalImageSet(id: String?, @Nullable imageInfo: ImageInfo?, @Nullable anim: Animatable?) {
                                if (imageInfo == null) {
                                    return
                                }
                                val vp = item_img1.layoutParams
                                vp.width = 400
                                vp.height = 400
                                item_img1.requestLayout()
                            }
                        })
                        .build()
                item_img1.controller = controller
                item_img_parenr?.setBackgroundColor(Color.TRANSPARENT)*/

            }
        }
    }

    internal class HomeOneViewHolder(view: View) : RecyclerView.ViewHolder(view)

}