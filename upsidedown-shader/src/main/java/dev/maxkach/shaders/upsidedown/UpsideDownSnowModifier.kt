package dev.maxkach.shaders.upsidedown

import android.graphics.RuntimeShader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ShaderBrush
import dev.maxkach.shaders.upsidedown.snow.SNOW_SHADER

@Composable
fun Modifier.upsideDownSnow(isEnabled: Boolean = true): Modifier {
    if (!isEnabled) {
        return this
    }

    val shader = remember { RuntimeShader(SNOW_SHADER) }
    val brush = remember(shader) { ShaderBrush(shader) }
    var timeSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        var startNanos = 0L
        while (true) {
            withFrameNanos { frameNanos ->
                if (startNanos == 0L) startNanos = frameNanos
                timeSeconds = (frameNanos - startNanos) / NanosPerSecond
            }
        }
    }

    return drawWithContent {
        drawContent()
        shader.setFloatUniform("size", size.width, size.height)
        shader.setFloatUniform("time", timeSeconds)
        drawRect(brush)
    }
}

private const val NanosPerSecond = 1_000_000_000f
