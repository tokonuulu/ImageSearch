package com.example.imagesearch.ui.image.query

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _queryChangeEvent = MutableLiveData<String>()
    val queryChangeEvent : LiveData<String> = _queryChangeEvent


    fun onSearchButtonClicked(query: String) {
        if (!query.isEmpty())
            _queryChangeEvent.value = query
    }
}