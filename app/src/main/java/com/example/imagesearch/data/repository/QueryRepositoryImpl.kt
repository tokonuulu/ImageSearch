package com.example.imagesearch.data.repository

import android.media.Image
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imagesearch.data.db.FavoritesDao
import com.example.imagesearch.data.db.ImageLocalDataSource
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.network.ImageNetworkDataSource
import com.example.imagesearch.data.network.response.ConnectivityInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
import java.util.function.BiFunction

class QueryRepositoryImpl(
    private val imageLocalDataSource: ImageLocalDataSource,
    private val imageNetworkDataSource: ImageNetworkDataSource
) : QueryRepository {

    override suspend fun getQueryResult(query: String): LiveData<List<ImageDescription>> {
        return withContext(Dispatchers.IO) {
            val networkQueryResult = imageNetworkDataSource.fetchQueryResponse(query)

            val mapOfIDs = imageLocalDataSource.fetchNonLiveFavorites().map {
                (it.id to it)
            }.toMap()
            val merged = networkQueryResult.map { mapOfIDs[it.id] ?: it }

            return@withContext MutableLiveData<List<ImageDescription>>(merged)
        }
    }

    override suspend fun getAllFavorites(): LiveData<List<ImageDescription>> {
        return withContext(Dispatchers.IO) {
            return@withContext imageLocalDataSource.fetchAllFavorites()
        }
    }

    override fun addFavorite(imageDescription: ImageDescription) {
        imageLocalDataSource.addFavorite(imageDescription)
    }

    override fun deleteFavorite(imageDescription: ImageDescription) {
        imageLocalDataSource.deleteFavorite(imageDescription)
    }
}