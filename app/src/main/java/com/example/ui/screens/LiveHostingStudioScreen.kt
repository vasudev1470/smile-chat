package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LiveBroadcastMode
import com.example.data.model.LiveHostingServer
import com.example.data.model.ServerStatus
import com.example.ui.theme.AiGlowGradient
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.DarkGlassSurface
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.LiveBadgeGradient
import com.example.ui.theme.LiveRed
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.viewmodel.SocialViewModel

@Composable
fun LiveHostingStudioScreen(
    viewModel: SocialViewModel,
    onClose: () -> Unit
) {
    val broadcastSession by viewModel.broadcastSession.collectAsState()
    val selectedServer by viewModel.selectedHostingServer.collectAsState()
    val context = LocalContext.current

    if (broadcastSession.isEnded) {
        BroadcastRecapView(
            session = broadcastSession,
            onDone = {
                viewModel.resetLiveBroadcast()
                onClose()
            }
        )
    } else if (broadcastSession.isBroadcasting) {
        ActiveLiveBroadcastingView(
            viewModel = viewModel,
            onEndBroadcast = { viewModel.stopLiveBroadcast() }
        )
    } else {
        PreBroadcastSetupView(
            selectedServer = selectedServer,
            viewModel = viewModel,
            onClose = onClose,
            onOpenServerPicker = { viewModel.toggleFreeServerPicker(true) },
            onStartLive = { title, category, mode ->
                viewModel.startLiveBroadcast(title, category, mode)
            }
        )
    }
}

@Composable
private fun PreBroadcastSetupView(
    selectedServer: LiveHostingServer,
    viewModel: SocialViewModel,
    onClose: () -> Unit,
    onOpenServerPicker: () -> Unit,
    onStartLive: (title: String, category: String, mode: LiveBroadcastMode) -> Unit
) {
    val context = LocalContext.current
    var streamTitle by remember { mutableStateOf("⚡ Next-Gen Generative Social Architecture & Live Demo") }
    var selectedCategory by remember { mutableStateOf("AI & Technology") }
    var selectedModeIndex by remember { mutableIntStateOf(0) }
    var isFrontCamera by remember { mutableStateOf(true) }
    var isBeautyFilter by remember { mutableStateOf(true) }
    var isCloudRecording by remember { mutableStateOf(true) }
    var showKeyCopied by remember { mutableStateOf(false) }

    val categories = listOf("AI & Technology", "Creative Design", "Music & Beats", "Gaming & Mesh", "AMA & Q&A")
    val broadcastModes = listOf(
        Pair(LiveBroadcastMode.CAMERA_DIRECT, "📱 Mobile Cam"),
        Pair(LiveBroadcastMode.EXTERNAL_OBS_RTMP, "💻 OBS / Free RTMP"),
        Pair(LiveBroadcastMode.SCREEN_SHARE, "🖥️ Screen Share")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0A1A))
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("live_hosting_setup_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(LiveBadgeGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sensors,
                            contentDescription = "Live Studio",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Live Hosting Studio",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MintEmerald)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "100% Free Hosting Server Active",
                                fontSize = 11.sp,
                                color = MintEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_live_studio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Free Server Connection Card (High Visibility)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenServerPicker() }
                    .testTag("selected_free_server_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF16132E))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = selectedServer.flagEmoji, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = selectedServer.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${selectedServer.region} • ${selectedServer.maxResolution}",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Free Badge & Ping
                        Column(horizontalAlignment = Alignment.End) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MintEmerald.copy(alpha = 0.2f))
                                    .border(1.dp, MintEmerald, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "FREE SERVER",
                                    color = MintEmerald,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = "Ping",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${selectedServer.latencyMs} ms",
                                    color = NeonCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color.White.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ Zero Cost • Unlimited Bandwidth • RTMP/WebRTC",
                            fontSize = 11.sp,
                            color = GoldYellow,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Change Server ›",
                            fontSize = 12.sp,
                            color = NeonViolet,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Camera Viewfinder / Preview Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E1B38))
                    .border(1.dp, NeonViolet.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Broadcast Preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay Controls on Preview
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .clickable { isFrontCamera = !isFrontCamera }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlipCameraAndroid,
                            contentDescription = "Flip Camera",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isBeautyFilter) NeonPink.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f))
                            .clickable { isBeautyFilter = !isBeautyFilter }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Beauty Filter",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "1080p 60fps Ultra HD Preview",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Broadcast Mode Selector
            Text(
                text = "Broadcast Source Mode",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                broadcastModes.forEachIndexed { index, pair ->
                    val isSelected = selectedModeIndex == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonViolet else Color(0xFF1E1B38))
                            .clickable { selectedModeIndex = index }
                            .padding(vertical = 10.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pair.second,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // OBS / RTMP Stream Ingestion Credentials Box
            if (broadcastModes[selectedModeIndex].first == LiveBroadcastMode.EXTERNAL_OBS_RTMP) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181534)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NeonCyan, NeonViolet)))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Tv, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Free RTMP / OBS Server Ingestion Details",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // RTMP Ingest Server URL
                        Text(text = "Server URL (RTMP):", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedServer.rtmpIngestUrl,
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Server URL", selectedServer.rtmpIngestUrl))
                                    Toast.makeText(context, "Free Server URL copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Stream Key
                        Text(text = "Stream Key (Private):", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "live_free_••••••••••••••",
                                color = GoldYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row {
                                IconButton(
                                    onClick = { viewModel.generateNewStreamKey() },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Regenerate", tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val key = viewModel.broadcastSession.value.streamKey
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Stream Key", key))
                                        Toast.makeText(context, "Stream Key copied for OBS!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy Key", tint = Color.White, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stream Title Input
            Text(
                text = "Stream Title",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = streamTitle,
                onValueChange = { streamTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stream_title_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonViolet,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color(0xFF16132E),
                    unfocusedContainerColor = Color(0xFF16132E),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Category Chips
            Text(
                text = "Select Category",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.take(3).forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color(0xFF16132E))
                            .border(1.dp, if (isSelected) NeonCyan else Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) NeonCyan else Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Free Cloud Recording Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF16132E))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CloudDone, contentDescription = null, tint = MintEmerald, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Free Cloud Recording", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(text = "Auto-save replay to profile after stream", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }
                Switch(
                    checked = isCloudRecording,
                    onCheckedChange = { isCloudRecording = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MintEmerald
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Start Live Broadcast Button
            Button(
                onClick = {
                    onStartLive(streamTitle, selectedCategory, broadcastModes[selectedModeIndex].first)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("start_live_broadcast_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LiveRed)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GO LIVE ON FREE SERVER",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun ActiveLiveBroadcastingView(
    viewModel: SocialViewModel,
    onEndBroadcast: () -> Unit
) {
    val session by viewModel.broadcastSession.collectAsState()
    val comments by viewModel.liveComments.collectAsState()
    val selectedServer = session.selectedServer

    val minutes = session.durationSeconds / 60
    val seconds = session.durationSeconds % 60
    val timerString = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("active_broadcasting_view")
    ) {
        // Broadcaster Camera Video Feed Background
        Image(
            painter = painterResource(id = R.drawable.img_hero_banner),
            contentDescription = "Active Live Stream Broadcast",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient Scrims
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.TopCenter)
                .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent)))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.95f))))
        )

        // Top Status HUD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Live Status Pill & Duration
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(LiveRed)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("LIVE", color = Color.White, fontWeight = FontWeight.Black, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = timerString, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "•", color = Color.White.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${session.viewersCount} 👥", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            // Server Telemetry Badge & End Button
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectedServer != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MintEmerald.copy(alpha = 0.2f))
                            .border(1.dp, MintEmerald, RoundedCornerShape(16.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "🟢 ${selectedServer.name.take(12)} (${selectedServer.latencyMs}ms)",
                            color = MintEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(LiveRed)
                        .clickable { onEndBroadcast() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("end_stream_button")
                ) {
                    Text("END", color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
                }
            }
        }

        // Live Bitrate & Metrics Telemetry HUD (Middle-Right)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 80.dp, end = 14.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(8.dp)
        ) {
            Text(text = "BITRATE: ${session.currentBitrateKbps} kbps", color = NeonCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(text = "FPS: ${session.currentFps} fps (60 max)", color = MintEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(text = "EARNED: ${session.coinsEarned} 🪙", color = GoldYellow, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(text = "GIFTS: ${session.giftsReceivedCount} 🎁", color = NeonPink, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }

        // Live Chat Messages Overlay (Bottom Left)
        Column(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(200.dp)
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, bottom = 80.dp)
        ) {
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(comments.takeLast(10).reversed()) { comment ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${comment.username}: ",
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = comment.message,
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Host Broadcaster Bottom Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mute / Unmute Mic
            IconButton(
                onClick = { viewModel.toggleBroadcastMic() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (session.isMicMuted) LiveRed else Color.Black.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = if (session.isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "Toggle Mic",
                    tint = Color.White
                )
            }

            // Flip Camera
            IconButton(
                onClick = { viewModel.flipBroadcastCamera() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = "Flip Camera",
                    tint = Color.White
                )
            }

            // Beauty Neon Glow Filter
            IconButton(
                onClick = { viewModel.toggleBroadcastBeautyFilter() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (session.isBeautyFilterOn) NeonPink.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Beauty Filter",
                    tint = Color.White
                )
            }

            // Free Server Node Switcher Quick Access
            IconButton(
                onClick = { viewModel.toggleFreeServerPicker(true) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MintEmerald.copy(alpha = 0.3f))
                    .border(1.dp, MintEmerald, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Dns,
                    contentDescription = "Free Server Settings",
                    tint = MintEmerald
                )
            }
        }
    }
}

@Composable
private fun BroadcastRecapView(
    session: com.example.data.model.LiveBroadcastSession,
    onDone: () -> Unit
) {
    val minutes = session.durationSeconds / 60
    val seconds = session.durationSeconds % 60
    val timeFormatted = String.format("%02dm %02ds", minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0A1C))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
            .testTag("broadcast_recap_view"),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF181534)),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NeonViolet, NeonPink)))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(BrandGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Live Stream Finished! 🎉",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Hosted for free on ${session.selectedServer?.name ?: "Aether Global Free Edge"}",
                    fontSize = 12.sp,
                    color = MintEmerald,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Stats Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatPill("Total Viewers", "${session.peakViewers}", NeonCyan)
                    StatPill("Duration", timeFormatted, Color.White)
                    StatPill("Coins Earned", "+${session.coinsEarned}", GoldYellow)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatPill("Gifts Received", "${session.giftsReceivedCount} 🎁", NeonPink)
                    StatPill("Server Bandwidth", "100% Free", MintEmerald)
                    StatPill("Cloud Replay", "Saved ☁️", NeonViolet)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDone,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("recap_done_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                ) {
                    Text(text = "Back to Feed", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: String, accentColor: Color) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF221E42))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Black, color = accentColor)
        Text(text = label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
    }
}
