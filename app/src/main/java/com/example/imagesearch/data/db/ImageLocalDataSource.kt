package com.example.imagesearch.data.db

import androidx.lifecycle.LiveData
import com.example.imagesearch.data.db.entity.ImageDescription

interface ImageLocalDataSource {

    val allFavorites : LiveData<List<ImageDescription>>

    suspend fun addFavorite(imageDescription: ImageDescription)

    suspend fun deleteFavorite(imageDescription: ImageDescription)

    suspend fun fetchNonLiveFavorites() : List<ImageDescription>
}