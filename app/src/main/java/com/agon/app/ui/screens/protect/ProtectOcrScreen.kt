package com.agon.app.ui.screens.protect

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agon.app.data.ExtractedFields
import com.agon.app.ui.components.ButtonVariant
import com.agon.app.ui.components.PrimaryButton
import com.agon.app.ui.components.ProcessingRings
import com.agon.app.ui.components.SurfaceCard
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoHashSmall
import com.agon.app.ui.theme.MonoLabel
import kotlinx.coroutines.delay

private enum class OcrPhase { SCANNING, EXTRACTED }

@Composable
fun ProtectOcrScreen(
    fields: ExtractedFields,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    var phase by remember { mutableStateOf(OcrPhase.SCANNING) }

    LaunchedEffect(Unit) {
        delay(2200)
        phase = OcrPhase.EXTRACTED
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Text Extraction", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "STEP 3 OF 4",
                color = MaterialTheme.colorScheme.primary,
                style = MonoLabel,
            )
            AnimatedContent(
                targetState = phase,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ocr",
            ) { p ->
                when (p) {
                    OcrPhase.SCANNING -> OcrScanning()
                    OcrPhase.EXTRACTED -> OcrExtracted(fields)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                text = if (phase == OcrPhase.SCANNING) "Reading document..." else "Create Fingerprint",
                onClick = onContinue,
                enabled = phase == OcrPhase.EXTRACTED,
                variant = ButtonVariant.Primary,
                leadingIcon = if (phase == OcrPhase.EXTRACTED) Icons.Outlined.Fingerprint else null,
            )
        }
    }
}

@Composable
private fun OcrScanning() {
    ProcessingRings(
        label = "Reading Document…",
        sublabel = "Running on-device OCR (demo)",
        icon = Icons.Outlined.DocumentScanner,
    )
}

@Composable
private fun OcrExtracted(fields: ExtractedFields) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Text Extracted",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Sample fields read from the document",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        SurfaceCard(modifier = Modifier.fillMaxWidth(), accent = true) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "EXTRACTED FIELDS",
                    color = MaterialTheme.colorScheme.primary,
                    style = MonoLabel,
                )
                FieldRow("Document Type", fields.documentType.displayName, Icons.Outlined.Category)
                FieldRow("Name", fields.name, Icons.Outlined.Person)
                FieldRow("Institution", fields.institution, Icons.Outlined.AccountBalance)
                FieldRow("Year", fields.year, Icons.Outlined.CalendarMonth)
            }
        }
        SurfaceCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp),
                ) {
                    Text(
                        text = "DEMO",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MonoLabel,
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Sample data shown for demonstration. Production version will use on-device OCR (ML Kit).",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun FieldRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(34.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        Column {
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