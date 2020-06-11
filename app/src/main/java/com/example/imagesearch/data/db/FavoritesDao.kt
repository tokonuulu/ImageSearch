package com.example.imagesearch.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.imagesearch.data.db.entity.ImageDescription

@Dao
interface FavoritesDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(imageDescription: ImageDescription)

    @Query("select * from favorites where queryString = :query")
    fun getFavorites (query: String) : LiveData<List<ImageDescription>>

    @Delete
    fun deleteFavorite(imageDescription: ImageDescription)

}