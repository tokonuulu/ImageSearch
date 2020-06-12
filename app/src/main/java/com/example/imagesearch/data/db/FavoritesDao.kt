package com.example.imagesearch.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.imagesearch.data.db.entity.ImageDescription

@Dao
interface FavoritesDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(imageDescription: ImageDescription)

    @Query ("select * from favorites")
    fun getAllFavorites () : LiveData<List<ImageDescription>>

    @Query ("select * from favorites")
    fun getNonLiveFavorites () : List<ImageDescription>

    @Delete
    fun deleteFavorite(imageDescription: ImageDescription)

}