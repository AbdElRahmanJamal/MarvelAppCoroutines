package com.marvelapp.coroutines.marvelhome.data.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarvelCharacters(
    val id: Int,
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("copyright") val copyright: String,
    @SerializedName("attributionText") val attributionText: String,
    @SerializedName("attributionHTML") val attributionHTML: String,
    @SerializedName("etag") val etag: String,
    @Embedded(prefix = "data_") @SerializedName("data") val data: Data
) : Parcelable, MarvelCharactersBaseResponse<MarvelCharacters> {
    override fun retrieveData() = this
}
