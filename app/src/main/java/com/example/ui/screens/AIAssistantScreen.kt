package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AIAssistantMode
import com.example.ui.theme.AiGlowGradient
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.viewmodel.SocialViewModel

data class AIToolInfo(
    val mode: AIAssistantMode,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val placeholderPrompt: String
)

@Composable
fun AIAssistantScreen(
    viewModel: SocialViewModel,
    modifier: Modifier = Modifier
) {
    val aiMode by viewModel.aiMode.collectAsState()
    val aiInput by viewModel.aiInputText.collectAsState()
    val aiOutput by viewModel.aiOutputText.collectAsState()
    val isAILoading by viewModel.isAILoading.collectAsState()

    var copiedToClipboard by remember { mutableStateOf(false) }

    val aiTools = listOf(
        AIToolInfo(AIAssistantMode.CAPTION_CREATOR, "Viral Captions", "Generate high-converting post captions", Icons.Default.Description, "E.g. Launching my new 3D digital art series with cyber aesthetics"),
        AIToolInfo(AIAssistantMode.HASHTAG_RADAR, "Hashtag Radar", "Discover top 15 trending tags", Icons.Default.LocalOffer, "E.g. Street photography in Tokyo rain"),
        AIToolInfo(AIAssistantMode.REEL_SCRIPT_WRITER, "Reel Scriptwriter", "Write viral 30s video scripts", Icons.Default.Movie, "E.g. 3 secret AI productivity hacks for creators"),
        AIToolInfo(AIAssistantMode.BIO_GENERATOR, "Bio Crafting", "Generate aesthetic creator profile bios", Icons.Default.Person, "E.g. AI researcher, DJ, based in SF & Tokyo"),
        AIToolInfo(AIAssistantMode.IMAGE_PROMPTER, "Prompt Expander", "Enhance text into photorealistic prompts", Icons.Default.Image, "E.g. Cyberpunk street vendor at midnight in 2088"),
        AIToolInfo(AIAssistantMode.TREND_ANALYZER, "Trend Analyzer", "Analyze audience velocity & metrics", Icons.Default.Timeline, "E.g. Generative audio vs vertical short form video"),
        AIToolInfo(AIAssistantMode.SPAM_MODERATION, "AI Content Shield", "Check safety & toxicity scores", Icons.Default.Security, "E.g. Verify incoming comment or message safety"),
        AIToolInfo(AIAssistantMode.TRANSLATOR, "Smart Translator", "Translate into 100+ global languages", Icons.Default.Translate, "E.g. Welcome everyone to the global creative stream!"),
        AIToolInfo(AIAssistantMode.CHAT_COMPANION, "Gemini Chat", "Chat freely with AI Copilot", Icons.Default.Chat, "E.g. How can I grow my engagement by 10x this month?")
    )

    val currentTool = aiTools.firstOrNull { it.mode == aiMode } ?: aiTools.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AiGlowGradient)
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AETHER AI STUDIO",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Native generative AI suite powered by Gemini 3.5 Flash. Supercharge captions, brainstorm scripts, and analyze trends instantly.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // AI Tools Carousel Selector
        item {
            Text(
                text = "Select AI Tool",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(aiTools) { tool ->
                    FilterChip(
                        selected = aiMode == tool.mode,
                        onClick = { viewModel.setAIMode(tool.mode) },
                        label = { Text(tool.title, fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(tool.icon, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonViolet
                        )
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(14.dp)) }

        // Active Tool Input Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(currentTool.icon, contentDescription = null, tint = NeonViolet)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = currentTool.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Text(
                        text = currentTool.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = aiInput,
                        onValueChange = { viewModel.setAIInput(it) },
                        placeholder = { Text(currentTool.placeholderPrompt) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("ai_studio_prompt_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.runAITool() },
                        enabled = !isAILoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("ai_studio_generate_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                    ) {
                        if (isAILoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gemini AI is thinking...", fontWeight = FontWeight.Bold, color = Color.White)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate with Gemini AI ⚡", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // Output Result Section
        if (aiOutput.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("ai_studio_result_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MintEmerald)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("AI Generation Result", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MintEmerald)
                            }

                            Row {
                                IconButton(
                                    onClick = { copiedToClipboard = true },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (copiedToClipboard) Icons.Default.Check else Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = if (copiedToClipboard) MintEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = aiOutput,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action button to apply output directly to a new post
                        Button(
                            onClick = {
                                viewModel.applyAICaptionToNewPost(aiOutput)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan.copy(alpha = 0.2f))
                        ) {
                            Text("Use in New Post / Story 🚀", color = NeonCyan, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
