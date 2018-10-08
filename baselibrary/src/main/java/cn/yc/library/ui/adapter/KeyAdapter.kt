package cn.yc.library.ui.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.yc.library.R
import cn.yc.library.bean.response.KeyResponse
import cn.yc.library.listener.ItemClickListener
import cn.yc.library.utils.setFresco
import kotlinx.android.synthetic.main.item_key.view.*

class KeyAdapter(val mAtv:Activity,val mListener:ItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val mLaouy=LayoutInflater.from(mAtv)

    fun setSelected(selected:Boolean){
        mList.forEach {
            it.check=selected
        }
        notifyDataSetChanged()
    }

    private var mList= arrayListOf<KeyResponse.DataSetBean>()

    fun getSelectedData()=mList.filter { it.check }

    fun getCheckAll()=mList.filter { it.check }.size==mList.size

    fun addItem(list:List<KeyResponse.DataSetBean>){
        list.forEach {data->
            //如果重新获取的列表列表没有这个id
            Log.e("adapter","添加 $data")
            mList.forEach {
                if(data.houseId==it.houseId && it.check){
                    data.check=true
                }
            }
        }
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(id:Int){
        var index=-1
        (0 until mList.size).forEach {
            if(mList[it].houseId==id){
                index=it
            }
        }
        if(index in 0 until mList.size){
            mList.removeAt(index)
            notifyDataSetChanged()
        }
    }

    fun refresh(list:List<KeyResponse.DataSetBean>){
        mList.clear()
        Log.e("Adapter","refresh ${list.size}")
        list.forEach {
            it.check=false
            mList.add(it)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeOneViewHolder(mLaouy.inflate(R.layout.item_key,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mList[position]
        if(data.check){
            holder.itemView.check.setBackgroundResource(R.mipmap.check_selected)
        }else{
            holder.itemView.check.setBackgroundResource(R.drawable.check_bg)
        }
        holder.itemView.check.setOnClickListener {
            if(data.check){
                data.check=false
                holder.itemView.check.setBackgroundResource(R.drawable.check_bg)
            }else{
                holder.itemView.check.setBackgroundResource(R.mipmap.check_selected)
                data.check=true
            }
            if(mList.none { it.check }){
                mListener.onClcik(false)
            }else{
                mListener.onClcik(true)
            }
            Log.e("Activity","onClick ${mList[position].check}")
        }
        holder.itemView?.apply {
            mAtv.setFresco(data.houseMainImg?:"",img)
            /*img?.apply {
                val co = Fresco.newDraweeControllerBuilder()
                        .setUri(data.houseMainImg?:"")
                        .setTapToRetryEnabled(true)
                        .setOldController(this.controller)
                        .build()
                controller=co
            }*/
            name.text=data.houseName
            tv1.text=data.houseAcreage.toString()
            tv2.text=data.address
            tv3.text=data.houseRent.toString()
            setOnClickListener {
                mListener.onClcik(data)
            }
        }
        /*holder.itemView?.apply {

            name.text=data.houseName
            tv1.text="${data.houseAcreage}"
        }*/
    }

    internal class HomeOneViewHolder(view:View):RecyclerView.ViewHolder(view)

}