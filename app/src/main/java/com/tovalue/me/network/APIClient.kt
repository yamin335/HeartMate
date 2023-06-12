package com.tovalue.me.network

import android.util.Log
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.network.interceptor.CookiesInterceptor
import com.tovalue.me.util.Constants
import com.tovalue.me.util.NetworkConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object APIClient {
	
	private val retrofit = Retrofit.Builder()
		.baseUrl(NetworkConstants.NETWORK_BASE_URL)
		.client(httpClient)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
	
	val aPIClient: ApiRoute = retrofit.create(ApiRoute::class.java)
	
	private val httpClient: OkHttpClient
		get() {
			val interceptor = HttpLoggingInterceptor()
			interceptor.level = HttpLoggingInterceptor.Level.BODY
			return OkHttpClient().newBuilder()
				.connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.retryOnConnectionFailure(true)
				.addInterceptor(AddHeaderInterceptor())
				.addInterceptor(interceptor)
				.build()
		}
	
	private class AddHeaderInterceptor : Interceptor {
		@Throws(IOException::class)
		override fun intercept(chain: Interceptor.Chain): Response {
			val jwt = MomensityBingoApp.preferencesManager!!.getStringValue(Constants.JWT_CODE_KEY)
			val request = chain.request().newBuilder()
				request.addHeader("key", "611ba88a47b7a")
				request.addHeader("version", "3")
			
			if (jwt!!.isNotEmpty()) request.addHeader("Authorization", "Bearer $jwt")
			Log.i("TAG-->", "key:-- " + "611ba88a47b7a")
			Log.i("TAG-->", "version:-- " + "3")
			Log.i("TAG-->", "Authorization:-- " + "611ba88a47b7a")


			return chain.proceed(request.build())
		}
	}
}

private class AddCookieInterceptor : Interceptor{
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request().newBuilder()
		val cookies: Set<String>? = MomensityBingoApp.preferencesManager!!.getStringSet(Constants.SECRET_COOKIE)
		if (cookies != null) {
			for (cookie in cookies) {
				request.addHeader("TVMUserCookie", cookie)
				Log.d("headers", cookie)
			}
		}
		return chain.proceed(request.build())
	}
}
