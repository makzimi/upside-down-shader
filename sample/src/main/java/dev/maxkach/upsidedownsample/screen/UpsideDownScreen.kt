package dev.maxkach.upsidedownsample.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.maxkach.shaders.glitch.glitchShader
import dev.maxkach.shaders.upsidedown.shaderUpsideDown
import dev.maxkach.upsidedownsample.common.ButtonState
import dev.maxkach.upsidedownsample.common.UpsideDownButton
import dev.maxkach.upsidedownsample.sampleproduct.ProductCard
import dev.maxkach.upsidedownsample.sampleproduct.ProductCardStateCreator

@Composable
fun UpsideDownScreen(
    title: String,
    withGlitch: Boolean,
    withVines: Boolean,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedImage by remember { mutableIntStateOf(0) }
    val state by remember(selectedImage) {
        mutableStateOf(
            ProductCardStateCreator.create(selectedImage)
        )
    }
    var buttonState by remember { mutableStateOf(ButtonState.NORMAL_WORLD) }
    val transition = rememberUpsideDownTransition(
        isActive = buttonState == ButtonState.UPSIDE_DOWN,
        withGlitch = withGlitch,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .glitchShader(
                intensity = { transition.glitchIntensity },
                colorBarsEnabled = true,
                isEnabled = transition.isGlitching,
            )
            .shaderUpsideDown(
                isEnabled = transition.isUpsideDown,
                vines = withVines,
            )
    ) {
        ProductCard(
            state = state,
            stepTitle = title,
            onColorClicked = { newColor ->
                selectedImage = newColor
            },
            onImageChanged = { newImage ->
                selectedImage = newImage
            },
            onBackPressed = onBackPressed,
            modifier = Modifier
                .fillMaxSize()
        )

        UpsideDownButton(
            state = buttonState,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(64.dp)
                .align(Alignment.CenterEnd),
            onClick = {
                buttonState = if (buttonState == ButtonState.NORMAL_WORLD) {
                    ButtonState.UPSIDE_DOWN
                } else {
                    ButtonState.NORMAL_WORLD
                }
            }
        )
    }
}


