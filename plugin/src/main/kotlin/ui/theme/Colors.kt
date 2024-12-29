package ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.ui.StartupUiUtil
import javax.swing.UIManager

val LocalColors = compositionLocalOf { lightColors() }

@Composable
fun TTKColors(content: @Composable () -> Unit) {
    val colors = rememberColors()

    CompositionLocalProvider(LocalColors provides colors) {
        content()
    }
}

@Composable
fun LightColors(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalColors provides lightColors()) {
        content()
    }
}

@Composable
fun DarkColors(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalColors provides darkColors()) {
        content()
    }
}

@Composable
private fun rememberColors(): Colors {
    val systemColors = rememberSystemColors()
    val combinedColors by remember(systemColors) {
        val colors = if (systemColors.isDark) darkColors() else lightColors()

        mutableStateOf(
            colors.copy(
                background = systemColors.background,
                onBackground = systemColors.onBackground,
                surface = systemColors.background,
                onSurface = systemColors.onBackground
            )
        )
    }

    return combinedColors
}

@Composable
private fun rememberSystemColors(): SystemColors {
    var systemColors by remember { mutableStateOf(SystemColors()) }

    DisposableEffect(systemColors) {
        val messageBus = ApplicationManager.getApplication().messageBus.connect()
        messageBus.subscribe(
            topic = LafManagerListener.TOPIC,
            handler = ColorsChangedListener {
                systemColors = SystemColors()
            }
        )

        onDispose {
            messageBus.disconnect()
        }
    }

    return systemColors
}

private data class SystemColors(
    val isDark: Boolean = StartupUiUtil.isUnderDarcula(),
    val background: Color = getColor(BACKGROUND_KEY),
    val onBackground: Color = getColor(FOREGROUND_KEY),
) {
    private companion object {
        const val BACKGROUND_KEY = "Panel.background"
        const val FOREGROUND_KEY = "Panel.foreground"

        val java.awt.Color.asComposeColor: Color get() = Color(red, green, blue, alpha)

        fun getColor(key: String): Color = UIManager.getColor(key)?.asComposeColor ?: Color.Unspecified
    }
}

private class ColorsChangedListener(
    val updateColors: () -> Unit
) : LafManagerListener {
    override fun lookAndFeelChanged(source: LafManager) {
        updateColors()
    }
}