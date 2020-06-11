package com.example.imagesearch.data.network.response

import android.content.Context
import android.net.ConnectivityManager
import com.example.imagesearch.internals.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptorImpl (context: Context): ConnectivityInterceptor {

    private var appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline())
            throw NoConnectionException()
        return chain.proceed(chain.request())
    }

    override fun isOnline () : Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
        as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}