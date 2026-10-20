package com.agon.app.ui.screens

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Cookie
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.PublicOff
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VerifiedUser
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agon.app.ui.components.SurfaceCard
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoLabel
import com.agon.app.ui.theme.StatusOk

@Composable
fun SecurityScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Device Security", onBack = onBack)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { StatusHero() }
            item { CryptoIdentityCard() }
            item { KeystoreCard() }
            item { OfflineVerificationCard() }
            item { StorageCard() }
            item { ProductionNote() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun StatusHero() {
    val infinite = rememberInfiniteTransition(label = "secpulse")
    val dotScale by infinite.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dotScale",
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.02f),
                    ),
                ),
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                shape = RoundedCornerShape(20.dp),
            )
            .padding(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .scale(dotScale)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "DEVICE SECURITY",
                    color = MaterialTheme.colorScheme.primary,
                    style = MonoLabel,
                )
            }
            Text(
                text = "Protected",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Cryptographic identity is initialized. Verification runs entirely on-device — no account, no cloud, no telemetry.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun CryptoIdentityCard() {
    SecurityCard(
        title = "Cryptographic Identity",
        status = "Initialized",
        icon = Icons.Outlined.VerifiedUser,
        tint = MaterialTheme.colorScheme.primary,
        detail = "Demo identity bound to this device. Production version will use an asymmetric key pair generated on first launch.",
    )
}

@Composable
private fun KeystoreCard() {
    SecurityCard(
        title = "Private Key",
        status = "Protected by Android Keystore",
        icon = Icons.Outlined.Key,
        tint = MaterialTheme.colorScheme.tertiary,
        detail = "In this prototype the key material is represented as a demo flag. The production version will request Android Keystore-backed key generation with hardware-backed protection where available.",
    )
}

@Composable
private fun OfflineVerificationCard() {
    SecurityCard(
        title = "Offline Verification",
        status = "Available",
        icon = Icons.Outlined.CloudOff,
        tint = StatusOk,
        detail = "All fingerprint, signature, and storage operations run locally. Verification succeeds even with airplane mode enabled.",
    )
}

@Composable
private fun StorageCard() {
    SecurityCard(
        title = "Document Storage",
        status = "Local Device",
        icon = Icons.Outlined.Folder,
        tint = MaterialTheme.colorScheme.onSurface,
        detail = "Documents and verification records live in the app's encrypted sandbox. Nothing is uploaded to any server.",
    )
}

@Composable
private fun SecurityCard(
    title: String,
    status: String,
    icon: ImageVector,
    tint: Color,
    detail: String,
) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(tint.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = status,
                        color = tint,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                StatusDot(tint)
            }
            Text(
                text = detail,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun StatusDot(tint: Color) {
    val infinite = rememberInfiniteTransition(label = "dot")
    val s by infinite.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dotS",
    )
    Box(
        modifier = Modifier
            .size(10.dp)
            .scale(s)
            .clip(CircleShape)
            .background(tint),
    )
}

@Composable
private fun ProductionNote() {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PRODUCTION TARGET",
                    color = MaterialTheme.colorScheme.primary,
                    style = MonoLabel,
                )
            }
            Text(
                text = "The production version of Zero-Trust Vault will use Android Keystore, asymmetric cryptography, SHA-256, CameraX, and on-device OCR with offline-first verification.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}