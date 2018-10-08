package cn.yc.library.data

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

data class Listing<T>(val mTwoLiveData: LiveData<PagedList<T>>)