package com.agon.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

// ============================================================
// ScanFrame — animated document scanner overlay with corner
// brackets and a sweeping scan line.
// ============================================================

@Composable
fun ScanFrame(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan")
    val scanY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "scanY",
    )
    val bracketGlow by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.78f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                ),
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp),
            ),
    ) {
        content()

        // Animated overlay (corner brackets + scan line)
        Canvas(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            val w = size.width
            val h = size.height
            val bracketLen = w / 6f
            val strokeW = 4.dp.toPx()
            val bracketColor = Color(0xFF00E5C7).copy(alpha = bracketGlow)

            // Top-left
            drawLine(bracketColor, Offset(0f, 0f), Offset(bracketLen, 0f), strokeWidth = strokeW)
            drawLine(bracketColor, Offset(0f, 0f), Offset(0f, bracketLen), strokeWidth = strokeW)
            // Top-right
            drawLine(bracketColor, Offset(w, 0f), Offset(w - bracketLen, 0f), strokeWidth = strokeW)
            drawLine(bracketColor, Offset(w, 0f), Offset(w, bracketLen), strokeWidth = strokeW)
            // Bottom-left
            drawLine(bracketColor, Offset(0f, h), Offset(bracketLen, h), strokeWidth = strokeW)
            drawLine(bracketColor, Offset(0f, h), Offset(0f, h - bracketLen), strokeWidth = strokeW)
            // Bottom-right
            drawLine(bracketColor, Offset(w, h), Offset(w - bracketLen, h), strokeWidth = strokeW)
            drawLine(bracketColor, Offset(w, h), Offset(w, h - bracketLen), strokeWidth = strokeW)

            // Subtle inner dim rectangle for the active area
            drawRoundRect(
                color = Color(0xFF00E5C7).copy(alpha = 0.05f),
                topLeft = Offset(0f, 0f),
                size = Size(w, h),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx()),
                style = Stroke(width = 1.dp.toPx()),
            )

            // Scan line
            val y = h * scanY
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.Transparent,
                        Color(0xFF00E5C7).copy(alpha = 0.9f),
                        Color.Transparent,
                    ),
                ),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 2.dp.toPx(),
            )

            // Subtle dotted grid for scanner feel
            val dot = 2.dp.toPx()
            val gap = 18.dp.toPx()
            var x = gap
            while (x < w) {
                var yPos = gap
                while (yPos < h) {
                    drawCircle(
                        color = Color(0xFF00E5C7).copy(alpha = 0.12f),
                        radius = dot,
                        center = Offset(x, yPos),
                    )
                    yPos += gap
                }
                x += gap
            }
        }
    }
}

// helper for path drawing kept minimal
private fun Path.dummy() {}