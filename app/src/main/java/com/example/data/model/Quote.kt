package com.example.data.model

data class Quote(
    val id: String,
    val text: String,
    val author: String,
    val source: String,
    val era: Era,
    val category: String // حكمة، بلاغة، أدب النفس، وفاء
)
