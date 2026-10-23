package com.example.data.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val era: Era,
    val category: String, // دواوين شعر، فلسفة وأدب، نقد ولغة، حكمة
    val description: String,
    val totalChapters: Int,
    val featuredExcerpt: String,
    val keyTopics: List<String>
)
