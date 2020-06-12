package com.example.imagesearch.ui.image.query

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.repository.QueryRepository
import com.example.imagesearch.internals.lazyDeferred

class ImageQueryViewModel(
    private val queryRepository: QueryRepository
) : ViewModel() {
    val query: String
        get() = "cats"

    val imageQueryResult by lazyDeferred {
        queryRepository.getQueryResult(query)
    }

    fun onImageClicked(imageDescription: ImageDescription) {
        Log.e("OnImageClicked", "image is clicked")
        imageDescription.let { image ->
            image.isFavorite = image.isFavorite.not()

            if (image.isFavorite) {
                Log.e("OnImageClicked", "image is clicked $imageDescription")
                queryRepository.addFavorite(imageDescription)
            } else {
                Log.e("OnImageClicked", "image is clicked $imageDescription")
                queryRepository.deleteFavorite(imageDescription)
            }
        }
    }

}