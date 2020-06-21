package com.example.imagesearch.data.repository

import androidx.lifecycle.LiveData
import com.example.imagesearch.data.db.entity.ImageDescription

interface QueryRepository {

    val queryResult: LiveData<List<ImageDescription>>
    val allFavorites: LiveData<List<ImageDescription>>

    suspend fun onNewQuery(query: String)

    suspend fun addFavorite(imageDescription: ImageDescription)
    suspend fun deleteFavorite(imageDescription: ImageDescription)
}