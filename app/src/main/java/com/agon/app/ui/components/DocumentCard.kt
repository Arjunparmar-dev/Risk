package com.agon.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.agon.app.data.Document
import com.agon.app.data.DocumentType
import com.agon.app.data.VerificationStatus
import com.agon.app.ui.theme.MonoHashLarge

// ============================================================
// DocumentCard — list-row component for documents in the vault
// ============================================================

@Composable
fun DocumentCard(
    document: Document,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon: ImageVector = when (document.type) {
        DocumentType.GRADUATION_CERTIFICATE -> Icons.Outlined.School
        DocumentType.RENT_AGREEMENT -> Icons.Outlined.Home
        DocumentType.EXPERIENCE_CERTIFICATE -> Icons.Outlined.Work
        DocumentType.IDENTITY_DOCUMENT -> Icons.Outlined.VerifiedUser
        DocumentType.CUSTOM -> Icons.Outlined.Description
    }

    SurfaceCard(
        modifier = modifier,
        onClick = onClick,
        accent = document.status == VerificationStatus.VERIFIED,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
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
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp),
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = document.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusPill(
                        text = when (document.status) {
                            VerificationStatus.VERIFIED -> "Verified"
                            VerificationStatus.TAMPERED -> "Tampered"
                            VerificationStatus.UNKNOWN -> "Unknown"
                        },
                        tone = when (document.status) {
                            VerificationStatus.VERIFIED -> PillTone.Success
                            VerificationStatus.TAMPERED -> PillTone.Danger
                            VerificationStatus.UNKNOWN -> PillTone.Warning
                        },
                    )
                }
                Text(
                    text = "SHA-256 ${document.fingerprintShort}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MonoHashLarge,
                )
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0x14FFFFFF)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}