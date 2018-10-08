package com.ysgj.order.ui.adapter

import android.app.Activity
import android.arch.paging.PagedListAdapter
import android.content.Intent
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.yc.library.base.BaseActivity
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.response.HomeTwoResponse
import cn.yc.library.utils.setBusinessType
import cn.yc.library.utils.setPath
import com.ysgj.order.R
import com.ysgj.order.ui.activity.order.HomeTwoInfoActivity
import kotlinx.android.synthetic.main.item_home_two.view.*


class HomeTwoAdapter(val mAtv:Activity): PagedListAdapter<HomeTwoResponse.DataSetBean, RecyclerView.ViewHolder>(POST_COMPARATOR){

    private val mLaouy=LayoutInflater.from(mAtv)

    private val mList= arrayListOf<HomeTwoResponse.DataSetBean>()

    fun refresh(list:ArrayList<HomeTwoResponse.DataSetBean>?){
        mList.clear()
        list?.forEach {
            mList.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun loadMore(list:ArrayList<HomeTwoResponse.DataSetBean>?){
        list?.forEach {
            mList.addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeOneViewHolder(mLaouy.inflate(R.layout.item_home_two,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=mList[position]
        holder.itemView?.setOnClickListener {
            Log.e("Adapter","data=$data")
            (mAtv as BaseActivity).getBaseViewModel<BaseViewModel>()?.setRequestCode(11106)
            mAtv.startActivity(Intent(mAtv,HomeTwoInfoActivity::class.java).apply {
                putExtra("applyId","${data.houseId}")
                putExtra("title","Improve listing information")
            })
        }
        holder?.itemView?.apply {

            home_one_id?.text="${mAtv.getString(R.string.settlement_4)}: ${data.orderCode}"
            sk_tv?.text=when(data.isCheck){
                1->mAtv.getString(R.string.skzt_tv2)
                2->mAtv.getString(R.string.skzt_tv3)
                3->mAtv.getString(R.string.skzt_tv4)
                else->mAtv.getString(R.string.skzt_tv1)
            }
            home_two_img?.setPath(data.houseMainImg)
            home_two_title?.text=data.houseName
            price?.text="${data.houseRent}"
            price_tv?.text=if(data.leaseType==0){
                "AED/year"
            }else{
                "AED"
            }
            mj?.text="${data.houseAcreage}"
            address?.text=data.address
            img?.setBusinessType(data.leaseType)
        }
    }

    internal class HomeOneViewHolder(view:View):RecyclerView.ViewHolder(view)

    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         * <p>
         * When you add a Cheese with the 'Add' button, the PagedListAdapter uses diffCallback to
         * detect there's only a single item difference from before, so it only needs to animate and
         * rebind a single view.
         *
         * @see android.support.v7.util.DiffUtil
         */
        private val PAYLOAD_SCORE = Any()
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<HomeTwoResponse.DataSetBean>() {
            override fun areContentsTheSame(oldItem: HomeTwoResponse.DataSetBean, newItem: HomeTwoResponse.DataSetBean): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: HomeTwoResponse.DataSetBean, newItem: HomeTwoResponse.DataSetBean): Boolean =
                    oldItem.houseId == newItem.houseId

            override fun getChangePayload(oldItem: HomeTwoResponse.DataSetBean, newItem: HomeTwoResponse.DataSetBean): Any? {
                return if (oldItem==newItem) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }

    }
}