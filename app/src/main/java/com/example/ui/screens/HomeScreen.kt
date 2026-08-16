package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.PostCard
import com.example.ui.components.StoriesSection
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.LiveBadgeGradient
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.viewmodel.FeedFilterTab
import com.example.ui.viewmodel.SocialViewModel

@Composable
fun HomeScreen(
    viewModel: SocialViewModel,
    modifier: Modifier = Modifier
) {
    val posts by viewModel.posts.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val feedFilter by viewModel.feedFilter.collectAsState()
    val activeLiveStreams by viewModel.activeLiveStreams.collectAsState()

    val filteredPosts = when (feedFilter) {
        FeedFilterTab.FOR_YOU -> posts
        FeedFilterTab.FOLLOWING -> posts.filter { it.authorUsername != "novalab_ai" }
        FeedFilterTab.TRENDING -> posts.sortedByDescending { it.likesCount }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Stories Row
        item {
            StoriesSection(
                stories = stories,
                onStoryClick = { story ->
                    if (story.isLive) {
                        viewModel.toggleLiveStream(true)
                    } else {
                        viewModel.openStory(story.id)
                    }
                },
                onAddStoryClick = {
                    viewModel.toggleCreatePost(true)
                }
            )
        }

        // Live Stream & Free Server Hosting Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("home_live_hosting_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF16132E))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.toggleLiveStream(true) }
                        ) {
                            Box(modifier = Modifier.size(44.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_hero_banner),
                                    contentDescription = "Live Host",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(LiveBadgeGradient)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("LIVE", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Aether Live Studio",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MintEmerald.copy(alpha = 0.2f))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text("FREE SERVER", color = MintEmerald, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Text(
                                    text = "🔴 18.4K watching • Global Free Edge Relay",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }

                        // Go Live / Host Free Server button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeonViolet)
                                .clickable { viewModel.toggleLiveHostingStudio(true) }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                                .testTag("home_go_live_free_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Sensors,
                                    contentDescription = "Go Live",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Host Free",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secondary row with quick server picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { viewModel.toggleFreeServerPicker(true) }
                        ) {
                            Icon(Icons.Default.Dns, contentDescription = null, tint = MintEmerald, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Free Ingest Nodes: US, EU, Tokyo, SG (16ms)",
                                fontSize = 10.sp,
                                color = MintEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Server Nodes ›",
                            fontSize = 10.sp,
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { viewModel.toggleFreeServerPicker(true) }
                        )
                    }
                }
            }
        }

        // Feed Filter Tabs (For You, Following, Trending)
        item {
            TabRow(
                selectedTabIndex = feedFilter.ordinal,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = NeonViolet,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[feedFilter.ordinal]),
                        color = NeonViolet
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Tab(
                    selected = feedFilter == FeedFilterTab.FOR_YOU,
                    onClick = { viewModel.setFeedFilter(FeedFilterTab.FOR_YOU) },
                    text = { Text("For You", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = feedFilter == FeedFilterTab.FOLLOWING,
                    onClick = { viewModel.setFeedFilter(FeedFilterTab.FOLLOWING) },
                    text = { Text("Following", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = feedFilter == FeedFilterTab.TRENDING,
                    onClick = { viewModel.setFeedFilter(FeedFilterTab.TRENDING) },
                    text = { Text("🔥 Trending", fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Post Cards
        items(filteredPosts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onLikeClick = { viewModel.toggleLikePost(post) },
                onSaveClick = { viewModel.toggleSavePost(post) },
                onRepostClick = { viewModel.toggleRepost(post) },
                onCommentClick = {
                    viewModel.openChat("chat_elena")
                },
                onTipClick = {
                    viewModel.toggleGiftShop(true)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
