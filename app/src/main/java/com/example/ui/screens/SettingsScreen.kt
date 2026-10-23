package com.example.ui.screens

import android.Manifest
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.SocialPrefsHelper
import com.example.data.model.Verse
import com.example.notification.PoetryNotificationHelper
import com.example.ui.components.OrnamentalDivider
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.AppThemeMode
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
import com.example.ui.theme.OledPureBlack
import com.example.ui.theme.TextIvoryPrimary
import com.example.ui.theme.TextIvorySecondary
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.CappuzoViewModel

@Composable
fun SettingsScreen(
    viewModel: CappuzoViewModel,
    onBack: () -> Unit,
    onOpenPublishingGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentThemeMode by viewModel.currentThemeMode.collectAsState()

    var isNotificationEnabled by remember {
        mutableStateOf(PoetryNotificationHelper.isDailyNotificationEnabled(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            PoetryNotificationHelper.setDailyNotificationEnabled(context, true)
            isNotificationEnabled = true
            Toast.makeText(context, "تم تفعيل إشعارات بيت اليوم", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "إذن الإشعارات مطلوب", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("settings_screen")
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
                    text = "الإعدادات وعن التطبيق",
                    fontFamily = AmiriFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldRoyal
                )
                Text(
                    text = "تواصل معنا، حساباتنا الرسمية، وخيارات التطبيق",
                    fontFamily = CairoFontFamily,
                    fontSize = 11.sp,
                    color = GoldChampagne.copy(alpha = 0.8f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp)
        ) {
            // SECTION: Theme Mode / Power Saving
            item {
                OrnamentalDivider(emblem = "مظهر التطبيق • توفير الطاقة")
            }

            item {
                Text(
                    text = "اختر السمة البصرية المناسبة؛ يدعم كابوزو سواد الـ OLED الخالص لإطفاء بكسلات الشاشة وتوفير البطارية:",
                    fontFamily = CairoFontFamily,
                    fontSize = 13.sp,
                    color = TextIvorySecondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Theme 1: Classical Gold
            item {
                val isSelected = currentThemeMode == AppThemeMode.CLASSICAL_GOLD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = if (isSelected) 1.8.dp else 1.dp,
                            brush = if (isSelected) Brush.horizontalGradient(listOf(GoldRoyal, GoldLuminous))
                            else Brush.horizontalGradient(listOf(GoldBorderSubtle, GoldBorderSubtle)),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            viewModel.setThemeMode(AppThemeMode.CLASSICAL_GOLD)
                        }
                        .testTag("theme_classical_gold"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Visual Palette Preview Swatch
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(BlackObsidian)
                                .border(1.5.dp, GoldRoyal, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = GoldRoyal,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "الذهب الكلاسيكي الملكي",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) GoldLuminous else TextIvoryPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldRoyal.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "الافتراضي",
                                        fontFamily = CairoFontFamily,
                                        fontSize = 10.sp,
                                        color = GoldChampagne
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "سواد ملكي حبري مع نقوش وزخارف ذهبية مستوحاة من المخطوطات القديمة",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = TextMuted,
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Selection Indicator
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) GoldRoyal else Color.Transparent)
                                .border(1.5.dp, if (isSelected) GoldRoyal else TextMuted, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "محدد",
                                    tint = BlackObsidian,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Theme 2: OLED Pure Black
            item {
                val isSelected = currentThemeMode == AppThemeMode.OLED_BLACK
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = if (isSelected) 1.8.dp else 1.dp,
                            brush = if (isSelected) Brush.horizontalGradient(listOf(Color(0xFF10B981), GoldRoyal))
                            else Brush.horizontalGradient(listOf(GoldBorderSubtle, GoldBorderSubtle)),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            viewModel.setThemeMode(AppThemeMode.OLED_BLACK)
                        }
                        .testTag("theme_oled_black"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Visual Pure Black Swatch
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(OledPureBlack)
                                .border(1.5.dp, Color(0xFF10B981), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BatteryChargingFull,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "أسود أوليد فائق التوفير (OLED)",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) GoldLuminous else TextIvoryPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF10B981).copy(alpha = 0.25f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "⚡ توفير البطارية",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = Color(0xFF10B981)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "سواد نقي 100% (#000000) يطفئ بكسلات شاشات OLED وAMOLED كلياً لحفظ الطاقة",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = TextMuted,
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Selection Indicator
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) GoldRoyal else Color.Transparent)
                                .border(1.5.dp, if (isSelected) GoldRoyal else TextMuted, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "محدد",
                                    tint = BlackObsidian,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // SECTION: Contact Us / Social Accounts
            item {
                OrnamentalDivider(emblem = "تواصل معنا • تابعنا")
            }

            item {
                Text(
                    text = "تواصل معنا عبر منصات التواصل الاجتماعي الرسمية لكابوزو:",
                    fontFamily = CairoFontFamily,
                    fontSize = 13.sp,
                    color = TextIvorySecondary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            // Clickable Instagram Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            Brush.horizontalGradient(listOf(GoldBorder, GoldBorderSubtle)),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            SocialPrefsHelper.openInstagramOfficial(context)
                        }
                        .testTag("instagram_clickable_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackCardElevated)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Instagram Branded Badge
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFF833AB4),
                                            Color(0xFFFD1D1D),
                                            Color(0xFFFCB045)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_instagram_logo),
                                contentDescription = "شعار انستقرام",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "انستقرام • Instagram",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = GoldLuminous
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = SocialPrefsHelper.INSTAGRAM_USERNAME,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 12.sp,
                                    color = GoldChampagne
                                )
                            }
                            Text(
                                text = SocialPrefsHelper.INSTAGRAM_URL,
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldRoyal)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "متابعة",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = BlackObsidian
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = "فتح انستقرام",
                                    tint = BlackObsidian,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Clickable Facebook Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            Brush.horizontalGradient(listOf(GoldBorder, GoldBorderSubtle)),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            SocialPrefsHelper.openFacebookOfficial(context)
                        }
                        .testTag("facebook_clickable_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackCardElevated)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Facebook Branded Badge
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFF1877F2),
                                            Color(0xFF0D5BB5)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_facebook_logo),
                                contentDescription = "شعار فيسبوك",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "فيسبوك • Facebook",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = GoldLuminous
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = SocialPrefsHelper.FACEBOOK_PAGE_NAME,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 12.sp,
                                    color = GoldChampagne
                                )
                            }
                            Text(
                                text = SocialPrefsHelper.FACEBOOK_URL,
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldRoyal)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "زيارة",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = BlackObsidian
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = "فتح فيسبوك",
                                    tint = BlackObsidian,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Share App Button
            item {
                OutlinedButton(
                    onClick = {
                        val shareText = "استكشف روائع الشعر العربي والدواوين الكلاسيكية مع تطبيق كابوزو • Cappuzo:\n" +
                                "انستقرام: ${SocialPrefsHelper.INSTAGRAM_URL}\n" +
                                "فيسبوك: ${SocialPrefsHelper.FACEBOOK_URL}"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "مشاركة تطبيق كابوزو"))
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldChampagne),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(GoldBorder)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "مشاركة روابط كابوزو مع الأصدقاء",
                        fontFamily = CairoFontFamily,
                        fontSize = 13.sp
                    )
                }
            }

            // SECTION: About Application
            item {
                OrnamentalDivider(emblem = "عن تطبيق كابوزو")
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
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
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cappuzo_logo),
                            contentDescription = "شعار كابوزو",
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, GoldRoyal, CircleShape)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "كابوزو • ديوان الأدب والشعر",
                            fontFamily = AmiriFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = GoldRoyal
                        )

                        Text(
                            text = "الإصدار 1.0 (تطبيق أصيل أوفلاين مع خطوط الأميري وكايرو)",
                            fontFamily = CairoFontFamily,
                            fontSize = 11.sp,
                            color = GoldChampagne
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "تطبيق أندرويد نيتف صُمم ليجسد بهاء ورفعة التراث الأدبي العربي، ويغطي عصور الشعر الكلاسيكية (الجاهلي، الأموي، العباسي، الأندلسي، والحديث والمعاصر)، مع تقديم الدواوين ونفائس الحكم والمأثورات، وحفظ المفضلة محلياً بدون إعلانات مزعجة وبأعلى جودة بصرية.",
                            fontFamily = AmiriFontFamily,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            color = TextIvoryPrimary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Justify
                        )
                    }
                }
            }

            // SECTION: Notifications
            item {
                OrnamentalDivider(emblem = "إعدادات الإشعارات")
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
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
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "إشعار بيت اليوم اليومي",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = GoldLuminous
                                )
                                Text(
                                    text = "تلقي بيت شعر كلاسيكي مختار كل يوم",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    color = TextIvorySecondary
                                )
                            }

                            Switch(
                                checked = isNotificationEnabled,
                                onCheckedChange = { checked ->
                                    if (checked) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        } else {
                                            PoetryNotificationHelper.setDailyNotificationEnabled(context, true)
                                            isNotificationEnabled = true
                                        }
                                    } else {
                                        PoetryNotificationHelper.setDailyNotificationEnabled(context, false)
                                        isNotificationEnabled = false
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = GoldRoyal,
                                    checkedTrackColor = GoldRoyal.copy(alpha = 0.4f),
                                    uncheckedThumbColor = TextIvorySecondary,
                                    uncheckedTrackColor = BlackObsidian
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                                val ok = PoetryNotificationHelper.sendDailyPoetryNotification(
                                    context = context,
                                    poetName = "أبو الطيب المتنبي",
                                    verse = Verse(
                                        1,
                                        "الخَيلُ وَاللَيلُ وَالبَيداءُ تَعرِفُني",
                                        "وَالسَيفُ وَالرُمحُ وَالقِرطاسُ وَالقَلَمُ"
                                    )
                                )
                                if (ok) {
                                    Toast.makeText(context, "تم إرسال إشعار تجريبي!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "يرجى منح إذن الإشعارات", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldRoyal)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تجربة إشعار بيت اليوم الآن",
                                fontFamily = CairoFontFamily,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // SECTION: Publishing Guide
            item {
                OrnamentalDivider(emblem = "النشر على متجر Google Play")
            }

            item {
                Button(
                    onClick = onOpenPublishingGuide,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldRoyal,
                        contentColor = BlackObsidian
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "عرض دليل تصدير حزمة AAB الموقعة",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
