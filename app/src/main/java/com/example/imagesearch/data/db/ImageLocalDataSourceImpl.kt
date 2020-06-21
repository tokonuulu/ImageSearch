package com.example.imagesearch.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.imagesearch.data.db.entity.ImageDescription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageLocalDataSourceImpl(
    private val favoritesDao: FavoritesDao
) : ImageLocalDataSource {

    /*
    * TODO: Implement database exception
    * */

    override val allFavorites = liveData {
        emitSource(favoritesDao.getAllFavorites())
    }

    override suspend fun addFavorite(imageDescription: ImageDescription) {
        withContext(Dispatchers.IO) {
            favoritesDao.insertFavorite(imageDescription)
        }
    }

    override suspend fun deleteFavorite(imageDescription: ImageDescription) {
        withContext(Dispatchers.IO) {
            favoritesDao.deleteFavorite(imageDescription)
        }
    }

    override suspend fun fetchNonLiveFavorites(): List<ImageDescription> {
        return withContext(Dispatchers.IO) {
            return@withContext favoritesDao.getNonLiveFavorites()
        }
    }
}