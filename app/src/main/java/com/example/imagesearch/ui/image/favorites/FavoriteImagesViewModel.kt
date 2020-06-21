package com.example.imagesearch.ui.image.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.repository.QueryRepository
import com.example.imagesearch.internals.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteImagesViewModel(
    private val queryRepository: QueryRepository
) : ViewModel() {

    val allFavorites: LiveData<List<ImageDescription>> = queryRepository.allFavorites

    fun onImageClicked(imageDescription: ImageDescription) {
        GlobalScope.launch(Dispatchers.IO) {
            imageDescription.isFavorite = false
            queryRepository.deleteFavorite(imageDescription)
        }
    }
}
