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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Book
import com.example.ui.components.BookCard
import com.example.ui.components.OrnamentalDivider
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
import com.example.ui.viewmodel.CappuzoViewModel

@Composable
fun BooksScreen(
    viewModel: CappuzoViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("الكل") }
    var selectedBookForDetail by remember { mutableStateOf<Book?>(null) }

    val categories = listOf("الكل", "دواوين شعرية", "أدب وفلسفة نفسية", "حكمة وأمثال أدبية", "أصول الشعر العربي", "شعر حديث ومعاصر")

    val filteredBooks = if (selectedCategory == "الكل") {
        viewModel.allBooks
    } else {
        viewModel.allBooks.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .testTag("books_screen")
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
            Column {
                Text(
                    text = "خزانة الكتب والدواوين",
                    fontFamily = AmiriFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldRoyal
                )
                Text(
                    text = "أمهات كتب الأدب والشعر ونفائس المخطوطات",
                    fontFamily = CairoFontFamily,
                    fontSize = 11.sp,
                    color = GoldChampagne.copy(alpha = 0.8f)
                )
            }
        }

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
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
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = category,
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
            item {
                OrnamentalDivider(emblem = "مؤلفات الأدب العربي (${filteredBooks.size})")
            }

            items(filteredBooks) { book ->
                BookCard(
                    book = book,
                    onClick = {
                        selectedBookForDetail = book
                    },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }

    // Book Detail Modal Dialog
    if (selectedBookForDetail != null) {
        val book = selectedBookForDetail!!
        Dialog(onDismissRequest = { selectedBookForDetail = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, GoldBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = BlackCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = GoldRoyal,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = book.title,
                                fontFamily = AmiriFontFamily,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldRoyal
                            )
                        }

                        IconButton(onClick = { selectedBookForDetail = null }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = GoldChampagne
                            )
                        }
                    }

                    Text(
                        text = "المؤلف: ${book.author} • ${book.era.arabicName}",
                        fontFamily = CairoFontFamily,
                        fontSize = 12.sp,
                        color = GoldChampagne,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = book.description,
                        fontFamily = AmiriFontFamily,
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        color = TextIvoryPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Excerpt Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(BlackCardElevated)
                            .border(0.5.dp, GoldBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "مقتطف من الكتاب:",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = GoldLuminous
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "«${book.featuredExcerpt}»",
                                fontFamily = AmiriFontFamily,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = TextIvorySecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Key topics
                    Text(
                        text = "أبرز الأبواب والمواضيع:",
                        fontFamily = CairoFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldChampagne
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        book.keyTopics.forEach { topic ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldBronze.copy(alpha = 0.2f))
                                    .border(0.5.dp, GoldBorder.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = topic,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 10.sp,
                                    color = GoldChampagne
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
