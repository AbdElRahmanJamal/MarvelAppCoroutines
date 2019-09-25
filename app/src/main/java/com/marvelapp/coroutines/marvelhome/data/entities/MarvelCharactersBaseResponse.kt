package com.marvelapp.coroutines.marvelhome.data.entities

interface MarvelCharactersBaseResponse<T> {
    fun retrieveData(): T
}