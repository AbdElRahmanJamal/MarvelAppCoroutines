package com.marvelapp.coroutines.marvelhome.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.marvelapp.coroutines.frameworks.appnetwork.ScreenState
import com.marvelapp.coroutines.marvelhome.data.GetMarvelHomeUseCase
import com.marvelapp.coroutines.marvelhome.data.entities.Results
import com.marvelapp.coroutines.marvelhome.presentation.mvi.HomePageIntents
import com.marvelapp.coroutines.marvelhome.presentation.mvi.HomePageStates
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MarvelHomePageViewModel(
    private val getMarvelHomeUseCase: GetMarvelHomeUseCase
    , private val handle: SavedStateHandle
) : ViewModel() {

    private lateinit var value: MutableList<Results>
    val intents: Channel<HomePageIntents> = Channel(Channel.CONFLATED)
    val homePageState: Channel<HomePageStates> = Channel(Channel.CONFLATED)
    val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        coroutineScope.launch {

            for (intent in intents) {
                if (handle.get<HomePageStates>("homePageState") == null) {
                    when (intent) {
                        is HomePageIntents.OnHomePageStartIntent -> getMarvelCharactersList(
                            intent.limit,
                            intent.offset
                        )
                        is HomePageIntents.OnEndlessRecyclerViewIntent -> getMarvelCharactersList(
                            intent.limit,
                            intent.offset
                        )
                    }
                } else {
                    when (intent) {
                        is HomePageIntents.OnHomePageStartIntent -> homePageState.send(handle.get<HomePageStates>("homePageState")!!)
                        is HomePageIntents.OnEndlessRecyclerViewIntent -> getMarvelCharactersList(
                            intent.limit,
                            intent.offset
                        )
                    }
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
                            is ScreenState.LoadingState -> if (offset == 0) {
                                homePageState.send(HomePageStates.LoadingState)
                            } else {
                                homePageState.send(HomePageStates.LoadingMoreCharactersLoadingState)
                            }

                            is ScreenState.DataStat -> if (offset == 0) {
                                value = it.value.toMutableList()
                                homePageState.send(HomePageStates.SuccessState(value))
                                handle.set("homePageState", HomePageStates.SuccessState(it.value))

                            } else {
                                value.addAll(it.value)
                                homePageState.send(HomePageStates.LoadingMoreCharactersSuccessState(value))
                                handle.set("homePageState", HomePageStates.LoadingMoreCharactersSuccessState(value))

                            }

                            is ScreenState.ErrorState -> if (offset == 0) {
                                homePageState.send(HomePageStates.ErrorState(it.exception))
                                handle.set("homePageState", HomePageStates.ErrorState(it.exception))
                            } else {
                                homePageState.send(HomePageStates.LoadingMoreCharactersErrorState(it.exception))
                                handle.set(
                                    "homePageState",
                                    HomePageStates.LoadingMoreCharactersErrorState(it.exception)
                                )

                            }
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
