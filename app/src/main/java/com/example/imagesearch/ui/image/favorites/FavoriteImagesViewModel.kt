package com.example.imagesearch.ui.image.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.repository.QueryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteImagesViewModel(
    private val queryRepository: QueryRepository
) : ViewModel() {

    val allFavorites: LiveData<List<ImageDescription>> = queryRepository.allFavorites

    fun onImageClicked(imageDescription: ImageDescription) {
        viewModelScope.launch(Dispatchers.IO) {
            imageDescription.isFavorite = false
            queryRepository.deleteFavorite(imageDescription)
        }
    }
}
