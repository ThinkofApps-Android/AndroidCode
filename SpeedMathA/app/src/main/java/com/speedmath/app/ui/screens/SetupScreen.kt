package com.speedmath.app.ui.screens

import androidx.compose.animation.core.spring
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
fun SetupScreen(state: GameState, vm: GameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SMColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(64.dp))

        // ── Logo ──────────────────────────────────────────────────────────────
        Text(
            text = "Speed Math",
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            style = LocalTextStyle.current.copy(
                brush = Brush.linearGradient(
                    colors = listOf(Color.White, SMColors.Accent2)
                )
            ),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "CHALLENGE YOUR MENTAL ARITHMETIC",
            fontFamily = MonoFamily,
            fontSize = 10.sp,
            letterSpacing = 2.sp,
            color = SMColors.Muted,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(36.dp))

        // ── Card ──────────────────────────────────────────────────────────────
        SMCard(modifier = Modifier.fillMaxWidth()) {

            // Timer section
            SectionLabel("⏱ Set Timer")
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SMColors.Surface2)
                        .border(1.dp, SMColors.Border, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.setupTimerDisplay,
                        fontFamily = MonoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = SMColors.TextPrimary,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Slider(
                        value = state.timerSeconds.toFloat(),
                        onValueChange = { vm.setTimerSeconds(it.toInt()) },
                        valueRange = 15f..120f,
                        steps = 6,
                        colors = SliderDefaults.colors(
                            thumbColor = SMColors.Accent,
                            activeTrackColor = SMColors.Accent,
                            inactiveTrackColor = SMColors.Border,
                        ),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        listOf("15s", "30s", "1m", "1m30", "2m").forEach { label ->
                            Text(
                                text = label,
                                fontFamily = MonoFamily,
                                fontSize = 9.sp,
                                color = SMColors.Muted,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = SMColors.Border)
            Spacer(Modifier.height(24.dp))

            // Operation section
            SectionLabel("⊕ Operation")
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Operation.entries.forEach { op ->
                    OpButton(
                        op = op,
                        selected = state.operation == op,
                        onClick = { vm.setOperation(op) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = SMColors.Border)
            Spacer(Modifier.height(24.dp))

            // Difficulty section
            SectionLabel("◈ Difficulty")
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Difficulty.entries.forEach { diff ->
                    DiffButton(
                        diff = diff,
                        selected = state.difficulty == diff,
                        onClick = { vm.setDifficulty(diff) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Start button
            Button(
                onClick = { vm.startSession() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SMColors.Accent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            ) {
                Text(
                    text = "START SESSION  →",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp,
                    color = Color.White,
                )
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

// ── OpButton ──────────────────────────────────────────────────────────────────

@Composable
fun OpButton(
    op: Operation,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) SMColors.Accent.copy(alpha = 0.15f) else SMColors.Surface2
    val border = if (selected) SMColors.Accent else SMColors.Border
    val textColor = if (selected) SMColors.Accent2 else SMColors.Muted

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = op.symbol,
            fontFamily = MonoFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = textColor,
        )
        Text(
            text = op.label,
            fontFamily = MonoFamily,
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            color = textColor,
        )
    }
}

// ── DiffButton ────────────────────────────────────────────────────────────────

@Composable
fun DiffButton(
    diff: Difficulty,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) SMColors.Accent.copy(alpha = 0.15f) else SMColors.Surface2
    val border = if (selected) SMColors.Accent else SMColors.Border
    val textColor = if (selected) SMColors.Accent2 else SMColors.Muted

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = diff.label,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = textColor,
        )
        Text(
            text = diff.rangeLabel,
            fontFamily = MonoFamily,
            fontSize = 10.sp,
            color = textColor.copy(alpha = 0.7f),
        )
    }
}
