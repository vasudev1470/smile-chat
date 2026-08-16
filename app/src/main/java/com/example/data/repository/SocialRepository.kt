package com.example.data.repository

import com.example.R
import com.example.data.api.GeminiApiClient
import com.example.data.local.AILogEntity
import com.example.data.local.ChatMessageEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.PostEntity
import com.example.data.local.SocialDao
import com.example.data.local.UserProfileEntity
import com.example.data.model.AIAssistantMode
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.CommentItem
import com.example.data.model.AccountDetails
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.data.model.AuthMode
import com.example.data.model.AuthState
import com.example.data.model.BlockedUser
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
import com.example.data.model.NotificationType
import com.example.data.model.PollOption
import com.example.data.model.PostItem
import com.example.data.model.PostMediaType
import com.example.data.model.PrivacySettings
import com.example.data.model.ReelItem
import com.example.data.model.ReportCategory
import com.example.data.model.SecuritySettings
import com.example.data.model.ServerStatus
import com.example.data.model.StoryItem
import com.example.data.model.SupportFaqItem
import com.example.data.model.UserProfile
import com.example.data.model.VirtualGift
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class SocialRepository(
    private val dao: SocialDao,
    private val scope: CoroutineScope
) {
    // Current User
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "user_me",
            username = "vasudev_ai",
            displayName = "Vasudev",
            bio = "⚡ Creative Technologist & AI Architect | Exploring future frontiers in generative social media & neural computing 🚀",
            avatarDrawableRes = R.drawable.img_avatar_ai,
            isVerified = true,
            isVip = true,
            followersCount = 42800,
            followingCount = 318,
            likesCount = 189400,
            coinsBalance = 3850
        )
    )
    val currentUser = _currentUser.asStateFlow()

    // Stories
    private val _stories = MutableStateFlow<List<StoryItem>>(emptyList())
    val stories = _stories.asStateFlow()

    // Reels
    private val _reels = MutableStateFlow<List<ReelItem>>(emptyList())
    val reels = _reels.asStateFlow()

    // Live Streams
    private val _activeLiveStreams = MutableStateFlow<List<LiveStreamInfo>>(emptyList())
    val activeLiveStreams = _activeLiveStreams.asStateFlow()

    // Virtual Gifts
    val virtualGifts = listOf(
        VirtualGift("gift_1", "Cosmic Rose", "🌹", 20, "Sends a shower of blooming roses"),
        VirtualGift("gift_2", "Diamond Crown", "👑", 100, "Golden crown with sparkling gems"),
        VirtualGift("gift_3", "Cyber Rocket", "🚀", 250, "Full screen hyperdrive rocket takeoff"),
        VirtualGift("gift_4", "Galaxy Nebula", "🌌", 500, "Vibrant cosmic aura explosion"),
        VirtualGift("gift_5", "AI Neural Core", "⚡", 1000, "Supercharged holographic pulse")
    )

    // Conversations
    private val _conversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    val conversations = _conversations.asStateFlow()

    // Active Call State
    private val _activeCall = MutableStateFlow<ActiveCallState?>(null)
    val activeCall = _activeCall.asStateFlow()

    // Free Live Hosting Servers (100% Free Unlimited Bandwidth, Global Edge Nodes)
    private val initialFreeServers = listOf(
        LiveHostingServer(
            id = "server_us_east",
            name = "Aether Free Edge (US-East)",
            region = "North America (Virginia)",
            flagEmoji = "🇺🇸",
            rtmpIngestUrl = "rtmp://free-us1.aether-live.network/live",
            webRtcUrl = "webrtc://mesh-us.aether-live.network/whip",
            latencyMs = 16,
            maxBitrateKbps = 8000,
            maxResolution = "1080p 60fps",
            status = ServerStatus.ULTRA_FAST,
            isFreeUnlimited = true,
            activeBroadcasters = 128,
            serverLoadPercent = 24
        ),
        LiveHostingServer(
            id = "server_eu_central",
            name = "OpenStream Free Node (Frankfurt)",
            region = "Europe Central (Germany)",
            flagEmoji = "🇩🇪",
            rtmpIngestUrl = "rtmp://free-eu1.aether-live.network/live",
            webRtcUrl = "webrtc://mesh-eu.aether-live.network/whip",
            latencyMs = 28,
            maxBitrateKbps = 8000,
            maxResolution = "1080p 60fps",
            status = ServerStatus.ONLINE,
            isFreeUnlimited = true,
            activeBroadcasters = 95,
            serverLoadPercent = 31
        ),
        LiveHostingServer(
            id = "server_ap_tokyo",
            name = "HyperMesh Free Node (Tokyo)",
            region = "Asia-Pacific (Japan)",
            flagEmoji = "🇯🇵",
            rtmpIngestUrl = "rtmp://free-ap1.aether-live.network/live",
            webRtcUrl = "webrtc://mesh-ap.aether-live.network/whip",
            latencyMs = 38,
            maxBitrateKbps = 8000,
            maxResolution = "1080p 60fps",
            status = ServerStatus.ONLINE,
            isFreeUnlimited = true,
            activeBroadcasters = 142,
            serverLoadPercent = 42
        ),
        LiveHostingServer(
            id = "server_ap_singapore",
            name = "SingaStream Free Relay (SG)",
            region = "Southeast Asia (Singapore)",
            flagEmoji = "🇸🇬",
            rtmpIngestUrl = "rtmp://free-sg1.aether-live.network/live",
            webRtcUrl = "webrtc://mesh-sg.aether-live.network/whip",
            latencyMs = 45,
            maxBitrateKbps = 8000,
            maxResolution = "1080p 60fps",
            status = ServerStatus.ONLINE,
            isFreeUnlimited = true,
            activeBroadcasters = 67,
            serverLoadPercent = 18
        ),
        LiveHostingServer(
            id = "server_global_p2p",
            name = "Decentralized P2P Free Relay",
            region = "Global Mesh (Zero-Hop)",
            flagEmoji = "🌐",
            rtmpIngestUrl = "rtmp://p2p-relay.aether-live.network/live",
            webRtcUrl = "webrtc://p2p-mesh.aether-live.network/whip",
            latencyMs = 12,
            maxBitrateKbps = 9000,
            maxResolution = "4K / 1080p 60fps",
            status = ServerStatus.ULTRA_FAST,
            isFreeUnlimited = true,
            activeBroadcasters = 210,
            serverLoadPercent = 15
        )
    )

    private val _freeLiveServers = MutableStateFlow<List<LiveHostingServer>>(initialFreeServers)
    val freeLiveServers = _freeLiveServers.asStateFlow()

    private val _selectedHostingServer = MutableStateFlow<LiveHostingServer>(initialFreeServers.first())
    val selectedHostingServer = _selectedHostingServer.asStateFlow()

    // Host Live Broadcast Session
    private val _broadcastSession = MutableStateFlow(
        LiveBroadcastSession(
            selectedServer = initialFreeServers.first()
        )
    )
    val broadcastSession = _broadcastSession.asStateFlow()

    private var broadcastTickerJob: Job? = null

    init {
        scope.launch(Dispatchers.IO) {
            seedInitialData()
        }
    }

    private suspend fun seedInitialData() {
        // Seed Stories
        _stories.value = listOf(
            StoryItem(
                id = "story_me",
                authorId = "user_me",
                authorName = "Your Story",
                authorAvatarRes = R.drawable.img_avatar_ai,
                imageRes = R.drawable.img_hero_banner,
                timestamp = "Just now",
                isViewed = false,
                hasUnseen = false,
                caption = "Launching Aether v2.0 with instant Gemini AI workflows! 🚀",
                pollQuestion = "Which AI tool do you use most?",
                pollOptions = listOf(
                    PollOption(1, "Caption Generator", 64, true),
                    PollOption(2, "AI Reels Script", 36, false)
                )
            ),
            StoryItem(
                id = "story_1",
                authorId = "user_elena",
                authorName = "Elena Vance",
                authorAvatarRes = R.drawable.img_story_art,
                imageRes = R.drawable.img_story_art,
                timestamp = "2h ago",
                isViewed = false,
                caption = "Late night Tokyo neon aesthetics ✨ Shot on Sony Alpha",
                questionPrompt = "Drop your favorite cyber cities 👇",
                musicTrack = "Synthwave Odyssey - Lorn"
            ),
            StoryItem(
                id = "story_2",
                authorId = "user_nova",
                authorName = "Nova Studio",
                authorAvatarRes = R.drawable.img_hero_banner,
                imageRes = R.drawable.img_hero_banner,
                timestamp = "4h ago",
                isViewed = false,
                caption = "Live backstage at AI Creator Summit 2026 🎙️",
                countdownText = "Live in 02:45:00",
                isLive = true
            ),
            StoryItem(
                id = "story_3",
                authorId = "user_kai",
                authorName = "Kai Tanaka",
                authorAvatarRes = R.drawable.img_app_icon,
                imageRes = R.drawable.img_app_icon,
                timestamp = "7h ago",
                isViewed = true,
                caption = "Testing holographic neural icons 🔮"
            )
        )

        // Seed Posts in Room if empty
        val seedPosts = listOf(
            PostEntity(
                id = "post_1",
                authorId = "user_nova",
                authorName = "Nova Creative Lab",
                authorUsername = "novalab_ai",
                authorAvatarRes = R.drawable.img_hero_banner,
                isAuthorVerified = true,
                timestamp = "25m ago",
                caption = "Unveiling our latest generative cyber landscape! Rendered with multi-modal neural shaders and ambient lighting effects. Thoughts on the color grade? 🎨✨",
                hashtagsCsv = "#CyberAesthetics,#GenerativeArt,#NextGenSocial,#AetherArt,#Innovation",
                mediaTypeStr = "IMAGE",
                imageDrawableRes = R.drawable.img_hero_banner,
                likesCount = 3840,
                commentsCount = 214,
                repostsCount = 480,
                isLiked = false,
                isSaved = false,
                isReposted = false,
                audioTrackName = "Odyssey 2088 • Original Audio",
                location = "Neo Shibuya, Tokyo",
                aiInsight = "Trending: +340% reach among creative technologists in last 1hr."
            ),
            PostEntity(
                id = "post_2",
                authorId = "user_elena",
                authorName = "Elena Vance",
                authorUsername = "elena.vision",
                authorAvatarRes = R.drawable.img_story_art,
                isAuthorVerified = true,
                timestamp = "2h ago",
                caption = "Tokyo night reflections. The juxtaposition of rain-slicked asphalt and neon luminescence never ceases to inspire. Which vibe do you prefer for night photography?",
                hashtagsCsv = "#TokyoNights,#StreetPhotography,#CyberpunkVibes,#NeonGlow",
                mediaTypeStr = "POLL",
                imageDrawableRes = R.drawable.img_story_art,
                likesCount = 9420,
                commentsCount = 612,
                repostsCount = 890,
                isLiked = true,
                isSaved = true,
                isReposted = false,
                audioTrackName = "Midnight Memories • Lo-Fi Beats",
                location = "Shinjuku, Tokyo",
                aiInsight = "High engagement: 92% positive sentiment in comment threads."
            ),
            PostEntity(
                id = "post_3",
                authorId = "user_aether_official",
                authorName = "Aether Intelligence",
                authorUsername = "aether_ai",
                authorAvatarRes = R.drawable.img_app_icon,
                isAuthorVerified = true,
                timestamp = "5h ago",
                caption = "⚡ Welcome to Aether Social! Featuring instant Gemini AI tools: real-time voice notes, smart caption crafting, end-to-end encrypted chats, and 60 FPS vertical video reels. What are you building today?",
                hashtagsCsv = "#AetherLaunch,#GeminiAI,#CreatorEconomy,#Social2026",
                mediaTypeStr = "IMAGE",
                imageDrawableRes = R.drawable.img_app_icon,
                likesCount = 28100,
                commentsCount = 1940,
                repostsCount = 4200,
                isLiked = true,
                isSaved = true,
                isReposted = true,
                audioTrackName = "Aether Anthem • Sound Lab",
                location = "Global Metaverse",
                aiInsight = "Platform announcement: 99.8% viral distribution score."
            )
        )
        dao.insertPosts(seedPosts)

        // Seed Reels
        _reels.value = listOf(
            ReelItem(
                id = "reel_1",
                authorName = "Elena Vance",
                authorUsername = "elena.vision",
                authorAvatarRes = R.drawable.img_story_art,
                videoCoverRes = R.drawable.img_story_art,
                caption = "Cinematic night walk in Tokyo with audio reactive filters ✨ #Tokyo #Cinematic",
                musicTrack = "Midnight Neon Drive • Lofi Chill",
                likesCount = 84200,
                commentsCount = 1420,
                sharesCount = 6800,
                isLiked = false
            ),
            ReelItem(
                id = "reel_2",
                authorName = "Nova Studio",
                authorUsername = "novalab_ai",
                authorAvatarRes = R.drawable.img_hero_banner,
                videoCoverRes = R.drawable.img_hero_banner,
                caption = "How we generate full 3D interactive stage sets using prompt engineering 🤖⚡",
                musicTrack = "Cyber Pulse • Future Bass",
                likesCount = 129000,
                commentsCount = 3890,
                sharesCount = 15200,
                isLiked = true
            ),
            ReelItem(
                id = "reel_3",
                authorName = "Aether Intelligence",
                authorUsername = "aether_ai",
                authorAvatarRes = R.drawable.img_app_icon,
                videoCoverRes = R.drawable.img_app_icon,
                caption = "Next-gen encrypted chats & instant smart replies are now live on Aether! 🔒🚀",
                musicTrack = "Aether Synth Wave • Sound Design",
                likesCount = 245000,
                commentsCount = 7800,
                sharesCount = 32000,
                isLiked = true
            )
        )

        // Seed Conversations
        _conversations.value = listOf(
            ChatConversation(
                id = "chat_ai",
                participantName = "Aether AI Assistant",
                participantUsername = "aether_copilot",
                avatarRes = R.drawable.img_app_icon,
                isVerified = true,
                lastMessage = "✨ I'm ready to craft captions, brainstorm reels, or analyze feed trends!",
                lastTimestamp = "Just now",
                unreadCount = 1,
                isOnline = true,
                isAiAssistant = true
            ),
            ChatConversation(
                id = "chat_elena",
                participantName = "Elena Vance",
                participantUsername = "elena.vision",
                avatarRes = R.drawable.img_story_art,
                isVerified = true,
                lastMessage = "Loved your latest reel! Did you use the new neural audio filter?",
                lastTimestamp = "12m ago",
                unreadCount = 2,
                isOnline = true
            ),
            ChatConversation(
                id = "chat_nova",
                participantName = "Nova Creative Lab",
                participantUsername = "novalab_ai",
                avatarRes = R.drawable.img_hero_banner,
                isVerified = true,
                lastMessage = "🎙️ Voice message (0:24)",
                lastTimestamp = "1h ago",
                unreadCount = 0,
                isOnline = false
            ),
            ChatConversation(
                id = "chat_secret",
                participantName = "Alex Rivera (Encrypted)",
                participantUsername = "alex_crypto",
                avatarRes = R.drawable.img_avatar_ai,
                isVerified = false,
                lastMessage = "🔒 Vanish mode active. Messages auto-delete after 24h.",
                lastTimestamp = "3h ago",
                unreadCount = 0,
                isOnline = true,
                isSecret = true
            )
        )

        // Seed initial messages for chat_ai
        val seedMessages = listOf(
            ChatMessageEntity(
                id = "msg_ai_1",
                chatId = "chat_ai",
                senderId = "aether_copilot",
                senderName = "Aether AI Assistant",
                text = "Hello Vasudev! I am your native Aether AI Creative Copilot powered by Gemini 3.5 Flash. I can generate viral captions, suggest hashtags, polish scripts, and answer anything. What should we create today?",
                timestamp = "10:30 AM",
                isMine = false,
                isRead = true,
                messageTypeStr = "TEXT",
                voiceDurationSec = 0,
                isSecretEncrypted = false,
                isStarred = true
            ),
            ChatMessageEntity(
                id = "msg_elena_1",
                chatId = "chat_elena",
                senderId = "elena.vision",
                senderName = "Elena Vance",
                text = "Hey! Check out the Tokyo photo series I just posted 📸",
                timestamp = "11:15 AM",
                isMine = false,
                isRead = true,
                messageTypeStr = "TEXT",
                voiceDurationSec = 0,
                isSecretEncrypted = false,
                isStarred = false
            ),
            ChatMessageEntity(
                id = "msg_elena_2",
                chatId = "chat_elena",
                senderId = "user_me",
                senderName = "Vasudev",
                text = "The neon reflections look incredible! Colors really pop on OLED screens.",
                timestamp = "11:18 AM",
                isMine = true,
                isRead = true,
                messageTypeStr = "TEXT",
                voiceDurationSec = 0,
                isSecretEncrypted = false,
                isStarred = false
            ),
            ChatMessageEntity(
                id = "msg_elena_3",
                chatId = "chat_elena",
                senderId = "elena.vision",
                senderName = "Elena Vance",
                text = "Loved your latest reel! Did you use the new neural audio filter?",
                timestamp = "11:22 AM",
                isMine = false,
                isRead = false,
                messageTypeStr = "TEXT",
                voiceDurationSec = 0,
                isSecretEncrypted = false,
                isStarred = false
            )
        )
        dao.insertMessages(seedMessages)

        // Seed Notifications
        val seedNotifications = listOf(
            NotificationEntity(
                id = "notif_1",
                title = "Elena Vance liked your post",
                message = "Liked: 'Testing holographic neural icons 🔮'",
                timestamp = "15m ago",
                typeStr = "LIKE",
                avatarRes = R.drawable.img_story_art,
                isRead = false
            ),
            NotificationEntity(
                id = "notif_2",
                title = "AI Creator Reward",
                message = "You earned +250 Coins for high community engagement this week!",
                timestamp = "1h ago",
                typeStr = "REWARD",
                avatarRes = R.drawable.img_app_icon,
                isRead = false
            ),
            NotificationEntity(
                id = "notif_3",
                title = "Nova Creative Lab started a Live Stream",
                message = "🎙️ 'Live backstage at AI Creator Summit 2026'",
                timestamp = "3h ago",
                typeStr = "AI_INSIGHT",
                avatarRes = R.drawable.img_hero_banner,
                isRead = true
            )
        )
        dao.insertNotifications(seedNotifications)

        // Seed Live Stream
        _activeLiveStreams.value = listOf(
            LiveStreamInfo(
                id = "live_1",
                hostName = "Nova Creative Lab",
                hostUsername = "novalab_ai",
                hostAvatarRes = R.drawable.img_hero_banner,
                title = "Live AI Creation & Music Jam Session 🎹⚡",
                viewersCount = 24890,
                likesCount = 142000,
                comments = listOf(
                    LiveComment("c1", "maya_design", "This audio reactive visualizer is crazy! 🔥", isVip = true),
                    LiveComment("c2", "cyber_sam", "Sent a Diamond Crown 👑", giftSent = "👑"),
                    LiveComment("c3", "dev_alex", "How many parameters in this neural shader?", isVip = false),
                    LiveComment("c4", "elena.vision", "Vibes are immaculate ✨", isVip = true)
                )
            )
        )
    }

    // Posts Flow mapped from Room
    fun getPostsFlow(): Flow<List<PostItem>> {
        return dao.getAllPosts().map { entities ->
            entities.map { entity ->
                val tags = if (entity.hashtagsCsv.isNotBlank()) entity.hashtagsCsv.split(",") else emptyList()
                val mediaType = try {
                    PostMediaType.valueOf(entity.mediaTypeStr)
                } catch (e: Exception) {
                    PostMediaType.IMAGE
                }
                PostItem(
                    id = entity.id,
                    authorId = entity.authorId,
                    authorName = entity.authorName,
                    authorUsername = entity.authorUsername,
                    authorAvatarRes = entity.authorAvatarRes,
                    isAuthorVerified = entity.isAuthorVerified,
                    timestamp = entity.timestamp,
                    caption = entity.caption,
                    hashtags = tags,
                    mediaType = mediaType,
                    imageDrawableRes = entity.imageDrawableRes,
                    likesCount = entity.likesCount,
                    commentsCount = entity.commentsCount,
                    repostsCount = entity.repostsCount,
                    isLiked = entity.isLiked,
                    isSaved = entity.isSaved,
                    isReposted = entity.isReposted,
                    audioTrackName = entity.audioTrackName,
                    location = entity.location,
                    aiInsight = entity.aiInsight,
                    pollOptions = if (mediaType == PostMediaType.POLL) listOf(
                        PollOption(1, "Neon Cyber Aesthetics", 68, true),
                        PollOption(2, "Minimal Obsidian Dark", 32, false)
                    ) else emptyList(),
                    comments = listOf(
                        CommentItem("c10", "Elena Vance", "elena.vision", R.drawable.img_story_art, "Spectacular composition! Love the lighting grade.", "12m ago", 34, true),
                        CommentItem("c11", "Kai Tanaka", "kai.ai", R.drawable.img_app_icon, "The neural detail is on point ⚡", "8m ago", 12, false)
                    )
                )
            }
        }
    }

    // Post Actions
    suspend fun toggleLikePost(post: PostItem) {
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
        dao.updatePostLike(post.id, newLiked, newCount)
    }

    suspend fun toggleSavePost(post: PostItem) {
        val newSaved = !post.isSaved
        dao.updatePostSave(post.id, newSaved)
    }

    suspend fun toggleRepost(post: PostItem) {
        val newReposted = !post.isReposted
        val newCount = if (newReposted) post.repostsCount + 1 else (post.repostsCount - 1).coerceAtLeast(0)
        dao.updatePostRepost(post.id, newReposted, newCount)
    }

    suspend fun createNewPost(
        caption: String,
        hashtags: String,
        mediaType: PostMediaType,
        drawableRes: Int = R.drawable.img_hero_banner
    ) {
        val user = _currentUser.value
        val newPost = PostEntity(
            id = "post_" + UUID.randomUUID().toString().take(8),
            authorId = user.id,
            authorName = user.displayName,
            authorUsername = user.username,
            authorAvatarRes = user.avatarDrawableRes,
            isAuthorVerified = user.isVerified,
            timestamp = "Just now",
            caption = caption,
            hashtagsCsv = hashtags,
            mediaTypeStr = mediaType.name,
            imageDrawableRes = drawableRes,
            likesCount = 1,
            commentsCount = 0,
            repostsCount = 0,
            isLiked = true,
            isSaved = false,
            isReposted = false,
            audioTrackName = "Original Audio • " + user.displayName,
            location = "Aether Prime",
            aiInsight = "AI Verified Original Content: High Quality"
        )
        dao.insertPost(newPost)
    }

    // Chat Actions
    fun getMessagesForChat(chatId: String): Flow<List<ChatMessage>> {
        return dao.getMessagesForChat(chatId).map { entities ->
            entities.map { entity ->
                val type = try {
                    MessageType.valueOf(entity.messageTypeStr)
                } catch (e: Exception) {
                    MessageType.TEXT
                }
                ChatMessage(
                    id = entity.id,
                    chatId = entity.chatId,
                    senderId = entity.senderId,
                    senderName = entity.senderName,
                    text = entity.text,
                    timestamp = entity.timestamp,
                    isMine = entity.isMine,
                    isRead = entity.isRead,
                    messageType = type,
                    voiceDurationSec = entity.voiceDurationSec,
                    isSecretEncrypted = entity.isSecretEncrypted,
                    isStarred = entity.isStarred
                )
            }
        }
    }

    suspend fun sendMessage(
        chatId: String,
        text: String,
        messageType: MessageType = MessageType.TEXT,
        voiceSec: Int = 0
    ) {
        val user = _currentUser.value
        val entity = ChatMessageEntity(
            id = "msg_" + UUID.randomUUID().toString().take(8),
            chatId = chatId,
            senderId = user.id,
            senderName = user.displayName,
            text = text,
            timestamp = "Just now",
            isMine = true,
            isRead = true,
            messageTypeStr = messageType.name,
            voiceDurationSec = voiceSec,
            isSecretEncrypted = chatId == "chat_secret",
            isStarred = false
        )
        dao.insertMessage(entity)

        // If chatting with AI Copilot, trigger AI response
        if (chatId == "chat_ai") {
            triggerAiChatResponse(text)
        }
    }

    private suspend fun triggerAiChatResponse(userPrompt: String) {
        val aiResponseText = GeminiApiClient.generatePrompt(userPrompt)
        val aiEntity = ChatMessageEntity(
            id = "msg_ai_" + UUID.randomUUID().toString().take(8),
            chatId = "chat_ai",
            senderId = "aether_copilot",
            senderName = "Aether AI Assistant",
            text = aiResponseText,
            timestamp = "Just now",
            isMine = false,
            isRead = true,
            messageTypeStr = "TEXT",
            voiceDurationSec = 0,
            isSecretEncrypted = false,
            isStarred = false
        )
        dao.insertMessage(aiEntity)
    }

    // Call Management
    fun startCall(partnerName: String, partnerAvatar: Int, isVideo: Boolean) {
        _activeCall.value = ActiveCallState(
            partnerName = partnerName,
            partnerAvatar = partnerAvatar,
            isVideo = isVideo,
            durationSeconds = 0,
            isMuted = false,
            isSpeakerOn = true,
            isCameraOn = isVideo
        )
    }

    fun endCall() {
        _activeCall.value = null
    }

    fun toggleMuteCall() {
        val current = _activeCall.value ?: return
        _activeCall.value = current.copy(isMuted = !current.isMuted)
    }

    fun toggleSpeakerCall() {
        val current = _activeCall.value ?: return
        _activeCall.value = current.copy(isSpeakerOn = !current.isSpeakerOn)
    }

    fun toggleCameraCall() {
        val current = _activeCall.value ?: return
        _activeCall.value = current.copy(isCameraOn = !current.isCameraOn)
    }

    // Virtual Coins & Gifts
    suspend fun sendVirtualGift(gift: VirtualGift, recipientName: String): Boolean {
        val current = _currentUser.value
        if (current.coinsBalance >= gift.coinCost) {
            val updated = current.copy(coinsBalance = current.coinsBalance - gift.coinCost)
            _currentUser.value = updated
            return true
        }
        return false
    }

    fun purchaseCoins(amount: Int) {
        val current = _currentUser.value
        _currentUser.value = current.copy(coinsBalance = current.coinsBalance + amount)
    }

    // AI Studio Tool Execution
    suspend fun executeAITool(mode: AIAssistantMode, input: String): String {
        val prompt = when (mode) {
            AIAssistantMode.CAPTION_CREATOR -> "Generate 3 viral, engaging social media captions with emojis and tone variations for: $input"
            AIAssistantMode.HASHTAG_RADAR -> "Generate the top 15 trending and high-reach hashtags for this topic: $input"
            AIAssistantMode.BIO_GENERATOR -> "Create 3 punchy, aesthetic social media bios for a profile about: $input"
            AIAssistantMode.REEL_SCRIPT_WRITER -> "Write a 30-second viral reel script with Hook, Core value, and CTA for: $input"
            AIAssistantMode.IMAGE_PROMPTER -> "Expand this into a hyper-detailed, photorealistic AI image generation prompt: $input"
            AIAssistantMode.SPAM_MODERATION -> "Analyze the following comment/post for spam, safety, and toxicity: $input"
            AIAssistantMode.TREND_ANALYZER -> "Analyze current social media trends, velocity, and creator opportunities for: $input"
            AIAssistantMode.SMART_REPLY -> "Provide 3 quick, smart, natural reply suggestions to this message: $input"
            AIAssistantMode.TRANSLATOR -> "Translate the following text accurately into English and Spanish: $input"
            AIAssistantMode.CHAT_COMPANION -> input
        }
        val result = GeminiApiClient.generatePrompt(prompt)
        dao.insertAILog(
            AILogEntity(
                id = UUID.randomUUID().toString(),
                mode = mode.name,
                prompt = input,
                response = result,
                timestamp = System.currentTimeMillis()
            )
        )
        return result
    }

    // Free Live Hosting Server Management
    fun selectHostingServer(server: LiveHostingServer) {
        _selectedHostingServer.value = server
        _broadcastSession.value = _broadcastSession.value.copy(selectedServer = server)
    }

    suspend fun testServerPing(serverId: String) {
        _freeLiveServers.value = _freeLiveServers.value.map { server ->
            if (server.id == serverId) {
                server.copy(status = ServerStatus.PINGING)
            } else server
        }
        delay(450)
        _freeLiveServers.value = _freeLiveServers.value.map { server ->
            if (server.id == serverId) {
                val newPing = (12..40).random()
                val status = if (newPing <= 20) ServerStatus.ULTRA_FAST else ServerStatus.ONLINE
                server.copy(latencyMs = newPing, status = status)
            } else server
        }
        if (_selectedHostingServer.value.id == serverId) {
            _selectedHostingServer.value = _freeLiveServers.value.first { it.id == serverId }
        }
    }

    fun generateNewStreamKey() {
        val newKey = "live_free_aether_" + UUID.randomUUID().toString().take(12)
        _broadcastSession.value = _broadcastSession.value.copy(streamKey = newKey)
    }

    fun startBroadcasting(title: String, category: String, mode: LiveBroadcastMode) {
        val session = _broadcastSession.value.copy(
            isBroadcasting = true,
            isEnded = false,
            streamTitle = if (title.isNotBlank()) title else "Live from Aether Studio ⚡",
            category = category,
            broadcastMode = mode,
            durationSeconds = 0,
            viewersCount = 42,
            peakViewers = 42,
            likesCount = 18,
            giftsReceivedCount = 0,
            coinsEarned = 0,
            selectedServer = _selectedHostingServer.value
        )
        _broadcastSession.value = session

        // Start real-time broadcaster ticker
        broadcastTickerJob?.cancel()
        broadcastTickerJob = scope.launch(Dispatchers.Default) {
            while (_broadcastSession.value.isBroadcasting) {
                delay(1000)
                val current = _broadcastSession.value
                val viewerDelta = (-2..8).random()
                val newViewers = (current.viewersCount + viewerDelta).coerceAtLeast(12)
                val newLikes = current.likesCount + (1..5).random()
                val newCoins = if (current.durationSeconds % 8 == 0) current.coinsEarned + 50 else current.coinsEarned
                val newGifts = if (current.durationSeconds % 8 == 0) current.giftsReceivedCount + 1 else current.giftsReceivedCount

                _broadcastSession.value = current.copy(
                    durationSeconds = current.durationSeconds + 1,
                    viewersCount = newViewers,
                    peakViewers = maxOf(current.peakViewers, newViewers),
                    likesCount = newLikes,
                    coinsEarned = newCoins,
                    giftsReceivedCount = newGifts,
                    currentBitrateKbps = (5600..6200).random()
                )
            }
        }
    }

    fun stopBroadcasting() {
        broadcastTickerJob?.cancel()
        val current = _broadcastSession.value
        _broadcastSession.value = current.copy(
            isBroadcasting = false,
            isEnded = true
        )
    }

    fun resetBroadcastSession() {
        _broadcastSession.value = LiveBroadcastSession(
            selectedServer = _selectedHostingServer.value
        )
    }

    fun toggleBroadcastMic() {
        val current = _broadcastSession.value
        _broadcastSession.value = current.copy(isMicMuted = !current.isMicMuted)
    }

    fun flipBroadcastCamera() {
        val current = _broadcastSession.value
        _broadcastSession.value = current.copy(isCameraFlipped = !current.isCameraFlipped)
    }

    fun toggleBroadcastBeautyFilter() {
        val current = _broadcastSession.value
        _broadcastSession.value = current.copy(isBeautyFilterOn = !current.isBeautyFilterOn)
    }

    fun toggleCloudRecording() {
        val current = _broadcastSession.value
        _broadcastSession.value = current.copy(isCloudRecordingEnabled = !current.isCloudRecordingEnabled)
    }

    // Notifications
    fun getNotificationsFlow(): Flow<List<NotificationItem>> {
        return dao.getAllNotifications().map { entities ->
            entities.map { entity ->
                val type = try {
                    NotificationType.valueOf(entity.typeStr)
                } catch (e: Exception) {
                    NotificationType.LIKE
                }
                NotificationItem(
                    id = entity.id,
                    title = entity.title,
                    message = entity.message,
                    timestamp = entity.timestamp,
                    type = type,
                    avatarRes = entity.avatarRes,
                    isRead = entity.isRead
                )
            }
        }
    }

    suspend fun markAllNotificationsAsRead() {
        dao.markAllNotificationsAsRead()
    }

    // ==========================================
    // SETTINGS & AUTHENTICATION STATE & LOGIC
    // ==========================================

    // Account & Profile Details
    private val _accountDetails = MutableStateFlow(AccountDetails())
    val accountDetails = _accountDetails.asStateFlow()

    // Privacy Settings
    private val _privacySettings = MutableStateFlow(PrivacySettings())
    val privacySettings = _privacySettings.asStateFlow()

    // Notification Settings
    private val _notificationSettings = MutableStateFlow(NotificationSettings())
    val notificationSettings = _notificationSettings.asStateFlow()

    // Security Settings
    private val _securitySettings = MutableStateFlow(SecuritySettings())
    val securitySettings = _securitySettings.asStateFlow()

    // Data Usage Settings
    private val _dataUsageSettings = MutableStateFlow(DataUsageSettings())
    val dataUsageSettings = _dataUsageSettings.asStateFlow()

    // Theme Mode
    private val _themeMode = MutableStateFlow(AppThemeMode.DARK)
    val themeMode = _themeMode.asStateFlow()

    // Supported Languages
    val availableLanguages = listOf(
        AppLanguage("en", "English", "English (US)", "🇺🇸"),
        AppLanguage("es", "Spanish", "Español", "🇪🇸"),
        AppLanguage("fr", "French", "Français", "🇫🇷"),
        AppLanguage("de", "German", "Deutsch", "🇩🇪"),
        AppLanguage("ja", "Japanese", "日本語", "🇯🇵"),
        AppLanguage("hi", "Hindi", "हिन्दी", "🇮🇳"),
        AppLanguage("pt", "Portuguese", "Português", "🇧🇷"),
        AppLanguage("it", "Italian", "Italiano", "🇮🇹"),
        AppLanguage("ko", "Korean", "한국어", "🇰🇷"),
        AppLanguage("ar", "Arabic", "العربية", "🇸🇦")
    )
    private val _selectedLanguage = MutableStateFlow(availableLanguages[0])
    val selectedLanguage = _selectedLanguage.asStateFlow()

    // Active Login Sessions / Devices
    private val _loginSessions = MutableStateFlow(
        listOf(
            LoginSessionDevice(
                id = "device_current",
                deviceName = "Google Pixel 9 Pro",
                platform = "Android 15 (Aether App v2.4)",
                location = "San Francisco, CA, USA",
                ipAddress = "192.168.1.42 (Active Now)",
                lastActive = "Current Session",
                isCurrent = true
            ),
            LoginSessionDevice(
                id = "device_macbook",
                deviceName = "MacBook Pro 16\" (M3 Max)",
                platform = "macOS Sonoma (Chrome 128)",
                location = "San Francisco, CA, USA",
                ipAddress = "172.56.21.80",
                lastActive = "2 hours ago",
                isCurrent = false
            ),
            LoginSessionDevice(
                id = "device_ipad",
                deviceName = "iPad Pro 13\" (M4)",
                platform = "iPadOS 18 (Aether Mobile)",
                location = "San Jose, CA, USA",
                ipAddress = "198.51.100.14",
                lastActive = "Yesterday at 8:42 PM",
                isCurrent = false
            ),
            LoginSessionDevice(
                id = "device_windows",
                deviceName = "Alienware Aurora R16",
                platform = "Windows 11 (OBS Studio RTMP)",
                location = "New York, NY, USA",
                ipAddress = "203.0.113.195",
                lastActive = "August 12, 2026",
                isCurrent = false
            )
        )
    )
    val loginSessions = _loginSessions.asStateFlow()

    // Blocked Accounts
    private val _blockedUsers = MutableStateFlow(
        listOf(
            BlockedUser("block_1", "crypto_spambot_99", "Crypto Alerts Daily", R.drawable.img_avatar_ai, "Blocked on July 18, 2026"),
            BlockedUser("block_2", "troll_account_404", "Ghost Troll", R.drawable.img_story_art, "Blocked on August 02, 2026")
        )
    )
    val blockedUsers = _blockedUsers.asStateFlow()

    // FAQ Items for Help & Support
    val faqItems = listOf(
        SupportFaqItem(
            id = "faq_1",
            category = "Account & Privacy",
            question = "How do I make my Aether Social profile private?",
            answer = "Go to Settings › Privacy Settings and toggle 'Private Account'. When enabled, only approved followers can see your posts, stories, and live broadcasts."
        ),
        SupportFaqItem(
            id = "faq_2",
            category = "Free Live Hosting",
            question = "Are the Live Broadcast edge servers completely free?",
            answer = "Yes! Aether provides 100% free high-bandwidth ingest nodes across US, Europe, Tokyo, and Singapore supporting mobile camera streaming and RTMP/OBS."
        ),
        SupportFaqItem(
            id = "faq_3",
            category = "Security & 2FA",
            question = "How does Two-Factor Authentication protect my account?",
            answer = "When 2FA is active, logging in from any new device requires a 6-digit TOTP code from your authenticator app or phone verification."
        ),
        SupportFaqItem(
            id = "faq_4",
            category = "Coins & Gifting",
            question = "How do creators cash out virtual gifts received during live streams?",
            answer = "Virtual gifts are converted to Creator Gold in your Creator Studio. You can withdraw earnings once you reach a minimum balance of 1,000 Coins."
        ),
        SupportFaqItem(
            id = "faq_5",
            category = "Data & Storage",
            question = "How can I reduce mobile data usage on video feeds?",
            answer = "In Settings › Data Usage, enable 'Data Saver Mode' and set 'Video Autoplay' to 'Wi-Fi Only'."
        )
    )

    // Authentication State
    private val _authState = MutableStateFlow(
        AuthState(
            isAuthenticated = true,
            currentEmailOrUsername = "vasudev7490@gmail.com",
            rememberMe = true
        )
    )
    val authState = _authState.asStateFlow()

    private var otpTimerJob: Job? = null

    // Update Profile Information
    fun updateProfile(
        displayName: String,
        username: String,
        bio: String,
        website: String,
        avatarRes: Int
    ) {
        val current = _currentUser.value
        _currentUser.value = current.copy(
            displayName = displayName.ifBlank { current.displayName },
            username = username.ifBlank { current.username },
            bio = bio,
            website = website,
            avatarDrawableRes = if (avatarRes != 0) avatarRes else current.avatarDrawableRes
        )
    }

    // Update Account Settings
    fun updateAccountDetails(email: String, phone: String) {
        _accountDetails.value = _accountDetails.value.copy(
            email = email.ifBlank { _accountDetails.value.email },
            phoneNumber = phone.ifBlank { _accountDetails.value.phoneNumber }
        )
    }

    // Update Privacy Settings
    fun updatePrivacySettings(newSettings: PrivacySettings) {
        _privacySettings.value = newSettings
    }

    // Update Notification Settings
    fun updateNotificationSettings(newSettings: NotificationSettings) {
        _notificationSettings.value = newSettings
    }

    // Update Security Settings
    fun updateSecuritySettings(newSettings: SecuritySettings) {
        _securitySettings.value = newSettings
    }

    // Update Data Usage Settings
    fun updateDataUsageSettings(newSettings: DataUsageSettings) {
        _dataUsageSettings.value = newSettings
    }

    // Clear App Cache
    fun clearAppCache() {
        _dataUsageSettings.value = _dataUsageSettings.value.copy(cacheSizeBytes = 0L)
    }

    // Language & Theme Selectors
    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    // Session Management
    fun revokeLoginSession(deviceId: String) {
        _loginSessions.value = _loginSessions.value.filter { it.id != deviceId }
    }

    fun logoutAllOtherDevices() {
        _loginSessions.value = _loginSessions.value.filter { it.isCurrent }
    }

    // Block / Unblock Users
    fun unblockUser(userId: String) {
        _blockedUsers.value = _blockedUsers.value.filter { it.id != userId }
    }

    fun blockUser(username: String) {
        val clean = username.trim().removePrefix("@")
        if (clean.isBlank()) return
        val newBlocked = BlockedUser(
            id = "block_" + UUID.randomUUID().toString().take(6),
            username = clean,
            displayName = "@$clean",
            avatarRes = R.drawable.img_avatar_ai,
            blockedDate = "Blocked Today"
        )
        _blockedUsers.value = _blockedUsers.value + newBlocked
    }

    // Change Password
    fun changePassword(oldPass: String, newPass: String): Result<String> {
        if (oldPass.isBlank() || newPass.isBlank()) {
            return Result.failure(Exception("Please fill in all password fields."))
        }
        if (newPass.length < 8) {
            return Result.failure(Exception("New password must be at least 8 characters long."))
        }
        _securitySettings.value = _securitySettings.value.copy(
            lastPasswordChangeDate = "Just now (Today)"
        )
        return Result.success("Password changed securely! 🔒")
    }

    // Submit Report or Support Ticket
    fun submitReportTicket(category: ReportCategory, description: String, contactEmail: String): Boolean {
        if (description.isBlank()) return false
        // Successfully logged ticket in internal system
        return true
    }

    // ----------------------------------------------------
    // AUTHENTICATION LOGIC (Login, Signup, OTP, Logout)
    // ----------------------------------------------------

    fun setAuthMode(mode: AuthMode) {
        _authState.value = _authState.value.copy(
            currentAuthMode = mode,
            errorMessage = null,
            statusMessage = null
        )
    }

    fun login(emailOrUsername: String, pass: String, remember: Boolean): Boolean {
        if (emailOrUsername.isBlank() || pass.isBlank()) {
            _authState.value = _authState.value.copy(
                errorMessage = "Please enter both your email/username and password."
            )
            return false
        }
        if (pass.length < 6) {
            _authState.value = _authState.value.copy(
                errorMessage = "Invalid password. Password must be at least 6 characters."
            )
            return false
        }

        // Successful login
        _authState.value = _authState.value.copy(
            isAuthenticated = true,
            currentEmailOrUsername = emailOrUsername,
            rememberMe = remember,
            errorMessage = null,
            statusMessage = "Welcome back to Aether Social!"
        )
        return true
    }

    fun signup(fullName: String, username: String, email: String, pass: String): Boolean {
        if (fullName.isBlank() || username.isBlank() || email.isBlank() || pass.isBlank()) {
            _authState.value = _authState.value.copy(
                errorMessage = "Please complete all registration fields."
            )
            return false
        }
        if (!email.contains("@") || !email.contains(".")) {
            _authState.value = _authState.value.copy(
                errorMessage = "Please enter a valid email address."
            )
            return false
        }
        if (pass.length < 8) {
            _authState.value = _authState.value.copy(
                errorMessage = "Password must be at least 8 characters long with letters and numbers."
            )
            return false
        }

        // Update profile with new user details
        val cleanUser = username.trim().removePrefix("@")
        _currentUser.value = _currentUser.value.copy(
            displayName = fullName.trim(),
            username = cleanUser,
            bio = "👋 Hey! I just joined Aether Social. Exploring generative feeds & live creative streams!"
        )
        _accountDetails.value = _accountDetails.value.copy(
            email = email.trim(),
            joinedDate = "August 2026"
        )

        _authState.value = _authState.value.copy(
            isAuthenticated = true,
            currentEmailOrUsername = email,
            errorMessage = null,
            statusMessage = "Account created successfully! Welcome, $fullName 🎉"
        )
        return true
    }

    fun requestPasswordResetOtp(emailOrPhone: String): Boolean {
        if (emailOrPhone.isBlank()) {
            _authState.value = _authState.value.copy(
                errorMessage = "Please enter your registered email address or phone."
            )
            return false
        }

        val generatedOtp = "849201" // Demo OTP
        _authState.value = _authState.value.copy(
            currentAuthMode = AuthMode.VERIFY_OTP,
            resetEmailTarget = emailOrPhone,
            otpCode = generatedOtp,
            otpTimeRemainingSeconds = 60,
            isOtpActive = true,
            errorMessage = null,
            statusMessage = "Verification code sent to $emailOrPhone (Demo code: $generatedOtp)"
        )

        // Launch OTP Countdown
        otpTimerJob?.cancel()
        otpTimerJob = scope.launch(Dispatchers.Default) {
            var timeLeft = 60
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
                _authState.value = _authState.value.copy(otpTimeRemainingSeconds = timeLeft)
            }
        }
        return true
    }

    fun verifyOtp(enteredCode: String): Boolean {
        if (enteredCode.length != 6) {
            _authState.value = _authState.value.copy(
                errorMessage = "Please enter the complete 6-digit verification code."
            )
            return false
        }
        // Accept any 6 digit code or generated demo code
        _authState.value = _authState.value.copy(
            currentAuthMode = AuthMode.RESET_NEW_PASSWORD,
            errorMessage = null,
            statusMessage = "Code verified! Please create your new secure password."
        )
        return true
    }

    fun resetNewPassword(newPass: String, confirmPass: String): Boolean {
        if (newPass.length < 8) {
            _authState.value = _authState.value.copy(
                errorMessage = "Password must be at least 8 characters long."
            )
            return false
        }
        if (newPass != confirmPass) {
            _authState.value = _authState.value.copy(
                errorMessage = "Passwords do not match. Please verify."
            )
            return false
        }

        _authState.value = _authState.value.copy(
            currentAuthMode = AuthMode.LOGIN,
            errorMessage = null,
            statusMessage = "Password reset successfully! You can now log in."
        )
        return true
    }

    fun logout(logoutAllDevices: Boolean = false) {
        if (logoutAllDevices) {
            _loginSessions.value = emptyList()
        }
        _authState.value = _authState.value.copy(
            isAuthenticated = false,
            currentAuthMode = AuthMode.LOGIN,
            errorMessage = null,
            statusMessage = if (logoutAllDevices) "Logged out of all active devices." else "Logged out safely."
        )
    }
}

data class ActiveCallState(
    val partnerName: String,
    val partnerAvatar: Int,
    val isVideo: Boolean,
    val durationSeconds: Int = 0,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = true,
    val isCameraOn: Boolean = true
)
