package com.example.imagesearch.ui.image.query

import androidx.lifecycle.*
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.repository.QueryRepository
import com.example.imagesearch.internals.NoConnectionException
import com.example.imagesearch.internals.QueryState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageQueryViewModel(
    private val queryRepository: QueryRepository
) : ViewModel() {

    private val _imageList = MutableLiveData<List<ImageDescription>>()
    val imageList : LiveData<List<ImageDescription>> = _imageList

    private val _queryState = MutableLiveData<QueryState>()
    val queryState: LiveData<QueryState> = _queryState

    init {
        queryRepository.apply {
            queryResult.observeForever {
                _imageList.postValue(it)
            }
        }

        _queryState.postValue(QueryState.IDLE)
    }

    fun loadNewImages (query: String?) {
        if (query == null) return

        val handler = CoroutineExceptionHandler {_, exception->
            if (exception is NoConnectionException) {
                changeState(QueryState.NETWORK_ERROR)
            }
            else {
                changeState(QueryState.UNKNOWN_ERROR)
            }
        }

        val queryJob = viewModelScope.launch(handler) {
            changeState(QueryState.RUNNING)
            queryRepository.onNewQuery(query)
        }
        queryJob.invokeOnCompletion { throwable->
            if (throwable == null)
                changeState(QueryState.SUCCESS)
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

    private fun changeState(newState: QueryState) {
        _queryState.postValue(newState)
    }

    fun onErrorHandled () {
        changeState(QueryState.IDLE)
    }
}