package cn.yc.library.ui.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.yc.library.R
import cn.yc.library.bean.response.HistoryMessagesResponse
import cn.yc.library.listener.ItemClickListener
import kotlinx.android.synthetic.main.item_msg_detalis_0.view.*

class MsgDetailsAdapter(val mAtv:Activity, val mListener:ItemClickListener?):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mLaouy=LayoutInflater.from(mAtv)

    private var mList= arrayListOf<HistoryMessagesResponse.DataSetBean>()

    fun refresh(list:List<HistoryMessagesResponse.DataSetBean>?){
        notifyItemRangeRemoved(0,mList.size)
        mList.clear()
        list?.forEach {
            mList.add(it)
        }
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return mList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0->MsgOneViewHolder((mLaouy.inflate(R.layout.item_msg_detalis_0,parent,false)))
            2->MsgTwowHolder((mLaouy.inflate(R.layout.item_msg_detalis_1,parent,false)))
            else->{
                MsgThreeViewHolder((mLaouy.inflate(R.layout.item_msg_detalis_2,parent,false)))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mList[position]
        holder?.itemView?.apply {
            when(data.type){
                //系统消息暂时不管
                0->{
                    details0_tv1.text=data.alert
                    details0_tv1.text=data.details?.sysMsg
                }
                //钥匙交接
                2->{

                }
                //派单抢单
                else->{
                    /*details2_title.text=mAtv.getString(if(data.type==3){
                        R.string.msg_type3
                    }else{
                        R.string.msg_type4
                    })
                    data?.details?.apply {
                        tv1.setStringRes(if(haveKey==0){
                            R.string.heihei_tv61
                        }else{
                            R.string.heihei_tv62
                        })
                        tv2.setStringRes(if(leaseType=="0"){
                            R.string.cz
                        }else{
                            R.string.cs
                        })
                        val df=SimpleDateFormat("yyyy-MM-dd  HH:mm")
                        var value=df.format(Date(meetingTime))
                        tv3.text=value
                        tv4.text
                    }
                    details2_status.text=mAtv.getString(if(data.type==3){
                        R.string.msg_type6
                    }else{
                        R.string.msg_type7
                    })*/
                }
            }
        }
    }

    internal class MsgOneViewHolder(view:View):RecyclerView.ViewHolder(view)
    internal class MsgTwowHolder(view:View):RecyclerView.ViewHolder(view)
    internal class MsgThreeViewHolder(view:View):RecyclerView.ViewHolder(view)

}