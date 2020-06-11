package com.example.imagesearch.ui.image.query

import androidx.lifecycle.ViewModel
import com.example.imagesearch.data.repository.QueryRepository
import com.example.imagesearch.internals.lazyDeferred

class ImageQueryViewModel (
    private val queryRepository: QueryRepository
) : ViewModel() {
    val query : String
        get() = "query"

    val queryResponse by lazyDeferred {
        queryRepository.getQueryResult(query)
    }

}