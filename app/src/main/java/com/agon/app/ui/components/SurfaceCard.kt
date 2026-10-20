package com.agon.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

// ============================================================
// SurfaceCard — rounded elevated card with subtle gradient
// border. Used as the base block for almost every screen.
// ============================================================

@Composable
fun SurfaceCard(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(20.dp),
    onClick: (() -> Unit)? = null,
    accent: Boolean = false,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    val interaction = remember { MutableInteractionSource() }

    val borderBrush = if (accent) {
        Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
            ),
        )
    } else {
        Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                MaterialTheme.colorScheme.outline.copy(alpha = 0.05f),
            ),
        )
    }

    val baseModifier = modifier
        .clip(shape)
        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        .border(
            width = 1.dp,
            brush = borderBrush,
            shape = shape,
        )
        .let { base ->
            if (onClick != null) {
                base.clickable(
                    interactionSource = interaction,
                    indication = androidx.compose.foundation.LocalIndication.current,
                    onClick = onClick,
                )
            } else base
        }
        .padding(padding)

    Box(modifier = baseModifier) {
        content()
    }
}