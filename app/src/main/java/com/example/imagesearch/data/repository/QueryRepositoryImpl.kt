package com.example.imagesearch.data.repository

import androidx.lifecycle.LiveData
import com.example.imagesearch.data.db.FavoritesDao
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.network.ImageNetworkDataSource
import com.example.imagesearch.data.network.response.ConnectivityInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QueryRepositoryImpl (
    private val favoritesDao: FavoritesDao,
    private val connectivityInterceptor: ConnectivityInterceptor,
    private val imageNetworkDataSource: ImageNetworkDataSource
): QueryRepository {

    override suspend fun getQueryResult(query: String): LiveData<List<ImageDescription>> {
        if (connectivityInterceptor.isOnline()) {
            /*
            * Probably a bad idea to fetch it every time (must be revised)
            * */
            imageNetworkDataSource.fetchQueryResponse(query)
            return imageNetworkDataSource.downloadedQueryResponse
        } else {
            return favoritesDao.getFavorites(query)
        }
    }

    private fun addFavorite(newFavorite: ImageDescription) {
        GlobalScope.launch(Dispatchers.IO) {
            favoritesDao.insertFavorite(newFavorite)
        }
    }
}