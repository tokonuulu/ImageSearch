package com.example.imagesearch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.imagesearch.data.db.entity.ImageDescription

@Database(
    entities = [ImageDescription::class],
    version = 1
)
abstract class FavoritesDatabase : RoomDatabase() {

    //Dao
    abstract fun favoriteDao(): FavoritesDao

    //Database singleton
    companion object {
        @Volatile
        private var instance: FavoritesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FavoritesDatabase::class.java,
                "favorites.db"
            )
                .build()
    }
}