package dev.maxkach.upsidedownsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.maxkach.upsidedownsample.screen.UpsideDownScreen
import dev.maxkach.upsidedownsample.ui.theme.ShadersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShadersTheme {
                Surface {
                    UpsideDownShaderSampleApp()
                }
            }
        }
    }
}

@Composable
fun UpsideDownShaderSampleApp() {
    var currentScreen by remember { mutableStateOf<ShaderScreen>(ShaderScreen.MainMenu) }

    // Handle system back button
    BackHandler(enabled = currentScreen != ShaderScreen.MainMenu) {
        currentScreen = ShaderScreen.MainMenu
    }

    when (currentScreen) {
        ShaderScreen.MainMenu -> {
            MainMenuScreen(
                onNavigate = { screen -> currentScreen = screen },
                modifier = Modifier.fillMaxSize()
            )
        }

        ShaderScreen.UpsideDownScreen -> {
            UpsideDownScreen(
                onBackPressed = { currentScreen = ShaderScreen.MainMenu },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

