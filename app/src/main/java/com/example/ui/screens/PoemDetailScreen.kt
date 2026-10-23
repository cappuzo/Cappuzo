package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import com.example.data.local.SocialPrefsHelper
import com.example.data.model.Poem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.viewmodel.CappuzoViewModel

@Composable
fun PoemDetailScreen(
    poemId: String,
    viewModel: CappuzoViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val poem = viewModel.getPoem(poemId)
    val context = LocalContext.current
    var isReciting by remember { mutableStateOf(false) }
    var fontScale by remember { mutableFloatStateOf(1.0f) }
    val bookmarks by viewModel.allBookmarks.collectAsState()
    val isPoemBookmarked = bookmarks.any { it.itemId == poemId && it.itemType == "POEM" }

    if (poem == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(androidx.compose.material3.MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("القصيدة غير موجودة", color = TextIvoryPrimary)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .testTag("poem_detail_screen")
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
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = poem.title,
                        fontFamily = AmiriFontFamily,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldRoyal,
                        maxLines = 1
                    )
                    Text(
                        text = "${poem.poetName} • ${poem.meter}",
                        fontFamily = CairoFontFamily,
                        fontSize = 11.sp,
                        color = GoldChampagne
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Font resize toggle
                IconButton(
                    onClick = {
                        fontScale = if (fontScale >= 1.3f) 1.0f else fontScale + 0.15f
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "تغيير حجم الخط",
                        tint = GoldChampagne
                    )
                }

                // Copy whole poem
                IconButton(
                    onClick = {
                        val fullPoemText = buildString {
                            appendLine(poem.title)
                            appendLine("الشاعر: ${poem.poetName} (${poem.era.arabicName})")
                            appendLine("البحر: ${poem.meter}")
                            appendLine("—".repeat(20))
                            poem.verses.forEach {
                                appendLine("${it.number}. ${it.firstHemistich} ... ${it.secondHemistich}")
                            }
                            appendLine("\nتطبيق كابوزو للأدب العربي")
                        }
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("قصيدة عربية", fullPoemText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "تم نسخ القصيدة كاملة", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "نسخ القصيدة",
                        tint = GoldChampagne
                    )
                }

                // Share whole poem
                IconButton(
                    onClick = {
                        sharePoem(context, poem)
                    },
                    modifier = Modifier.testTag("topbar_share_poem_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "مشاركة القصيدة",
                        tint = GoldChampagne
                    )
                }

                // Bookmark poem
                IconButton(
                    onClick = {
                        viewModel.toggleBookmark(
                            itemId = poem.id,
                            type = "POEM",
                            title = poem.title,
                            subtitle = poem.poetName,
                            previewText = poem.excerpt,
                            eraName = poem.era.arabicName
                        )
                    }
                ) {
                    Icon(
                        imageVector = if (isPoemBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (isPoemBookmarked) "إلغاء حفظ القصيدة" else "حفظ القصيدة",
                        tint = if (isPoemBookmarked) GoldLuminous else GoldChampagne
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp)
        ) {
            // Poem Info Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            1.dp,
                            Brush.verticalGradient(listOf(GoldBorder, GoldBorderSubtle)),
                            RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = poem.title,
                            fontFamily = AmiriFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = GoldLuminous
                        )

                        Text(
                            text = "الشاعر: ${poem.poetName} • ${poem.era.arabicName}",
                            fontFamily = CairoFontFamily,
                            fontSize = 13.sp,
                            color = GoldChampagne
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Chips Row: Meter, Rhyme, Verses Count
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ChipBadge(text = poem.meter)
                            ChipBadge(text = poem.rhyme)
                            ChipBadge(text = "${poem.versesCount} أبيات")
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = poem.occasion,
                            fontFamily = CairoFontFamily,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = TextIvorySecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Audio Recital Simulation Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(BlackCardElevated)
                                .border(1.dp, GoldBorder.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (isReciting) GoldRoyal else GoldBronze)
                                            .border(1.dp, GoldBorder, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        IconButton(
                                            onClick = {
                                                isReciting = !isReciting
                                                if (isReciting) {
                                                    Toast.makeText(context, "بدأ الإنشاد الصوتي للأبيات", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (isReciting) Icons.Default.Stop else Icons.Default.PlayArrow,
                                                contentDescription = if (isReciting) "إيقاف الإنشاد" else "استماع للإنشاد",
                                                tint = BlackObsidian,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            text = if (isReciting) "جاري الإنشاد الصوتي البهي..." else "الإنشاد الصوتي للقصيدة",
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = GoldChampagne
                                        )
                                        Text(
                                            text = "بصوت رخيم على إيقاع ${poem.meter}",
                                            fontFamily = CairoFontFamily,
                                            fontSize = 10.sp,
                                            color = TextIvorySecondary
                                        )
                                    }
                                }

                                if (isReciting) {
                                    Icon(
                                        imageVector = Icons.Default.GraphicEq,
                                        contentDescription = null,
                                        tint = GoldLuminous,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dedicated Share Poem & Copy Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    sharePoem(context, poem)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("share_poem_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldRoyal,
                                    contentColor = BlackObsidian
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "مشاركة القصيدة كاملة",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val fullPoemText = buildString {
                                        appendLine(poem.title)
                                        appendLine("الشاعر: ${poem.poetName} (${poem.era.arabicName})")
                                        appendLine("البحر: ${poem.meter}")
                                        appendLine("—".repeat(20))
                                        poem.verses.forEach {
                                            appendLine("${it.number}. ${it.firstHemistich} ... ${it.secondHemistich}")
                                        }
                                        appendLine("\nمن تطبيق كابوزو للأدب والشعر العربي")
                                    }
                                    val clipboard =
                                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("قصيدة عربية", fullPoemText)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "تم نسخ القصيدة كاملة إلى الحافظة", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("copy_poem_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldChampagne),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(GoldBorder)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "نسخ",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            item {
                OrnamentalDivider(emblem = "أبيات القصيدة")
            }

            // Poem Verses List
            items(poem.verses) { verse ->
                val isVerseSaved = bookmarks.any { it.itemId == "${poem.id}_v_${verse.number}" }
                VerseCard(
                    verse = verse,
                    poetName = null,
                    poemTitle = poem.title,
                    isBookmarked = isVerseSaved,
                    onBookmarkToggle = {
                        viewModel.toggleBookmark(
                            itemId = "${poem.id}_v_${verse.number}",
                            type = "POEM",
                            title = verse.fullText,
                            subtitle = "${poem.poetName} • ${poem.title}",
                            previewText = verse.fullText,
                            eraName = poem.era.arabicName
                        )
                    },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun ChipBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(BlackCardElevated)
            .border(0.5.dp, GoldBorder.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            fontFamily = CairoFontFamily,
            fontSize = 11.sp,
            color = GoldChampagne
        )
    }
}

private fun sharePoem(context: Context, poem: Poem) {
    val shareText = buildString {
        appendLine("📜 ${poem.title}")
        appendLine("الشاعر: ${poem.poetName} (${poem.era.arabicName})")
        appendLine("بحر الشعر: ${poem.meter} • القافية: ${poem.rhyme}")
        appendLine("—".repeat(24))
        poem.verses.forEach { verse ->
            appendLine("${verse.number}. ${verse.firstHemistich} ... ${verse.secondHemistich}")
        }
        appendLine("—".repeat(24))
        appendLine("مُشاركة من تطبيق كابوزو للأدب والشعر العربي")
        appendLine("انستقرام: ${SocialPrefsHelper.INSTAGRAM_URL}")
        appendLine("فيسبوك: ${SocialPrefsHelper.FACEBOOK_URL}")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
        putExtra(Intent.EXTRA_SUBJECT, poem.title)
    }
    context.startActivity(Intent.createChooser(intent, "مشاركة القصيدة عبر..."))
}
