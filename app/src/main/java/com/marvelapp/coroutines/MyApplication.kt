package com.marvelapp.coroutines

import android.app.Application
import com.marvelapp.coroutines.frameworks.appnetwork.apiFactory
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, listOf(apiFactory))

    }
}
