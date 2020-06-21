package com.example.imagesearch.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.imagesearch.data.db.entity.ImageDescription

@Dao
interface FavoritesDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(imageDescription: ImageDescription)

    @Query ("select * from favorites")
    fun getAllFavorites () : LiveData<List<ImageDescription>>

    @Query ("select * from favorites")
    suspend fun getNonLiveFavorites () : List<ImageDescription>

    @Delete
    suspend fun deleteFavorite(imageDescription: ImageDescription)

}