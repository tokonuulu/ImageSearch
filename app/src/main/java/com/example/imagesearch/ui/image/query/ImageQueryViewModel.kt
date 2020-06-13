package com.example.imagesearch.ui.image.query

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.repository.QueryRepository
import com.example.imagesearch.internals.lazyDeferred
import kotlinx.coroutines.*
import okhttp3.internal.notifyAll

class ImageQueryViewModel(
    private val queryRepository: QueryRepository
) : ViewModel() {

    private val _imageList = MutableLiveData<List<ImageDescription>>()
    val imageList : LiveData<List<ImageDescription>> = _imageList

    init {
        queryRepository.apply {
            queryResult.observeForever(Observer {
                _imageList.postValue(it)
            })
        }
    }

    fun loadNewImages (query: String?) {
        if (query == null) return
        queryRepository.onNewQuery(query)
    }

    fun onImageClicked(imageDescription: ImageDescription) {
        imageDescription.let { image ->
            image.isFavorite = image.isFavorite.not()

            if (image.isFavorite) {
                queryRepository.addFavorite(imageDescription)
            } else {
                queryRepository.deleteFavorite(imageDescription)
            }
        }
    }

}