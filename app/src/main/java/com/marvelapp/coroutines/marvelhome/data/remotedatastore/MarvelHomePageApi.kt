package com.marvelapp.coroutines.marvelhome.data.remotedatastore

import com.marvelapp.coroutines.marvelhome.data.entities.MarvelCharacters
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelHomePageApi {

    @GET("v1/public/characters")
    fun getMarvelCharactersAsync(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Deferred<Response<MarvelCharacters>>
}