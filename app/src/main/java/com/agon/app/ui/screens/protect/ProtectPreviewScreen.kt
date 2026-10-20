package com.agon.app.ui.screens.protect

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agon.app.data.Document
import com.agon.app.data.DocumentType
import com.agon.app.ui.components.ButtonVariant
import com.agon.app.ui.components.PrimaryButton
import com.agon.app.ui.components.SampleDocumentImage
import com.agon.app.ui.components.ScanFrame
import com.agon.app.ui.components.SurfaceCard
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoHashSmall
import com.agon.app.ui.theme.MonoLabel

@Composable
fun ProtectPreviewScreen(
    captured: Document,
    onBack: () -> Unit,
    onRetake: () -> Unit,
    onContinue: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Document Preview", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "STEP 2 OF 4",
                    color = MaterialTheme.colorScheme.primary,
                    style = MonoLabel,
                )
                Text(
                    text = "Review the captured document",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "Confirm the image is clear and readable, then continue to extract its fields.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                ScanFrame(modifier = Modifier.fillMaxWidth()) {
                    SampleDocumentImage(type = captured.type, modifier = Modifier.fillMaxSize())
                }
            }
            QuickSummary(captured)
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PrimaryButton(
                    text = "Retake",
                    onClick = onRetake,
                    variant = ButtonVariant.Ghost,
                    leadingIcon = Icons.Outlined.Replay,
                    modifier = Modifier.weight(1f),
                )
                PrimaryButton(
                    text = "Continue",
                    onClick = onContinue,
                    variant = ButtonVariant.Primary,
                    leadingIcon = Icons.Outlined.ArrowForward,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun QuickSummary(doc: Document) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "CAPTURE SUMMARY",
                color = MaterialTheme.colorScheme.primary,
                style = MonoLabel,
            )
            Text(
                text = doc.name,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${doc.ownerName} · ${doc.institution} · ${doc.year}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "Image quality: Good · Edge detection: Ready",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MonoHashSmall,
            )
        }
    }
}