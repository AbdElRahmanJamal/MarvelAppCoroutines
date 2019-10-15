package com.marvelapp.coroutines.frameworks

import android.widget.ImageView
import com.marvelapp.coroutines.R
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso


fun downloadImage(url: String, imageView: ImageView) {
    val get = Picasso
        .get()
    get.setIndicatorsEnabled(true)
    get.load(url)
        .error(R.drawable.ic_marvel_logo)
        .fit()
        .placeholder(R.drawable.ic_the_avengers)
        .memoryPolicy(MemoryPolicy.NO_CACHE)
        .into(imageView)
}