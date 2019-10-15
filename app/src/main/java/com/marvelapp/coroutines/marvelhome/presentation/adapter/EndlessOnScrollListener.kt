package com.marvelapp.coroutines.marvelhome.presentation.adapter

import androidx.recyclerview.widget.RecyclerView


abstract class EndlessOnScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (!recyclerView.canScrollVertically(2)) {
            onLoadMore()
        }
        onScroll(dx, dy)
    }

    abstract fun onLoadMore()
    abstract fun onScroll(dx: Int, dy: Int)

}