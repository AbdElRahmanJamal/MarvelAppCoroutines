package com.marvelapp.coroutines.marvelhome.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marvelapp.coroutines.AppExceptions
import com.marvelapp.coroutines.frameworks.appnetwork.ScreenState
import com.marvelapp.coroutines.marvelhome.data.entities.MarvelCharacters
import com.marvelapp.coroutines.marvelhome.data.entities.Results
import com.marvelapp.coroutines.marvelhome.data.localdatastore.MarvelHomeLocalDataStore
import com.marvelapp.coroutines.marvelhome.data.remotedatastore.MarvelHomeRemoteDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarvelHomeRepository(
        private val marvelHomeLocalDataStore: MarvelHomeLocalDataStore
        , private val marvelHomeRemoteDataStore: MarvelHomeRemoteDataStore
) {


    fun getMarvelCharactersList(limit: Int = 15, offset: Int = 0): MutableLiveData<ScreenState<List<Results>>> {
        val marvelCharactersResultCurrentState = MutableLiveData<ScreenState<List<Results>>>()
        marvelHomeRemoteDataStore.getRemoteMarvelCharactersInRange(limit = limit, offset = offset)
                .observeForever {
                    when (it) {
                        is ScreenState.LoadingState -> {
                            marvelCharactersResultCurrentState.postValue(it)
                        }
                        is ScreenState.DataStat -> {
                            getRemoteMarvelCharactersAndInsertIntoDB(it, marvelCharactersResultCurrentState)
                        }
                        is ScreenState.ErrorState -> {
                            if (it.exception is AppExceptions.NoConnectivityException) {
                                getLocalMarvelCharactersInCaseOfOfflineMode(
                                        limit,
                                        offset,
                                        marvelCharactersResultCurrentState
                                )
                            } else {
                                marvelCharactersResultCurrentState.postValue(it)
                            }
                        }
                    }
                }
        return marvelCharactersResultCurrentState
    }

    private fun getLocalMarvelCharactersInCaseOfOfflineMode(
            limit: Int,
            offset: Int,
            marvelCharactersResultCurrentState: MutableLiveData<ScreenState<List<Results>>>
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            getLocalMarvelCharactersInRange(limit = limit, offset = offset)
                    .observeForever { listOfCharacters ->
                        marvelCharactersResultCurrentState.postValue(
                                ScreenState.DataStat(
                                        listOfCharacters
                                )
                        )
                    }
        }
    }

    private fun getRemoteMarvelCharactersAndInsertIntoDB(
        it: ScreenState.DataStat<MarvelCharacters>,
        marvelCharactersResultCurrentState: MutableLiveData<ScreenState<List<Results>>>
    ) {
        updateInsertMarvelCharactersIntoDB(it.value.data.results)
        marvelCharactersResultCurrentState.postValue(
                ScreenState.DataStat(
                        it.value.data.results
                )
        )
    }


    private suspend fun getLocalMarvelCharactersInRange(limit: Int = 15, offset: Int = 0): LiveData<List<Results>> {
        return withContext(Dispatchers.IO) {
            return@withContext marvelHomeLocalDataStore.getLocalMarvelCharactersInRange(limit = limit, offset = offset)
        }
    }

    private fun updateInsertMarvelCharactersIntoDB(listOfMarvelCharacter: List<Results>) {
        GlobalScope.launch(Dispatchers.IO) {
            marvelHomeLocalDataStore.updateInsert(listOfMarvelCharacter)
        }
    }
}

