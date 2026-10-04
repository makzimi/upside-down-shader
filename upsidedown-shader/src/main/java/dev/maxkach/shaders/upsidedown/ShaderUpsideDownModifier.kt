package dev.maxkach.shaders.upsidedown

import android.graphics.RuntimeShader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
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
    particles: Boolean = true,
    vines: Boolean = true,
): Modifier {
    if (!isEnabled) {
        return this
    }

    // Snow and vines wrap the grade, so they are drawn on top and not color-graded.
    return this
        .clipToBounds()
        .upsideDownSnow(isEnabled = particles)
        .upsideDownVines(isEnabled = vines)
        .upsideDownGrade(darknessIntensity = darknessIntensity)
}

@Stable
@Composable
fun Modifier.upsideDownGrade(
    isEnabled: Boolean = true,
    darknessIntensity: Float = 0.4f,
): Modifier {
    if (!isEnabled) {
        return this
    }

    val shader = remember { RuntimeShader(UPSIDE_DOWN_SHADER) }

    return this
        .clipToBounds()
        .graphicsLayer {
            shader.setFloatUniform("imageSize", size.width, size.height)
            shader.setFloatUniform("darknessIntensity", darknessIntensity)

            renderEffect = shader.asEffect("image")
        }
}

private fun RuntimeShader.asEffect(uniformName: String): RenderEffect {
    return android.graphics.RenderEffect
        .createRuntimeShaderEffect(this, uniformName)
        .asComposeRenderEffect()
}
