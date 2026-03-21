package com.speedmath.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.speedmath.app.*
import com.speedmath.app.ui.components.*
import com.speedmath.app.ui.theme.*

@Composable
fun ResultsScreen(state: GameState, vm: GameViewModel) {

    // Score reveal animation
    val revealed = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        revealed.animateTo(
            1f,
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 120f)
        )
    }

    // Accuracy bar width animation
    val barProgress by animateFloatAsState(
        targetValue = state.accuracy / 100f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 80f),
        label = "accuracy_bar"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SMColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.height(40.dp))

        SMCard(modifier = Modifier.fillMaxWidth()) {

            // ── Grade ──────────────────────────────────────────────────────
            Text(
                text = state.grade,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SMColors.TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(8.dp))

            // ── Big score ──────────────────────────────────────────────────
            Text(
                text = "${state.score}",
                fontFamily = MonoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = (72 * revealed.value).sp,
                style = LocalTextStyle.current.copy(
                    brush = Brush.verticalGradient(
                        colors = listOf(SMColors.Accent2, SMColors.Accent)
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "POINTS SCORED",
                fontFamily = MonoFamily,
                fontSize = 11.sp,
                letterSpacing = 3.sp,
                color = SMColors.Muted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(28.dp))

            // ── Stats row ──────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatBox(
                    value = "${state.correctCount}",
                    label = "Correct",
                    valueColor = SMColors.Green,
                    modifier = Modifier.weight(1f),
                )
                StatBox(
                    value = "${state.wrongCount}",
                    label = "Wrong",
                    valueColor = SMColors.Red,
                    modifier = Modifier.weight(1f),
                )
                StatBox(
                    value = "${state.accuracy}%",
                    label = "Accuracy",
                    valueColor = SMColors.Accent2,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Accuracy bar ───────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "ACCURACY",
                    fontFamily = MonoFamily,
                    fontSize = 9.sp,
                    letterSpacing = 2.sp,
                    color = SMColors.Muted,
                )
                Text(
                    "${state.accuracy}%",
                    fontFamily = MonoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = SMColors.Accent2,
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(SMColors.Border)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(barProgress)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(SMColors.Accent, SMColors.Accent2)
                            )
                        )
                )
            }

            Spacer(Modifier.height(28.dp))

            // ── Play Again ─────────────────────────────────────────────────
            OutlinedButton(
                onClick = { vm.resetToSetup() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(2.dp, SMColors.Accent),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SMColors.Accent2,
                    containerColor = SMColors.Accent.copy(alpha = 0.1f),
                ),
            ) {
                Text(
                    text = "↺  PLAY AGAIN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp,
                )
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}
