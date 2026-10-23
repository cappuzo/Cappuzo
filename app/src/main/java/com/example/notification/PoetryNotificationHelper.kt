package com.example.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.Verse

object PoetryNotificationHelper {
    const val CHANNEL_ID = "cappuzo_daily_verse_channel"
    private const val PREFS_NAME = "cappuzo_notification_prefs"
    private const val KEY_DAILY_ENABLED = "daily_notification_enabled"

    fun initNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "بيت اليوم - روائع الشعر العربي"
            val descriptionText = "إشعارات يومية بأجمل أبيات الشعر والحكم الأدبية"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = 0xFFD4AF37.toInt()
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun isDailyNotificationEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_DAILY_ENABLED, true)
    }

    fun setDailyNotificationEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DAILY_ENABLED, enabled).apply()
    }

    fun sendDailyPoetryNotification(
        context: Context,
        poetName: String,
        verse: Verse
    ): Boolean {
        initNotificationChannel(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val fullText = "${verse.firstHemistich}\n${verse.secondHemistich}"

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("بيت اليوم • $poetName")
            .setContentText(verse.firstHemistich)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(fullText)
                    .setSummaryText("كابوزو - ديوان الأدب العربي")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(0xFFD4AF37.toInt())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            with(NotificationManagerCompat.from(context)) {
                notify(1001, builder.build())
            }
            return true
        } catch (_: SecurityException) {
            return false
        }
    }
}
