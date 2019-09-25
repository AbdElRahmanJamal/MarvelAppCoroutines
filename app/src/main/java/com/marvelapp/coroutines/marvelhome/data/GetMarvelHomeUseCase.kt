package com.marvelapp.coroutines.marvelhome.data

import androidx.lifecycle.MutableLiveData
import com.marvelapp.coroutines.frameworks.appnetwork.ScreenState
import com.marvelapp.coroutines.marvelhome.data.entities.Results

class GetMarvelHomeUseCase(private val marvelHomeRepository: MarvelHomeRepository) {

    fun getMarvelCharactersList(limit: Int = 15, offset: Int = 0): MutableLiveData<ScreenState<List<Results>>> {
        marvelHomeRepository.run {
            return getMarvelCharactersList(limit = limit, offset = offset)
        }
    }

}