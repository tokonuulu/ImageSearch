package com.example.imagesearch.ui.image.query

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imagesearch.data.repository.QueryRepository

class ImageQueryViewModelFactory (
    private val queryRepository: QueryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageQueryViewModel(queryRepository) as T
    }
}