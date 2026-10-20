package com.agon.app.ui.screens.verify

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agon.app.data.Document
import com.agon.app.ui.components.ButtonVariant
import com.agon.app.ui.components.PrimaryButton
import com.agon.app.ui.components.SurfaceCard
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoHashSmall
import com.agon.app.ui.theme.MonoLabel
import com.agon.app.ui.theme.StatusDanger

@Composable
fun TamperedScreen(
    expected: Document,
    current: Document,
    onTryAgain: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Verification Result", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            TamperHero()
            ComparisonCard(expected, current)
            SignatureInvalid()
            DemoNote()
            PrimaryButton(
                text = "Try Again",
                onClick = onTryAgain,
                variant = ButtonVariant.Primary,
                leadingIcon = Icons.Outlined.Refresh,
            )
            PrimaryButton(
                text = "Back to Vault",
                onClick = onBack,
                variant = ButtonVariant.Ghost,
                leadingIcon = Icons.Outlined.Home,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TamperHero() {
    val infinite = rememberInfiniteTransition(label = "danger")
    val pulse by infinite.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            StatusDanger.copy(alpha = 0.32f),
                            StatusDanger.copy(alpha = 0.04f),
                        ),
                    ),
                )
                .border(width = 2.dp, color = StatusDanger.copy(alpha = 0.7f), shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(pulse)
                    .clip(CircleShape)
                    .background(StatusDanger.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.WarningAmber,
                    contentDescription = null,
                    tint = StatusDanger,
                    modifier = Modifier.size(52.dp),
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "⚠ TAMPER DETECTED",
                color = StatusDanger,
                style = MonoLabel,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The current document does not match the protected verification record.",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun ComparisonCard(expected: Document, current: Document) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = "FINGERPRINT COMPARISON",
                color = MaterialTheme.colorScheme.primary,
                style = MonoLabel,
            )
            HashRow("Expected", expected.fingerprintFull, highlight = false)
            HashRow("Current", current.fingerprintFull, highlight = true)
        }
    }
}

@Composable
private fun HashRow(label: String, value: String, highlight: Boolean) {
    val tint = if (highlight) StatusDanger else MaterialTheme.colorScheme.onSurface
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label.uppercase(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MonoLabel,
                modifier = Modifier.weight(1f),
            )
            if (highlight) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = StatusDanger,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = "MISMATCH",
                        color = StatusDanger,
                        style = MonoLabel,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .border(
                    width = 1.dp,
                    color = if (highlight) StatusDanger.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(12.dp),
        ) {
            Text(
                text = value,
                color = tint,
                style = MonoHashSmall,
            )
        }
    }
}

@Composable
private fun SignatureInvalid() {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(StatusDanger.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.ReportProblem,
                    contentDescription = null,
                    tint = StatusDanger,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Signature",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MonoLabel,
                )
                Text(
                    text = "Invalid",
                    color = StatusDanger,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun DemoNote() {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "DEMO BEHAVIOUR",
                color = StatusDanger,
                style = MonoLabel,
            )
            Text(
                text = "The tamper detection result is simulated for this prototype. Production version will use real SHA-256 fingerprint comparison and cryptographic signature verification.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}