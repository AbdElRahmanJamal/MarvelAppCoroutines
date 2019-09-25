package com.marvelapp.coroutines.marvelhome.data.entities

import android.os.Parcelable
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.marvelapp.coroutines.marvelhome.data.localdatastore.ItemsDataConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Events(

    @SerializedName("available") val available: Int,
    @SerializedName("collectionURI") val collectionURI: String?,
    @TypeConverters(ItemsDataConverter::class)  @SerializedName("items") val items: List<Items>?,
    @SerializedName("returned") val returned: Int
) : Parcelable