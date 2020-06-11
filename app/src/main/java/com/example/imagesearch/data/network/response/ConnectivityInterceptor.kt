package com.example.imagesearch.data.network.response

import okhttp3.Interceptor

interface ConnectivityInterceptor: Interceptor {
    fun isOnline () : Boolean
}