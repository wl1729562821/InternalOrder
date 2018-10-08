package cn.yc.library.data

import android.annotation.SuppressLint
import android.app.Activity
import cn.yc.library.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse
import com.google.android.gms.location.places.Places

class LocationData{

    private lateinit var context: Activity

    private var mPlaceDetectionClient: PlaceDetectionClient?=null
    private var mGeoDataClient: GeoDataClient?=null
    private var mFusedLocationProviderClient: FusedLocationProviderClient?=null

    private var mNext: (Any) -> Unit = { }
    private var mError: Throwable.()->Unit= {}

    @SuppressLint("MissingPermission", "RestrictedApi")
    fun onSend(){
        mPlaceDetectionClient?.getCurrentPlace(null)?.addOnCompleteListener {
            if(it==null || !it.isSuccessful || it.result==null || it.result?.count?:0<=0) {
                mError(Throwable(context.getString(R.string.location_error)))
                return@addOnCompleteListener
            }
            (it.result as? PlaceLikelihoodBufferResponse)?.apply {
                mNext(get(0))
            }
        }
    }

    fun initData(atv:Activity){
        context=atv
        mGeoDataClient= Places.getGeoDataClient(atv,null)
        mPlaceDetectionClient= Places.getPlaceDetectionClient(atv,null)
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(atv)
    }

    open fun onError(error:Throwable.()->Unit){
        mError=error
    }

    open fun onNext(onSuccess: (Any) -> Unit){
        mNext=onSuccess
    }



}