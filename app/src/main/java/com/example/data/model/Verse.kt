package com.example.data.model

data class Verse(
    val number: Int,
    val firstHemistich: String, // الصدر
    val secondHemistich: String  // العجز
) {
    val fullText: String
        get() = "$firstHemistich ... $secondHemistich"
}
