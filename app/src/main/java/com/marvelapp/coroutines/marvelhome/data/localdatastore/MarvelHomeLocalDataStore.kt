package com.marvelapp.coroutines.marvelhome.data.localdatastore

import com.marvelapp.coroutines.marvelhome.data.entities.Results

class MarvelHomeLocalDataStore(private val marvelCharactersDao: MarvelCharactersDao) {

    fun updateInsert(listOfMarvelCharacter: List<Results>) = marvelCharactersDao.updateInsert(listOfMarvelCharacter)
    fun getLocalMarvelCharactersInRange(limit: Int = 15, offset: Int = 0) =
        marvelCharactersDao.getLocalMarvelCharactersInRange(limit = limit, offset = offset)
}