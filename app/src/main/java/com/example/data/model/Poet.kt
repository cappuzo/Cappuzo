package com.example.data.model

data class Poet(
    val id: String,
    val name: String,
    val epithet: String, // اللقب (مثل: أمير الشعراء، شاعر العرب، رهين المحبسين)
    val era: Era,
    val lifespan: String,
    val birthplace: String,
    val biography: String,
    val famousVerse: Verse,
    val featuredPoems: List<String>, // Poem IDs
    val diwanTitle: String,
    val isFeatured: Boolean = false
)
