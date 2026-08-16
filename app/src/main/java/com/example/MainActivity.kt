package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.model.AppThemeMode
import com.example.ui.components.ActiveCallOverlay
import com.example.ui.components.AppBottomNavigation
import com.example.ui.components.AppTopBar
import com.example.ui.components.AuthModalSheet
import com.example.ui.components.CreatePostBottomSheet
import com.example.ui.components.FreeServerPickerModal
import com.example.ui.components.GiftShopBottomSheet
import com.example.ui.components.NotificationsBottomSheet
import com.example.ui.screens.AIAssistantScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.ChatListScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveHostingStudioScreen
import com.example.ui.screens.LiveStreamScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReelsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StoryViewerScreen
import com.example.ui.theme.AetherAppTheme
import com.example.ui.viewmodel.MainNavigationTab
import com.example.ui.viewmodel.SocialViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: SocialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            val isDarkTheme = when (themeMode) {
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                AppThemeMode.DARK -> true
                AppThemeMode.LIGHT -> false
            }

            AetherAppTheme(darkTheme = isDarkTheme) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: SocialViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val activeStory by viewModel.activeStory.collectAsState()
    val activeLiveStream by viewModel.activeLiveStream.collectAsState()
    val activeChatId by viewModel.activeChatId.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val virtualGifts = viewModel.virtualGifts
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    // Modals visibility
    val showCreatePost by viewModel.showCreatePost.collectAsState()
    val showGiftShop by viewModel.showGiftShop.collectAsState()
    val showNotifications by viewModel.showNotifications.collectAsState()
    val showAuthModal by viewModel.showAuthModal.collectAsState()
    val showAnalytics by viewModel.showAnalytics.collectAsState()
    val showLiveHostingStudio by viewModel.showLiveHostingStudio.collectAsState()
    val showFreeServerPicker by viewModel.showFreeServerPicker.collectAsState()
    val showSettings by viewModel.showSettings.collectAsState()
    val authState by viewModel.authState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // System Back navigation handling
    BackHandler(
        enabled = showSettings ||
                activeStory != null ||
                activeLiveStream != null ||
                showLiveHostingStudio ||
                showFreeServerPicker ||
                activeChatId != null ||
                showAnalytics ||
                showGiftShop ||
                showCreatePost ||
                showNotifications ||
                showAuthModal ||
                currentTab != MainNavigationTab.HOME_FEED
    ) {
        when {
            showSettings -> viewModel.toggleSettings(false)
            showFreeServerPicker -> viewModel.toggleFreeServerPicker(false)
            showLiveHostingStudio -> viewModel.toggleLiveHostingStudio(false)
            activeStory != null -> viewModel.closeStory()
            activeLiveStream != null -> viewModel.closeLiveStream()
            activeChatId != null -> viewModel.selectChat(null)
            showAnalytics -> viewModel.toggleAnalytics(false)
            showGiftShop -> viewModel.toggleGiftShop(false)
            showNotifications -> viewModel.toggleNotifications(false)
            showCreatePost -> viewModel.toggleCreatePost(false)
            showAuthModal -> viewModel.toggleAuthModal(false)
            currentTab != MainNavigationTab.HOME_FEED -> viewModel.selectTab(MainNavigationTab.HOME_FEED)
        }
    }

    Box(modifier = Modifier.fillMaxSize().testTag("main_app_container")) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                // Show AppTopBar on main feed and explore tabs when no sub-overlay is active
                if (activeChatId == null && (currentTab == MainNavigationTab.HOME_FEED || currentTab == MainNavigationTab.EXPLORE)) {
                    AppTopBar(
                        coinsBalance = currentUser.coinsBalance,
                        unreadNotificationsCount = notifications.count { !it.isRead },
                        unreadMessagesCount = 2,
                        onCoinsClick = { viewModel.toggleGiftShop(true) },
                        onNotificationsClick = { viewModel.toggleNotifications(true) },
                        onMessagesClick = { viewModel.selectTab(MainNavigationTab.CHAT) },
                        onCreatePostClick = { viewModel.toggleCreatePost(true) }
                    )
                }
            },
            bottomBar = {
                // Bottom navigation bar
                AppBottomNavigation(
                    selectedTab = currentTab,
                    onTabSelected = { tab ->
                        viewModel.selectTab(tab)
                    },
                    modifier = Modifier.testTag("app_bottom_navigation")
                )
            }
        ) { innerPadding ->
            Crossfade(
                targetState = currentTab,
                label = "MainTabsCrossfade",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { tab ->
                when (tab) {
                    MainNavigationTab.HOME_FEED -> HomeScreen(viewModel = viewModel)
                    MainNavigationTab.EXPLORE -> ExploreScreen(viewModel = viewModel)
                    MainNavigationTab.AI_STUDIO -> AIAssistantScreen(viewModel = viewModel)
                    MainNavigationTab.REELS -> ReelsScreen(viewModel = viewModel)
                    MainNavigationTab.CHAT -> {
                        if (activeChatId != null) {
                            ChatDetailScreen(chatId = activeChatId!!, viewModel = viewModel)
                        } else {
                            ChatListScreen(viewModel = viewModel)
                        }
                    }
                    MainNavigationTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                }
            }
        }

        // Fullscreen Overlays

        // 1. Story Viewer
        activeStory?.let { story ->
            StoryViewerScreen(
                story = story,
                viewModel = viewModel,
                onClose = { viewModel.closeStory() }
            )
        }

        // 2. Live Stream Room
        activeLiveStream?.let { liveStream ->
            LiveStreamScreen(
                stream = liveStream,
                viewModel = viewModel,
                onClose = { viewModel.closeLiveStream() }
            )
        }

        // 3. Active Video/Audio Call Overlay
        ActiveCallOverlay(viewModel = viewModel)

        // 4. Analytics Studio Screen
        AnimatedVisibility(
            visible = showAnalytics,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AnalyticsScreen(
                viewModel = viewModel,
                onBack = { viewModel.toggleAnalytics(false) }
            )
        }

        // 5. Create Post Bottom Sheet
        if (showCreatePost) {
            CreatePostBottomSheet(
                onDismiss = { viewModel.toggleCreatePost(false) },
                onPublish = { caption, tags, media ->
                    viewModel.publishPost(caption, tags, media)
                }
            )
        }

        // 6. Creator Gifts & Coin Shop Modal
        if (showGiftShop) {
            GiftShopBottomSheet(
                coinsBalance = currentUser.coinsBalance,
                virtualGifts = virtualGifts,
                onSendGift = { gift -> viewModel.sendGift(gift, "Elena Rostova") },
                onTopUpCoins = { amount -> viewModel.topUpCoins(amount) },
                onDismiss = { viewModel.toggleGiftShop(false) }
            )
        }

        // 7. Activity & Notifications Modal
        if (showNotifications) {
            NotificationsBottomSheet(
                notifications = notifications,
                onDismiss = { viewModel.toggleNotifications(false) }
            )
        }

        // 8. Auth & Security Modal Sheet
        if (showAuthModal && authState.isAuthenticated) {
            AuthModalSheet(
                viewModel = viewModel,
                onDismiss = { viewModel.toggleAuthModal(false) }
            )
        }

        // 9. Free Live Hosting Broadcast Studio
        if (showLiveHostingStudio) {
            LiveHostingStudioScreen(
                viewModel = viewModel,
                onClose = { viewModel.toggleLiveHostingStudio(false) }
            )
        }

        // 10. Free Live Server Picker Bottom Sheet
        if (showFreeServerPicker) {
            FreeServerPickerModal(
                viewModel = viewModel,
                onDismiss = { viewModel.toggleFreeServerPicker(false) }
            )
        }

        // 11. Full Settings Screen Overlay
        AnimatedVisibility(
            visible = showSettings,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { viewModel.toggleSettings(false) }
            )
        }

        // 12. Fullscreen Authentication Flow (when logged out)
        if (!authState.isAuthenticated) {
            AuthScreen(
                viewModel = viewModel,
                onAuthSuccess = { }
            )
        }
    }
}
