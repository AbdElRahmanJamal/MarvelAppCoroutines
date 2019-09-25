package com.marvelapp.coroutines.marvelhome.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marvelapp.coroutines.marvelhome.data.GetMarvelHomeUseCase

class MarvelHomePageViewModelFactory(
    private val getMarvelHomeUseCase: GetMarvelHomeUseCase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MarvelHomePageViewModel(getMarvelHomeUseCase) as T
    }
}