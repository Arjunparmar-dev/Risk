package com.agon.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// ============================================================
// PrimaryButton — filled action button with optional leading
// icon, gradient glow on the primary style.
// ============================================================

enum class ButtonVariant { Primary, Secondary, Ghost, Danger }

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
) {
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(14.dp)

    val containerBrush = when (variant) {
        ButtonVariant.Primary -> Brush.horizontalGradient(
            listOf(MaterialTheme.colorScheme.primary, Color(0xFF2DD4BF)),
        )
        ButtonVariant.Secondary -> Brush.horizontalGradient(
            listOf(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.colorScheme.surfaceContainerHigh),
        )
        ButtonVariant.Ghost -> Brush.horizontalGradient(
            listOf(Color.Transparent, Color.Transparent),
        )
        ButtonVariant.Danger -> Brush.horizontalGradient(
            listOf(MaterialTheme.colorScheme.error, Color(0xFFFF7A95)),
        )
    }

    val textColor by animateColorAsState(
        targetValue = when (variant) {
            ButtonVariant.Primary -> MaterialTheme.colorScheme.onPrimary
            ButtonVariant.Secondary -> MaterialTheme.colorScheme.onSurface
            ButtonVariant.Ghost -> MaterialTheme.colorScheme.primary
            ButtonVariant.Danger -> MaterialTheme.colorScheme.onError
        },
        label = "btnText",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape)
            .background(containerBrush)
            .clickable(
                interactionSource = interaction,
                indication = LocalIndication,
                enabled = enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(contentPadding),
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(20.dp),
                )
                Box(modifier = Modifier.size(8.dp))
            }
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// Fallback indication — use the system default.
private val LocalIndication: androidx.compose.foundation.Indication
    @Composable
    get() = androidx.compose.foundation.LocalIndication.current