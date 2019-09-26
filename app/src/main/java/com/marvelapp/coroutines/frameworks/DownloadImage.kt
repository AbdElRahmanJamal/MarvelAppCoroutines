package com.marvelapp.coroutines.frameworks

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.widget.ImageView
import com.marvelapp.coroutines.R
import com.squareup.picasso.LruCache
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


fun downloadImage(url: String, imageView: ImageView) {
    //clear cache and cancel pending requests
    Picasso.get().setIndicatorsEnabled(true)
    Picasso.get().invalidate(url)
    Picasso.get().cancelRequest(imageView)

    var rc = Picasso.get().load(url)
    //set error image, memory policy
    rc = rc.error(R.drawable.ic_marvel_logo)
    rc.placeholder(R.drawable.ic_the_avengers)
    rc.networkPolicy(NetworkPolicy.OFFLINE)
    rc.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
    rc = rc.fit()
    rc.into(imageView)
}

fun getCustomPicasso(context: Context): Picasso {
    val builder = Picasso.Builder(context)
    //set 12% of available app memory for image cache
    builder.memoryCache(LruCache(getBytesForMemCache(12, context)))
    //set request transformer
    val requestTransformer = Picasso.RequestTransformer { request ->
        request
    }
    builder.requestTransformer(requestTransformer)

    return builder.build()
}

private fun getBytesForMemCache(percent: Int, context: Context): Int {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
    activityManager!!.getMemoryInfo(mi)

    val availableMemory = mi.availMem.toDouble()

    return (percent * availableMemory / 100).toInt()
}
