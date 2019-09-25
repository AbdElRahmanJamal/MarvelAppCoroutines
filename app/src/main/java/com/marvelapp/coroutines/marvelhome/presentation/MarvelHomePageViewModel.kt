package com.marvelapp.coroutines.marvelhome.presentation

import androidx.lifecycle.ViewModel
import com.marvelapp.coroutines.frameworks.appnetwork.ScreenState
import com.marvelapp.coroutines.marvelhome.data.GetMarvelHomeUseCase
import com.marvelapp.coroutines.marvelhome.presentation.mvi.HomePageIntents
import com.marvelapp.coroutines.marvelhome.presentation.mvi.HomePageStates
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MarvelHomePageViewModel(private val getMarvelHomeUseCase: GetMarvelHomeUseCase) : ViewModel() {

    val intents: Channel<HomePageIntents> = Channel(Channel.CONFLATED)
    val homePageState: Channel<HomePageStates> = Channel(Channel.CONFLATED)
    val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        coroutineScope.launch {
            for (intent in intents) {
                when (intent) {
                    is HomePageIntents.OnHomePageStartIntent -> getMarvelCharactersList(intent.limit, intent.offset)
                }
            }
        }
    }

    private fun getMarvelCharactersList(limit: Int = 15, offset: Int = 0) {
        getMarvelHomeUseCase.run {
            getMarvelCharactersList(limit = limit, offset = offset)
                .observeForever {
                coroutineScope.launch {
                    when (it) {
                        is ScreenState.LoadingState -> homePageState.send(HomePageStates.LoadingState)
                        is ScreenState.DataStat -> homePageState.send(HomePageStates.SuccessState(it.value))
                        is ScreenState.ErrorState -> homePageState.send(HomePageStates.ErrorState(it.exception))
                    }
                }
            }
        }
    }

    override fun onCleared() {
        coroutineScope.cancel()
        intents.cancel()
        homePageState.cancel()
        super.onCleared()
    }
}
