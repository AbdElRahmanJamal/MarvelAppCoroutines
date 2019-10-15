package com.marvelapp.coroutines.marvelhome.presentation.mvi

import android.os.Parcelable
import com.marvelapp.coroutines.marvelhome.data.entities.Results
import kotlinx.android.parcel.Parcelize

sealed class HomePageStates {

    @Parcelize
    class SuccessState(val value: List<Results>) : HomePageStates(), Parcelable

    @Parcelize
    class ErrorState(val exception: Throwable) : HomePageStates(), Parcelable

    @Parcelize
    object LoadingState : HomePageStates(), Parcelable

    @Parcelize
    object LoadingMoreCharactersLoadingState : HomePageStates(), Parcelable

    @Parcelize
    data class LoadingMoreCharactersErrorState(val exception: Throwable) : HomePageStates(), Parcelable

    @Parcelize
    class LoadingMoreCharactersSuccessState(val value: List<Results>) : HomePageStates(), Parcelable
}