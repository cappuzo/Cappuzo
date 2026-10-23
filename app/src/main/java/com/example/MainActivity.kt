package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notification.PoetryNotificationHelper
import com.example.ui.components.NotificationSettingsDialog
import com.example.ui.components.PublishingGuideDialog
import com.example.ui.components.SocialLinksDialog
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.BooksScreen
import com.example.ui.screens.ErasScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PoemDetailScreen
import com.example.ui.screens.PoetDetailScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BlackCard
import com.example.ui.theme.BlackObsidian
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.CappuzoTheme
import com.example.ui.theme.GoldBorderSubtle
import com.example.ui.theme.GoldLuminous
import com.example.ui.theme.GoldRoyal
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.CappuzoViewModel
import com.example.ui.viewmodel.Screen

enum class BottomTab(
    val title: String,
    val icon: ImageVector,
    val screen: Screen,
    val testTag: String
) {
    HOME("الرئيسية", Icons.Default.Home, Screen.Home, "tab_home"),
    ERAS("العصور", Icons.Default.AutoStories, Screen.Eras(), "tab_eras"),
    BOOKS("الكتب", Icons.Default.MenuBook, Screen.Books, "tab_books"),
    SEARCH("البحث", Icons.Default.Search, Screen.Search, "tab_search"),
    BOOKMARKS("المحفوظات", Icons.Default.Bookmarks, Screen.Bookmarks, "tab_bookmarks")
}

class MainActivity : ComponentActivity() {

    private val viewModel: CappuzoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize notification channel for daily poetry
        PoetryNotificationHelper.initNotificationChannel(this)

        setContent {
            val themeMode by viewModel.currentThemeMode.collectAsState()
            CappuzoTheme(themeMode = themeMode) {
                // Enforce Arabic RTL layout across the entire application
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    CappuzoApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CappuzoApp(viewModel: CappuzoViewModel) {
    val screenStack by viewModel.screenStack.collectAsState()
    val currentScreen = screenStack.lastOrNull() ?: Screen.Home

    var showPublishingGuide by remember { mutableStateOf(false) }
    var showNotificationSettings by remember { mutableStateOf(false) }
    var showSocialDialog by remember { mutableStateOf(false) }

    // Intercept back navigation
    BackHandler(enabled = screenStack.size > 1) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
        bottomBar = {
            // Determine active tab
            val currentTab = when (currentScreen) {
                is Screen.Home -> BottomTab.HOME
                is Screen.Eras -> BottomTab.ERAS
                is Screen.Books, is Screen.BookDetail -> BottomTab.BOOKS
                is Screen.Search -> BottomTab.SEARCH
                is Screen.Bookmarks -> BottomTab.BOOKMARKS
                else -> null
            }

            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .border(0.5.dp, androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant)
                    .testTag("cappuzo_bottom_navigation"),
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                BottomTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            viewModel.switchTab(tab.screen)
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BlackObsidian,
                            selectedTextColor = GoldLuminous,
                            indicatorColor = GoldRoyal,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screenTransition"
            ) { screen ->
                when (screen) {
                    is Screen.Home -> HomeScreen(
                        viewModel = viewModel,
                        onOpenNotifications = { showNotificationSettings = true },
                        onOpenPublishingGuide = { showPublishingGuide = true },
                        onOpenSocialLinks = { showSocialDialog = true },
                        onOpenSettings = { viewModel.navigateTo(Screen.Settings) }
                    )

                    is Screen.Eras -> ErasScreen(
                        viewModel = viewModel,
                        initialEra = screen.selectedEra,
                        onBack = { viewModel.navigateBack() }
                    )

                    is Screen.Books -> BooksScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() }
                    )

                    is Screen.Search -> SearchScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() }
                    )

                    is Screen.Bookmarks -> BookmarksScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() }
                    )

                    is Screen.PoetDetail -> PoetDetailScreen(
                        poetId = screen.poetId,
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() }
                    )

                    is Screen.PoemDetail -> PoemDetailScreen(
                        poemId = screen.poemId,
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() }
                    )

                    is Screen.BookDetail -> BooksScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() }
                    )

                    is Screen.Settings -> SettingsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() },
                        onOpenPublishingGuide = { showPublishingGuide = true }
                    )
                }
            }
        }
    }

    if (showPublishingGuide) {
        PublishingGuideDialog(onDismiss = { showPublishingGuide = false })
    }

    if (showNotificationSettings) {
        NotificationSettingsDialog(onDismiss = { showNotificationSettings = false })
    }

    if (showSocialDialog) {
        SocialLinksDialog(onDismiss = { showSocialDialog = false })
    }
}
