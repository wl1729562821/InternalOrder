package cn.yc.library.data.cookie

import android.content.Context
import android.util.Log
import cn.yc.library.utils.getCookit
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AddCookiesInterceptor(private val mContext:Context):Interceptor{

    override fun intercept(chain: Interceptor.Chain?): Response {
        val builder = chain?.request()?.newBuilder()?:Request.Builder()
        val preferences = getCookit(mContext)
        Log.e("OkHttp", "Adding Header: intercept ${preferences.size}")
        for (cookie in preferences) {
            builder.addHeader("Cookie", cookie)
            Log.e("OkHttp", "Adding Header: $cookie") // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain?.proceed(builder.build())?:Response.Builder().build()
    }
}