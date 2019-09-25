package com.marvelapp.coroutines.marvelhome.data.localdatastore

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marvelapp.coroutines.marvelhome.data.entities.Results

@Dao
interface MarvelCharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateInsert(listOfMarvelCharacter: List<Results>)

    @Query("select * from marvel_character_list LIMIT :limit OFFSET :offset")
    fun getLocalMarvelCharactersInRange(limit: Int , offset: Int): LiveData<List<Results>>
}