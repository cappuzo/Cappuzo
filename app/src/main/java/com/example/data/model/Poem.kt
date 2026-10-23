package com.example.data.model

data class Poem(
    val id: String,
    val title: String,
    val poetId: String,
    val poetName: String,
    val era: Era,
    val meter: String, // بحر الشعر (الطويل، البسيط، الكامل، الوافر، etc.)
    val rhyme: String, // الروي / القافية
    val verses: List<Verse>,
    val theme: String, // فخر، غزل، حكمة، رثاء، حماسة
    val occasion: String // مناسبة القصيدة أو الشرح
) {
    val versesCount: Int get() = verses.size
    val excerpt: String
        get() = verses.firstOrNull()?.fullText ?: ""
}
