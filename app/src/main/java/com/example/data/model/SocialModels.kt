package com.example.data.model

import java.util.UUID

data class UserProfile(
    val id: String,
    val username: String,
    val displayName: String,
    val bio: String,
    val website: String = "https://aether.social/@" + username,
    val avatarUrl: String = "",
    val avatarDrawableRes: Int = 0,
    val isVerified: Boolean = true,
    val isVip: Boolean = false,
    val followersCount: Int = 124500,
    val followingCount: Int = 412,
    val likesCount: Int = 982300,
    val coinsBalance: Int = 2450,
    val isOnline: Boolean = true,
    val badges: List<String> = listOf("AI Creator", "Top 1%", "Verified Partner")
)

enum class PostMediaType {
    IMAGE,
    CAROUSEL,
    VIDEO_REEL,
    POLL,
    AUDIO_NOTE,
    TEXT_UPDATE
}

data class PollOption(
    val id: Int,
    val text: String,
    val votes: Int = 0,
    val isSelectedByMe: Boolean = false
)

data class CommentItem(
    val id: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarRes: Int = 0,
    val text: String,
    val timestamp: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class PostItem(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarRes: Int = 0,
    val isAuthorVerified: Boolean = false,
    val timestamp: String,
    val caption: String,
    val hashtags: List<String> = emptyList(),
    val mediaType: PostMediaType = PostMediaType.IMAGE,
    val imageDrawableRes: Int = 0,
    val carouselImages: List<Int> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val repostsCount: Int = 0,
    val viewsCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isReposted: Boolean = false,
    val pollOptions: List<PollOption> = emptyList(),
    val audioTrackName: String = "",
    val location: String = "",
    val comments: List<CommentItem> = emptyList(),
    val aiInsight: String = ""
)

data class StoryItem(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorAvatarRes: Int = 0,
    val imageRes: Int = 0,
    val timestamp: String,
    val isViewed: Boolean = false,
    val hasUnseen: Boolean = true,
    val caption: String = "",
    val pollQuestion: String = "",
    val pollOptions: List<PollOption> = emptyList(),
    val questionPrompt: String = "",
    val countdownText: String = "",
    val musicTrack: String = "",
    val isLive: Boolean = false
)

data class ReelItem(
    val id: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarRes: Int = 0,
    val isVerified: Boolean = true,
    val videoCoverRes: Int = 0,
    val caption: String,
    val musicTrack: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val remixesCount: Int = 1420,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val aiSubtitles: List<String> = listOf("Generating next-gen visual feeds...", "Powered by Gemini AI ⚡"),
    val filterEffect: String = "Cyber Neon"
)

enum class MessageType {
    TEXT,
    VOICE,
    IMAGE,
    LOCATION,
    POLL,
    GIFT
}

data class ChatMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isMine: Boolean,
    val isRead: Boolean = true,
    val messageType: MessageType = MessageType.TEXT,
    val voiceDurationSec: Int = 0,
    val voiceWaveform: List<Float> = listOf(0.3f, 0.7f, 0.9f, 0.4f, 0.8f, 0.5f, 0.2f, 0.6f, 1.0f, 0.4f),
    val isSecretEncrypted: Boolean = false,
    val isStarred: Boolean = false,
    val replyToText: String? = null
)

data class ChatConversation(
    val id: String,
    val participantName: String,
    val participantUsername: String,
    val avatarRes: Int = 0,
    val isVerified: Boolean = false,
    val lastMessage: String,
    val lastTimestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val isGroup: Boolean = false,
    val isSecret: Boolean = false,
    val isAiAssistant: Boolean = false,
    val isTyping: Boolean = false
)

data class VirtualGift(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val coinCost: Int,
    val animationDescription: String
)

data class LiveComment(
    val id: String,
    val username: String,
    val message: String,
    val giftSent: String? = null,
    val isVip: Boolean = false
)

data class LiveStreamInfo(
    val id: String,
    val hostName: String,
    val hostUsername: String,
    val hostAvatarRes: Int = 0,
    val title: String,
    val viewersCount: Int = 18420,
    val likesCount: Int = 94300,
    val category: String = "AI Art & Creative Tech",
    val isLive: Boolean = true,
    val comments: List<LiveComment> = emptyList()
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val avatarRes: Int = 0,
    val isRead: Boolean = false
)

enum class NotificationType {
    LIKE,
    COMMENT,
    MENTION,
    FOLLOW,
    AI_INSIGHT,
    REWARD,
    CALL
}

enum class AIAssistantMode {
    CHAT_COMPANION,
    CAPTION_CREATOR,
    HASHTAG_RADAR,
    IMAGE_PROMPTER,
    REEL_SCRIPT_WRITER,
    BIO_GENERATOR,
    SMART_REPLY,
    SPAM_MODERATION,
    TREND_ANALYZER,
    TRANSLATOR
}

data class AIInteractionLog(
    val id: String,
    val mode: AIAssistantMode,
    val prompt: String,
    val response: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class ServerStatus {
    ONLINE,
    ULTRA_FAST,
    OPTIMAL,
    PINGING
}

enum class LiveBroadcastMode {
    CAMERA_DIRECT,
    EXTERNAL_OBS_RTMP,
    SCREEN_SHARE
}

data class LiveHostingServer(
    val id: String,
    val name: String,
    val region: String,
    val flagEmoji: String,
    val rtmpIngestUrl: String,
    val webRtcUrl: String,
    val latencyMs: Int,
    val maxBitrateKbps: Int = 8000,
    val maxResolution: String = "1080p 60fps",
    val status: ServerStatus = ServerStatus.ONLINE,
    val isFreeUnlimited: Boolean = true,
    val activeBroadcasters: Int = 84,
    val serverLoadPercent: Int = 28,
    val protocols: List<String> = listOf("RTMP / RTMPS", "WebRTC WHIP", "SRT Relay")
)

data class LiveBroadcastSession(
    val isBroadcasting: Boolean = false,
    val streamTitle: String = "⚡ Next-Gen Generative Social Architecture & Live Demo",
    val category: String = "AI & Technology",
    val selectedServer: LiveHostingServer? = null,
    val streamKey: String = "live_free_aether_" + UUID.randomUUID().toString().take(12),
    val broadcastMode: LiveBroadcastMode = LiveBroadcastMode.CAMERA_DIRECT,
    val durationSeconds: Int = 0,
    val viewersCount: Int = 0,
    val peakViewers: Int = 0,
    val likesCount: Int = 0,
    val giftsReceivedCount: Int = 0,
    val coinsEarned: Int = 0,
    val currentBitrateKbps: Int = 5820,
    val currentFps: Int = 60,
    val isMicMuted: Boolean = false,
    val isCameraFlipped: Boolean = false,
    val isBeautyFilterOn: Boolean = true,
    val isCloudRecordingEnabled: Boolean = true,
    val isEnded: Boolean = false
)
