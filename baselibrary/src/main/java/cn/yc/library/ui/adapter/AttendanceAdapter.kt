package cn.yc.library.ui.adapter

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.yc.library.R
import cn.yc.library.bean.AttendanceBean
import kotlinx.android.synthetic.main.item_attendance.view.*
import kotlinx.android.synthetic.main.item_attendance_footer.view.*

class AttendanceAdapter(val mAtv:Activity):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mLaouy=LayoutInflater.from(mAtv)

    private var mcqts:Int=0
    private var mxxts:Int=0
    private var mwcqts:Int=0
    private var mqjts:Int=0
    private var mTime= arrayListOf<String>()

    private var mData= arrayListOf<ArrayList<AttendanceBean>>()

    fun refresh(data:ArrayList<ArrayList<AttendanceBean>>,time:ArrayList<String>){
        mData=data
        mTime=time
        Log.e("Adapter","refresh ${mData.size} ${data.size}")
        mcqts=0
        mxxts=0
        mwcqts=0
        mqjts=0
        data?.forEach {
            it.forEach {
                when(it.type){
                    1->mqjts++
                    2->mxxts++
                    3->mcqts++
                    4->mwcqts++
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType==2){
            AttendanceViewHolder((mLaouy.inflate(R.layout.item_attendance,parent,false)))
        }else{
            AttendanceFooterViewHolder((mLaouy.inflate(R.layout.item_attendance_footer,parent,false)))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==itemCount-1){
            1
        }else{
            2
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mData[position]
        if(position==0){
            holder.itemView?.time_tv?.visibility=View.VISIBLE
            holder.itemView?.time_tv?.text="${mTime[0]}${mAtv.getString(R.string.heihei_tv17)}" +
                    "${mTime[1]}${mAtv.getString(R.string.heihei_tv18)}"
        }else{
            holder.itemView?.time_tv?.visibility=View.GONE
        }
        if(position<itemCount-1){
            Log.e("adapter","onbindViewHolder $position $itemCount")
            val setColor:(TextView,Int)->Unit={it: TextView, i: Int ->
                it.setTextColor(when {
                    data[i].header -> Color.parseColor("#656565")
                    data[i].empty -> Color.parseColor("#999999")
                    else -> Color.parseColor("#373737")
                })
            }
            val setContent:(TextView,Int)->Unit={textView, i ->
                if(data[i].type==2){
                    textView.text = "休"
                }else{
                    Log.e("adapter","设置为空")
                    textView.text = ""
                }
            }
            val setImg:(ImageView,Int)->Unit={imageView, i ->
                imageView.setBackgroundResource(when{
                    data[i].type==1->{
                        R.drawable.round1
                    }
                    data[i].type==2->{
                        R.drawable.round4
                    }
                    data[i].type==4->{
                        R.drawable.round3
                    }
                    data[i].type==3->{
                        R.drawable.round2
                    }
                    else->{
                        R.drawable.round4
                    }
                })
            }
            holder.itemView?.apply {
                attendance_child1_title.text=data[0].title
                setColor(attendance_child1_title,0)
                setImg(attendance_child1_img,0)

                attendance_child2_title.text=data[1].title
                setColor(attendance_child2_title,1)
                setImg(attendance_child2_img,1)

                attendance_child3_title.text=data[2].title
                setColor(attendance_child3_title,2)
                setImg(attendance_child3_img,2)

                attendance_child4_title.text=data[3].title
                setColor(attendance_child4_title,3)

                setImg(attendance_child4_img,3)

                attendance_child5_title.text=data[4].title
                setColor(attendance_child5_title,4)
                setImg(attendance_child5_img,4)

                attendance_child6_title.text=data[5].title
                setColor(attendance_child6_title,5)
                setImg(attendance_child6_img,5)

                attendance_child7_title.text=data[6].title
                setColor(attendance_child7_title,6)
                setImg(attendance_child7_img,6)

                setContent(attendance_child1_content,0)
                setContent(attendance_child2_content,1)
                setContent(attendance_child3_content,2)
                setContent(attendance_child4_content,3)
                setContent(attendance_child5_content,4)
                setContent(attendance_child6_content,5)
                setContent(attendance_child7_content,6)
            }
        }else{
            holder.itemView?.apply {
                qjts.text="$mqjts${mAtv.getString(R.string.heihei_tv16)}"
                cqts.text="$mcqts${mAtv.getString(R.string.heihei_tv16)}"
                wctqs.text="$mwcqts${mAtv.getString(R.string.heihei_tv16)}"
                xxts.text="$mxxts${mAtv.getString(R.string.heihei_tv16)}"
            }
        }
    }

    internal class AttendanceViewHolder(view:View):RecyclerView.ViewHolder(view)

    internal class AttendanceFooterViewHolder(view:View):RecyclerView.ViewHolder(view)

}