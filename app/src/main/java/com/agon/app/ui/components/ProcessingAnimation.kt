package com.agon.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agon.app.ui.theme.MonoHashSmall

// ============================================================
// ProcessingAnimations — animated indicators for the protect
// and verify flows.
// ============================================================

@Composable
fun ProcessingRings(
    modifier: Modifier = Modifier,
    label: String? = null,
    sublabel: String? = null,
    icon: ImageVector = Icons.Outlined.Fingerprint,
) {
    val infinite = rememberInfiniteTransition(label = "proc")
    val rot by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing)),
        label = "rot",
    )
    val pulse by infinite.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val ringStroke = 3.dp.toPx()

                drawCircle(
                    color = Color(0xFF00E5C7).copy(alpha = 0.05f),
                    radius = size.minDimension / 2 * pulse,
                    center = center,
                    style = Stroke(width = ringStroke),
                )
                drawArc(
                    color = Color(0xFF00E5C7).copy(alpha = 0.85f),
                    startAngle = rot,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(center.x - size.minDimension / 3, center.y - size.minDimension / 3),
                    size = androidx.compose.ui.geometry.Size(size.minDimension / 1.5f, size.minDimension / 1.5f),
                    style = Stroke(width = ringStroke + 1, cap = StrokeCap.Round),
                )
                drawArc(
                    color = Color(0xFF2DD4BF).copy(alpha = 0.55f),
                    startAngle = -rot * 0.7f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(center.x - size.minDimension / 2.4f, center.y - size.minDimension / 2.4f),
                    size = androidx.compose.ui.geometry.Size(size.minDimension / 1.2f, size.minDimension / 1.2f),
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
                )
            }
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF00E5C7).copy(alpha = 0.22f),
                                Color(0xFF00E5C7).copy(alpha = 0.04f),
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(34.dp),
                )
            }
        }

        if (label != null) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (sublabel != null) {
            Text(
                text = sublabel,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MonoHashSmall,
            )
        }
    }
}

// Vertical step list shown during the verify flow
@Composable
fun StepProgressList(
    steps: List<Step>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        steps.forEachIndexed { i, step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                StepDot(state = step.state, index = i + 1)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = step.label,
                        color = when (step.state) {
                            StepStatus.PENDING -> MaterialTheme.colorScheme.onSurfaceVariant
                            StepStatus.ACTIVE, StepStatus.DONE -> MaterialTheme.colorScheme.onSurface
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (step.state == StepStatus.ACTIVE) FontWeight.SemiBold else FontWeight.Normal,
                    )
                    if (step.detail.isNotEmpty()) {
                        Text(
                            text = step.detail,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MonoHashSmall,
                        )
                    }
                }
            }
        }
    }
}

enum class StepStatus { PENDING, ACTIVE, DONE }

data class Step(
    val label: String,
    val detail: String = "",
    val state: StepStatus = StepStatus.PENDING,
)

@Composable
private fun StepDot(state: StepStatus, index: Int) {
    val infinite = rememberInfiniteTransition(label = "dot")
    val pulse by infinite.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseDot",
    )
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(
                when (state) {
                    StepStatus.DONE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                    StepStatus.ACTIVE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.22f * pulse)
                    StepStatus.PENDING -> MaterialTheme.colorScheme.surfaceContainerHigh
                },
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (state == StepStatus.DONE) {
            Icon(
                imageVector = Icons.Outlined.Verified,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp),
            )
        } else {
            Text(
                text = index.toString(),
                color = if (state == StepStatus.ACTIVE) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}