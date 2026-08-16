package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AiGradient
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.RadiantPink
import kotlinx.coroutines.delay

/**
 * Display style variants for the reusable [LoadingView].
 */
enum class LoadingViewVariant {
    /** Centered card with icon, pulsating glow, message, and optional cancel button. */
    CARD,
    /** Compact horizontal banner/row suitable for inline status or list item placeholders. */
    INLINE,
    /** Fullscreen overlay with dimmed background and glowing floating status. */
    FULLSCREEN,
    /** Specialized AI synthesis state with rotating halo, AI sparkles, and dynamic phrase cycle. */
    AI_GENERATING,
    /** Skeleton shimmer placeholders mimicking post/feed content cards. */
    SKELETON
}

/**
 * Main reusable [LoadingView] component designed for network calls, AI operations, and data sync.
 *
 * @param modifier Modifier applied to the outer container.
 * @param message Primary title or status message.
 * @param subMessage Secondary informative subtitle or description.
 * @param variant Visual variant layout (CARD, INLINE, FULLSCREEN, AI_GENERATING, SKELETON).
 * @param isAiMode Whether this loading view displays Gemini AI styling and radiant gradients.
 * @param progress Optional determinate progress value between 0.0f and 1.0f (null for indeterminate).
 * @param onCancel Optional callback when user taps cancel or dismiss.
 * @param cancelText Label for the cancel button.
 * @param icon Optional leading icon override.
 * @param testTag Identifier for automated testing.
 */
@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    message: String = "Loading...",
    subMessage: String? = null,
    variant: LoadingViewVariant = LoadingViewVariant.CARD,
    isAiMode: Boolean = false,
    progress: Float? = null,
    onCancel: (() -> Unit)? = null,
    cancelText: String = "Cancel",
    icon: ImageVector? = null,
    testTag: String = "reusable_loading_view"
) {
    when (variant) {
        LoadingViewVariant.CARD -> {
            LoadingCard(
                modifier = modifier.testTag(testTag),
                message = message,
                subMessage = subMessage,
                isAiMode = isAiMode,
                progress = progress,
                onCancel = onCancel,
                cancelText = cancelText,
                icon = icon
            )
        }
        LoadingViewVariant.INLINE -> {
            LoadingInline(
                modifier = modifier.testTag(testTag),
                message = message,
                subMessage = subMessage,
                isAiMode = isAiMode,
                progress = progress,
                icon = icon
            )
        }
        LoadingViewVariant.FULLSCREEN -> {
            LoadingFullscreen(
                modifier = modifier.testTag(testTag),
                message = message,
                subMessage = subMessage,
                isAiMode = isAiMode,
                progress = progress,
                onCancel = onCancel,
                cancelText = cancelText,
                icon = icon
            )
        }
        LoadingViewVariant.AI_GENERATING -> {
            AiGeneratingLoading(
                modifier = modifier.testTag(testTag),
                message = message.ifEmpty { "Gemini AI is processing..." },
                subMessage = subMessage,
                progress = progress,
                onCancel = onCancel
            )
        }
        LoadingViewVariant.SKELETON -> {
            SkeletonCardLoading(
                modifier = modifier.testTag(testTag)
            )
        }
    }
}

/**
 * Convenience full-screen overlay that displays a [LoadingView] when [isLoading] is true.
 */
@Composable
fun FullscreenLoadingOverlay(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    message: String = "Please wait...",
    subMessage: String? = null,
    isAiMode: Boolean = false,
    onCancel: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()

        if (isLoading) {
            Dialog(
                onDismissRequest = { onCancel?.invoke() },
                properties = DialogProperties(
                    dismissOnBackPress = onCancel != null,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.65f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { /* Consume clicks */ },
                    contentAlignment = Alignment.Center
                ) {
                    LoadingView(
                        modifier = Modifier
                            .padding(24.dp)
                            .widthIn(max = 380.dp),
                        message = message,
                        subMessage = subMessage,
                        variant = if (isAiMode) LoadingViewVariant.AI_GENERATING else LoadingViewVariant.CARD,
                        isAiMode = isAiMode,
                        onCancel = onCancel,
                        testTag = "fullscreen_loading_overlay"
                    )
                }
            }
        }
    }
}

/**
 * Dedicated Gemini AI Loading banner / card with glowing gradient aura.
 */
@Composable
fun AILoadingView(
    modifier: Modifier = Modifier,
    message: String = "Gemini AI is thinking...",
    subMessage: String? = "Analyzing context and crafting real-time response",
    progress: Float? = null,
    onCancel: (() -> Unit)? = null,
    testTag: String = "ai_loading_view"
) {
    LoadingView(
        modifier = modifier,
        message = message,
        subMessage = subMessage,
        variant = LoadingViewVariant.AI_GENERATING,
        isAiMode = true,
        progress = progress,
        onCancel = onCancel,
        testTag = testTag
    )
}

/**
 * Card presentation of the loading view.
 */
@Composable
private fun LoadingCard(
    modifier: Modifier = Modifier,
    message: String,
    subMessage: String?,
    isAiMode: Boolean,
    progress: Float?,
    onCancel: (() -> Unit)?,
    cancelText: String,
    icon: ImageVector?
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_ring")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAiMode) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon / Spinner Area with Aura
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center
            ) {
                // Background aura ring
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            brush = if (isAiMode) {
                                AiGradient.copy(alpha = 0.25f)
                            } else {
                                Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                                        Color.Transparent
                                    )
                                )
                            }
                        )
                )

                if (progress != null) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(56.dp),
                        color = if (isAiMode) NeonViolet else MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 4.dp,
                        strokeCap = StrokeCap.Round
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(56.dp),
                        color = if (isAiMode) NeonViolet else MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp,
                        strokeCap = StrokeCap.Round
                    )
                }

                // Inner central icon
                val centerIcon = icon ?: if (isAiMode) Icons.Default.AutoAwesome else Icons.Default.CloudSync
                Icon(
                    imageVector = centerIcon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isAiMode) NeonCyan else MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Message
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            // Secondary Subtitle
            if (!subMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Progress Bar if determinate
            if (progress != null) {
                Spacer(modifier = Modifier.height(14.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isAiMode) NeonViolet else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                PulsingDotsWave(
                    dotColor = if (isAiMode) NeonViolet else MaterialTheme.colorScheme.primary
                )
            }

            // Cancel Action
            if (onCancel != null) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("loading_view_cancel_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = cancelText)
                }
            }
        }
    }
}

/**
 * Compact inline status bar for in-place loading.
 */
@Composable
private fun LoadingInline(
    modifier: Modifier = Modifier,
    message: String,
    subMessage: String?,
    isAiMode: Boolean,
    progress: Float?,
    icon: ImageVector?
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = if (isAiMode) {
            NeonViolet.copy(alpha = 0.12f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (progress != null) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.5.dp,
                    color = if (isAiMode) NeonViolet else MaterialTheme.colorScheme.primary
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.5.dp,
                    color = if (isAiMode) NeonViolet else MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isAiMode) {
                        Icon(
                            imageVector = icon ?: Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = NeonViolet
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (!subMessage.isNullOrEmpty()) {
                    Text(
                        text = subMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (progress != null) {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Fullscreen presentation with centered card.
 */
@Composable
private fun LoadingFullscreen(
    modifier: Modifier = Modifier,
    message: String,
    subMessage: String?,
    isAiMode: Boolean,
    progress: Float?,
    onCancel: (() -> Unit)?,
    cancelText: String,
    icon: ImageVector?
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.92f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        LoadingCard(
            modifier = Modifier.widthIn(max = 400.dp),
            message = message,
            subMessage = subMessage,
            isAiMode = isAiMode,
            progress = progress,
            onCancel = onCancel,
            cancelText = cancelText,
            icon = icon
        )
    }
}

/**
 * Futuristic AI synthesis animation with rotating glowing rings, radiant badge, and cycling AI status phrases.
 */
@Composable
private fun AiGeneratingLoading(
    modifier: Modifier = Modifier,
    message: String,
    subMessage: String?,
    progress: Float?,
    onCancel: (() -> Unit)?
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ai_glow")

    // Rotation angle for iridescent halo
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "halo_rotation"
    )

    // Pulse scale for center core
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_pulse"
    )

    // Dynamic cycling status phrases for engaging feedback
    val phrases = remember {
        listOf(
            "Connecting to Gemini 2.5 Flash...",
            "Synthesizing contextual embeddings...",
            "Analyzing prompt parameters...",
            "Generating creative output...",
            "Polishing and formatting responses..."
        )
    }
    var currentPhraseIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2400)
            currentPhraseIndex = (currentPhraseIndex + 1) % phrases.size
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NeonViolet.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Radiant AI Core Orb
                Box(
                    modifier = Modifier.size(96.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer rotating iridescent gradient ring
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .rotate(rotationAngle)
                            .clip(CircleShape)
                            .border(
                                width = 3.dp,
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        NeonViolet,
                                        NeonCyan,
                                        RadiantPink,
                                        MintEmerald,
                                        NeonViolet
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    // Inner glowing background
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        NeonViolet.copy(alpha = 0.4f),
                                        RadiantPink.copy(alpha = 0.15f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Central Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Gemini AI",
                            modifier = Modifier
                                .size(28.dp)
                                .scale(pulseScale),
                            tint = NeonCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // AI Badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = NeonViolet.copy(alpha = 0.18f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = NeonViolet
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "GEMINI 2.5 FLASH ACTIVE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = NeonViolet,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Main Message
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Dynamic cycling phrase or custom subMessage
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = subMessage ?: phrases[currentPhraseIndex],
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Determinate progress or animated bouncing dots
                if (progress != null) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = NeonViolet,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Round
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${(progress * 100).toInt()}% completed",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    PulsingDotsWave(dotColor = NeonCyan)
                }

                // Optional Cancel Button
                if (onCancel != null) {
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("ai_loading_cancel_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Stop Generation")
                    }
                }
            }
        }
    }
}

/**
 * Animated 3-dot wave indicator for gentle feedback.
 */
@Composable
fun PulsingDotsWave(
    modifier: Modifier = Modifier,
    dotColor: Color = MaterialTheme.colorScheme.primary,
    dotSize: Dp = 8.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots_wave")

    val dot1Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )

    val dot2Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 150, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )

    val dot3Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dotSize)
                .scale(dot1Scale)
                .clip(CircleShape)
                .background(dotColor)
        )
        Box(
            modifier = Modifier
                .size(dotSize)
                .scale(dot2Scale)
                .clip(CircleShape)
                .background(dotColor)
        )
        Box(
            modifier = Modifier
                .size(dotSize)
                .scale(dot3Scale)
                .clip(CircleShape)
                .background(dotColor)
        )
    }
}

/**
 * Shimmer effect brush for skeleton placeholders.
 */
@Composable
fun rememberShimmerBrush(): Brush {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
}

/**
 * Shimmer Skeleton placeholders representing feed items / cards.
 */
@Composable
fun SkeletonCardLoading(
    modifier: Modifier = Modifier,
    count: Int = 2
) {
    val shimmerBrush = rememberShimmerBrush()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(count) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header skeleton: Avatar + Name + Subtitle
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(shimmerBrush)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Box(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(shimmerBrush)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(shimmerBrush)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Text lines skeleton
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(shimmerBrush)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(shimmerBrush)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Media placeholder skeleton
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(shimmerBrush)
                    )
                }
            }
        }
    }
}
