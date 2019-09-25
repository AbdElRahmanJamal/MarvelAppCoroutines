package com.marvelapp.coroutines.frameworks.appnetwork.interceptor

import com.marvelapp.coroutines.BuildConfig.API_KEY
import com.marvelapp.coroutines.BuildConfig.HASH
import com.marvelapp.coroutines.API_KEY_STRING
import com.marvelapp.coroutines.HASH_STRING
import com.marvelapp.coroutines.TIME_STAMP
import com.marvelapp.coroutines.TIME_STAMP_STRING
import okhttp3.Interceptor
import okhttp3.Response

class MarvelApiKeysInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .url()
            .newBuilder()
            .addQueryParameter(API_KEY_STRING, API_KEY)
            .addQueryParameter(TIME_STAMP_STRING, TIME_STAMP)
            .addQueryParameter(HASH_STRING, HASH)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}