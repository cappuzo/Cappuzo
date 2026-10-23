package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.BlackCard
import com.example.ui.theme.BlackCardElevated
import com.example.ui.theme.BlackObsidian
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldLuminous
import com.example.ui.theme.GoldRoyal
import com.example.ui.theme.TextIvoryPrimary
import com.example.ui.theme.TextIvorySecondary

@Composable
fun PublishingGuideDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
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
                    .verticalScroll(scrollState)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = GoldRoyal,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "دليل تصدير حزمة AAB للنشر",
                            fontFamily = AmiriFontFamily,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldRoyal
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = GoldChampagne
                        )
                    }
                }

                Text(
                    text = "خطوات بناء حزمة التطبيق الموقعة (Signed Android App Bundle) المعتمدة على متجر Google Play:",
                    fontFamily = CairoFontFamily,
                    fontSize = 13.sp,
                    color = TextIvorySecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                GuideStep(
                    stepNumber = "١",
                    title = "إنشاء مفتاح التوقيع (Keystore)",
                    explanation = "قم بتشغيل هذا الأمر في الطرفية لإنشاء مفتاح التوقيع الخاص بك:",
                    command = "keytool -genkey -v -keystore my-upload-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload",
                    onCopy = { copyText(context, it) }
                )

                GuideStep(
                    stepNumber = "٢",
                    title = "تعيين المتغيرات البيئية",
                    explanation = "قم بضبط مسار المفتاح وكلمة المرور في بيئة البناء:",
                    command = "export KEYSTORE_PATH=\"./my-upload-key.jks\"\nexport STORE_PASSWORD=\"your_password\"\nexport KEY_PASSWORD=\"your_password\"",
                    onCopy = { copyText(context, it) }
                )

                GuideStep(
                    stepNumber = "٣",
                    title = "بناء حزمة AAB",
                    explanation = "نفّذ أمر Gradle المخصص لتوليد حزمة Release AAB:",
                    command = "gradle :app:bundleRelease",
                    onCopy = { copyText(context, it) }
                )

                GuideStep(
                    stepNumber = "٤",
                    title = "موقع الحزمة الناتجة",
                    explanation = "ستجد الحزمة الموقعة الجاهزة للرفع على مسار:",
                    command = "app/build/outputs/bundle/release/app-release.aab",
                    onCopy = { copyText(context, it) }
                )

                GuideStep(
                    stepNumber = "٥",
                    title = "الرفع إلى Google Play Console",
                    explanation = "سجّل الدخول إلى Google Play Console، أنشئ إصداراً جديداً (Release) في مسار الإنتاج أو الاختبار الداخلي، واسحب ملف app-release.aab.",
                    command = null,
                    onCopy = null
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldRoyal,
                        contentColor = BlackObsidian
                    )
                ) {
                    Text(
                        text = "فهمت الخطوات، عودة للتطبيق",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideStep(
    stepNumber: String,
    title: String,
    explanation: String,
    command: String?,
    onCopy: ((String) -> Unit)?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BlackCardElevated)
            .border(1.dp, GoldBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(GoldRoyal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    color = BlackObsidian,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    fontFamily = CairoFontFamily
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = GoldLuminous
            )
        }

        Text(
            text = explanation,
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = TextIvoryPrimary,
            modifier = Modifier.padding(vertical = 6.dp)
        )

        if (command != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF07080B))
                    .border(0.5.dp, GoldBorder.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = command,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = GoldChampagne,
                        modifier = Modifier.weight(1f)
                    )

                    if (onCopy != null) {
                        IconButton(
                            onClick = { onCopy(command) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "نسخ الأمر",
                                tint = GoldRoyal,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun copyText(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("أمر البناء", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "تم نسخ الأمر إلى الحافظة", Toast.LENGTH_SHORT).show()
}
