package com.agon.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.agon.app.ui.theme.StatusDanger
import com.agon.app.ui.theme.StatusOk
import com.agon.app.ui.theme.StatusWarn

// ============================================================
// StatusPill — compact label with a leading dot indicator.
// Used for "VERIFIED", "TAMPER DETECTED", "PROCESSING", etc.
// ============================================================

enum class PillTone { Success, Danger, Warning, Info, Neutral }

@Composable
fun StatusPill(
    text: String,
    modifier: Modifier = Modifier,
    tone: PillTone = PillTone.Success,
    leading: Boolean = true,
) {
    val (dot, bg, fg) = when (tone) {
        PillTone.Success -> Triple(StatusOk, Color(0x1434D399), StatusOk)
        PillTone.Danger -> Triple(StatusDanger, Color(0x14FF5C7C), StatusDanger)
        PillTone.Warning -> Triple(StatusWarn, Color(0x14FBBF24), StatusWarn)
        PillTone.Info -> Triple(MaterialTheme.colorScheme.tertiary, Color(0x147DD3FC), MaterialTheme.colorScheme.tertiary)
        PillTone.Neutral -> Triple(MaterialTheme.colorScheme.onSurfaceVariant, Color(0x14B5C8D3), MaterialTheme.colorScheme.onSurfaceVariant)
    }

    val pulse by animateFloatAsState(
        targetValue = if (tone == PillTone.Success || tone == PillTone.Danger) 1.15f else 1f,
        label = "pulse",
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .border(1.dp, dot.copy(alpha = 0.35f), RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        if (leading) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .scale(pulse)
                    .clip(CircleShape)
                    .background(dot),
            )
        }
        Text(
            text = text.uppercase(),
            color = fg,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}