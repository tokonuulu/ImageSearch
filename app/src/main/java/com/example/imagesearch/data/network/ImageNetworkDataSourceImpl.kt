package com.example.imagesearch.data.network

import com.example.imagesearch.data.db.entity.ImageDescription

class ImageNetworkDataSourceImpl(
    private val apiService: ImageApiService
) : ImageNetworkDataSource {

    override suspend fun fetchQueryResponse(query: String): List<ImageDescription> {
        val fetchedQueryResponse = apiService.searchImages(query)
            .await().documents

        fetchedQueryResponse.forEach {
            it.id = "${it.displaySitename}&${it.imageUrl}"
            it.queryString = query
            it.isFavorite = false
        }

        return fetchedQueryResponse
    }
}