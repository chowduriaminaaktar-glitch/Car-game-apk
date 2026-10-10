package com.example

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StormlineApp()
                }
            }
        }
    }
}

@Composable
fun StormlineApp() {
    val configuration = LocalConfiguration.current
    val orientationState = rememberOrientationState()
    var selectedLevelId by rememberSaveable { mutableIntStateOf(0) }

    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Crossfade(
        targetState = isLandscape,
        animationSpec = tween(durationMillis = 350),
        label = "orientation_crossfade"
    ) { landscapeActive ->
        if (landscapeActive) {
            GameWebView(
                orientationState = orientationState,
                selectedLevelId = selectedLevelId,
                onSelectLevel = { selectedLevelId = it },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            RotatePhoneScreen(
                orientationState = orientationState,
                selectedLevelId = selectedLevelId,
                onSelectLevel = { selectedLevelId = it },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
