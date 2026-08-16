package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DarkGlassSurface
import com.example.ui.theme.LiveRed
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.viewmodel.SocialViewModel
import kotlinx.coroutines.delay

@Composable
fun ActiveCallOverlay(
    viewModel: SocialViewModel,
    modifier: Modifier = Modifier
) {
    val activeCallState by viewModel.activeCallState.collectAsState()

    AnimatedVisibility(
        visible = activeCallState.isActive,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        var callSeconds by remember { mutableIntStateOf(0) }
        var isMuted by remember { mutableStateOf(false) }
        var isVideoOff by remember { mutableStateOf(false) }

        LaunchedEffect(activeCallState.isActive) {
            callSeconds = 0
            while (activeCallState.isActive) {
                delay(1000)
                callSeconds++
            }
        }

        val formattedDuration = String.format("%02d:%02d", callSeconds / 60, callSeconds % 60)

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .testTag("active_call_overlay")
        ) {
            // Main Video Background (or Call Avatar)
            if (activeCallState.isVideo && !isVideoOff) {
                Image(
                    painter = painterResource(id = R.drawable.img_story_art),
                    contentDescription = "Active Video Stream",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Picture-in-picture Self View
                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(top = 16.dp, end = 16.dp)
                        .size(width = 110.dp, height = 160.dp)
                        .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_avatar_ai),
                        contentDescription = "Self Camera Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                // Audio Call visual
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(NeonViolet.copy(alpha = 0.3f), Color.Black)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val avatarRes = if (activeCallState.participantAvatarRes != 0) activeCallState.participantAvatarRes else R.drawable.img_avatar_ai
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(3.dp, MintEmerald, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = avatarRes),
                                contentDescription = activeCallState.participantName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = activeCallState.participantName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Connected • $formattedDuration",
                            fontSize = 14.sp,
                            color = MintEmerald,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Top Status Pill
            if (activeCallState.isVideo) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .statusBarsPadding()
                        .padding(start = 16.dp, top = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(DarkGlassSurface)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MintEmerald)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${activeCallState.participantName} • $formattedDuration",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Bottom Call Control Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 36.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(DarkGlassSurface)
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(28.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mute Button
                    IconButton(
                        onClick = { isMuted = !isMuted },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (isMuted) LiveRed.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f))
                            .testTag("call_mute_button")
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mute",
                            tint = if (isMuted) LiveRed else Color.White
                        )
                    }

                    // Video Toggle Button
                    if (activeCallState.isVideo) {
                        IconButton(
                            onClick = { isVideoOff = !isVideoOff },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(if (isVideoOff) LiveRed.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f))
                                .testTag("call_video_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isVideoOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                                contentDescription = "Toggle Video",
                                tint = if (isVideoOff) LiveRed else Color.White
                            )
                        }

                        // Flip camera
                        IconButton(
                            onClick = { /* Switch Camera Front/Back */ },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                                .testTag("call_flip_camera_button")
                        ) {
                            Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip Camera", tint = Color.White)
                        }
                    } else {
                        // Speaker toggle
                        IconButton(
                            onClick = { /* Speakerphone toggle */ },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                                .testTag("call_speaker_button")
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Speaker", tint = Color.White)
                        }
                    }

                    // End Call Button
                    IconButton(
                        onClick = { viewModel.endCall() },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(LiveRed)
                            .testTag("end_call_button")
                    ) {
                        Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
            }
        }
    }
}
