package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AiGlowGradient
import com.example.ui.theme.NeonViolet
import com.example.ui.viewmodel.MainNavigationTab

@Composable
fun AppBottomNavigation(
    selectedTab: MainNavigationTab,
    onTabSelected: (MainNavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = if (selectedTab == MainNavigationTab.HOME_FEED) Icons.Filled.Home else Icons.Outlined.Home,
                label = "Feed",
                isSelected = selectedTab == MainNavigationTab.HOME_FEED,
                onClick = { onTabSelected(MainNavigationTab.HOME_FEED) },
                tag = "nav_feed"
            )

            NavItem(
                icon = if (selectedTab == MainNavigationTab.EXPLORE) Icons.Filled.Explore else Icons.Outlined.Explore,
                label = "Explore",
                isSelected = selectedTab == MainNavigationTab.EXPLORE,
                onClick = { onTabSelected(MainNavigationTab.EXPLORE) },
                tag = "nav_explore"
            )

            // Center Glowing AI Studio
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AiGlowGradient)
                    .clickable { onTabSelected(MainNavigationTab.AI_STUDIO) }
                    .padding(10.dp)
                    .testTag("nav_ai_studio"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Studio",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            NavItem(
                icon = if (selectedTab == MainNavigationTab.REELS) Icons.Filled.Movie else Icons.Outlined.Movie,
                label = "Reels",
                isSelected = selectedTab == MainNavigationTab.REELS,
                onClick = { onTabSelected(MainNavigationTab.REELS) },
                tag = "nav_reels"
            )

            NavItem(
                icon = if (selectedTab == MainNavigationTab.CHAT) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                label = "Chat",
                isSelected = selectedTab == MainNavigationTab.CHAT,
                onClick = { onTabSelected(MainNavigationTab.CHAT) },
                tag = "nav_chat"
            )

            NavItem(
                icon = if (selectedTab == MainNavigationTab.PROFILE) Icons.Filled.Person else Icons.Outlined.PersonOutline,
                label = "Profile",
                isSelected = selectedTab == MainNavigationTab.PROFILE,
                onClick = { onTabSelected(MainNavigationTab.PROFILE) },
                tag = "nav_profile"
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 2.dp)
            .testTag(tag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) NeonViolet else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) NeonViolet else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}
