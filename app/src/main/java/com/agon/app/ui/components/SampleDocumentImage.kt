package com.agon.app.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Work
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.agon.app.data.DocumentType

// ============================================================
// SampleDocumentImage — a stylised illustration of a paper
// document. Used inside the scan/preview screens in place of
// a real camera preview so the demo feels realistic without
// requiring CameraX permissions.
// ============================================================

@Composable
fun SampleDocumentImage(
    type: DocumentType,
    modifier: Modifier = Modifier,
) {
    val title = when (type) {
        DocumentType.GRADUATION_CERTIFICATE -> "Graduation Certificate"
        DocumentType.RENT_AGREEMENT -> "Rent Agreement"
        DocumentType.EXPERIENCE_CERTIFICATE -> "Experience Certificate"
        DocumentType.IDENTITY_DOCUMENT -> "Identity Document"
        DocumentType.CUSTOM -> "Document"
    }
    val subtitle = when (type) {
        DocumentType.GRADUATION_CERTIFICATE -> "Sample University"
        DocumentType.RENT_AGREEMENT -> "Residential Lease"
        DocumentType.EXPERIENCE_CERTIFICATE -> "Sample Tech Co."
        DocumentType.IDENTITY_DOCUMENT -> "Government of Sample"
        DocumentType.CUSTOM -> "Reference Document"
    }
    val icon: ImageVector = when (type) {
        DocumentType.GRADUATION_CERTIFICATE -> Icons.Outlined.School
        DocumentType.RENT_AGREEMENT -> Icons.Outlined.Home
        DocumentType.EXPERIENCE_CERTIFICATE -> Icons.Outlined.Work
        DocumentType.IDENTITY_DOCUMENT -> Icons.Outlined.AccountBalance
        DocumentType.CUSTOM -> Icons.Outlined.Description
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1EFE7))
            .padding(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1F2C3A)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFFE6F1F5),
                        modifier = Modifier.size(18.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = subtitle,
                        color = Color(0xFF1F2C3A),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Document ID — D-${System.currentTimeMillis().toString().takeLast(6)}",
                        color = Color(0xFF5B6E78),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0x331F2C3A)),
            )

            Text(
                text = title.uppercase(),
                color = Color(0xFF1F2C3A),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                textAlign = TextAlign.Center,
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DocLine(widthFraction = 0.85f)
                DocLine(widthFraction = 0.95f)
                DocLine(widthFraction = 0.7f)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "ISSUED",
                        color = Color(0xFF5B6E78),
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(1.dp)
                            .background(Color(0xFF1F2C3A)),
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "SEAL",
                        color = Color(0xFF5B6E78),
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Color(0xFF1F2C3A),
                                        Color(0x55000000),
                                    ),
                                ),
                            ),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                DocLine(widthFraction = 0.6f)
                DocLine(widthFraction = 0.9f)
                DocLine(widthFraction = 0.75f)
                DocLine(widthFraction = 0.85f)
            }
        }
    }
}

@Composable
private fun DocLine(widthFraction: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Color(0x331F2C3A)),
    )
}