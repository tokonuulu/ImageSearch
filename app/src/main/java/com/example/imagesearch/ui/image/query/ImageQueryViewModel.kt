package com.example.imagesearch.ui.image.query

import androidx.lifecycle.*
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.repository.QueryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        viewModelScope.launch(Dispatchers.IO) {
            queryRepository.onNewQuery(query)
        }
    }

    fun onImageClicked(imageDescription: ImageDescription) {
        imageDescription.let { image ->
            image.isFavorite = image.isFavorite.not()

            viewModelScope.launch(Dispatchers.IO) {
                if (image.isFavorite) {
                    queryRepository.addFavorite(imageDescription)
                } else {
                    queryRepository.deleteFavorite(imageDescription)
                }
            }
        }
    }

}