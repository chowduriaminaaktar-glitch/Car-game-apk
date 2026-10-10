package com.example

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.OrientationEventListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

enum class DevicePhysicalOrientation {
    PORTRAIT,
    LANDSCAPE_RIGHT,
    LANDSCAPE_LEFT,
    REVERSE_PORTRAIT,
    UNKNOWN
}

class OrientationState(
    val currentAngle: State<Float>,
    val physicalOrientation: State<DevicePhysicalOrientation>,
    private val activity: Activity?
) {
    fun forceLandscape() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    fun forcePortrait() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun unlockAutoRotate() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }
}

@Composable
fun rememberOrientationState(): OrientationState {
    val context = LocalContext.current
    val activity = context as? Activity

    val currentAngle = remember { mutableFloatStateOf(0f) }
    val physicalOrientation = remember { mutableStateOf(DevicePhysicalOrientation.PORTRAIT) }

    DisposableEffect(context) {
        val listener = object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return

                currentAngle.floatValue = orientation.toFloat()

                val detected = when (orientation) {
                    in 60..120 -> DevicePhysicalOrientation.LANDSCAPE_RIGHT
                    in 240..300 -> DevicePhysicalOrientation.LANDSCAPE_LEFT
                    in 150..210 -> DevicePhysicalOrientation.REVERSE_PORTRAIT
                    in 330..360, in 0..30 -> DevicePhysicalOrientation.PORTRAIT
                    else -> physicalOrientation.value
                }
                physicalOrientation.value = detected

                // If user physically turns phone into landscape position, we can request sensor landscape
                // so even if their Android quick setting has auto-rotate locked, the game naturally rotates!
                if (detected == DevicePhysicalOrientation.LANDSCAPE_RIGHT ||
                    detected == DevicePhysicalOrientation.LANDSCAPE_LEFT
                ) {
                    if (activity?.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE &&
                        activity?.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    ) {
                        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                    }
                }
            }
        }

        if (listener.canDetectOrientation()) {
            listener.enable()
        }

        onDispose {
            listener.disable()
        }
    }

    return remember(activity) {
        OrientationState(currentAngle, physicalOrientation, activity)
    }
}
