package com.example.imagesearch.internals

enum class QueryState {
    SUCCESS,
    NETWORK_ERROR,
    DB_ERROR,
    UNKNOWN_ERROR,
    RUNNING,
    IDLE
}