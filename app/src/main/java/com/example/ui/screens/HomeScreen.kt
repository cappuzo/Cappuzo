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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
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
import com.example.data.model.Era
import com.example.ui.components.BookCard
import com.example.ui.components.GoldHeaderBanner
import com.example.ui.components.OrnamentalDivider
import com.example.ui.components.PoetCard
import com.example.ui.components.QuoteCard
import com.example.ui.components.VerseCard
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.BlackCard
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.material.icons.filled.Settings
import com.example.ui.theme.TextIvorySecondary
import com.example.ui.viewmodel.CappuzoViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: CappuzoViewModel,
    onOpenNotifications: () -> Unit,
    onOpenPublishingGuide: () -> Unit,
    onOpenSocialLinks: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val daily = viewModel.dailyVerse
    val bookmarks by viewModel.allBookmarks.collectAsState()
    val isDailyBookmarked = bookmarks.any { it.itemId == "daily_verse_${daily.verse.number}" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Top App Bar Controls
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "كابوزو • ديوان الأدب",
                        fontFamily = AmiriFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldRoyal
                    )
                    Text(
                        text = "شعر، حكم، دواوين، وعصور الأدب العربي",
                        fontFamily = CairoFontFamily,
                        fontSize = 11.sp,
                        color = GoldChampagne.copy(alpha = 0.8f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenSocialLinks,
                        modifier = Modifier.testTag("home_social_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "حسابات التواصل انستقرام وفيسبوك",
                            tint = GoldLuminous
                        )
                    }

                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.Search) },
                        modifier = Modifier.testTag("home_search_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث",
                            tint = GoldRoyal
                        )
                    }

                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier.testTag("home_settings_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "الإعدادات وعن التطبيق",
                            tint = GoldRoyal
                        )
                    }
                }
            }
        }

        // Illuminated Manuscript Banner
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                GoldHeaderBanner()
            }
        }

        // Daily Verse Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldLuminous,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "بيت اليوم المختار",
                            fontFamily = AmiriFontFamily,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLuminous
                        )
                    }

                    Text(
                        text = daily.poet.name,
                        fontFamily = CairoFontFamily,
                        fontSize = 12.sp,
                        color = GoldChampagne
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                VerseCard(
                    verse = daily.verse,
                    poetName = "${daily.poet.name} (${daily.poet.era.arabicName})",
                    poemTitle = daily.poem?.title,
                    isBookmarked = isDailyBookmarked,
                    onBookmarkToggle = {
                        viewModel.toggleBookmark(
                            itemId = "daily_verse_${daily.verse.number}",
                            type = "POEM",
                            title = daily.verse.fullText,
                            subtitle = daily.poet.name,
                            previewText = daily.verse.fullText,
                            eraName = daily.poet.era.arabicName
                        )
                    }
                )

                if (daily.poem != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BlackCardElevated)
                            .border(0.5.dp, GoldBorder.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.navigateTo(Screen.PoemDetail(daily.poem.id))
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "القصيدة: ${daily.poem.title} • ${daily.poem.meter}",
                                fontFamily = CairoFontFamily,
                                fontSize = 12.sp,
                                color = GoldChampagne
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "قراءة القصيدة كاملة",
                                tint = GoldRoyal,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        item { OrnamentalDivider(emblem = "عصور الشعر العربي") }

        // Historical Eras Quick Row
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(viewModel.eras) { era ->
                        EraPill(
                            era = era,
                            onClick = {
                                viewModel.navigateTo(Screen.Eras(selectedEra = era))
                            }
                        )
                    }
                }
            }
        }

        item { OrnamentalDivider(emblem = "أعلام الشعر والأدب") }

        // Featured Poets Section
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                viewModel.featuredPoets.take(3).forEach { poet ->
                    PoetCard(
                        poet = poet,
                        onClick = {
                            viewModel.navigateTo(Screen.PoetDetail(poet.id))
                        },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }

        item { OrnamentalDivider(emblem = "حكم وأقوال أدبية") }

        // Literary Quotes Section
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                viewModel.allQuotes.take(2).forEach { quote ->
                    val isQuoteSaved = bookmarks.any { it.itemId == quote.id }
                    QuoteCard(
                        quote = quote,
                        isBookmarked = isQuoteSaved,
                        onBookmarkToggle = {
                            viewModel.toggleBookmark(
                                itemId = quote.id,
                                type = "QUOTE",
                                title = quote.text,
                                subtitle = "${quote.author} (${quote.source})",
                                previewText = quote.text,
                                eraName = quote.era.arabicName
                            )
                        },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }

        item { OrnamentalDivider(emblem = "روائع الدواوين والكتب") }

        // Featured Books Section
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                viewModel.allBooks.take(2).forEach { book ->
                    BookCard(
                        book = book,
                        onClick = {
                            viewModel.navigateTo(Screen.BookDetail(book.id))
                        },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }

        item { OrnamentalDivider(emblem = "المجتمع الأدبي والتواصل") }

        // Social Media Community Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(GoldBorder, GoldBorderSubtle)),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable(onClick = onOpenSocialLinks),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlackCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📸 🌐", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تابع كابوزو على انستقرام وفيسبوك",
                                fontFamily = AmiriFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = GoldRoyal
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = GoldChampagne,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "كن جزءاً من ملتقى عشاق الأدب والشعر العربي وشاركنا مختاراتك واقتراحاتك الأدبية.",
                        fontFamily = CairoFontFamily,
                        fontSize = 12.sp,
                        color = TextIvorySecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val context = androidx.compose.ui.platform.LocalContext.current
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(androidx.compose.ui.graphics.Color(0xFFE1306C).copy(alpha = 0.2f))
                                .border(0.5.dp, androidx.compose.ui.graphics.Color(0xFFE1306C), RoundedCornerShape(8.dp))
                                .clickable {
                                    com.example.data.local.SocialPrefsHelper.openInstagramOfficial(context)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "📸 انستقرام (@cappuzo)",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldChampagne
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(androidx.compose.ui.graphics.Color(0xFF1877F2).copy(alpha = 0.2f))
                                .border(0.5.dp, androidx.compose.ui.graphics.Color(0xFF1877F2), RoundedCornerShape(8.dp))
                                .clickable {
                                    com.example.data.local.SocialPrefsHelper.openFacebookOfficial(context)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "🌐 فيسبوك (Cappuzo)",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldChampagne
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EraPill(
    era: Era,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(135.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                Brush.verticalGradient(listOf(GoldBorder, GoldBorderSubtle)),
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BlackCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = era.emblem, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = era.arabicName,
                fontFamily = AmiriFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = GoldRoyal
            )
            Text(
                text = era.period,
                fontFamily = CairoFontFamily,
                fontSize = 10.sp,
                color = TextIvoryPrimary.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
