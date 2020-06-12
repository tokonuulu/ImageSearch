package com.example.imagesearch.data.db

import androidx.lifecycle.LiveData
import com.example.imagesearch.data.db.entity.ImageDescription

interface ImageLocalDataSource {

    fun addFavorite(imageDescription: ImageDescription)

    fun deleteFavorite(imageDescription: ImageDescription)

    suspend fun fetchNonLiveFavorites() : List<ImageDescription>
    suspend fun fetchAllFavorites() : LiveData<List<ImageDescription>>
}