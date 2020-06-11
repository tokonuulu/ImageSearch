package com.example.imagesearch.data.repository

import androidx.lifecycle.LiveData
import com.example.imagesearch.data.db.entity.ImageDescription

interface QueryRepository {
    suspend fun getQueryResult(query: String) : LiveData<List<ImageDescription>>
}