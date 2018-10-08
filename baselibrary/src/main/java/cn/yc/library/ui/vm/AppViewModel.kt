package cn.yc.library.ui.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import cn.yc.library.base.BaseViewModel
import cn.yc.library.bean.response.CityResponse
import cn.yc.library.bean.response.ListingConfigResponse

class AppViewModel(application: Application):BaseViewModel(application){

    val mCity= MutableLiveData<CityResponse>()
    var mPhone=""
    var mMessages=-1  //0:在msg界面 1：未登录状态  2：登陆状态

    val mListingType= MutableLiveData<ListingConfigResponse>()
    val mHousesType= MutableLiveData<ListingConfigResponse>()
    val mHousesStatus= MutableLiveData<ListingConfigResponse>()
    val mHousesMatching= MutableLiveData<ListingConfigResponse>()

    init {
        mListingType.observeForever {
            Log.e("App","mListingType $it")
        }
        mCity.observeForever {
            Log.e("App","mCity $it")
        }
        mHousesType.observeForever {
            Log.e("App","mHousesType ListingConfigResponse $it")
        }
        mHousesStatus.observeForever {
            Log.e("App","mHousesStatus ListingConfigResponse $it")
        }
        mHousesMatching.observeForever {
            Log.e("App","mHousesMatching ListingConfigResponse $it")
        }
    }
}