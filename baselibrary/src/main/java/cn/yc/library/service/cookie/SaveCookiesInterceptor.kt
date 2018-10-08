package cn.yc.library.data.cookie

import android.content.Context
import cn.yc.library.utils.saveCookie
import okhttp3.Interceptor
import okhttp3.Response


class SaveCookiesInterceptor(private val mContext: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val response=chain?.proceed(chain?.request())?:Response.Builder().build()
        val cookit=response.headers("set-cookie")
        if (cookit.isNotEmpty()) {
            val cookies = HashSet<String>()

            for (header in response.headers("Set-Cookie")) {
                cookies.add(header)
            }
            saveCookie(mContext,cookies)
        }
        return response
    }

}