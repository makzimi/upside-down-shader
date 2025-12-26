package dev.maxkach.upsidedownsample.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import dev.maxkach.upsidedownsample.R

enum class ButtonState {
    DISABLED,
    NORMAL_WORLD,
    UPSIDE_DOWN,
    ;
}

@Composable
fun UpsideDownButton(
  state: ButtonState,
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
) {
  if (state == ButtonState.DISABLED) return

  val showUpsideDown by remember(state) {
    mutableStateOf(state == ButtonState.UPSIDE_DOWN)
  }

  val rotation by animateFloatAsState(
      targetValue = if (!showUpsideDown) 0f else 180f,
      animationSpec = tween(durationMillis = 600),
      label = "upside_down_rotation"
  )

  IconButton(
      onClick = onClick,
      modifier = modifier
          .clip(CircleShape)
          .background(Color(0x88FFFFFF))
          .graphicsLayer {
            rotationZ = rotation
          }
  ) {
    Image(
        painter = painterResource(R.drawable.ic_home),
        contentDescription = null,
    )
  }
}
