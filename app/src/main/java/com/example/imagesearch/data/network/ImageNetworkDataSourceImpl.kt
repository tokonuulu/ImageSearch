package com.example.imagesearch.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.db.entity.QueryResponse
import com.example.imagesearch.internals.NoConnectionException

class ImageNetworkDataSourceImpl (
    private val apiService: ImageApiService
) : ImageNetworkDataSource {

    override suspend fun fetchQueryResponse(query: String) : List<ImageDescription> {
        var fetchedQueryResponse : List<ImageDescription> = emptyList()
        try {
            fetchedQueryResponse = apiService.searchImages(query)
                .await().documents

            fetchedQueryResponse.forEach {
                    it.id = "${it.displaySitename}&${it.imageUrl}"
                    it.queryString = query
                    it.isFavorite = false
                }
            }
        catch (e : NoConnectionException) {
            Log.e("Connectivity", "No internet connection", e)

        }
        return fetchedQueryResponse
    }
}