package com.example.data.local

import android.content.Context
import android.content.Intent
import android.net.Uri

object SocialPrefsHelper {
    const val INSTAGRAM_URL = "https://www.instagram.com/cappuzo?mdxt=aW1wcjF1eTBiZTVl"
    const val FACEBOOK_URL = "https://www.facebook.com/share/1Fa27SGyT7/"

    const val INSTAGRAM_USERNAME = "@cappuzo"
    const val FACEBOOK_PAGE_NAME = "Cappuzo"

    /**
     * Opens Instagram using Intent with fallback to the web browser.
     */
    fun openInstagramOfficial(context: Context) {
        val webUri = Uri.parse(INSTAGRAM_URL)
        val appUri = Uri.parse("http://instagram.com/_u/cappuzo")

        val appIntent = Intent(Intent.ACTION_VIEW, appUri).apply {
            setPackage("com.instagram.android")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(appIntent)
        } catch (_: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
        }
    }

    /**
     * Opens Facebook using Intent with fallback to the web browser.
     */
    fun openFacebookOfficial(context: Context) {
        val webUri = Uri.parse(FACEBOOK_URL)
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=$FACEBOOK_URL")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(appIntent)
        } catch (_: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
        }
    }
}
