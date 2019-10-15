package com.marvelapp.coroutines.marvelhome.presentation

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.marvelapp.coroutines.marvelhome.data.GetMarvelHomeUseCase

class MarvelHomePageViewModelFactory(
    private val marvelHomeUseCase: GetMarvelHomeUseCase,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return MarvelHomePageViewModel(marvelHomeUseCase, handle) as T
    }
}