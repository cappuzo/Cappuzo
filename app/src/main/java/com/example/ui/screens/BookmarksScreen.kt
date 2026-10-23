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
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.ui.components.OrnamentalDivider
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.BlackCard
import com.example.ui.theme.BlackCardElevated
import com.example.ui.theme.BlackObsidian
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.ErrorRed
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
fun BookmarksScreen(
    viewModel: CappuzoViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookmarks by viewModel.allBookmarks.collectAsState()
    var selectedFilter by remember { mutableStateOf("الكل") }

    val filters = listOf("الكل", "قصائد وأبيات", "الشعراء", "حكم ومأثورات")

    val filteredList = when (selectedFilter) {
        "قصائد وأبيات" -> bookmarks.filter { it.itemType == "POEM" }
        "الشعراء" -> bookmarks.filter { it.itemType == "POET" }
        "حكم ومأثورات" -> bookmarks.filter { it.itemType == "QUOTE" }
        else -> bookmarks
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .testTag("bookmarks_screen")
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
                    text = "المحفوظات والمفضلة",
                    fontFamily = AmiriFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldRoyal
                )
                Text(
                    text = "الأبيات والشعراء والحكم المحفوظة في ذاكرتك الأدبية",
                    fontFamily = CairoFontFamily,
                    fontSize = 11.sp,
                    color = GoldChampagne.copy(alpha = 0.8f)
                )
            }
        }

        // Filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                val isSelected = filter == selectedFilter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) GoldRoyal else BlackCardElevated)
                        .border(
                            1.dp,
                            if (isSelected) GoldLuminous else GoldBorderSubtle,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
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
                OrnamentalDivider(emblem = "العناصر المحفوظة (${filteredList.size})")
            }

            if (filteredList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = null,
                            tint = GoldBronze.copy(alpha = 0.4f),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لا توجد محفوظات في هذا القسم حالياً",
                            fontFamily = AmiriFontFamily,
                            fontSize = 18.sp,
                            color = GoldChampagne
                        )
                        Text(
                            text = "اضغط على أيقونة الحفظ (🔖) في أي بيت أو شاعر لإضافته إلى مكتبتك",
                            fontFamily = CairoFontFamily,
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 4.dp, start = 24.dp, end = 24.dp)
                        )
                    }
                }
            } else {
                items(filteredList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                1.dp,
                                Brush.verticalGradient(listOf(GoldBorder, GoldBorderSubtle)),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                when (item.itemType) {
                                    "POET" -> viewModel.navigateTo(Screen.PoetDetail(item.itemId))
                                    "POEM" -> {
                                        // If itemId is a poem ID or verse
                                        val poem = viewModel.getPoem(item.itemId)
                                        if (poem != null) {
                                            viewModel.navigateTo(Screen.PoemDetail(poem.id))
                                        }
                                    }
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = BlackCard)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(GoldBronze.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                    Text(
                                        text = item.eraName.ifEmpty { item.itemType },
                                        fontFamily = CairoFontFamily,
                                        fontSize = 10.sp,
                                        color = GoldChampagne
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.removeBookmark(item.id) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkRemove,
                                        contentDescription = "حذف من المحفوظات",
                                        tint = ErrorRed.copy(alpha = 0.8f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = item.title,
                                fontFamily = AmiriFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = GoldRoyal
                            )

                            if (item.subtitle.isNotEmpty()) {
                                Text(
                                    text = item.subtitle,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 12.sp,
                                    color = TextIvorySecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
