package com.tovalue.me.network.interceptor

import android.util.Log
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.util.Constants.SECRET_COOKIE
import com.tovalue.me.util.Constants.JWT_CODE_KEY
import okhttp3.Interceptor
import okhttp3.Response

internal class CookiesInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val response = chain.proceed(chain.request())
		if (response.headers("Set-Cookie").isNotEmpty()) {
			val cookies: HashSet<String> = HashSet()
			for (header in response.headers("Set-Cookie")) {
				cookies.add(header)
				Log.d("headers", header)
			}
			
			MomensityBingoApp.preferencesManager!!.setStringSet(SECRET_COOKIE, cookies)
			
		}
		return response
	}
}