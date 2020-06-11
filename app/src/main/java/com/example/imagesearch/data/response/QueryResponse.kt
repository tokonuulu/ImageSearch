package com.example.imagesearch.data.response

data class QueryResponse(
    val meta: Meta,
    val documents: List<ImageDescription>
)