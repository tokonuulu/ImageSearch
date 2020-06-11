package com.example.imagesearch.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.db.entity.Meta

data class QueryResponse(
    val meta: Meta,
    val documents: List<ImageDescription>
)