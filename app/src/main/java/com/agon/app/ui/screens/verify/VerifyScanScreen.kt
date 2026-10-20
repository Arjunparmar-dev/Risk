package com.agon.app.ui.screens.verify

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.DocumentScanner
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
import com.agon.app.data.DocumentType
import com.agon.app.ui.components.ButtonVariant
import com.agon.app.ui.components.PrimaryButton
import com.agon.app.ui.components.ProcessingRings
import com.agon.app.ui.components.SampleDocumentImage
import com.agon.app.ui.components.ScanFrame
import com.agon.app.ui.components.SurfaceCard
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoHashSmall
import com.agon.app.ui.theme.MonoLabel
import com.agon.app.ui.theme.StatusDanger
import kotlinx.coroutines.delay

private enum class ScanPhase { SCANNING, ANALYSING }

@Composable
fun VerifyScanScreen(
    onBack: () -> Unit,
    onVerified: () -> Unit,
    onTampered: () -> Unit,
) {
    var phase by remember { mutableStateOf(ScanPhase.SCANNING) }

    LaunchedEffect(Unit) {
        phase = ScanPhase.SCANNING
        delay(2200)
        phase = ScanPhase.ANALYSING
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Verifying Document", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "SCAN & ANALYSE",
                color = MaterialTheme.colorScheme.primary,
                style = MonoLabel,
            )
            AnimatedContent(
                targetState = phase,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "scan",
            ) { p ->
                when (p) {
                    ScanPhase.SCANNING -> {
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            Text(
                                text = "Align the document inside the frame",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            ScanFrame(modifier = Modifier.fillMaxWidth()) {
                                SampleDocumentImage(
                                    type = DocumentType.GRADUATION_CERTIFICATE,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }
                    ScanPhase.ANALYSING -> {
                        ProcessingRings(
                            label = "Analysing Document…",
                            sublabel = "Extracting image features",
                            icon = Icons.Outlined.DocumentScanner,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            DemoSplitButtons(
                enabled = phase == ScanPhase.ANALYSING,
                onVerified = onVerified,
                onTampered = onTampered,
            )
            DemoHintCard()
        }
    }
}

@Composable
private fun DemoSplitButtons(
    enabled: Boolean,
    onVerified: () -> Unit,
    onTampered: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Choose demo outcome",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MonoLabel,
        )
        PrimaryButton(
            text = "Run Verification (Verified)",
            onClick = onVerified,
            enabled = enabled,
            variant = ButtonVariant.Primary,
            leadingIcon = Icons.Outlined.DocumentScanner,
        )
        PrimaryButton(
            text = "Test Tampered Document",
            onClick = onTampered,
            enabled = enabled,
            variant = ButtonVariant.Danger,
            leadingIcon = Icons.Outlined.BugReport,
        )
    }
}

@Composable
private fun DemoHintCard() {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "DEMO NOTE",
                color = StatusDanger,
                style = MonoLabel,
            )
            Text(
                text = "Both outcomes use the same captured document. The \"Test Tampered\" path simulates a modified document by comparing against a different fingerprint.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}