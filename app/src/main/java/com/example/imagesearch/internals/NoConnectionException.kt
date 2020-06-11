package com.example.imagesearch.internals

import java.io.IOException

class NoConnectionException : IOException() {
    override val message: String?
        get() = "No internet connection"
}