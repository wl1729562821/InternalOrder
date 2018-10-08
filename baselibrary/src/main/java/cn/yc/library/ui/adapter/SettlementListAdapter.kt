package cn.yc.library.ui.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.yc.library.R
import cn.yc.library.bean.response.BrokeragesReponse
import cn.yc.library.ui.activity.SettlementDetailsActivity
import com.google.gson.Gson
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.item_settlementlist.view.*

class SettlementListAdapter(val mAtv:Activity):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mLaouy=LayoutInflater.from(mAtv)
    private var mList= arrayListOf<BrokeragesReponse.DataSetBean>()
    fun refresh(data: BrokeragesReponse){
        mList.clear()
        if(data.dataSet?.isNotEmpty() != false){
            mList.addAll(data.dataSet!!)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder= HomeOneViewHolder(mLaouy.inflate(R.layout.item_settlementlist,parent,false))
        AutoUtils.autoSize(holder.itemView)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView?.apply {
            setOnClickListener {
                val json=Gson().toJson(mList[position])
                mAtv.startActivity(Intent(mAtv, SettlementDetailsActivity::class.java).apply {
                    putExtra("json",json)
                })
            }
            time_tv.text=mList[position].createTime
            id_tv.text=mList[position].orderCode
            name_tv.text=mList[position].houseName
        }
    }

    internal class HomeOneViewHolder(view:View):RecyclerView.ViewHolder(view)

}