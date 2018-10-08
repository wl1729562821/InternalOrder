package cn.yc.library.api

import android.content.Context
import cn.yc.library.service.cookie.PersistentCookieStore
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieManger(context: Context) : CookieJar {

    init {
        mContext = context
        if(mContext!=null){
            cookieStore= cookieStore?: PersistentCookieStore(mContext!!)
        }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>?) {
        cookieStore?.apply {
            if (cookies != null && cookies.isNotEmpty()) {
                for (item in cookies) {
                    add(url, item)
                }
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore!![url]
    }

    internal class Customer(var userID: String?, var token: String?)

    companion object {

        var APP_PLATFORM = "app-platform"

        private var mContext: Context?=null

        private var cookieStore: PersistentCookieStore? = null
    }
}
