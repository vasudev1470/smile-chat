package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.data.model.DataUsageSettings
import com.example.data.model.LoginSessionDevice
import com.example.data.model.NotificationSettings
import com.example.data.model.PrivacySettings
import com.example.data.model.ReportCategory
import com.example.data.model.SecuritySettings
import com.example.ui.theme.AiGlowGradient
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.RadiantPink
import com.example.ui.viewmodel.SocialViewModel

enum class SettingsSubScreen {
    HUB,
    EDIT_PROFILE,
    ACCOUNT_INFO,
    PRIVACY,
    NOTIFICATIONS,
    SECURITY_AND_PASSWORDS,
    MANAGE_DEVICES,
    BLOCKED_ACCOUNTS,
    LANGUAGE_PICKER,
    THEME_PICKER,
    DATA_USAGE,
    HELP_FAQ,
    REPORT_PROBLEM,
    ABOUT_AND_LEGAL,
    TERMS_OF_SERVICE,
    PRIVACY_POLICY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SocialViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSubScreen by remember { mutableStateOf(SettingsSubScreen.HUB) }

    // Dialog state controllers
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showRevokeSessionDialog by remember { mutableStateOf<LoginSessionDevice?>(null) }
    var showLogoutAllDevicesDialog by remember { mutableStateOf(false) }
    var showClearCacheDialog by remember { mutableStateOf(false) }

    val currentUser by viewModel.currentUser.collectAsState()
    val accountDetails by viewModel.accountDetails.collectAsState()
    val privacySettings by viewModel.privacySettings.collectAsState()
    val notificationSettings by viewModel.notificationSettings.collectAsState()
    val securitySettings by viewModel.securitySettings.collectAsState()
    val dataUsageSettings by viewModel.dataUsageSettings.collectAsState()
    val currentTheme by viewModel.themeMode.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val loginSessions by viewModel.loginSessions.collectAsState()
    val blockedUsers by viewModel.blockedUsers.collectAsState()
    val faqItems = viewModel.faqItems

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("settings_screen"),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (currentSubScreen) {
                            SettingsSubScreen.HUB -> "Settings & Privacy"
                            SettingsSubScreen.EDIT_PROFILE -> "Edit Profile"
                            SettingsSubScreen.ACCOUNT_INFO -> "Account Information"
                            SettingsSubScreen.PRIVACY -> "Privacy Settings"
                            SettingsSubScreen.NOTIFICATIONS -> "Notification Preferences"
                            SettingsSubScreen.SECURITY_AND_PASSWORDS -> "Security & Passwords"
                            SettingsSubScreen.MANAGE_DEVICES -> "Login Sessions & Devices"
                            SettingsSubScreen.BLOCKED_ACCOUNTS -> "Blocked Accounts"
                            SettingsSubScreen.LANGUAGE_PICKER -> "Language & Region"
                            SettingsSubScreen.THEME_PICKER -> "Display & Theme"
                            SettingsSubScreen.DATA_USAGE -> "Data & Media Usage"
                            SettingsSubScreen.HELP_FAQ -> "Help & FAQ Center"
                            SettingsSubScreen.REPORT_PROBLEM -> "Report a Problem"
                            SettingsSubScreen.ABOUT_AND_LEGAL -> "About Aether Social"
                            SettingsSubScreen.TERMS_OF_SERVICE -> "Terms of Service"
                            SettingsSubScreen.PRIVACY_POLICY -> "Privacy Policy"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (currentSubScreen == SettingsSubScreen.HUB) {
                                onBack()
                            } else {
                                currentSubScreen = SettingsSubScreen.HUB
                            }
                        },
                        modifier = Modifier.testTag("settings_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentSubScreen) {
                SettingsSubScreen.HUB -> {
                    SettingsHubContent(
                        currentUser = currentUser,
                        currentTheme = currentTheme,
                        selectedLanguage = selectedLanguage,
                        sessionsCount = loginSessions.size,
                        blockedCount = blockedUsers.size,
                        onNavigate = { currentSubScreen = it },
                        onShowLogout = { showLogoutDialog = true }
                    )
                }
                SettingsSubScreen.EDIT_PROFILE -> {
                    EditProfileSection(
                        currentUser = currentUser,
                        onSave = { name, username, bio, website, avatarRes ->
                            viewModel.updateProfile(name, username, bio, website, avatarRes)
                            currentSubScreen = SettingsSubScreen.HUB
                        }
                    )
                }
                SettingsSubScreen.ACCOUNT_INFO -> {
                    AccountInfoSection(
                        accountDetails = accountDetails,
                        onSave = { email, phone ->
                            viewModel.updateAccountDetails(email, phone)
                            currentSubScreen = SettingsSubScreen.HUB
                        }
                    )
                }
                SettingsSubScreen.PRIVACY -> {
                    PrivacySettingsSection(
                        settings = privacySettings,
                        onUpdate = { viewModel.updatePrivacySettings(it) }
                    )
                }
                SettingsSubScreen.NOTIFICATIONS -> {
                    NotificationSettingsSection(
                        settings = notificationSettings,
                        onUpdate = { viewModel.updateNotificationSettings(it) }
                    )
                }
                SettingsSubScreen.SECURITY_AND_PASSWORDS -> {
                    SecuritySettingsSection(
                        securitySettings = securitySettings,
                        onChangePassword = { oldPass, newPass -> viewModel.changePassword(oldPass, newPass) },
                        onUpdateSecurity = { viewModel.updateSecuritySettings(it) },
                        onManageDevices = { currentSubScreen = SettingsSubScreen.MANAGE_DEVICES }
                    )
                }
                SettingsSubScreen.MANAGE_DEVICES -> {
                    ManageDevicesSection(
                        sessions = loginSessions,
                        onRevoke = { showRevokeSessionDialog = it },
                        onLogoutAll = { showLogoutAllDevicesDialog = true }
                    )
                }
                SettingsSubScreen.BLOCKED_ACCOUNTS -> {
                    BlockedAccountsSection(
                        blockedUsers = blockedUsers,
                        onUnblock = { viewModel.unblockUser(it) },
                        onBlockUser = { viewModel.blockUser(it) }
                    )
                }
                SettingsSubScreen.LANGUAGE_PICKER -> {
                    LanguagePickerSection(
                        languages = viewModel.availableLanguages,
                        selectedLanguage = selectedLanguage,
                        onSelect = { viewModel.selectLanguage(it) }
                    )
                }
                SettingsSubScreen.THEME_PICKER -> {
                    ThemePickerSection(
                        currentTheme = currentTheme,
                        onSelect = { viewModel.selectTheme(it) }
                    )
                }
                SettingsSubScreen.DATA_USAGE -> {
                    DataUsageSection(
                        settings = dataUsageSettings,
                        onUpdate = { viewModel.updateDataUsageSettings(it) },
                        onClearCache = { showClearCacheDialog = true }
                    )
                }
                SettingsSubScreen.HELP_FAQ -> {
                    HelpFaqSection(
                        faqItems = faqItems,
                        onReportProblem = { currentSubScreen = SettingsSubScreen.REPORT_PROBLEM }
                    )
                }
                SettingsSubScreen.REPORT_PROBLEM -> {
                    ReportProblemSection(
                        onSubmit = { category, desc, email ->
                            val ok = viewModel.submitReportProblem(category, desc, email)
                            if (ok) currentSubScreen = SettingsSubScreen.HUB
                        }
                    )
                }
                SettingsSubScreen.ABOUT_AND_LEGAL -> {
                    AboutAppSection(
                        onOpenTerms = { currentSubScreen = SettingsSubScreen.TERMS_OF_SERVICE },
                        onOpenPrivacy = { currentSubScreen = SettingsSubScreen.PRIVACY_POLICY }
                    )
                }
                SettingsSubScreen.TERMS_OF_SERVICE -> {
                    LegalDocumentViewer(
                        title = "Terms of Service",
                        content = TERMS_OF_SERVICE_TEXT
                    )
                }
                SettingsSubScreen.PRIVACY_POLICY -> {
                    LegalDocumentViewer(
                        title = "Privacy Policy",
                        content = PRIVACY_POLICY_TEXT
                    )
                }
            }
        }
    }

    // ==========================================
    // CONFIRMATION DIALOGS
    // ==========================================

    // 1. Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = RadiantPink)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out of Aether?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            },
            text = {
                Text(
                    "Are you sure you want to log out of @${currentUser.username}? You will need your credentials to log back in.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logoutUser(logoutAllDevices = false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RadiantPink)
                ) {
                    Text("Log Out", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // 2. Revoke Single Device Session Dialog
    showRevokeSessionDialog?.let { session ->
        AlertDialog(
            onDismissRequest = { showRevokeSessionDialog = null },
            title = {
                Text("Revoke Session?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            },
            text = {
                Text(
                    "Terminate login session for '${session.deviceName}' located in ${session.location}? This device will be signed out immediately.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.revokeSession(session.id)
                        showRevokeSessionDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RadiantPink)
                ) {
                    Text("Revoke Session", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRevokeSessionDialog = null }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // 3. Logout from All Devices Dialog
    if (showLogoutAllDevicesDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutAllDevicesDialog = false },
            title = {
                Text("Log Out All Other Devices?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            },
            text = {
                Text(
                    "This will terminate all active web, mobile, and OBS streaming sessions except for this current device.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logoutAllOtherDevices()
                        showLogoutAllDevicesDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RadiantPink)
                ) {
                    Text("Log Out Others", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutAllDevicesDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // 4. Clear App Cache Confirmation Dialog
    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = {
                Text("Clear Media & Stream Cache?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            },
            text = {
                Text(
                    "This will free up ${(dataUsageSettings.cacheSizeBytes / (1024 * 1024))} MB of temporary cached reels, video chunks, and AI previews. Your account data will not be affected.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearCache()
                        showClearCacheDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintEmerald)
                ) {
                    Text("Clear Cache", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

// ==========================================
// 1. SETTINGS MAIN HUB
// ==========================================

@Composable
private fun SettingsHubContent(
    currentUser: com.example.data.model.UserProfile,
    currentTheme: AppThemeMode,
    selectedLanguage: AppLanguage,
    sessionsCount: Int,
    blockedCount: Int,
    onNavigate: (SettingsSubScreen) -> Unit,
    onShowLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(SettingsSubScreen.EDIT_PROFILE) }
                    .testTag("settings_profile_summary_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val avatarRes = if (currentUser.avatarDrawableRes != 0) currentUser.avatarDrawableRes else R.drawable.img_avatar_ai
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = currentUser.displayName,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, NeonViolet, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentUser.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (currentUser.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = NeonCyan, modifier = Modifier.size(14.dp))
                            }
                        }
                        Text(
                            text = "@${currentUser.username}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap to edit bio & avatar ⚡",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = NeonViolet
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Edit Profile",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Section: Account & Profile
        item {
            SettingsSectionHeader(title = "Account & Profile")
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Edit,
                        iconTint = NeonViolet,
                        title = "Edit Profile",
                        subtitle = "Name, username, bio, links, and avatar",
                        onClick = { onNavigate(SettingsSubScreen.EDIT_PROFILE) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.AccountCircle,
                        iconTint = NeonCyan,
                        title = "Account Information",
                        subtitle = "Email, phone number, joined date, account type",
                        onClick = { onNavigate(SettingsSubScreen.ACCOUNT_INFO) }
                    )
                }
            }
        }

        // Section: Privacy & Security
        item {
            SettingsSectionHeader(title = "Privacy & Security")
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Lock,
                        iconTint = RadiantPink,
                        title = "Privacy Settings",
                        subtitle = "Private account, activity status, story resharing",
                        onClick = { onNavigate(SettingsSubScreen.PRIVACY) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.Security,
                        iconTint = MintEmerald,
                        title = "Security & Passwords",
                        subtitle = "Change password, 2FA, biometric unlock",
                        onClick = { onNavigate(SettingsSubScreen.SECURITY_AND_PASSWORDS) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.Devices,
                        iconTint = GoldYellow,
                        title = "Login Sessions & Devices",
                        subtitle = "$sessionsCount active device sessions",
                        onClick = { onNavigate(SettingsSubScreen.MANAGE_DEVICES) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.Block,
                        iconTint = RadiantPink,
                        title = "Blocked Accounts",
                        subtitle = "$blockedCount blocked users",
                        onClick = { onNavigate(SettingsSubScreen.BLOCKED_ACCOUNTS) }
                    )
                }
            }
        }

        // Section: App Preferences
        item {
            SettingsSectionHeader(title = "App Preferences")
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Notifications,
                        iconTint = NeonCyan,
                        title = "Notification Settings",
                        subtitle = "Push alerts, likes, comments, live streams",
                        onClick = { onNavigate(SettingsSubScreen.NOTIFICATIONS) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.DarkMode,
                        iconTint = NeonViolet,
                        title = "Display Theme",
                        subtitle = currentTheme.title,
                        onClick = { onNavigate(SettingsSubScreen.THEME_PICKER) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.Language,
                        iconTint = MintEmerald,
                        title = "Language",
                        subtitle = "${selectedLanguage.displayName} (${selectedLanguage.nativeName}) ${selectedLanguage.flagEmoji}",
                        onClick = { onNavigate(SettingsSubScreen.LANGUAGE_PICKER) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.DataUsage,
                        iconTint = GoldYellow,
                        title = "Data & Media Usage",
                        subtitle = "Data saver, autoplay, clear cache",
                        onClick = { onNavigate(SettingsSubScreen.DATA_USAGE) }
                    )
                }
            }
        }

        // Section: Support & About
        item {
            SettingsSectionHeader(title = "Support & Legal")
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.AutoMirrored.Filled.Help,
                        iconTint = NeonCyan,
                        title = "Help & FAQ",
                        subtitle = "Troubleshooting, creator guides, safety",
                        onClick = { onNavigate(SettingsSubScreen.HELP_FAQ) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.ReportProblem,
                        iconTint = RadiantPink,
                        title = "Report a Problem",
                        subtitle = "Submit bug report, abuse ticket, or feedback",
                        onClick = { onNavigate(SettingsSubScreen.REPORT_PROBLEM) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.Info,
                        iconTint = NeonViolet,
                        title = "About Aether Social",
                        subtitle = "v2.4.0 (Build 2026.08) • Terms & Privacy",
                        onClick = { onNavigate(SettingsSubScreen.ABOUT_AND_LEGAL) }
                    )
                }
            }
        }

        // Section: Session Actions (Log Out)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShowLogout() }
                    .testTag("settings_logout_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RadiantPink.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(RadiantPink.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = RadiantPink, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Log Out", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RadiantPink)
                            Text("Log out of @${currentUser.username}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Log Out",
                        tint = RadiantPink,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = NeonCyan,
        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
    )
}

@Composable
private fun SettingsRowItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(14.dp)
        )
    }
}

// ==========================================
// 2. EDIT PROFILE SECTION
// ==========================================

@Composable
private fun EditProfileSection(
    currentUser: com.example.data.model.UserProfile,
    onSave: (displayName: String, username: String, bio: String, website: String, avatarRes: Int) -> Unit
) {
    var displayName by remember { mutableStateOf(currentUser.displayName) }
    var username by remember { mutableStateOf(currentUser.username) }
    var bio by remember { mutableStateOf(currentUser.bio) }
    var website by remember { mutableStateOf(currentUser.website) }
    var selectedAvatarRes by remember { mutableStateOf(currentUser.avatarDrawableRes) }

    val avatarOptions = listOf(
        R.drawable.img_avatar_ai to "AI Hologram",
        R.drawable.img_story_art to "Cyber Neon",
        R.drawable.img_hero_banner to "Astral Glow",
        R.drawable.img_app_icon to "Aether Brand"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Avatar selector
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(96.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val res = if (selectedAvatarRes != 0) selectedAvatarRes else R.drawable.img_avatar_ai
                    Image(
                        painter = painterResource(id = res),
                        contentDescription = "Avatar Preview",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .border(3.dp, NeonViolet, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("Select Profile Avatar Theme", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    avatarOptions.forEach { (drawableRes, label) ->
                        val isSelected = selectedAvatarRes == drawableRes
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (isSelected) 2.5.dp else 1.dp,
                                    color = if (isSelected) NeonCyan else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedAvatarRes = drawableRes }
                        ) {
                            Image(
                                painter = painterResource(id = drawableRes),
                                contentDescription = label,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                modifier = Modifier.fillMaxWidth().testTag("edit_display_name_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                prefix = { Text("@", color = NeonCyan) },
                modifier = Modifier.fillMaxWidth().testTag("edit_username_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio (AI & Creator Intro)") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth().testTag("edit_bio_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = website,
                onValueChange = { website = it },
                label = { Text("Website or Social Link") },
                modifier = Modifier.fillMaxWidth().testTag("edit_website_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { onSave(displayName, username, bio, website, selectedAvatarRes) },
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("save_profile_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Profile Changes", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// 3. ACCOUNT INFO SECTION
// ==========================================

@Composable
private fun AccountInfoSection(
    accountDetails: com.example.data.model.AccountDetails,
    onSave: (email: String, phone: String) -> Unit
) {
    var email by remember { mutableStateOf(accountDetails.email) }
    var phone by remember { mutableStateOf(accountDetails.phoneNumber) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Personal details used for account recovery and verified creator payout communications.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Primary Email Address") },
                modifier = Modifier.fillMaxWidth().testTag("account_email_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth().testTag("account_phone_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Account Metadata", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonCyan)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Account Tier", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(accountDetails.accountType, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MintEmerald)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Date Joined", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(accountDetails.joinedDate, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Region", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(accountDetails.country, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        item {
            Button(
                onClick = { onSave(email, phone) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
            ) {
                Text("Save Account Info", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// ==========================================
// 4. PRIVACY SETTINGS SECTION
// ==========================================

@Composable
private fun PrivacySettingsSection(
    settings: PrivacySettings,
    onUpdate: (PrivacySettings) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Private Account Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Private Account", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("When active, only approved followers can view your posts and stories", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.isPrivateAccount,
                            onCheckedChange = { onUpdate(settings.copy(isPrivateAccount = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonViolet)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Online Activity Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Show Online Activity Status", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Allow friends and connections to see when you're active on Aether", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.showOnlineStatus,
                            onCheckedChange = { onUpdate(settings.copy(showOnlineStatus = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = MintEmerald)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Story Sharing
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Allow Story Sharing & Resharing", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Let others reshare your stories to their personal feeds", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.allowStorySharing,
                            onCheckedChange = { onUpdate(settings.copy(allowStorySharing = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Read Receipts
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Direct Message Read Receipts", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Send and receive blue double checkmarks in encrypted chat", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.readReceiptsEnabled,
                            onCheckedChange = { onUpdate(settings.copy(readReceiptsEnabled = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. NOTIFICATION SETTINGS SECTION
// ==========================================

@Composable
private fun NotificationSettingsSection(
    settings: NotificationSettings,
    onUpdate: (NotificationSettings) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Master Push Notifications
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Push Notifications (Master)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Enable real-time push alerts on this device", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.pushNotificationsMaster,
                            onCheckedChange = { onUpdate(settings.copy(pushNotificationsMaster = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonViolet)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Likes & Reactions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Likes & Reactions", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("When someone likes your post, reel, or story", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.likesAndReactions && settings.pushNotificationsMaster,
                            enabled = settings.pushNotificationsMaster,
                            onCheckedChange = { onUpdate(settings.copy(likesAndReactions = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = RadiantPink)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Comments & Mentions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Comments & Mentions", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("When someone replies or mentions @${"vasudev_ai"}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.commentsAndMentions && settings.pushNotificationsMaster,
                            enabled = settings.pushNotificationsMaster,
                            onCheckedChange = { onUpdate(settings.copy(commentsAndMentions = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Live Stream Broadcast Alerts
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Live Stream Broadcast Alerts", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Get notified when creators you follow go live on free servers", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.liveStreamBroadcastAlerts && settings.pushNotificationsMaster,
                            enabled = settings.pushNotificationsMaster,
                            onCheckedChange = { onUpdate(settings.copy(liveStreamBroadcastAlerts = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = MintEmerald)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Direct Messages & Video Calls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Direct Messages & Incoming Calls", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Incoming chat messages, voice notes, and video rings", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.directMessagesAndCalls && settings.pushNotificationsMaster,
                            enabled = settings.pushNotificationsMaster,
                            onCheckedChange = { onUpdate(settings.copy(directMessagesAndCalls = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldYellow)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. SECURITY & PASSWORDS SECTION
// ==========================================

@Composable
private fun SecuritySettingsSection(
    securitySettings: SecuritySettings,
    onChangePassword: (oldPass: String, newPass: String) -> Boolean,
    onUpdateSecurity: (SecuritySettings) -> Unit,
    onManageDevices: () -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isOldPassVisible by remember { mutableStateOf(false) }
    var isNewPassVisible by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Change Password Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Key, contentDescription = null, tint = NeonViolet)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Change Password", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Text("Last changed: ${securitySettings.lastPasswordChangeDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Current Password") },
                        visualTransformation = if (isOldPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isOldPassVisible = !isOldPassVisible }) {
                                Icon(if (isOldPassVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("current_password_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password (min 8 characters)") },
                        visualTransformation = if (isNewPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isNewPassVisible = !isNewPassVisible }) {
                                Icon(if (isNewPassVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("new_password_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("confirm_new_password_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            if (newPassword == confirmPassword) {
                                val ok = onChangePassword(oldPassword, newPassword)
                                if (ok) {
                                    oldPassword = ""
                                    newPassword = ""
                                    confirmPassword = ""
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp).testTag("update_password_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                    ) {
                        Text("Update Password", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // 2FA & Biometrics Toggles
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = MintEmerald)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Two-Factor Authentication (2FA)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Secured via ${securitySettings.twoFactorMethod}", fontSize = 11.sp, color = MintEmerald)
                            }
                        }
                        Switch(
                            checked = securitySettings.isTwoFactorEnabled,
                            onCheckedChange = { onUpdateSecurity(securitySettings.copy(isTwoFactorEnabled = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = MintEmerald)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = NeonCyan)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Biometric & Face ID Unlock", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Instant unlock using device sensors", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Switch(
                            checked = securitySettings.isBiometricLoginEnabled,
                            onCheckedChange = { onUpdateSecurity(securitySettings.copy(isBiometricLoginEnabled = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                        )
                    }
                }
            }
        }

        // Quick Link to Manage Devices
        item {
            OutlinedButton(
                onClick = onManageDevices,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Devices, contentDescription = null, tint = NeonViolet)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Manage Active Login Sessions / Devices", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// 7. MANAGE LOGIN SESSIONS & DEVICES
// ==========================================

@Composable
private fun ManageDevicesSection(
    sessions: List<LoginSessionDevice>,
    onRevoke: (LoginSessionDevice) -> Unit,
    onLogoutAll: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Active login sessions connected to your account. Revoke any unrecognized devices immediately.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (sessions.any { !it.isCurrent }) {
            item {
                Button(
                    onClick = onLogoutAll,
                    modifier = Modifier.fillMaxWidth().height(44.dp).testTag("logout_all_devices_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RadiantPink.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = RadiantPink, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out From All Other Devices", color = RadiantPink, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        items(sessions, key = { it.id }) { session ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (session.isCurrent) NeonViolet.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (session.isCurrent) MintEmerald.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Devices,
                                contentDescription = null,
                                tint = if (session.isCurrent) MintEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(session.deviceName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                if (session.isCurrent) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MintEmerald.copy(alpha = 0.2f))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text("THIS DEVICE", color = MintEmerald, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Text(session.platform, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${session.location} • ${session.lastActive}", fontSize = 10.sp, color = NeonCyan)
                        }
                    }

                    if (!session.isCurrent) {
                        IconButton(
                            onClick = { onRevoke(session) },
                            modifier = Modifier.testTag("revoke_session_${session.id}")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Revoke Session", tint = RadiantPink)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. BLOCKED ACCOUNTS SECTION
// ==========================================

@Composable
private fun BlockedAccountsSection(
    blockedUsers: List<com.example.data.model.BlockedUser>,
    onUnblock: (String) -> Unit,
    onBlockUser: (String) -> Unit
) {
    var blockInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Blocked users cannot view your profile, direct message you, or join your private live streams.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Add block input
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = blockInput,
                    onValueChange = { blockInput = it },
                    placeholder = { Text("Enter username to block (e.g. spammer_1)") },
                    prefix = { Text("@", color = RadiantPink) },
                    modifier = Modifier.weight(1f).testTag("block_username_input"),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (blockInput.isNotBlank()) {
                            onBlockUser(blockInput)
                            blockInput = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RadiantPink)
                ) {
                    Text("Block", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        if (blockedUsers.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No blocked accounts", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            items(blockedUsers, key = { it.id }) { user ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = user.avatarRes),
                                contentDescription = user.displayName,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(user.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text("@${user.username} • ${user.blockedDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        OutlinedButton(
                            onClick = { onUnblock(user.id) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("unblock_button_${user.id}")
                        ) {
                            Text("Unblock", fontSize = 12.sp, color = NeonCyan)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 9. LANGUAGE PICKER SECTION
// ==========================================

@Composable
private fun LanguagePickerSection(
    languages: List<AppLanguage>,
    selectedLanguage: AppLanguage,
    onSelect: (AppLanguage) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(languages, key = { it.code }) { lang ->
            val isSelected = lang.code == selectedLanguage.code
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(lang) }
                    .testTag("lang_item_${lang.code}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) NeonViolet.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NeonViolet)) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(lang.flagEmoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(lang.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text(lang.nativeName, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = NeonViolet)
                    }
                }
            }
        }
    }
}

// ==========================================
// 10. THEME PICKER SECTION
// ==========================================

@Composable
private fun ThemePickerSection(
    currentTheme: AppThemeMode,
    onSelect: (AppThemeMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AppThemeMode.entries.forEach { mode ->
            val isSelected = currentTheme == mode
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(mode) }
                    .testTag("theme_mode_${mode.name}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) NeonViolet.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NeonViolet)) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    when (mode) {
                                        AppThemeMode.SYSTEM -> NeonCyan.copy(alpha = 0.2f)
                                        AppThemeMode.DARK -> Color(0xFF1E1E2E)
                                        AppThemeMode.LIGHT -> Color(0xFFE2E8F0)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (mode) {
                                    AppThemeMode.SYSTEM -> Icons.Default.Devices
                                    AppThemeMode.DARK -> Icons.Default.DarkMode
                                    AppThemeMode.LIGHT -> Icons.Default.LightMode
                                },
                                contentDescription = null,
                                tint = when (mode) {
                                    AppThemeMode.SYSTEM -> NeonCyan
                                    AppThemeMode.DARK -> NeonViolet
                                    AppThemeMode.LIGHT -> GoldYellow
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(mode.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text(mode.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelect(mode) },
                        colors = RadioButtonDefaults.colors(selectedColor = NeonViolet)
                    )
                }
            }
        }
    }
}

// ==========================================
// 11. DATA USAGE & CACHE SECTION
// ==========================================

@Composable
private fun DataUsageSection(
    settings: DataUsageSettings,
    onUpdate: (DataUsageSettings) -> Unit,
    onClearCache: () -> Unit
) {
    val cacheMB = (settings.cacheSizeBytes / (1024 * 1024))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Data Saver Mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Data Saver Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Reduces video resolutions on mobile cellular connections", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.dataSaverEnabled,
                            onCheckedChange = { onUpdate(settings.copy(dataSaverEnabled = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldYellow)
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // High Quality Uploads on Wi-Fi
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("High-Quality 4K Uploads on Wi-Fi Only", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Save mobile data when posting reels or high-res photos", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.highQualityUploadsOnWifiOnly,
                            onCheckedChange = { onUpdate(settings.copy(highQualityUploadsOnWifiOnly = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                        )
                    }
                }
            }
        }

        // Cache Cleaner Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("App Cache & Storage", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text("Temporary video & image cache: ${cacheMB} MB", fontSize = 12.sp, color = NeonCyan)
                    }

                    Button(
                        onClick = onClearCache,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (cacheMB > 0) MintEmerald else Color.Gray),
                        enabled = cacheMB > 0,
                        modifier = Modifier.testTag("clear_cache_button")
                    ) {
                        Text("Clear Cache", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

// ==========================================
// 12. HELP & FAQ SECTION
// ==========================================

@Composable
private fun HelpFaqSection(
    faqItems: List<com.example.data.model.SupportFaqItem>,
    onReportProblem: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Button(
                onClick = onReportProblem,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
            ) {
                Icon(Icons.Default.ReportProblem, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contact Safety Support / Report a Bug", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        item {
            Text("Frequently Asked Questions", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonCyan)
        }

        items(faqItems, key = { it.id }) { faq ->
            var isExpanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = faq.question,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Text(if (isExpanded) "▲" else "▼", fontSize = 10.sp, color = NeonCyan)
                    }
                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = faq.answer,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 13. REPORT A PROBLEM SECTION
// ==========================================

@Composable
private fun ReportProblemSection(
    onSubmit: (category: ReportCategory, description: String, email: String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(ReportCategory.BUG) }
    var descriptionText by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("vasudev7490@gmail.com") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Submit a report directly to the Aether Security & Safety team. Reports are reviewed 24/7.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Text("Select Category", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonCyan)
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ReportCategory.entries.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCategory = cat },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) NeonViolet.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NeonViolet)) else null
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(cat.iconEmoji, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(cat.title, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                label = { Text("Detailed Description of Issue") },
                placeholder = { Text("Please explain what happened, steps to reproduce, or content URL...") },
                minLines = 4,
                maxLines = 8,
                modifier = Modifier.fillMaxWidth().testTag("report_description_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = contactEmail,
                onValueChange = { contactEmail = it },
                label = { Text("Contact Email for Follow-up") },
                modifier = Modifier.fillMaxWidth().testTag("report_email_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Button(
                onClick = { onSubmit(selectedCategory, descriptionText, contactEmail) },
                enabled = descriptionText.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("submit_report_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RadiantPink)
            ) {
                Icon(Icons.Default.ReportProblem, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Submit Official Report", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// 14. ABOUT APP & LEGAL SECTION
// ==========================================

@Composable
private fun AboutAppSection(
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            Image(
                painter = painterResource(id = R.drawable.img_app_icon),
                contentDescription = "Aether App",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, NeonViolet, RoundedCornerShape(20.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Aether Social", fontWeight = FontWeight.Black, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
            Text("Version 2.4.0 (Production Build 2026.08)", fontSize = 12.sp, color = NeonCyan)
            Text("Next-Gen AI Creative Social Platform", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Policy,
                        iconTint = NeonCyan,
                        title = "Terms of Service",
                        subtitle = "Community guidelines, broadcaster policies",
                        onClick = onOpenTerms
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRowItem(
                        icon = Icons.Default.Lock,
                        iconTint = RadiantPink,
                        title = "Privacy Policy",
                        subtitle = "Data processing, encryption, cookie policies",
                        onClick = onOpenPrivacy
                    )
                }
            }
        }

        item {
            Text(
                "© 2026 Aether Interactive Technologies Inc.\nAll rights reserved worldwide.",
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

// ==========================================
// 15. LEGAL DOCUMENT VIEWER
// ==========================================

@Composable
private fun LegalDocumentViewer(
    title: String,
    content: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(title, fontWeight = FontWeight.Black, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(12.dp))
            Text(content, fontSize = 12.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

private const val TERMS_OF_SERVICE_TEXT = """
Last Updated: August 2026

1. ACCEPTANCE OF TERMS
By accessing or using Aether Social, you agree to be bound by these Terms of Service. If you do not agree to these terms, do not access or use our services.

2. ELIGIBILITY & USER ACCOUNTS
You must be at least 13 years old to create an account. You are responsible for safeguarding your password and account security. You agree to notify us immediately of any unauthorized use of your account.

3. FREE LIVE BROADCASTING POLICIES
Aether Social provides zero-cost edge server ingestion (RTMP, WebRTC, SRT) for live creators. Broadcasters are prohibited from streaming copyrighted content, unlawful material, hate speech, or harassment. Violation of broadcast safety guidelines will result in instant node termination.

4. USER GENERATED & AI CONTENT
You retain ownership of the content you create and publish. By submitting content to Aether Social, you grant us a worldwide license to host, display, and distribute such media across our mesh network.

5. VIRTUAL COINS & CREATOR GIFTS
Virtual coins purchased within Aether Social are non-refundable digital goods used to tip creators. Verified creators can withdraw creator gifts subject to minimum threshold policies.
"""

private const val PRIVACY_POLICY_TEXT = """
Last Updated: August 2026

1. INFORMATION WE COLLECT
We collect account information (username, display name, email, phone number), content uploaded to feeds and stories, live streaming session telemetry, and device diagnostic logs.

2. HOW WE USE INFORMATION
We use your data to maintain platform reliability, provide end-to-end encrypted direct messaging, personalize feed recommendations using on-device machine learning, and deliver real-time live streaming.

3. DATA PROTECTION & ZERO-TRACKING
Aether Social does not sell your private data to third-party ad networks. Direct chat messages and video calls utilize robust end-to-end encryption protocols.

4. YOUR PRIVACY RIGHTS
You may update your profile details, toggle account visibility to private, clear local cached media, or export your account archives anytime within Settings.
"""
