package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val authorId: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarRes: Int,
    val isAuthorVerified: Boolean,
    val timestamp: String,
    val caption: String,
    val hashtagsCsv: String,
    val mediaTypeStr: String,
    val imageDrawableRes: Int,
    val likesCount: Int,
    val commentsCount: Int,
    val repostsCount: Int,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isReposted: Boolean,
    val audioTrackName: String,
    val location: String,
    val aiInsight: String
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isMine: Boolean,
    val isRead: Boolean,
    val messageTypeStr: String,
    val voiceDurationSec: Int,
    val isSecretEncrypted: Boolean,
    val isStarred: Boolean
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val typeStr: String,
    val avatarRes: Int,
    val isRead: Boolean
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val username: String,
    val displayName: String,
    val bio: String,
    val website: String,
    val avatarDrawableRes: Int,
    val isVerified: Boolean,
    val isVip: Boolean,
    val followersCount: Int,
    val followingCount: Int,
    val likesCount: Int,
    val coinsBalance: Int
)

@Entity(tableName = "ai_logs")
data class AILogEntity(
    @PrimaryKey val id: String,
    val mode: String,
    val prompt: String,
    val response: String,
    val timestamp: Long
)
