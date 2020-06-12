package com.example.imagesearch.data.network

import androidx.lifecycle.LiveData
import com.example.imagesearch.data.db.entity.ImageDescription

interface ImageNetworkDataSource {

    suspend fun fetchQueryResponse(query: String) : List<ImageDescription>
}