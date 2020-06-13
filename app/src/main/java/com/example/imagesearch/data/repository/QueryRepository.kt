package com.example.imagesearch.data.repository

import androidx.lifecycle.LiveData
import com.example.imagesearch.data.db.entity.ImageDescription

interface QueryRepository {

    val queryResult: LiveData<List<ImageDescription>>

    fun onNewQuery(query: String)
    suspend fun getAllFavorites() : LiveData<List<ImageDescription>>

    fun addFavorite(imageDescription: ImageDescription)
    fun deleteFavorite(imageDescription: ImageDescription)
}