package com.speedmath.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.speedmath.app.Difficulty
import com.speedmath.app.GameState
import com.speedmath.app.GameViewModel
import com.speedmath.app.Operation
import com.speedmath.app.ui.components.SMCard
import com.speedmath.app.ui.components.SectionLabel
import com.speedmath.app.ui.theme.MonoFamily
import com.speedmath.app.ui.theme.SMColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetupScreen(state: GameState, vm: GameViewModel) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SMColors.Background)
    ) {
        val isCompact = maxWidth < 390.dp

        val horizontalPadding = if (isCompact) 16.dp else 20.dp
        val contentMaxWidth = 430.dp

        val titleSize = if (isCompact) 30.sp else 34.sp
        val subtitleSize = if (isCompact) 9.sp else 10.sp
        val subtitleSpacing = if (isCompact) 1.5.sp else 2.sp
        val topSpacer = if (isCompact) 36.dp else 56.dp
        val betweenHeaderAndCard = if (isCompact) 24.dp else 32.dp
        val sectionGap = if (isCompact) 20.dp else 24.dp
        val buttonHeight = if (isCompact) 54.dp else 56.dp

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(topSpacer))

                Text(
                    text = "Speed Math",
                    fontSize = titleSize,
                    fontWeight = FontWeight.ExtraBold,
                    style = LocalTextStyle.current.copy(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.White, SMColors.Accent2)
                        )
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "CHALLENGE YOUR MENTAL ARITHMETIC",
                    fontFamily = MonoFamily,
                    fontSize = subtitleSize,
                    letterSpacing = subtitleSpacing,
                    color = SMColors.Muted,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(betweenHeaderAndCard))

                SMCard(modifier = Modifier.fillMaxWidth()) {
                    SectionLabel("⏱ Set Timer")
                    Spacer(Modifier.height(12.dp))

                    if (isCompact) {
                        CompactTimerSection(
                            timerText = state.setupTimerDisplay,
                            timerSeconds = state.timerSeconds,
                            onTimerChanged = { vm.setTimerSeconds(it) }
                        )
                    } else {
                        RegularTimerSection(
                            timerText = state.setupTimerDisplay,
                            timerSeconds = state.timerSeconds,
                            onTimerChanged = { vm.setTimerSeconds(it) }
                        )
                    }

                    Spacer(Modifier.height(sectionGap))
                    HorizontalDivider(color = SMColors.Border)
                    Spacer(Modifier.height(sectionGap))

                    SectionLabel("⊕ Operation")
                    Spacer(Modifier.height(12.dp))

                    if (isCompact) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = 2
                        ) {
                            Operation.entries.forEach { op ->
                                OpButton(
                                    op = op,
                                    selected = state.operation == op,
                                    onClick = { vm.setOperation(op) },
                                    modifier = Modifier
                                        .widthIn(min = 0.dp)
                                        .fillMaxWidth(0.48f)
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Operation.entries.forEach { op ->
                                OpButton(
                                    op = op,
                                    selected = state.operation == op,
                                    onClick = { vm.setOperation(op) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(sectionGap))
                    HorizontalDivider(color = SMColors.Border)
                    Spacer(Modifier.height(sectionGap))

                    SectionLabel("◈ Difficulty")
                    Spacer(Modifier.height(12.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = if (isCompact) 2 else 3
                    ) {
                        Difficulty.entries.forEach { diff ->
                            DiffButton(
                                diff = diff,
                                selected = state.difficulty == diff,
                                onClick = { vm.setDifficulty(diff) },
                                modifier = if (isCompact) {
                                    Modifier
                                        .widthIn(min = 0.dp)
                                        .fillMaxWidth(0.48f)
                                } else {
                                    Modifier
                                        .widthIn(min = 96.dp)
                                        .weight(1f, fill = false)
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(if (isCompact) 22.dp else 26.dp))

                    Button(
                        onClick = { vm.startSession() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SMColors.Accent),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            text = "START SESSION  →",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(if (isCompact) 28.dp else 36.dp))
            }
        }
    }
}

@Composable
private fun RegularTimerSection(
    timerText: String,
    timerSeconds: Int,
    onTimerChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TimerDisplay(
            timerText = timerText,
            compact = false
        )

        Column(modifier = Modifier.weight(1f)) {
            TimerSlider(
                timerSeconds = timerSeconds,
                onTimerChanged = onTimerChanged
            )
        }
    }
}

@Composable
private fun CompactTimerSection(
    timerText: String,
    timerSeconds: Int,
    onTimerChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TimerDisplay(
            timerText = timerText,
            compact = true
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            TimerSlider(
                timerSeconds = timerSeconds,
                onTimerChanged = onTimerChanged
            )
        }
    }
}

@Composable
private fun TimerSlider(
    timerSeconds: Int,
    onTimerChanged: (Int) -> Unit
) {
    Slider(
        value = timerSeconds.toFloat(),
        onValueChange = { onTimerChanged(it.toInt()) },
        valueRange = 15f..120f,
        steps = 6,
        colors = SliderDefaults.colors(
            thumbColor = SMColors.Accent,
            activeTrackColor = SMColors.Accent,
            inactiveTrackColor = SMColors.Border
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("15s", "30s", "1m", "1m30", "2m").forEach { label ->
            Text(
                text = label,
                fontFamily = MonoFamily,
                fontSize = 9.sp,
                color = SMColors.Muted
            )
        }
    }
}

@Composable
private fun TimerDisplay(
    timerText: String,
    compact: Boolean
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SMColors.Surface2)
            .border(1.dp, SMColors.Border, RoundedCornerShape(12.dp))
            .padding(
                horizontal = if (compact) 12.dp else 14.dp,
                vertical = if (compact) 8.dp else 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = timerText,
            fontFamily = MonoFamily,
            fontWeight = FontWeight.Bold,
            fontSize = if (compact) 24.sp else 26.sp,
            color = SMColors.TextPrimary
        )
    }
}

@Composable
fun OpButton(
    op: Operation,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
            .heightIn(min = 72.dp)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = op.symbol,
            fontFamily = MonoFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = textColor
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = op.label,
            fontFamily = MonoFamily,
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DiffButton(
    diff: Difficulty,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
            .heightIn(min = 72.dp)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = diff.label,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = diff.rangeLabel,
            fontFamily = MonoFamily,
            fontSize = 10.sp,
            color = textColor.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}