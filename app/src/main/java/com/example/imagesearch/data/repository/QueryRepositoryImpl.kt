package com.example.imagesearch.data.repository

import android.media.Image
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.imagesearch.data.db.FavoritesDao
import com.example.imagesearch.data.db.ImageLocalDataSource
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.network.ImageNetworkDataSource
import com.example.imagesearch.data.network.response.ConnectivityInterceptor
import kotlinx.coroutines.*
import okhttp3.internal.toImmutableList
import java.util.function.BiFunction

class QueryRepositoryImpl(
    private val imageLocalDataSource: ImageLocalDataSource,
    private val imageNetworkDataSource: ImageNetworkDataSource
) : QueryRepository {

    private val _queryResult = MutableLiveData<List<ImageDescription>>()
    override val queryResult = _queryResult

    override fun onNewQuery(query: String) {
        var networkQueryResult: List<ImageDescription> = emptyList()
        var mapOfIDs: Map<String, ImageDescription> = emptyMap()

        runBlocking {
            networkQueryResult = imageNetworkDataSource.fetchQueryResponse(query)
            mapOfIDs = imageLocalDataSource.fetchNonLiveFavorites().map {
                (it.id to it)
            }.toMap()
        }
        val mergedList = networkQueryResult.map { mapOfIDs[it.id] ?: it }
        _queryResult.postValue(mergedList)
    }

    override suspend fun getAllFavorites(): LiveData<List<ImageDescription>> {
        return withContext(Dispatchers.IO) {
            return@withContext imageLocalDataSource.fetchAllFavorites()
        }
    }

    override fun addFavorite(imageDescription: ImageDescription) {
        imageLocalDataSource.addFavorite(imageDescription)
        updateQueryResult(imageDescription)
    }

    override fun deleteFavorite(imageDescription: ImageDescription) {
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