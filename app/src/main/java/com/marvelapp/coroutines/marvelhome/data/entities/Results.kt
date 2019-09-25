package com.marvelapp.coroutines.marvelhome.data.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.marvelapp.coroutines.marvelhome.data.localdatastore.URlsDataConverter
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "marvel_character_list")
data class Results(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("modified") val modified: String?,
    @SerializedName("resourceURI") val resourceURI: String?,
    @Embedded(prefix = "thumbnail_") @SerializedName("thumbnail") val thumbnail: Thumbnail?,
    @Embedded(prefix = "comics_") @SerializedName("comics") val comics: Comics?,
    @Embedded(prefix = "series_") @SerializedName("series") val series: Series?,
    @Embedded(prefix = "stories_") @SerializedName("stories") val stories: Stories?,
    @Embedded(prefix = "events_") @SerializedName("events") val events: Events?,
    @TypeConverters(URlsDataConverter::class) @SerializedName("urls") val urls: List<Urls>?
) : Parcelable