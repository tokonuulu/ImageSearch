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

    private val _downloadedQueryResponse = MutableLiveData<List<ImageDescription>>()
    override val downloadedQueryResponse: LiveData<List<ImageDescription>>
        get() = _downloadedQueryResponse

    override suspend fun fetchQueryResponse(query: String) {
        try {
            val fetchedQueryResponse = apiService.searchImages(query)
                .await()

            _downloadedQueryResponse.postValue(fetchedQueryResponse.documents.apply {
                this.forEach {
                    it.queryString = query
                }
            })
        }
        catch (e : NoConnectionException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}