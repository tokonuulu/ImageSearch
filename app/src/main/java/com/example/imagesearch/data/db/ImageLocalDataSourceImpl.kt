package com.example.imagesearch.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.network.ImageApiService
import com.example.imagesearch.internals.NoConnectionException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList

class ImageLocalDataSourceImpl(
    private val favoritesDao: FavoritesDao
) : ImageLocalDataSource {

    /*
    * TODO: Implement database exception
    * */

    override fun addFavorite(imageDescription: ImageDescription) {
        GlobalScope.launch(Dispatchers.IO) {
            favoritesDao.insertFavorite(imageDescription)
        }
    }

    override fun deleteFavorite(imageDescription: ImageDescription) {
        GlobalScope.launch(Dispatchers.IO) {
            favoritesDao.deleteFavorite(imageDescription)
        }
    }

    override suspend fun fetchAllFavorites(): LiveData<List<ImageDescription>> {
        return withContext(Dispatchers.IO) {
            return@withContext favoritesDao.getAllFavorites()
        }
    }

    override suspend fun fetchNonLiveFavorites(): List<ImageDescription> {
        return withContext(Dispatchers.IO) {
            return@withContext favoritesDao.getNonLiveFavorites()
        }
    }
}