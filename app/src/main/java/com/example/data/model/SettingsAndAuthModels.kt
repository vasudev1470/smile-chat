package com.example.data.model

import com.example.R

enum class AppThemeMode(val title: String, val description: String) {
    SYSTEM("System Default", "Follows your Android device theme"),
    DARK("Obsidian Dark", "Energy efficient cyber-dark aesthetic"),
    LIGHT("Aether Light", "Crisp, vibrant high-contrast light mode")
}

data class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flagEmoji: String
)

data class LoginSessionDevice(
    val id: String,
    val deviceName: String,
    val platform: String,
    val location: String,
    val ipAddress: String,
    val lastActive: String,
    val isCurrent: Boolean = false
)

data class BlockedUser(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarRes: Int,
    val blockedDate: String
)

data class PrivacySettings(
    val isPrivateAccount: Boolean = false,
    val showOnlineStatus: Boolean = true,
    val allowStorySharing: Boolean = true,
    val allowMessageReplies: String = "Everyone", // Everyone, People You Follow, Off
    val allowTagging: String = "Everyone", // Everyone, People You Follow, Off
    val readReceiptsEnabled: Boolean = true
)

data class NotificationSettings(
    val pushNotificationsMaster: Boolean = true,
    val likesAndReactions: Boolean = true,
    val commentsAndMentions: Boolean = true,
    val directMessagesAndCalls: Boolean = true,
    val liveStreamBroadcastAlerts: Boolean = true,
    val newFollowersAlerts: Boolean = true,
    val productAnnouncementsAndSecurityEmail: Boolean = true
)

data class DataUsageSettings(
    val dataSaverEnabled: Boolean = false,
    val highQualityUploadsOnWifiOnly: Boolean = false,
    val videoAutoplay: String = "Always", // Always, Wi-Fi Only, Never
    val cacheSizeBytes: Long = 245_800_000L // 245.8 MB
)

data class SecuritySettings(
    val isTwoFactorEnabled: Boolean = true,
    val twoFactorMethod: String = "Authenticator App (TOTP)",
    val isBiometricLoginEnabled: Boolean = true,
    val isLoginAlertsEnabled: Boolean = true,
    val lastPasswordChangeDate: String = "June 14, 2026"
)

data class AccountDetails(
    val email: String = "vasudev7490@gmail.com",
    val phoneNumber: String = "+1 (555) 389-8421",
    val dateOfBirth: String = "September 24, 1998",
    val joinedDate: String = "March 2024",
    val accountType: String = "Creator Partner",
    val country: String = "United States"
)

data class SupportFaqItem(
    val id: String,
    val category: String,
    val question: String,
    val answer: String
)

enum class ReportCategory(val title: String, val iconEmoji: String) {
    BUG("App Bug or Crash", "🐛"),
    HARASSMENT("Harassment or Abuse", "🛡️"),
    INAPPROPRIATE_CONTENT("Inappropriate Content", "⚠️"),
    ACCOUNT_HACKED("Security / Account Compromise", "🔒"),
    PAYMENT_OR_COINS("Coins & Payment Issue", "🪙"),
    OTHER("Feedback or Feature Request", "💡")
}

enum class AuthMode {
    LOGIN,
    SIGNUP,
    FORGOT_PASSWORD,
    VERIFY_OTP,
    RESET_NEW_PASSWORD
}

data class AuthState(
    val isAuthenticated: Boolean = true,
    val currentAuthMode: AuthMode = AuthMode.LOGIN,
    val currentEmailOrUsername: String = "vasudev7490@gmail.com",
    val rememberMe: Boolean = true,
    val otpCode: String = "",
    val otpTimeRemainingSeconds: Int = 60,
    val isOtpActive: Boolean = false,
    val resetEmailTarget: String = "",
    val isLoading: Boolean = false,
    val statusMessage: String? = null,
    val errorMessage: String? = null
)
