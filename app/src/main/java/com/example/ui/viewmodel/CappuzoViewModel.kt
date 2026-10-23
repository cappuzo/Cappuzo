package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.BookmarkEntity
import com.example.data.model.Book
import com.example.data.model.Era
import com.example.data.model.Poem
import com.example.data.model.Poet
import com.example.data.model.Quote
import com.example.data.model.Verse
import com.example.data.local.ThemePrefsHelper
import com.example.data.repository.DailyVerseInfo
import com.example.data.repository.PoetryRepository
import com.example.data.repository.SearchCategory
import com.example.data.repository.SearchResults
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    data class Eras(val selectedEra: Era? = null) : Screen()
    object Books : Screen()
    object Search : Screen()
    object Bookmarks : Screen()
    data class PoetDetail(val poetId: String) : Screen()
    data class PoemDetail(val poemId: String) : Screen()
    data class BookDetail(val bookId: String) : Screen()
    object Settings : Screen()
}

class CappuzoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PoetryRepository

    // App Theme Mode state
    private val _currentThemeMode = MutableStateFlow(ThemePrefsHelper.getThemeMode(application))
    val currentThemeMode: StateFlow<AppThemeMode> = _currentThemeMode.asStateFlow()

    fun setThemeMode(mode: AppThemeMode) {
        _currentThemeMode.value = mode
        ThemePrefsHelper.setThemeMode(getApplication(), mode)
    }

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PoetryRepository(database.bookmarkDao())
    }

    // Navigation state with Back-stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    val screenStack: StateFlow<List<Screen>> = _screenStack.asStateFlow()
    val currentScreen: Screen get() = _screenStack.value.lastOrNull() ?: Screen.Home

    fun navigateTo(screen: Screen) {
        val current = _screenStack.value
        // Avoid duplicate pushes
        if (current.lastOrNull() != screen) {
            _screenStack.value = current + screen
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        if (current.size > 1) {
            _screenStack.value = current.dropLast(1)
            return true
        }
        return false
    }

    fun switchTab(screen: Screen) {
        _screenStack.value = listOf(screen)
    }

    // Daily Verse
    val dailyVerse: DailyVerseInfo = repository.getDailyVerse()

    // Static Data access
    val eras: List<Era> = repository.getEras()
    val allPoets: List<Poet> = repository.getAllPoets()
    val featuredPoets: List<Poet> = repository.getFeaturedPoets()
    val allPoems: List<Poem> = repository.getAllPoems()
    val allBooks: List<Book> = repository.getAllBooks()
    val allQuotes: List<Quote> = repository.getAllQuotes()

    fun getPoet(id: String): Poet? = repository.getPoetById(id)
    fun getPoem(id: String): Poem? = repository.getPoemById(id)
    fun getPoemsByPoet(poetId: String): List<Poem> = repository.getPoemsByPoet(poetId)
    fun getBook(id: String): Book? = repository.getBookById(id)

    // Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSearchCategory = MutableStateFlow(SearchCategory.ALL)
    val selectedSearchCategory: StateFlow<SearchCategory> = _selectedSearchCategory.asStateFlow()

    private val _searchResults = MutableStateFlow(SearchResults())
    val searchResults: StateFlow<SearchResults> = _searchResults.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        executeSearch()
    }

    fun onSearchCategoryChange(category: SearchCategory) {
        _selectedSearchCategory.value = category
        executeSearch()
    }

    private fun executeSearch() {
        val query = _searchQuery.value
        val category = _selectedSearchCategory.value
        _searchResults.value = repository.search(query, category)
    }

    // Bookmarks Flow
    val allBookmarks: StateFlow<List<BookmarkEntity>> =
        repository.getAllBookmarks()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleBookmark(
        itemId: String,
        type: String,
        title: String,
        subtitle: String,
        previewText: String,
        eraName: String
    ) {
        viewModelScope.launch {
            repository.toggleBookmark(
                itemId = itemId,
                type = type,
                title = title,
                subtitle = subtitle,
                previewText = previewText,
                eraName = eraName
            )
        }
    }

    fun isBookmarked(itemId: String, type: String): Boolean {
        return allBookmarks.value.any { it.itemId == itemId && it.itemType == type }
    }

    fun removeBookmark(id: Long) {
        viewModelScope.launch {
            repository.removeBookmarkById(id)
        }
    }
}
