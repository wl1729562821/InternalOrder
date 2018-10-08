package com.ysgj.order.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.ysgj.order.R
import com.ysgj.order.base.BaseActivity
import com.ysgj.order.ui.adapter.DefaultAdapter
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity:BaseActivity(){

    private var mAdapter:DefaultAdapter?=null

    inner class DefaultHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what==0){
                mAdapter?.add()
                xrecycler?.finishLoadMore()
            }
        }
    }
    private val mDefaultHandler=DefaultHandler()

    override val layoutId: Int
        get() = R.layout.activity_test

    private var mPlaceDetectionClient: PlaceDetectionClient?=null
    private var mGeoDataClient: GeoDataClient?=null
    private var mFusedLocationProviderClient: FusedLocationProviderClient?=null

    @SuppressLint("MissingPermission", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGeoDataClient= Places.getGeoDataClient(this,null)
        mPlaceDetectionClient= Places.getPlaceDetectionClient(this,null)
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        xrecycler.apply {
            setRefreshLayout(refresh_layout)
            setLoadingListener {
                Log.e(TAG,"onLoadMore send")
                mDefaultHandler.sendEmptyMessageDelayed(0,5000)
            }
            layoutManager=LinearLayoutManager(this@TestActivity)
            mAdapter= DefaultAdapter(this@TestActivity,arrayListOf<String>().apply {
                (1..10).forEach {
                    add("$it")
                }
            })
            adapter=mAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG,"onActivityResult $requestCode $resultCode")
    }
}