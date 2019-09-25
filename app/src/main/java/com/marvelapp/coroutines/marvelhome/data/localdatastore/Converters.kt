package com.marvelapp.coroutines.marvelhome.data.localdatastore

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marvelapp.coroutines.marvelhome.data.entities.Items
import com.marvelapp.coroutines.marvelhome.data.entities.Urls
import java.util.Collections.emptyList

class ItemsDataConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Items> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Items>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Items>): String {
        return gson.toJson(someObjects)
    }
}


class URlsDataConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Urls> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Urls>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Urls>): String {
        return gson.toJson(someObjects)
    }
}