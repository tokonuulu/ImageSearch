package com.example.imagesearch.ui.image.query

import android.content.SearchRecentSuggestionsProvider

class SuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.example.imagesearch.ui.image.query.SuggestionProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }
}