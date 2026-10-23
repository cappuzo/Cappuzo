package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.SearchCategory
import com.example.ui.components.BookCard
import com.example.ui.components.OrnamentalDivider
import com.example.ui.components.PoetCard
import com.example.ui.components.QuoteCard
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.BlackCard
import com.example.ui.theme.BlackCardElevated
import com.example.ui.theme.BlackObsidian
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBorderSubtle
import com.example.ui.theme.GoldBronze
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldLuminous
import com.example.ui.theme.GoldRoyal
import com.example.ui.theme.TextIvoryPrimary
import com.example.ui.theme.TextIvorySecondary
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.CappuzoViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun SearchScreen(
    viewModel: CappuzoViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val query by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedSearchCategory.collectAsState()
    val results by viewModel.searchResults.collectAsState()
    val bookmarks by viewModel.allBookmarks.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .testTag("search_screen")
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "رجوع",
                    tint = GoldRoyal
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "البحث في ديوان كابوزو",
                fontFamily = AmiriFontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = GoldRoyal
            )
        }

        // Search Input Field
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_text_field"),
                placeholder = {
                    Text(
                        text = "ابحث عن شاعر، بيت، كتاب، أو حكمة…",
                        fontFamily = CairoFontFamily,
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = GoldRoyal
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "مسح",
                                tint = GoldChampagne
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BlackCard,
                    unfocusedContainerColor = BlackCard,
                    focusedBorderColor = GoldRoyal,
                    unfocusedBorderColor = GoldBorder,
                    cursorColor = GoldRoyal,
                    focusedTextColor = TextIvoryPrimary,
                    unfocusedTextColor = TextIvoryPrimary
                )
            )
        }

        // Category Filter Tabs
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SearchCategory.values()) { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) GoldRoyal else BlackCardElevated)
                        .border(
                            1.dp,
                            if (isSelected) GoldLuminous else GoldBorderSubtle,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { viewModel.onSearchCategoryChange(category) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = category.title,
                        fontFamily = CairoFontFamily,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) BlackObsidian else TextIvoryPrimary
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp)
        ) {
            if (query.trim().isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = GoldBronze.copy(alpha = 0.5f),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "اكتب كلمة للبحث في التراث العربي",
                            fontFamily = AmiriFontFamily,
                            fontSize = 18.sp,
                            color = GoldChampagne
                        )
                        Text(
                            text = "يمكنك البحث عن (المتنبي، الجاهلي، الحكمة، الأندلس، المعلقات...)",
                            fontFamily = CairoFontFamily,
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else if (results.totalCount == 0) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "لم نعثر على نتائج مطابقة لـ «$query»",
                            fontFamily = AmiriFontFamily,
                            fontSize = 17.sp,
                            color = TextIvorySecondary
                        )
                    }
                }
            } else {
                // Matching Poets
                if (results.matchingPoets.isNotEmpty()) {
                    item {
                        OrnamentalDivider(emblem = "الشعراء المطابقون (${results.matchingPoets.size})")
                    }
                    items(results.matchingPoets) { poet ->
                        PoetCard(
                            poet = poet,
                            onClick = { viewModel.navigateTo(Screen.PoetDetail(poet.id)) },
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }

                // Matching Poems
                if (results.matchingPoems.isNotEmpty()) {
                    item {
                        OrnamentalDivider(emblem = "القصائد المطابقة (${results.matchingPoems.size})")
                    }
                    items(results.matchingPoems) { poem ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    1.dp,
                                    Brush.horizontalGradient(listOf(GoldBorder, GoldBorderSubtle)),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { viewModel.navigateTo(Screen.PoemDetail(poem.id)) },
                            colors = CardDefaults.cardColors(containerColor = BlackCard)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(BlackCardElevated),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = GoldRoyal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = poem.title,
                                        fontFamily = AmiriFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = GoldRoyal
                                    )
                                    Text(
                                        text = "${poem.poetName} • ${poem.meter}",
                                        fontFamily = CairoFontFamily,
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = GoldChampagne,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Matching Books
                if (results.matchingBooks.isNotEmpty()) {
                    item {
                        OrnamentalDivider(emblem = "الكتب والدواوين المطابقة (${results.matchingBooks.size})")
                    }
                    items(results.matchingBooks) { book ->
                        BookCard(
                            book = book,
                            onClick = { viewModel.navigateTo(Screen.Books) },
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }

                // Matching Quotes
                if (results.matchingQuotes.isNotEmpty()) {
                    item {
                        OrnamentalDivider(emblem = "المأثورات والحكم المطابقة (${results.matchingQuotes.size})")
                    }
                    items(results.matchingQuotes) { quote ->
                        val isSaved = bookmarks.any { it.itemId == quote.id }
                        QuoteCard(
                            quote = quote,
                            isBookmarked = isSaved,
                            onBookmarkToggle = {
                                viewModel.toggleBookmark(
                                    itemId = quote.id,
                                    type = "QUOTE",
                                    title = quote.text,
                                    subtitle = quote.author,
                                    previewText = quote.text,
                                    eraName = quote.era.arabicName
                                )
                            },
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
