package com.speedmath.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.speedmath.app.*
import com.speedmath.app.ui.components.*
import com.speedmath.app.ui.theme.*

@Composable
fun GameScreen(state: GameState, vm: GameViewModel) {
    val focusRequester = remember { FocusRequester() }

    // Question pop-in animation
    var questionKey by remember { mutableIntStateOf(0) }
    LaunchedEffect(state.questionRevision) {
        questionKey = state.questionRevision
        try { focusRequester.requestFocus() } catch (_: Exception) {}
    }

    // Score bump
    val scoreBump by animateFloatAsState(
        targetValue = if (state.feedbackState == FeedbackState.CORRECT) 1.3f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 400f),
        label = "score_bump"
    )

    // Shake offset
    val shakeAnim = remember { Animatable(0f) }
    LaunchedEffect(state.feedbackState) {
        if (state.feedbackState == FeedbackState.WRONG) {
            repeat(4) {
                shakeAnim.animateTo(if (it % 2 == 0) 8f else -8f,
                    animationSpec = tween(60))
            }
            shakeAnim.animateTo(0f, tween(60))
        }
    }

    // Feedback overlay color
    val flashAlpha by animateFloatAsState(
        targetValue = if (state.feedbackState == FeedbackState.NONE) 0f else 0.18f,
        animationSpec = tween(150),
        label = "flash_alpha"
    )
    val flashColor = when (state.feedbackState) {
        FeedbackState.CORRECT -> SMColors.Green.copy(alpha = flashAlpha)
        FeedbackState.WRONG   -> SMColors.Red.copy(alpha = flashAlpha)
        else                  -> Color.Transparent
    }

    // Input border
    val inputBorder = when (state.feedbackState) {
        FeedbackState.CORRECT -> SMColors.Green
        FeedbackState.WRONG   -> SMColors.Red
        else                  -> SMColors.Border
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SMColors.Background)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(56.dp))

        // ── Header row ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ScoreBox(
                label = "Score",
                value = "${state.score}",
                valueColor = SMColors.Green,
                modifier = Modifier.graphicsLayer { scaleX = scoreBump; scaleY = scoreBump },
            )
            TimerRing(
                progress = state.timeProgress,
                display = state.timerDisplay,
            )
            ScoreBox(
                label = "Q #",
                value = "${state.questionNumber}",
            )
        }

        Spacer(Modifier.height(28.dp))

        // ── Question card ─────────────────────────────────────────────────────
        AnimatedContent(
            targetState = questionKey,
            transitionSpec = {
                (scaleIn(initialScale = 0.88f) + fadeIn(tween(200))
                    togetherWith scaleOut(targetScale = 0.88f) + fadeOut(tween(100)))
            },
            label = "question_anim"
        ) { _ ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = shakeAnim.value.dp),
            ) {
                // Base card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(SMColors.Surface2)
                        .border(1.dp, SMColors.Border, RoundedCornerShape(20.dp))
                        .padding(vertical = 32.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "QUESTION ${state.questionNumber}",
                        fontFamily = MonoFamily,
                        fontSize = 11.sp,
                        letterSpacing = 2.sp,
                        color = SMColors.Muted,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.currentQuestion.text,
                        fontFamily = MonoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = SMColors.TextPrimary,
                        textAlign = TextAlign.Center,
                    )
                }
                // Flash overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(20.dp))
                        .background(flashColor)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Answer row ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = state.answerInput,
                onValueChange = { vm.setAnswerInput(it) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                textStyle = LocalTextStyle.current.copy(
                    fontFamily = MonoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    color = SMColors.TextPrimary,
                ),
                placeholder = {
                    Text(
                        "?",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontFamily = MonoFamily,
                        fontSize = 28.sp,
                        color = SMColors.Muted,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (state.answerInput.isNotEmpty()) vm.submitAnswer()
                    }
                ),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SMColors.Accent,
                    unfocusedBorderColor = inputBorder,
                    cursorColor = SMColors.Accent2,
                    focusedContainerColor = SMColors.Surface2,
                    unfocusedContainerColor = SMColors.Surface2,
                ),
            )

            Button(
                onClick = { if (state.answerInput.isNotEmpty()) vm.submitAnswer() },
                modifier = Modifier.size(58.dp),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SMColors.Accent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            ) {
                Text("→", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Streak dots ───────────────────────────────────────────────────────
        StreakDots(results = state.recentResults)

        Spacer(Modifier.height(32.dp))
    }
}
