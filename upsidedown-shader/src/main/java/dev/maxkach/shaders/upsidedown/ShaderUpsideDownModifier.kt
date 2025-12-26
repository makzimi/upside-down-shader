
package dev.maxkach.shaders.upsidedown

import android.graphics.RuntimeShader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer

@Stable
@Composable
fun Modifier.shaderUpsideDown(
    isEnabled: Boolean = true,
    darknessIntensity: Float = 0.4f,
): Modifier {
    if (!isEnabled) {
        return this
    }

    val shader = remember {
        RuntimeShader(UPSIDE_DOWN_SHADER)
    }

    var time by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { frameTime ->
                time = frameTime
            }
        }
    }

    return this
        .clipToBounds()
        .graphicsLayer {
            shader.setFloatUniform("imageSize", size.width, size.height)
            shader.setFloatUniform("time", time / 1000f) // Convert to seconds
            shader.setFloatUniform("darknessIntensity", darknessIntensity)

            renderEffect = shader.asEffect("image")
        }
}

private fun RuntimeShader.asEffect(uniformName: String): RenderEffect {
    return android.graphics.RenderEffect
        .createRuntimeShaderEffect(this, uniformName)
        .asComposeRenderEffect()
}
