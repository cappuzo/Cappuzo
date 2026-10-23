package com.example.data.repository

import com.example.data.local.BookmarkDao
import com.example.data.local.BookmarkEntity
import com.example.data.model.Book
import com.example.data.model.Era
import com.example.data.model.Poem
import com.example.data.model.Poet
import com.example.data.model.Quote
import com.example.data.model.Verse
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

data class DailyVerseInfo(
    val verse: Verse,
    val poet: Poet,
    val poem: Poem?
)

enum class SearchCategory(val title: String) {
    ALL("الكل"),
    POETS("الشعراء"),
    POEMS("القصائد"),
    BOOKS("الكتب"),
    QUOTES("الاقتباسات")
}

data class SearchResults(
    val matchingPoets: List<Poet> = emptyList(),
    val matchingPoems: List<Poem> = emptyList(),
    val matchingBooks: List<Book> = emptyList(),
    val matchingQuotes: List<Quote> = emptyList()
) {
    val totalCount: Int
        get() = matchingPoets.size + matchingPoems.size + matchingBooks.size + matchingQuotes.size
}

class PoetryRepository(
    private val bookmarkDao: BookmarkDao
) {
    fun getEras(): List<Era> = Era.values().toList()

    fun getAllPoets(): List<Poet> = PoetryData.poets

    fun getFeaturedPoets(): List<Poet> = PoetryData.poets.filter { it.isFeatured }

    fun getPoetsByEra(era: Era): List<Poet> = PoetryData.poets.filter { it.era == era }

    fun getPoetById(id: String): Poet? = PoetryData.poets.find { it.id == id }

    fun getAllPoems(): List<Poem> = PoetryData.poems

    fun getPoemsByPoet(poetId: String): List<Poem> = PoetryData.poems.filter { it.poetId == poetId }

    fun getPoemById(id: String): Poem? = PoetryData.poems.find { it.id == id }

    fun getAllBooks(): List<Book> = PoetryData.books

    fun getBookById(id: String): Book? = PoetryData.books.find { it.id == id }

    fun getAllQuotes(): List<Quote> = PoetryData.quotes

    fun getDailyVerse(): DailyVerseInfo {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val poems = PoetryData.poems
        val selectedPoem = poems[dayOfYear % poems.size]
        val verseIndex = (dayOfYear * 3) % selectedPoem.verses.size
        val selectedVerse = selectedPoem.verses[verseIndex]
        val poet = getPoetById(selectedPoem.poetId) ?: PoetryData.poets.first()
        return DailyVerseInfo(
            verse = selectedVerse,
            poet = poet,
            poem = selectedPoem
        )
    }

    companion object {
        fun normalizeArabic(text: String): String {
            return text
                .replace(Regex("[\\u064B-\\u065F\\u0670]"), "") // remove tashkeel & harakat
                .replace(Regex("[إأآٱ]"), "ا") // normalize alef variants
                .replace('ة', 'ه')
                .replace('ى', 'ي')
                .replace(Regex("\\s+"), " ")
                .trim()
                .lowercase()
        }
    }

    fun search(query: String, category: SearchCategory = SearchCategory.ALL): SearchResults {
        val cleanQuery = normalizeArabic(query)
        if (cleanQuery.isEmpty()) return SearchResults()

        val poets = if (category == SearchCategory.ALL || category == SearchCategory.POETS) {
            PoetryData.poets.filter {
                normalizeArabic(it.name).contains(cleanQuery) ||
                normalizeArabic(it.epithet).contains(cleanQuery) ||
                normalizeArabic(it.biography).contains(cleanQuery) ||
                normalizeArabic(it.diwanTitle).contains(cleanQuery) ||
                normalizeArabic(it.era.arabicName).contains(cleanQuery)
            }
        } else emptyList()

        val poems = if (category == SearchCategory.ALL || category == SearchCategory.POEMS) {
            PoetryData.poems.filter {
                normalizeArabic(it.title).contains(cleanQuery) ||
                normalizeArabic(it.poetName).contains(cleanQuery) ||
                normalizeArabic(it.theme).contains(cleanQuery) ||
                normalizeArabic(it.occasion).contains(cleanQuery) ||
                it.verses.any { v ->
                    normalizeArabic(v.firstHemistich).contains(cleanQuery) ||
                    normalizeArabic(v.secondHemistich).contains(cleanQuery)
                }
            }
        } else emptyList()

        val books = if (category == SearchCategory.ALL || category == SearchCategory.BOOKS) {
            PoetryData.books.filter {
                normalizeArabic(it.title).contains(cleanQuery) ||
                normalizeArabic(it.author).contains(cleanQuery) ||
                normalizeArabic(it.description).contains(cleanQuery) ||
                normalizeArabic(it.category).contains(cleanQuery)
            }
        } else emptyList()

        val quotes = if (category == SearchCategory.ALL || category == SearchCategory.QUOTES) {
            PoetryData.quotes.filter {
                normalizeArabic(it.text).contains(cleanQuery) ||
                normalizeArabic(it.author).contains(cleanQuery) ||
                normalizeArabic(it.source).contains(cleanQuery) ||
                normalizeArabic(it.category).contains(cleanQuery)
            }
        } else emptyList()

        return SearchResults(
            matchingPoets = poets,
            matchingPoems = poems,
            matchingBooks = books,
            matchingQuotes = quotes
        )
    }

    // Bookmarks integration
    fun getAllBookmarks(): Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    fun getBookmarksByType(type: String): Flow<List<BookmarkEntity>> =
        bookmarkDao.getBookmarksByType(type)

    fun isBookmarked(itemId: String, type: String): Flow<Boolean> =
        bookmarkDao.isItemBookmarked(itemId, type)

    suspend fun toggleBookmark(
        itemId: String,
        type: String,
        title: String,
        subtitle: String,
        previewText: String,
        eraName: String
    ): Boolean {
        val exists = bookmarkDao.isItemBookmarkedSync(itemId, type)
        return if (exists) {
            bookmarkDao.deleteBookmark(itemId, type)
            false
        } else {
            bookmarkDao.insertBookmark(
                BookmarkEntity(
                    itemId = itemId,
                    itemType = type,
                    title = title,
                    subtitle = subtitle,
                    previewText = previewText,
                    eraName = eraName
                )
            )
            true
        }
    }

    suspend fun removeBookmarkById(id: Long) {
        bookmarkDao.deleteBookmarkById(id)
    }
}
