package com.speedmath.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.speedmath.app.ui.theme.MonoFamily
import com.speedmath.app.ui.theme.SMColors
import kotlin.math.min

// ── SMCard ────────────────────────────────────────────────────────────────────

@Composable
fun SMCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SMColors.Surface)
            .border(1.dp, SMColors.Border, RoundedCornerShape(20.dp))
            .padding(24.dp),
        content = content,
    )
}

// ── Section label ─────────────────────────────────────────────────────────────

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = MonoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        letterSpacing = 2.sp,
        color = SMColors.Accent2,
    )
}

// ── Timer Ring ────────────────────────────────────────────────────────────────

@Composable
fun TimerRing(
    progress: Float,
    display: String,
    modifier: Modifier = Modifier,
) {
    val ringColor = when {
        progress < 0.25f -> SMColors.Red
        progress < 0.50f -> SMColors.Amber
        else             -> SMColors.Accent
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "ring_progress"
    )

    Box(
        modifier = modifier
            .size(72.dp)
            .drawBehind {
                val stroke = 5.dp.toPx()
                val radius = (size.minDimension - stroke) / 2f
                val center = Offset(size.width / 2f, size.height / 2f)

                // Background ring
                drawArc(
                    color = SMColors.Border,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                )
                // Foreground arc
                drawArc(
                    color = ringColor,
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = display,
            fontFamily = MonoFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = SMColors.TextPrimary,
        )
    }
}

// ── Score Box ─────────────────────────────────────────────────────────────────

@Composable
fun ScoreBox(
    label: String,
    value: String,
    valueColor: Color = SMColors.TextPrimary,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label.uppercase(),
            fontFamily = MonoFamily,
            fontSize = 9.sp,
            letterSpacing = 2.sp,
            color = SMColors.Muted,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = value,
            fontFamily = MonoFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = valueColor,
            textAlign = TextAlign.Center,
        )
    }
}

// ── Stat Box ──────────────────────────────────────────────────────────────────

@Composable
fun StatBox(
    value: String,
    label: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SMColors.Surface2)
            .border(1.dp, SMColors.Border, RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            fontFamily = MonoFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = valueColor,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label.uppercase(),
            fontFamily = MonoFamily,
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            color = SMColors.Muted,
        )
    }
}

// ── Streak dots ───────────────────────────────────────────────────────────────

@Composable
fun StreakDots(results: List<Boolean>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(5) { i ->
            val color = when {
                i >= results.size -> SMColors.Border
                results[i]        -> SMColors.Green
                else              -> SMColors.Red
            }
            val scale by animateFloatAsState(
                targetValue = if (i < results.size) 1.15f else 1f,
                animationSpec = spring(dampingRatio = 0.5f),
                label = "dot_scale_$i"
            )
            Box(
                modifier = Modifier
                    .size((9 * scale).dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
