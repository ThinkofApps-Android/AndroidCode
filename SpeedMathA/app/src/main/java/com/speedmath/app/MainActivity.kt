package com.speedmath.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.speedmath.app.ui.theme.SMColors
import com.speedmath.app.ui.theme.SpeedMathTheme
import com.speedmath.app.ui.screens.BrandingSplashScreen
import com.speedmath.app.ui.screens.GameScreen
import com.speedmath.app.ui.screens.ResultsScreen
import com.speedmath.app.ui.screens.SetupScreen
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private val gameModel = GameModel()
    private val vm: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedMathTheme {
                AppRoot(vm)
            }
        }
    }

    fun getGameModel(): GameModel = gameModel

    fun showGame() {
        vm.startSession()
    }

    fun showResults() {
        vm.showResults()
    }

    fun showSetup() {
        vm.resetToSetup()
    }
}

@Composable
fun AppRoot(vm: GameViewModel = viewModel()) {
    var showBrandingSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1100L)
        showBrandingSplash = false
    }

    if (showBrandingSplash) {
        BrandingSplashScreen()
    } else {
        SpeedMathApp(vm)
    }
}

@Composable
fun SpeedMathApp(vm: GameViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SMColors.Background)
    ) {
        AnimatedContent(
            targetState = state.screen,
            transitionSpec = {
                when (targetState) {
                    Screen.PLAYING -> (
                        slideInHorizontally { it } + fadeIn()
                            togetherWith slideOutHorizontally { -it } + fadeOut()
                    )
                    Screen.RESULTS -> (
                        scaleIn(initialScale = 0.92f) + fadeIn()
                            togetherWith scaleOut(targetScale = 0.92f) + fadeOut()
                    )
                    Screen.SETUP -> (
                        slideInHorizontally { -it } + fadeIn()
                            togetherWith slideOutHorizontally { it } + fadeOut()
                    )
                }
            },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                Screen.SETUP   -> SetupScreen(state = state, vm = vm)
                Screen.PLAYING -> GameScreen(state = state, vm = vm)
                Screen.RESULTS -> ResultsScreen(state = state, vm = vm)
            }
        }
    }
}
