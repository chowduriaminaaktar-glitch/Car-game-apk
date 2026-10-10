package com.example

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StormAcidGreen
import com.example.ui.theme.StormAsphalt
import com.example.ui.theme.StormBorder
import com.example.ui.theme.StormBrakeRed
import com.example.ui.theme.StormCyan
import com.example.ui.theme.StormCyanDark
import com.example.ui.theme.StormSurface
import com.example.ui.theme.StormSurfaceVariant
import com.example.ui.theme.StormText
import com.example.ui.theme.StormTextMuted

@Composable
fun RotatePhoneScreen(
    orientationState: OrientationState,
    selectedLevelId: Int,
    onSelectLevel: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showHelpDialog by remember { mutableStateOf(false) }

    val activeLevel = RallyLevels.allLevels.find { it.id == selectedLevelId } ?: RallyLevels.allLevels[0]

    val infiniteTransition = rememberInfiniteTransition(label = "phone_rotation")
    val animatedAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 90f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "angle"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        StormAsphalt,
                        Color(0xFF06181D),
                        Color(0xFF040F12)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(StormSurfaceVariant.copy(alpha = 0.7f))
                        .border(1.dp, StormBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsMotorsports,
                        contentDescription = "Rally",
                        tint = activeLevel.accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RALLY SIMULATOR • ${RallyLevels.allLevels.size} STAGES",
                        color = activeLevel.accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "STORMLINE",
                    color = StormText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp,
                    fontFamily = FontFamily.SansSerif
                )

                Text(
                    text = "WEATHER-DRIVEN RACING ENGINE",
                    color = StormTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }

            // Stage Selection Carousel
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SELECT RALLY STAGE",
                        color = StormCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "${activeLevel.stageNumber} OF 0${RallyLevels.allLevels.size}",
                        color = StormTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(RallyLevels.allLevels) { level ->
                        val isSelected = level.id == selectedLevelId
                        StageCard(
                            level = level,
                            isSelected = isSelected,
                            onSelect = { onSelectLevel(level.id) }
                        )
                    }
                }
            }

            // Central Animated Phone Rotate Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("rotate_phone_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = StormSurface.copy(alpha = 0.85f)),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    activeLevel.accentColor.copy(alpha = 0.4f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated Phone Graphic
                    Box(
                        modifier = Modifier.size(130.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Ambient rotation trajectory circle
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .border(
                                    width = 1.5.dp,
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            activeLevel.accentColor.copy(alpha = 0.1f),
                                            activeLevel.accentColor.copy(alpha = pulseGlow),
                                            StormAcidGreen.copy(alpha = 0.1f)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )

                        // Smartphone Graphic rotating
                        Box(
                            modifier = Modifier
                                .size(width = 54.dp, height = 96.dp)
                                .rotate(animatedAngle)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF0F262E))
                                .border(
                                    width = 2.dp,
                                    color = activeLevel.accentColor.copy(alpha = pulseGlow),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(3.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(Color(0xFF06151A)),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = "Speedometer",
                                    tint = activeLevel.accentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "RACE",
                                    color = StormText,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ScreenRotation,
                            contentDescription = "Rotate",
                            tint = activeLevel.accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ROTATE TO PLAY ${activeLevel.name}",
                            color = activeLevel.accentColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Turn your phone horizontally into landscape mode to enter the cockpit and race ${activeLevel.name}.",
                        color = StormTextMuted,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Live Sensor Degree Status Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF091F26))
                            .padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(StormAcidGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Sensor: ${orientationState.currentAngle.value.toInt()}° (Turn 90° to start)",
                            color = StormText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Dynamic Stage Telemetry Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StormSurface.copy(alpha = 0.6f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, StormBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${activeLevel.stageNumber} TELEMETRY",
                            color = activeLevel.accentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "${activeLevel.laps} • ${activeLevel.timeLimit}",
                            color = StormTextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TelemetryItem(
                            icon = Icons.Default.WaterDrop,
                            label = "WEATHER",
                            value = activeLevel.weather.take(18),
                            tint = activeLevel.accentColor
                        )
                        TelemetryItem(
                            icon = Icons.Default.Navigation,
                            label = "SURFACE",
                            value = activeLevel.surface.take(18),
                            tint = StormAcidGreen
                        )
                        TelemetryItem(
                            icon = Icons.Default.Speed,
                            label = "GRIP LEVEL",
                            value = activeLevel.grip.take(18),
                            tint = StormBrakeRed
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = activeLevel.description,
                        color = StormTextMuted,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        orientationState.forceLandscape()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("force_landscape_button")
                        .shadow(12.dp, RoundedCornerShape(14.dp), ambientColor = activeLevel.accentColor, spotColor = activeLevel.accentColor),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = activeLevel.accentColor,
                        contentColor = Color(0xFF002026)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play in Landscape",
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PLAY ${activeLevel.name} IN LANDSCAPE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                OutlinedButton(
                    onClick = { showHelpDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("help_controls_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StormBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = StormText
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Controls",
                        tint = StormCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Driving Guide & Touch Controls",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    if (showHelpDialog) {
        ControlsHelpDialog(onDismiss = { showHelpDialog = false })
    }
}

@Composable
private fun StageCard(
    level: RallyLevel,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) level.accentColor else StormBorder
    val bgColor = if (isSelected) StormSurfaceVariant else StormSurface.copy(alpha = 0.7f)

    Box(
        modifier = Modifier
            .width(170.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onSelect)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = level.stageNumber,
                    color = if (isSelected) level.accentColor else StormTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = level.accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = level.name,
                color = StormText,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )

            Text(
                text = level.tag,
                color = level.accentColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = level.laps,
                    color = StormTextMuted,
                    fontSize = 9.sp
                )
                Text(
                    text = level.difficulty,
                    color = level.accentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TelemetryItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    tint: Color
) {
    Column(horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = StormTextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = StormText,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ControlsHelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = StormCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Stormline Driving Guide",
                    color = StormText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Controls are available on-screen via touch buttons in landscape mode:",
                    color = StormTextMuted,
                    fontSize = 13.sp
                )

                ControlItem(
                    name = "STEERING (LEFT / RIGHT)",
                    desc = "Bottom-left paddle buttons. Counter-steer into slides when drifting on wet roads."
                )

                ControlItem(
                    name = "THROTTLE & BRAKE",
                    desc = "Bottom-right tall pedal is throttle. Wide pedal is brake. Feather throttle through deep puddles to avoid aquaplaning."
                )

                ControlItem(
                    name = "HANDBRAKE (DRIFT) & PUSH (BOOST)",
                    desc = "Extra buttons next to pedals. Tap handbrake to initiate sharp hairpin drifts. Tap boost for straightaway acceleration."
                )

                ControlItem(
                    name = "VEHICLE TUNING & GARAGE",
                    desc = "Tune wing downforce, suspension stiffness, and final drive ratio in the pre-race menu for maximum weather grip."
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = StormCyan, contentColor = Color(0xFF002026))
            ) {
                Text("Got It", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = StormSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun ControlItem(name: String, desc: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(StormSurfaceVariant.copy(alpha = 0.5f))
            .padding(10.dp)
    ) {
        Text(
            text = name,
            color = StormAcidGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = desc,
            color = StormText,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

