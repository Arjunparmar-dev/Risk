package com.agon.app.ui.screens.protect

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Storage
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agon.app.data.Document
import com.agon.app.ui.components.ButtonVariant
import com.agon.app.ui.components.PrimaryButton
import com.agon.app.ui.components.SurfaceCard
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoHashSmall
import com.agon.app.ui.theme.MonoLabel
import com.agon.app.ui.theme.StatusOk

@Composable
fun ProtectSuccessScreen(
    document: Document,
    onView: () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Document Protected", onBack = onDone)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            SuccessHero()
            DocumentSummary(document)
            Spacer(modifier = Modifier.height(4.dp))
            PrimaryButton(
                text = "View Document",
                onClick = onView,
                variant = ButtonVariant.Primary,
                leadingIcon = Icons.Outlined.ArrowForward,
            )
            PrimaryButton(
                text = "Back to Vault",
                onClick = onDone,
                variant = ButtonVariant.Ghost,
                leadingIcon = Icons.Outlined.Home,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SuccessHero() {
    val infinite = rememberInfiniteTransition(label = "success")
    val pulse by infinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                StatusOk.copy(alpha = 0.32f),
                                StatusOk.copy(alpha = 0.04f),
                            ),
                        ),
                    )
                    .border(
                        width = 2.dp,
                        color = StatusOk.copy(alpha = 0.7f),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(pulse)
                        .clip(CircleShape)
                        .background(StatusOk.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = StatusOk,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "✓ DOCUMENT PROTECTED",
                    color = StatusOk,
                    style = MonoLabel,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Your document has been added to the local Zero-Trust Vault.",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun DocumentSummary(document: Document) {
    SurfaceCard(modifier = Modifier.fillMaxWidth(), accent = true) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Fingerprint,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "PROTECTION RECORD",
                    color = MaterialTheme.colorScheme.primary,
                    style = MonoLabel,
                )
            }
            SummaryRow("Document", document.name)
            SummaryRow("Fingerprint", document.fingerprintShort, mono = true)
            SummaryRow("Signature", "Demo Valid", icon = Icons.Outlined.Key)
            SummaryRow("Storage", document.storageLocation, icon = Icons.Outlined.Storage)
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    icon: ImageVector? = null,
    mono: Boolean = false,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(96.dp),
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            style = if (mono) MonoHashSmall else MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}