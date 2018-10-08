package cn.yc.library.data

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

fun recyclerviewInit(data:RecyclerViewData.()->Unit){
    RecyclerViewData().apply {
        data()
        init()
    }

}

class RecyclerViewData{

    var mRv:RecyclerView?=null

    var mAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>?=null


    fun init(){
        Log.e("Activity","init $mRv  $mAdapter")
        mRv?.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=mAdapter
        }
    }

}