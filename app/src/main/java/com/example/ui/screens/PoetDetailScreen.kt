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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.components.OrnamentalDivider
import com.example.ui.components.VerseCard
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
fun PoetDetailScreen(
    poetId: String,
    viewModel: CappuzoViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val poet = viewModel.getPoet(poetId)
    val poems = viewModel.getPoemsByPoet(poetId)
    val bookmarks by viewModel.allBookmarks.collectAsState()
    val isPoetBookmarked = bookmarks.any { it.itemId == poetId && it.itemType == "POET" }

    if (poet == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(androidx.compose.material3.MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("الشاعر غير موجود", color = TextIvoryPrimary)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .testTag("poet_detail_screen")
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = GoldRoyal
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poet.name,
                    fontFamily = AmiriFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldRoyal
                )
            }

            IconButton(
                onClick = {
                    viewModel.toggleBookmark(
                        itemId = poet.id,
                        type = "POET",
                        title = poet.name,
                        subtitle = poet.epithet,
                        previewText = poet.famousVerse.fullText,
                        eraName = poet.era.arabicName
                    )
                }
            ) {
                Icon(
                    imageVector = if (isPoetBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = if (isPoetBookmarked) "إلغاء حفظ الشاعر" else "حفظ الشاعر",
                    tint = if (isPoetBookmarked) GoldLuminous else GoldChampagne
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp)
        ) {
            // Poet Hero Profile Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            1.dp,
                            Brush.verticalGradient(listOf(GoldBorder, GoldBorderSubtle)),
                            RoundedCornerShape(18.dp)
                        ),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(BlackCardElevated)
                                .border(1.5.dp, GoldRoyal, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = poet.era.emblem, fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = poet.name,
                            fontFamily = AmiriFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = GoldRoyal
                        )

                        Text(
                            text = poet.epithet,
                            fontFamily = CairoFontFamily,
                            fontSize = 13.sp,
                            color = GoldChampagne
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Lifespan & Era badges row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BlackCardElevated)
                                    .border(0.5.dp, GoldBorder.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = poet.era.arabicName,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    color = GoldRoyal
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BlackCardElevated)
                                    .border(0.5.dp, GoldBorder.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = poet.lifespan,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    color = TextIvorySecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Biography paragraph
                        Text(
                            text = poet.biography,
                            fontFamily = AmiriFontFamily,
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            color = TextIvoryPrimary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Justify
                        )
                    }
                }
            }

            item {
                OrnamentalDivider(emblem = "شاهد من عيون شعره")
            }

            // Famous Verse
            item {
                VerseCard(
                    verse = poet.famousVerse,
                    poetName = poet.name,
                    poemTitle = poet.diwanTitle,
                    isBookmarked = bookmarks.any { it.itemId == "famous_verse_${poet.id}" },
                    onBookmarkToggle = {
                        viewModel.toggleBookmark(
                            itemId = "famous_verse_${poet.id}",
                            type = "POEM",
                            title = poet.famousVerse.fullText,
                            subtitle = poet.name,
                            previewText = poet.famousVerse.fullText,
                            eraName = poet.era.arabicName
                        )
                    }
                )
            }

            item {
                OrnamentalDivider(emblem = "القصائد والدواوين (${poems.size})")
            }

            if (poems.isEmpty()) {
                item {
                    Text(
                        text = "ديوانه قيد التوثيق في كابوزو",
                        fontFamily = CairoFontFamily,
                        color = TextMuted,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(poems) { poem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                1.dp,
                                Brush.horizontalGradient(listOf(GoldBorder, GoldBorderSubtle)),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                viewModel.navigateTo(Screen.PoemDetail(poem.id))
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = BlackCard)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(BlackCardElevated)
                                    .border(1.dp, GoldBorder, CircleShape),
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
                                    fontSize = 17.sp,
                                    color = GoldRoyal
                                )
                                Text(
                                    text = "${poem.meter} • ${poem.versesCount} أبيات • ${poem.theme}",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "قراءة القصيدة",
                                tint = GoldChampagne,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
