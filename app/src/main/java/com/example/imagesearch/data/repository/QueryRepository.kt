package com.example.imagesearch.data.repository

import androidx.lifecycle.LiveData
import com.example.imagesearch.data.db.entity.ImageDescription

interface QueryRepository {

    suspend fun getQueryResult(query: String) : LiveData<List<ImageDescription>>
    suspend fun getAllFavorites() : LiveData<List<ImageDescription>>

    fun addFavorite(imageDescription: ImageDescription)
    fun deleteFavorite(imageDescription: ImageDescription)
}