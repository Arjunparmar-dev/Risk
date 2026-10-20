package com.agon.app.ui.screens

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agon.app.data.DocumentType
import com.agon.app.data.VerificationStatus
import com.agon.app.ui.components.ButtonVariant
import com.agon.app.ui.components.PrimaryButton
import com.agon.app.ui.components.PillTone
import com.agon.app.ui.components.StatusPill
import com.agon.app.ui.components.SurfaceCard
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoHashLarge
import com.agon.app.ui.theme.MonoHashSmall
import com.agon.app.ui.theme.MonoLabel
import com.agon.app.ui.theme.StatusDanger
import com.agon.app.ui.theme.StatusOk
import com.agon.app.viewmodel.VaultViewModel

@Composable
fun DocumentDetailsScreen(
    docId: String,
    onBack: () -> Unit,
    onVerifyAgain: (String) -> Unit,
    vm: VaultViewModel = viewModel(),
) {
    val documents by vm.documents.collectAsState()
    val doc = documents.firstOrNull { it.id == docId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Document Details", onBack = onBack)

        if (doc == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Document not found",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                VerificationBanner(doc.status)
            }
            item {
                DocumentSummaryCard(doc)
            }
            item {
                FingerprintCard(doc.fingerprintFull)
            }
            item {
                SecurityInfoCard()
            }
            item {
                PrimaryButton(
                    text = "Verify Again",
                    onClick = { onVerifyAgain(doc.id) },
                    variant = ButtonVariant.Primary,
                    leadingIcon = Icons.Outlined.Verified,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun VerificationBanner(status: VerificationStatus) {
    val tone = when (status) {
        VerificationStatus.VERIFIED -> Triple(StatusOk, "DOCUMENT VERIFIED", "Hash match · Signature valid · Local record found")
        VerificationStatus.TAMPERED -> Triple(StatusDanger, "TAMPER DETECTED", "Hash mismatch · Signature invalid")
        VerificationStatus.UNKNOWN -> Triple(
            MaterialTheme.colorScheme.onSurfaceVariant, "STATUS UNKNOWN", "Record not yet verified",
        )
    }
    val tint = tone.first
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        tint.copy(alpha = 0.18f),
                        tint.copy(alpha = 0.02f),
                    ),
                ),
            )
            .border(
                width = 1.dp,
                color = tint.copy(alpha = 0.45f),
                shape = RoundedCornerShape(20.dp),
            )
            .padding(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(tint.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(22.dp),
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = tone.second,
                    color = tint,
                    style = MonoLabel,
                )
            }
            Text(
                text = tone.third,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            VerificationChecklist(status)
        }
    }
}

@Composable
private fun VerificationChecklist(status: VerificationStatus) {
    val items = listOf(
        Triple("Hash Match", status == VerificationStatus.VERIFIED, Icons.Outlined.Fingerprint),
        Triple("Signature Valid", status == VerificationStatus.VERIFIED, Icons.Outlined.Key),
        Triple("Local Record Found", true, Icons.Outlined.Shield),
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { (label, passed, icon) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(
                            if (passed) StatusOk.copy(alpha = 0.18f)
                            else StatusDanger.copy(alpha = 0.18f),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = if (passed) Icons.Outlined.Check else Icons.Outlined.Info,
                        contentDescription = null,
                        tint = if (passed) StatusOk else StatusDanger,
                        modifier = Modifier.size(14.dp),
                    )
                }
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun DocumentSummaryCard(doc: com.agon.app.data.Document) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val typeIcon: ImageVector = when (doc.type) {
                DocumentType.GRADUATION_CERTIFICATE -> Icons.Outlined.School
                DocumentType.RENT_AGREEMENT -> Icons.Outlined.Home
                DocumentType.EXPERIENCE_CERTIFICATE -> Icons.Outlined.Work
                DocumentType.IDENTITY_DOCUMENT -> Icons.Outlined.AccountBalance
                DocumentType.CUSTOM -> Icons.Outlined.Category
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
                                ),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = typeIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp),
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = doc.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = doc.type.displayName,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            InfoRow("Owner", doc.ownerName, Icons.Outlined.Person)
            InfoRow("Institution", doc.institution, Icons.Outlined.AccountBalance)
            InfoRow("Year", doc.year, Icons.Outlined.CalendarMonth)
            InfoRow("Protected on", doc.protectedDate, Icons.Outlined.CalendarMonth)
            InfoRow("Storage", doc.storageLocation, Icons.Outlined.Shield)
            InfoRow("Signature", doc.signature, Icons.Outlined.Key)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label.uppercase(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MonoLabel,
            )
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun FingerprintCard(full: String) {
    SurfaceCard(modifier = Modifier.fillMaxWidth(), accent = true) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Fingerprint,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SHA-256 FINGERPRINT",
                    color = MaterialTheme.colorScheme.primary,
                    style = MonoLabel,
                )
            }
            Text(
                text = "Demo value — not generated by a real SHA-256 implementation in this prototype.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(14.dp),
            ) {
                Text(
                    text = full,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MonoHashSmall,
                )
            }
        }
    }
}

@Composable
private fun SecurityInfoCard() {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SECURITY INFORMATION",
                    color = MaterialTheme.colorScheme.primary,
                    style = MonoLabel,
                )
            }
            BulletItem("Fingerprint computed over document image and metadata")
            BulletItem("Signature bound to this device's cryptographic key (demo)")
            BulletItem("Verification is fully offline — no network calls")
            BulletItem("Protected records are stored in local app sandbox")
        }
    }
}

@Composable
private fun BulletItem(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}