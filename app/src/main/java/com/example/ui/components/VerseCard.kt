package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Verse
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.BlackCard
import com.example.ui.theme.BlackCardElevated
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBorderSubtle
import com.example.ui.theme.GoldBronze
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldLuminous
import com.example.ui.theme.GoldRoyal
import com.example.ui.theme.TextIvoryPrimary
import com.example.ui.theme.TextMuted

@Composable
fun VerseCard(
    verse: Verse,
    poetName: String? = null,
    poemTitle: String? = null,
    isBookmarked: Boolean = false,
    onBookmarkToggle: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bookmarkColor by animateColorAsState(
        targetValue = if (isBookmarked) GoldLuminous else TextMuted,
        label = "bookmarkColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("verse_card_${verse.number}")
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(GoldBorder, GoldBorderSubtle)
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BlackCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with optional poet name and verse number badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (poetName != null) {
                    Text(
                        text = poetName,
                        fontFamily = CairoFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = GoldChampagne
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(BlackCardElevated)
                        .border(1.dp, GoldBorderSubtle, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${verse.number}",
                        fontFamily = CairoFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldRoyal
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // الصدر (First Hemistich)
            Text(
                text = verse.firstHemistich,
                fontFamily = AmiriFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 32.sp,
                color = TextIvoryPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Classical poetic delimiter
            Text(
                text = "✦   ❦   ✦",
                color = GoldBronze,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // العجز (Second Hemistich)
            Text(
                text = verse.secondHemistich,
                fontFamily = AmiriFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 32.sp,
                color = TextIvoryPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action row: Copy, Share, Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val fullVerseText = "${verse.firstHemistich}\n${verse.secondHemistich}" +
                                (if (poetName != null) "\n— $poetName" else "")
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("شعر عربي", fullVerseText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "تم نسخ البيت إلى الحافظة", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.testTag("copy_button_${verse.number}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "نسخ البيت",
                        tint = GoldChampagne.copy(alpha = 0.8f),
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val fullVerseText = "${verse.firstHemistich}\n${verse.secondHemistich}\n\n" +
                                (if (poetName != null) "الشاعر: $poetName\n" else "") +
                                (if (poemTitle != null) "القصيدة: $poemTitle\n" else "") +
                                "عبر تطبيق كابوزو للأدب والشعر العربي"
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, fullVerseText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "مشاركة بيت الشعر"))
                    },
                    modifier = Modifier.testTag("share_button_${verse.number}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "مشاركة البيت",
                        tint = GoldChampagne.copy(alpha = 0.8f),
                        modifier = Modifier.size(19.dp)
                    )
                }

                if (onBookmarkToggle != null) {
                    IconButton(
                        onClick = onBookmarkToggle,
                        modifier = Modifier.testTag("bookmark_button_${verse.number}")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isBookmarked) "إلغاء الحفظ" else "حفظ البيت",
                            tint = bookmarkColor,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }
            }
        }
    }
}
