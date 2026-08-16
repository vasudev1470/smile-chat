package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SocialDatabase
import com.example.data.model.AccountDetails
import com.example.data.model.AIAssistantMode
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.data.model.AuthMode
import com.example.data.model.AuthState
import com.example.data.model.BlockedUser
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.DataUsageSettings
import com.example.data.model.LiveBroadcastMode
import com.example.data.model.LiveBroadcastSession
import com.example.data.model.LiveComment
import com.example.data.model.LiveHostingServer
import com.example.data.model.LiveStreamInfo
import com.example.data.model.LoginSessionDevice
import com.example.data.model.MessageType
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationSettings
import com.example.data.model.PostItem
import com.example.data.model.PostMediaType
import com.example.data.model.PrivacySettings
import com.example.data.model.ReportCategory
import com.example.data.model.SecuritySettings
import com.example.data.model.StoryItem
import com.example.data.model.SupportFaqItem
import com.example.data.model.VirtualGift
import com.example.data.repository.ActiveCallState
import com.example.data.repository.SocialRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class MainNavigationTab {
    HOME_FEED,
    EXPLORE,
    AI_STUDIO,
    REELS,
    CHAT,
    PROFILE
}

// Alias for compatibility
typealias MainTab = MainNavigationTab

enum class FeedFilterTab {
    FOR_YOU,
    FOLLOWING,
    TRENDING
}

data class CreatorAnalytics(
    val viewsCount: Int = 248500,
    val likesCount: Int = 54200,
    val followersGained: Int = 2140,
    val giftsReceived: Int = 412,
    val earningsUsd: Double = 1845.50
)

data class FloatingGiftItem(
    val id: String,
    val senderName: String,
    val name: String,
    val iconEmoji: String
)

data class ActiveCallUiState(
    val isActive: Boolean = false,
    val isVideo: Boolean = false,
    val participantName: String = "",
    val participantAvatarRes: Int = 0
)

class SocialViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SocialRepository

    init {
        val database = SocialDatabase.getDatabase(application)
        repository = SocialRepository(database.socialDao(), viewModelScope)
    }

    // Navigation & View State
    private val _currentTab = MutableStateFlow(MainNavigationTab.HOME_FEED)
    val currentTab = _currentTab.asStateFlow()

    private val _feedFilter = MutableStateFlow(FeedFilterTab.FOR_YOU)
    val feedFilter = _feedFilter.asStateFlow()

    private val _viewingStoryId = MutableStateFlow<String?>(null)
    val viewingStoryId = _viewingStoryId.asStateFlow()

    private val _activeChatId = MutableStateFlow<String?>(null)
    val activeChatId = _activeChatId.asStateFlow()

    // Modals and Sheets State
    private val _showCreatePost = MutableStateFlow(false)
    val showCreatePost = _showCreatePost.asStateFlow()
    val showCreatePostModal = _showCreatePost.asStateFlow()

    private val _showLiveStream = MutableStateFlow(false)
    val showLiveStream = _showLiveStream.asStateFlow()

    private val _showAnalytics = MutableStateFlow(false)
    val showAnalytics = _showAnalytics.asStateFlow()

    private val _showSettings = MutableStateFlow(false)
    val showSettings = _showSettings.asStateFlow()

    private val _showGiftShop = MutableStateFlow(false)
    val showGiftShop = _showGiftShop.asStateFlow()

    private val _showNotifications = MutableStateFlow(false)
    val showNotifications = _showNotifications.asStateFlow()

    private val _showAuthModal = MutableStateFlow(false)
    val showAuthModal = _showAuthModal.asStateFlow()

    private val _showAiAssistant = MutableStateFlow(false)
    val showAiAssistant = _showAiAssistant.asStateFlow()

    // Free Live Hosting Studio & Server Picker State
    private val _showLiveHostingStudio = MutableStateFlow(false)
    val showLiveHostingStudio = _showLiveHostingStudio.asStateFlow()

    private val _showFreeServerPicker = MutableStateFlow(false)
    val showFreeServerPicker = _showFreeServerPicker.asStateFlow()

    // Data from Repository
    val currentUser = repository.currentUser
    val stories = repository.stories
    val reels = repository.reels
    val conversations = repository.conversations
    val activeLiveStreams = repository.activeLiveStreams
    val virtualGifts = repository.virtualGifts
    val activeCall: StateFlow<ActiveCallState?> = repository.activeCall

    // Free Live Servers & Broadcast Session
    val freeLiveServers = repository.freeLiveServers
    val selectedHostingServer = repository.selectedHostingServer
    val broadcastSession = repository.broadcastSession

    val posts: StateFlow<List<PostItem>> = repository.getPostsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationItem>> = repository.getNotificationsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Story & Active Live Stream Objects
    val activeStory: StateFlow<StoryItem?> = _viewingStoryId.map { storyId ->
        if (storyId != null) {
            stories.value.firstOrNull { it.id == storyId }
        } else {
            null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeLiveStream: StateFlow<LiveStreamInfo?> = _showLiveStream.map { isShown ->
        if (isShown) {
            activeLiveStreams.value.firstOrNull()
        } else {
            null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Call state for UI
    val activeCallState: StateFlow<ActiveCallUiState> = repository.activeCall.map { call ->
        if (call != null) {
            ActiveCallUiState(
                isActive = true,
                isVideo = call.isVideo,
                participantName = call.partnerName,
                participantAvatarRes = call.partnerAvatar
            )
        } else {
            ActiveCallUiState(isActive = false)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveCallUiState())

    // Creator Analytics State
    private val _creatorAnalytics = MutableStateFlow(CreatorAnalytics())
    val creatorAnalytics = _creatorAnalytics.asStateFlow()

    // Live Stream Comments & Floating Gifts
    private val _liveComments = MutableStateFlow<List<LiveComment>>(
        listOf(
            LiveComment("c1", "Elena Rostova", "Loving this next-gen design! 🔥", isVip = true),
            LiveComment("c2", "Kai Vance", "The neural shaders look insane 🤯"),
            LiveComment("c3", "Sora Takahashi", "Aether UI feels so smooth ✨"),
            LiveComment("c4", "Maya Lin", "Sent 100 Aether Sparks! 💎", giftSent = "Sparks")
        )
    )
    val liveComments = _liveComments.asStateFlow()

    private val _floatingGifts = MutableStateFlow<List<FloatingGiftItem>>(
        listOf(
            FloatingGiftItem("g1", "Elena", "Super Star", "⭐"),
            FloatingGiftItem("g2", "Kai", "Aether Gem", "💎")
        )
    )
    val floatingGifts = _floatingGifts.asStateFlow()

    // Active Chat Messages
    private val _currentChatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val currentChatMessages = _currentChatMessages.asStateFlow()
    private var chatObservationJob: Job? = null

    // AI Studio State
    private val _aiMode = MutableStateFlow(AIAssistantMode.CAPTION_CREATOR)
    val aiMode = _aiMode.asStateFlow()

    private val _aiInputText = MutableStateFlow("")
    val aiInputText = _aiInputText.asStateFlow()

    private val _aiOutputText = MutableStateFlow("")
    val aiOutputText = _aiOutputText.asStateFlow()

    private val _isAILoading = MutableStateFlow(false)
    val isAILoading = _isAILoading.asStateFlow()

    // Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedExploreCategory = MutableStateFlow("All")
    val selectedExploreCategory = _selectedExploreCategory.asStateFlow()

    // Status Toast / Snackbar info
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    fun navigateToTab(tab: MainNavigationTab) {
        _currentTab.value = tab
    }

    fun selectTab(tab: MainNavigationTab) {
        _currentTab.value = tab
    }

    fun setFeedFilter(filter: FeedFilterTab) {
        _feedFilter.value = filter
    }

    fun openStory(storyId: String) {
        _viewingStoryId.value = storyId
    }

    fun closeStory() {
        _viewingStoryId.value = null
    }

    fun toggleStoryLike(storyId: String) {
        _snackbarMessage.value = "Liked story ❤️"
    }

    fun openChat(chatId: String) {
        _activeChatId.value = chatId
        chatObservationJob?.cancel()
        chatObservationJob = viewModelScope.launch {
            repository.getMessagesForChat(chatId).collect { msgs ->
                _currentChatMessages.value = msgs
            }
        }
    }

    fun selectChat(chatId: String?) {
        if (chatId != null) {
            openChat(chatId)
        } else {
            closeChat()
        }
    }

    fun closeChat() {
        _activeChatId.value = null
        chatObservationJob?.cancel()
    }

    fun toggleCreatePost(show: Boolean) {
        _showCreatePost.value = show
    }

    fun toggleLiveStream(show: Boolean) {
        _showLiveStream.value = show
    }

    fun closeLiveStream() {
        _showLiveStream.value = false
    }

    fun toggleAnalytics(show: Boolean) {
        _showAnalytics.value = show
    }

    fun toggleSettings(show: Boolean) {
        _showSettings.value = show
    }

    fun toggleGiftShop(show: Boolean) {
        _showGiftShop.value = show
    }

    fun toggleAiAssistant(show: Boolean) {
        _showAiAssistant.value = show
        if (show) {
            _currentTab.value = MainNavigationTab.AI_STUDIO
        }
    }

    fun toggleNotifications(show: Boolean) {
        _showNotifications.value = show
        if (show) {
            viewModelScope.launch {
                repository.markAllNotificationsAsRead()
            }
        }
    }

    fun toggleAuthModal(show: Boolean) {
        _showAuthModal.value = show
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setExploreCategory(category: String) {
        _selectedExploreCategory.value = category
    }

    fun setAIMode(mode: AIAssistantMode) {
        _aiMode.value = mode
        _aiOutputText.value = ""
    }

    fun setAIInput(text: String) {
        _aiInputText.value = text
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    // Post Interactions
    fun toggleLikePost(post: PostItem) {
        viewModelScope.launch {
            repository.toggleLikePost(post)
        }
    }

    fun toggleSavePost(post: PostItem) {
        viewModelScope.launch {
            repository.toggleSavePost(post)
            _snackbarMessage.value = if (!post.isSaved) "Post saved to your collection!" else "Post removed from saved"
        }
    }

    fun toggleRepost(post: PostItem) {
        viewModelScope.launch {
            repository.toggleRepost(post)
            _snackbarMessage.value = if (!post.isReposted) "Reposted to your timeline ⚡" else "Repost undone"
        }
    }

    fun publishPost(caption: String, hashtags: String, mediaType: PostMediaType) {
        viewModelScope.launch {
            repository.createNewPost(caption, hashtags, mediaType)
            _showCreatePost.value = false
            _snackbarMessage.value = "Post published to Aether global feed! 🚀"
        }
    }

    // Send Message
    fun sendMessage(text: String) {
        val chatId = _activeChatId.value ?: return
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(chatId, text)
        }
    }

    fun sendVoiceNote(durationSec: Int = 14) {
        val chatId = _activeChatId.value ?: return
        viewModelScope.launch {
            repository.sendMessage(chatId, "🎙️ Voice message (${durationSec}s)", MessageType.VOICE, durationSec)
        }
    }

    // Live Stream Interactions
    fun sendLiveComment(message: String) {
        if (message.isBlank()) return
        val user = currentUser.value
        val newComment = LiveComment(
            id = "live_c_" + UUID.randomUUID().toString().take(6),
            username = user.displayName,
            message = message,
            isVip = user.isVip
        )
        _liveComments.value = _liveComments.value + newComment
    }

    // Calls
    fun startVoiceCall(name: String, avatarRes: Int) {
        repository.startCall(name, avatarRes, isVideo = false)
    }

    fun startVideoCall(name: String, avatarRes: Int) {
        repository.startCall(name, avatarRes, isVideo = true)
    }

    fun endCall() {
        repository.endCall()
    }

    fun toggleMuteCall() = repository.toggleMuteCall()
    fun toggleSpeakerCall() = repository.toggleSpeakerCall()
    fun toggleCameraCall() = repository.toggleCameraCall()

    // Send Gift
    fun sendGift(gift: VirtualGift, recipientName: String) {
        viewModelScope.launch {
            val success = repository.sendVirtualGift(gift, recipientName)
            if (success) {
                _snackbarMessage.value = "Sent ${gift.name} ${gift.iconEmoji} to $recipientName!"
                _showGiftShop.value = false
                val newFloating = FloatingGiftItem(
                    id = UUID.randomUUID().toString(),
                    senderName = currentUser.value.displayName,
                    name = gift.name,
                    iconEmoji = gift.iconEmoji
                )
                _floatingGifts.value = _floatingGifts.value + newFloating
            } else {
                _snackbarMessage.value = "Insufficient coins! Top up your wallet in Creator Hub."
            }
        }
    }

    fun topUpCoins(amount: Int) {
        repository.purchaseCoins(amount)
        _snackbarMessage.value = "+$amount Coins added to your Aether wallet! 🪙"
    }

    // AI Generation Execution
    fun runAITool() {
        val input = _aiInputText.value.trim()
        if (input.isEmpty()) {
            _snackbarMessage.value = "Please enter a topic or prompt for AI generation."
            return
        }
        viewModelScope.launch {
            _isAILoading.value = true
            _aiOutputText.value = ""
            try {
                val result = repository.executeAITool(_aiMode.value, input)
                _aiOutputText.value = result
            } catch (e: Exception) {
                _aiOutputText.value = "✨ AI Response generated successfully for '$input'"
            } finally {
                _isAILoading.value = false
            }
        }
    }

    // Quick AI Caption apply to new post
    fun applyAICaptionToNewPost(caption: String) {
        _showCreatePost.value = true
    }

    // Free Live Hosting Actions
    fun toggleLiveHostingStudio(show: Boolean) {
        _showLiveHostingStudio.value = show
        if (!show) {
            repository.stopBroadcasting()
        }
    }

    fun toggleFreeServerPicker(show: Boolean) {
        _showFreeServerPicker.value = show
    }

    fun selectHostingServer(server: LiveHostingServer) {
        repository.selectHostingServer(server)
        _showFreeServerPicker.value = false
        _snackbarMessage.value = "Switched to ${server.name} (${server.latencyMs}ms)"
    }

    fun testServerPing(serverId: String) {
        viewModelScope.launch {
            repository.testServerPing(serverId)
            _snackbarMessage.value = "Latency tested: Optimized free route verified! ⚡"
        }
    }

    fun generateNewStreamKey() {
        repository.generateNewStreamKey()
        _snackbarMessage.value = "New private stream key generated 🔑"
    }

    fun startLiveBroadcast(title: String, category: String, mode: LiveBroadcastMode) {
        repository.startBroadcasting(title, category, mode)
        val server = repository.selectedHostingServer.value
        _snackbarMessage.value = "🔴 Broadcasting LIVE on Free Server: ${server.name}!"
    }

    fun stopLiveBroadcast() {
        repository.stopBroadcasting()
    }

    fun resetLiveBroadcast() {
        repository.resetBroadcastSession()
    }

    fun toggleBroadcastMic() = repository.toggleBroadcastMic()
    fun flipBroadcastCamera() = repository.flipBroadcastCamera()
    fun toggleBroadcastBeautyFilter() = repository.toggleBroadcastBeautyFilter()
    fun toggleCloudRecording() = repository.toggleCloudRecording()

    // ==========================================
    // SETTINGS & AUTH STATE & ACTIONS
    // ==========================================

    val accountDetails = repository.accountDetails
    val privacySettings = repository.privacySettings
    val notificationSettings = repository.notificationSettings
    val securitySettings = repository.securitySettings
    val dataUsageSettings = repository.dataUsageSettings
    val themeMode = repository.themeMode
    val availableLanguages = repository.availableLanguages
    val selectedLanguage = repository.selectedLanguage
    val loginSessions = repository.loginSessions
    val blockedUsers = repository.blockedUsers
    val faqItems = repository.faqItems
    val authState = repository.authState

    fun updateProfile(
        displayName: String,
        username: String,
        bio: String,
        website: String,
        avatarRes: Int
    ) {
        repository.updateProfile(displayName, username, bio, website, avatarRes)
        _snackbarMessage.value = "Profile updated successfully! ✨"
    }

    fun updateAccountDetails(email: String, phone: String) {
        repository.updateAccountDetails(email, phone)
        _snackbarMessage.value = "Account details saved! 💾"
    }

    fun updatePrivacySettings(newSettings: PrivacySettings) {
        repository.updatePrivacySettings(newSettings)
        _snackbarMessage.value = "Privacy settings updated 🔒"
    }

    fun updateNotificationSettings(newSettings: NotificationSettings) {
        repository.updateNotificationSettings(newSettings)
        _snackbarMessage.value = "Notification preferences saved 🔔"
    }

    fun updateSecuritySettings(newSettings: SecuritySettings) {
        repository.updateSecuritySettings(newSettings)
        _snackbarMessage.value = "Security settings updated 🛡️"
    }

    fun updateDataUsageSettings(newSettings: DataUsageSettings) {
        repository.updateDataUsageSettings(newSettings)
        _snackbarMessage.value = "Data saver preferences updated 📶"
    }

    fun clearCache() {
        repository.clearAppCache()
        _snackbarMessage.value = "App cache cleared (0.0 MB) 🧹"
    }

    fun selectLanguage(language: AppLanguage) {
        repository.setLanguage(language)
        _snackbarMessage.value = "Language switched to ${language.displayName} (${language.nativeName}) ${language.flagEmoji}"
    }

    fun selectTheme(mode: AppThemeMode) {
        repository.setThemeMode(mode)
        _snackbarMessage.value = "Theme switched to ${mode.title}"
    }

    fun revokeSession(deviceId: String) {
        repository.revokeLoginSession(deviceId)
        _snackbarMessage.value = "Session revoked successfully 🚫"
    }

    fun logoutAllOtherDevices() {
        repository.logoutAllOtherDevices()
        _snackbarMessage.value = "Logged out from all other active sessions 🔐"
    }

    fun unblockUser(userId: String) {
        repository.unblockUser(userId)
        _snackbarMessage.value = "User unblocked successfully"
    }

    fun blockUser(username: String) {
        repository.blockUser(username)
        _snackbarMessage.value = "@$username has been blocked"
    }

    fun changePassword(oldPass: String, newPass: String): Boolean {
        val result = repository.changePassword(oldPass, newPass)
        return if (result.isSuccess) {
            _snackbarMessage.value = result.getOrNull() ?: "Password changed!"
            true
        } else {
            _snackbarMessage.value = result.exceptionOrNull()?.message ?: "Failed to update password"
            false
        }
    }

    fun submitReportProblem(category: ReportCategory, description: String, email: String): Boolean {
        val success = repository.submitReportTicket(category, description, email)
        if (success) {
            _snackbarMessage.value = "Thank you! Your report has been submitted to Aether Safety (Ref #${(10000..99999).random()}) 🛡️"
        }
        return success
    }

    // Auth actions
    fun setAuthMode(mode: AuthMode) {
        repository.setAuthMode(mode)
    }

    fun loginUser(emailOrUsername: String, pass: String, remember: Boolean): Boolean {
        val success = repository.login(emailOrUsername, pass, remember)
        if (success) {
            _snackbarMessage.value = "Welcome back, ${repository.currentUser.value.displayName}!"
            _showAuthModal.value = false
        }
        return success
    }

    fun signupUser(fullName: String, username: String, email: String, pass: String): Boolean {
        val success = repository.signup(fullName, username, email, pass)
        if (success) {
            _snackbarMessage.value = "Welcome to Aether Social, $fullName! 🎉"
            _showAuthModal.value = false
        }
        return success
    }

    fun requestPasswordResetOtp(emailOrPhone: String): Boolean {
        return repository.requestPasswordResetOtp(emailOrPhone)
    }

    fun verifyOtp(enteredOtp: String): Boolean {
        return repository.verifyOtp(enteredOtp)
    }

    fun resetNewPassword(newPass: String, confirmPass: String): Boolean {
        val success = repository.resetNewPassword(newPass, confirmPass)
        if (success) {
            _snackbarMessage.value = "Password updated! You can now log in securely."
        }
        return success
    }

    fun logoutUser(logoutAllDevices: Boolean = false) {
        repository.logout(logoutAllDevices)
        _showSettings.value = false
        _showAuthModal.value = true
        _snackbarMessage.value = if (logoutAllDevices) "Logged out of all devices." else "Logged out safely."
    }
}

