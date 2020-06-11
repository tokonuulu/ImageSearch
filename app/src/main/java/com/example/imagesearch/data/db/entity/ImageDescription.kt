package com.example.imagesearch.data.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "favorites")
data class ImageDescription(
    @Expose
    var queryString: String,

    @SerializedName("display_sitename")
    val displaySitename: String,

    val height: Int,
    val width: Int,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}