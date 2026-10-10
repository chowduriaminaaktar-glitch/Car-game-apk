package com.example

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color as AndroidColor
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.ui.theme.StormBorder
import com.example.ui.theme.StormCyan
import com.example.ui.theme.StormSurface
import com.example.ui.theme.StormSurfaceVariant
import com.example.ui.theme.StormText
import com.example.ui.theme.StormTextMuted

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GameWebView(
    orientationState: OrientationState,
    selectedLevelId: Int,
    onSelectLevel: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showMenu by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showLevelDialog by remember { mutableStateOf(false) }

    // Immersive mode: hide system navigation and status bars in landscape
    DisposableEffect(activity) {
        val window = activity?.window
        if (window != null) {
            val controller = WindowCompat.getInsetsController(window, window.decorView)
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.systemBars())
        }

        onDispose {
            val window = activity?.window
            if (window != null) {
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    // Synchronize level with the web game engine
    LaunchedEffect(selectedLevelId, webViewRef) {
        webViewRef?.evaluateJavascript("window.setGameLevel && window.setGameLevel($selectedLevelId);", null)
    }

    // Handle back button: show menu or toggle portrait
    BackHandler {
        if (showMenu) {
            showMenu = false
        } else if (showLevelDialog) {
            showLevelDialog = false
        } else {
            orientationState.forcePortrait()
        }
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF081317))) {
        // Native Hardware Accelerated WebView container
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(AndroidColor.parseColor("#081317"))
                    setLayerType(View.LAYER_TYPE_HARDWARE, null)
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                    overScrollMode = View.OVER_SCROLL_NEVER

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        allowFileAccess = true
                        allowContentAccess = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        builtInZoomControls = false
                        displayZoomControls = false
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                            return true
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            view?.evaluateJavascript("window.setGameLevel && window.setGameLevel($selectedLevelId);", null)
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                        }
                    }

                    loadUrl("file:///android_asset/stormline/index.html")
                    webViewRef = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .testTag("game_webview")
        )

        // Loading spinner during initial asset parse
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF081317)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = StormCyan,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(42.dp)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        text = "INITIALIZING STORMLINE ENGINE...",
                        color = StormCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        // Floating HUD Action Pill (Top Right, minimal and unobtrusive)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 12.dp)
        ) {
            if (!showMenu) {
                // Compact Floating Button
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0x990A1B22))
                        .border(1.dp, StormBorder, CircleShape)
                        .testTag("floating_hud_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Quick Menu",
                        tint = StormCyan.copy(alpha = 0.85f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                // Expanded HUD Pill Menu
                AnimatedVisibility(
                    visible = showMenu,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(StormSurface.copy(alpha = 0.95f))
                            .border(1.dp, StormCyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Level / Stages Selector
                        HudActionItem(
                            icon = Icons.Default.Flag,
                            text = "Stages",
                            onClick = {
                                showMenu = false
                                showLevelDialog = true
                            }
                        )

                        // Portrait / Cockpit Toggle
                        HudActionItem(
                            icon = Icons.Default.ScreenRotation,
                            text = "Portrait",
                            onClick = {
                                showMenu = false
                                orientationState.forcePortrait()
                            }
                        )

                        // Reload / Restart Stage
                        HudActionItem(
                            icon = Icons.Default.Refresh,
                            text = "Restart",
                            onClick = {
                                showMenu = false
                                webViewRef?.evaluateJavascript("window.__stormlineGame && window.__stormlineGame.start ? window.__stormlineGame.start(window.__stormlineGame.tune) : window.location.reload();", null)
                            }
                        )

                        // Driving Guide
                        HudActionItem(
                            icon = Icons.Default.HelpOutline,
                            text = "Controls",
                            onClick = {
                                showMenu = false
                                showHelpDialog = true
                            }
                        )

                        // Close Pill
                        IconButton(
                            onClick = { showMenu = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = StormTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        if (showHelpDialog) {
            ControlsHelpDialog(onDismiss = { showHelpDialog = false })
        }

        if (showLevelDialog) {
            LevelSelectDialog(
                selectedLevelId = selectedLevelId,
                onSelectLevel = { newId ->
                    onSelectLevel(newId)
                    webViewRef?.evaluateJavascript("window.setGameLevel && window.setGameLevel($newId);", null)
                    showLevelDialog = false
                },
                onDismiss = { showLevelDialog = false }
            )
        }
    }
}

@Composable
private fun HudActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF142B33))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = StormCyan,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = StormText,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun LevelSelectDialog(
    selectedLevelId: Int,
    onSelectLevel: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = null,
                    tint = StormCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select Rally Stage",
                    color = StormText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(280.dp)
            ) {
                items(RallyLevels.allLevels) { lvl ->
                    val isCurrent = lvl.id == selectedLevelId
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isCurrent) StormSurfaceVariant else Color(0xFF0B191E))
                            .border(
                                width = 1.dp,
                                color = if (isCurrent) lvl.accentColor else StormBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectLevel(lvl.id) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = lvl.stageNumber,
                                    color = lvl.accentColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${lvl.difficulty}",
                                    color = StormTextMuted,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = lvl.name,
                                color = StormText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${lvl.weather} • ${lvl.laps}",
                                color = StormTextMuted,
                                fontSize = 11.sp
                            )
                        }

                        if (isCurrent) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active",
                                tint = lvl.accentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = StormCyan, contentColor = Color(0xFF002026))
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = StormSurface,
        shape = RoundedCornerShape(20.dp)
    )
}
