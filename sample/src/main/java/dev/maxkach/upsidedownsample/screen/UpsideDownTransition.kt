package dev.maxkach.upsidedownsample.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos

@Stable
class UpsideDownTransition {
    var isGlitching: Boolean by mutableStateOf(false)
        private set

    var isUpsideDown: Boolean by mutableStateOf(false)
        private set

    var glitchIntensity: Float by mutableFloatStateOf(0f)
        private set

    internal fun update(elapsedMillis: Long, withGlitch: Boolean) {
        isGlitching = withGlitch && elapsedMillis < SecondBurstStartMillis + SecondBurstMillis
        glitchIntensity = if (withGlitch) glitchIntensityAt(elapsedMillis) else 0f
        isUpsideDown = !withGlitch || elapsedMillis >= UpsideDownStartMillis
    }

    internal fun reset() {
        isGlitching = false
        glitchIntensity = 0f
        isUpsideDown = false
    }
}

@Composable
fun rememberUpsideDownTransition(isActive: Boolean, withGlitch: Boolean): UpsideDownTransition {
    val transition = remember { UpsideDownTransition() }

    LaunchedEffect(isActive, withGlitch) {
        if (!isActive) {
            transition.reset()
            return@LaunchedEffect
        }

        var startNanos = 0L
        while (!transition.isUpsideDown) {
            withFrameNanos { frameNanos ->
                if (startNanos == 0L) startNanos = frameNanos
                transition.update((frameNanos - startNanos) / NanosPerMilli, withGlitch)
            }
        }
    }

    return transition
}

private fun glitchIntensityAt(elapsedMillis: Long): Float {
    return when {
        elapsedMillis < FirstBurstMillis ->
            1f - elapsedMillis / FirstBurstMillis.toFloat()

        elapsedMillis < SecondBurstStartMillis -> 0f

        elapsedMillis < SecondBurstStartMillis + SecondBurstMillis ->
            1f - (elapsedMillis - SecondBurstStartMillis) / SecondBurstMillis.toFloat()

        else -> 0f
    }
}

private const val FirstBurstMillis = 200L
private const val SecondBurstStartMillis = 500L
private const val SecondBurstMillis = 500L
private const val UpsideDownStartMillis = 1100L
private const val NanosPerMilli = 1_000_000L
