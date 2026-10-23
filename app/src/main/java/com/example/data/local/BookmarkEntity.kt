package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: String,
    val itemType: String, // "POEM", "POET", "QUOTE", "BOOK"
    val title: String,
    val subtitle: String,
    val previewText: String,
    val eraName: String,
    val savedAtTimestamp: Long = System.currentTimeMillis()
)
