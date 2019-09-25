package com.marvelapp.coroutines.marvelhome.data.remotedatastore

import com.marvelapp.coroutines.frameworks.appnetwork.getRemoteDate
import com.marvelapp.coroutines.marvelhome.data.entities.MarvelCharacters

class MarvelHomeRemoteDataStore(private val marvelHomePageApi: MarvelHomePageApi) {

    fun getRemoteMarvelCharactersInRange(limit: Int = 15, offset: Int = 0) =
        getRemoteDate<MarvelCharacters> {
            client = marvelHomePageApi.getMarvelCharactersAsync(offset = offset, limit = limit)
        }
}