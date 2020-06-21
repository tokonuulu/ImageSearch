package com.example.imagesearch.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imagesearch.data.db.ImageLocalDataSource
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.network.ImageNetworkDataSource

class QueryRepositoryImpl(
    private val imageLocalDataSource: ImageLocalDataSource,
    private val imageNetworkDataSource: ImageNetworkDataSource
) : QueryRepository {

    private val _queryResult = MutableLiveData<List<ImageDescription>>()
    override val queryResult = _queryResult

    override val allFavorites: LiveData<List<ImageDescription>> = imageLocalDataSource.allFavorites

    override suspend fun onNewQuery(query: String) {
        val networkQueryResult = imageNetworkDataSource.fetchQueryResponse(query)
        val mapOfIDs = imageLocalDataSource.fetchNonLiveFavorites().map { it.id to it }.toMap()
        val mergedList = networkQueryResult.map { mapOfIDs[it.id] ?: it }
        _queryResult.postValue(mergedList)
    }

    override suspend fun addFavorite(imageDescription: ImageDescription) {
        imageLocalDataSource.addFavorite(imageDescription)
        updateQueryResult(imageDescription)
    }

    override suspend fun deleteFavorite(imageDescription: ImageDescription) {
        imageLocalDataSource.deleteFavorite(imageDescription)
        updateQueryResult(imageDescription)
    }

    private fun updateQueryResult(imageDescription: ImageDescription) {

        val checkList = _queryResult.value ?: emptyList()
        if (checkList.isEmpty())
            return

        val curList = _queryResult.value

        curList?.forEach {
            if (it.id == imageDescription.id)
                it.isFavorite = imageDescription.isFavorite
        }

        _queryResult.postValue(curList)
    }
}