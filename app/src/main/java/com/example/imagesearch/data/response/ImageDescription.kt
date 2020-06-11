package com.example.imagesearch.data.response

import com.google.gson.annotations.SerializedName

data class ImageDescription(
    val collection: String,
    val datetime: String,
    @SerializedName("display_sitename")
    val displaySitename: String,
    @SerializedName("doc_url")
    val docUrl: String,
    val height: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    val width: Int
)