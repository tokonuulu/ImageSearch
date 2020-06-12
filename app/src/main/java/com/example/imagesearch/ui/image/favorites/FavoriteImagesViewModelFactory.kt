package com.example.imagesearch.ui.image.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imagesearch.data.repository.QueryRepository

class FavoriteImagesViewModelFactory (
    private val queryRepository: QueryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoriteImagesViewModel(queryRepository) as T
    }
}