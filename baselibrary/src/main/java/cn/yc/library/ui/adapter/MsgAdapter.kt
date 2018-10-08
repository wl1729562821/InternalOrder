package cn.yc.library.ui.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.yc.library.R
import cn.yc.library.bean.response.MessageListResponse
import cn.yc.library.bean.response.MessagesTypeResponse
import cn.yc.library.listener.ItemClickListener
import cn.yc.library.ui.activity.messages.MessagesDetailsActivity
import kotlinx.android.synthetic.main.item_msg.view.*

class MsgAdapter(val mAtv:Activity, val mListener:ItemClickListener?):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mLaouy=LayoutInflater.from(mAtv)

    private var mList= arrayListOf<MessageListResponse.DataSetBean>()
    private var mCurrentPostion=-1
    private var mTypeList= arrayListOf<MessagesTypeResponse.DataSetBean>()

    fun refresh(list:List<MessageListResponse.DataSetBean>?){
        notifyItemRangeRemoved(0,mList.size)
        mList.clear()
        list?.forEach {data->
            mTypeList.filter { data.msgCode== it.code}.forEach {
                data.title=it.zhCn?:""
            }
            mList.add(data)
        }
        notifyDataSetChanged()
    }

    fun setTypeList(data:MessagesTypeResponse){
        mTypeList.clear()
        data.dataSet?.forEach {
            mTypeList.add(it)
        }
    }

    fun setMessageDel(){
        mList.filter { it.id==mCurrentPostion }.forEach {
            //设置消息已经读取
            it.isDel=1
            notifyDataSetChanged()
            return
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeOneViewHolder(mLaouy.inflate(R.layout.item_msg,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mList[position]
        holder?.itemView?.apply {
            msg_update.visibility = if (data.isDel == 0) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            val msgData:()-> ArrayList<Int> ={
                arrayListOf<Int>().apply {
                    when(data.msgCode){
                        2->{
                            add(R.mipmap.msg_type2)
                            add(R.string.msg_type3)
                        }
                        3->{
                            add(R.mipmap.msg_type3)
                            add(R.string.msg_type4)
                        }
                        4->{
                            add(R.mipmap.msg_type3)
                            add(R.string.msg_type4)
                        }
                        else->{
                            add(R.mipmap.msg_type1)
                            add(R.string.msg_type1)
                        }
                    }
                }
            }
            msg_img.setBackgroundResource(msgData()[0])
            msg_title.text=data.title
            msg_time.text=data.createTime
            msg_content.text=data.alert
            setOnClickListener {
                mCurrentPostion=position
                mAtv.startActivityForResult(Intent(mAtv, MessagesDetailsActivity::class.java).apply {
                    putExtra("type",data.msgCode.toString())
                    putExtra("title",mAtv.getString(msgData()[1]))
                },101)
                data.isDel=1
                notifyItemChanged(position)
            }
        }
    }

    internal class HomeOneViewHolder(view:View):RecyclerView.ViewHolder(view)

}