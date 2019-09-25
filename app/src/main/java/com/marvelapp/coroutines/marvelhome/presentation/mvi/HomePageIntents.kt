package com.marvelapp.coroutines.marvelhome.presentation.mvi


sealed class HomePageIntents {
    data class OnHomePageStartIntent(val limit: Int = 15, val offset: Int = 0) : HomePageIntents()
}