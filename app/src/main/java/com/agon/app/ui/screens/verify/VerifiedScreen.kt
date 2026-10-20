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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Shield
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
import com.agon.app.ui.theme.StatusOk

@Composable
fun VerifiedScreen(
    document: Document,
    onViewDetails: () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Verification Result", onBack = onDone)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            VerifiedHero()
            ChecksCard()
            FingerprintCard(document)
            PrimaryButton(
                text = "View Details",
                onClick = onViewDetails,
                variant = ButtonVariant.Primary,
                leadingIcon = Icons.Outlined.ArrowForward,
            )
            PrimaryButton(
                text = "Done",
                onClick = onDone,
                variant = ButtonVariant.Ghost,
                leadingIcon = Icons.Outlined.Home,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun VerifiedHero() {
    val infinite = rememberInfiniteTransition(label = "ok")
    val pulse by infinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
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
                            StatusOk.copy(alpha = 0.32f),
                            StatusOk.copy(alpha = 0.04f),
                        ),
                    ),
                )
                .border(width = 2.dp, color = StatusOk.copy(alpha = 0.7f), shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(pulse)
                    .clip(CircleShape)
                    .background(StatusOk.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = StatusOk,
                    modifier = Modifier.size(50.dp),
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "✓ DOCUMENT VERIFIED",
                color = StatusOk,
                style = MonoLabel,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The document matches the protected record stored on this device.",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun ChecksCard() {
    SurfaceCard(modifier = Modifier.fillMaxWidth(), accent = true) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CheckRow("Hash Match", Icons.Outlined.Fingerprint)
            CheckRow("Signature Valid", Icons.Outlined.Key)
            CheckRow("Local Record Found", Icons.Outlined.Shield)
        }
    }
}

@Composable
private fun CheckRow(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(StatusOk.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = StatusOk,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = StatusOk,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun FingerprintCard(document: Document) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "FINGERPRINT",
                color = MaterialTheme.colorScheme.primary,
                style = MonoLabel,
            )
            Text(
                text = document.fingerprintFull,
                color = MaterialTheme.colorScheme.onSurface,
                style = MonoHashSmall,
            )
        }
    }
}