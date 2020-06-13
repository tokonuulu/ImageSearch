package com.example.imagesearch.ui.image.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.repository.QueryRepository
import com.example.imagesearch.internals.lazyDeferred
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class FavoriteImagesViewModel(
    private val queryRepository: QueryRepository
) : ViewModel() {

    val allFavorites by lazyDeferred {
        queryRepository.getAllFavorites()
    }

    fun onImageClicked(imageDescription: ImageDescription) {
        imageDescription.isFavorite = false
        queryRepository.deleteFavorite(imageDescription)
    }
}
