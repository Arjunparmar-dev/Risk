package com.agon.app.ui.screens.protect

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Shield
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
import com.agon.app.ui.components.ProcessingRings
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoHashSmall
import com.agon.app.ui.theme.MonoLabel
import kotlinx.coroutines.delay

private enum class Phase(val title: String, val sub: String) {
    FINGERPRINT("Creating Document Fingerprint…", "Hashing content & metadata"),
    SIGNING("Generating Verification Record…", "Binding signature to local key"),
    PROTECTED("Document Protected", "Stored in local vault"),
}

@Composable
fun ProtectProcessingScreen(
    onComplete: () -> Unit,
) {
    var phase by remember { mutableStateOf(Phase.FINGERPRINT) }

    LaunchedEffect(Unit) {
        phase = Phase.FINGERPRINT
        delay(1400)
        phase = Phase.SIGNING
        delay(1400)
        phase = Phase.PROTECTED
        delay(900)
        onComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Security Processing", onBack = { /* locked during processing */ })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "STEP 4 OF 4",
                color = MaterialTheme.colorScheme.primary,
                style = MonoLabel,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedContent(
                targetState = phase,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "proc",
            ) { p ->
                val icon = when (p) {
                    Phase.FINGERPRINT -> Icons.Outlined.Fingerprint
                    Phase.SIGNING -> Icons.Outlined.Key
                    Phase.PROTECTED -> Icons.Outlined.Shield
                }
                ProcessingRings(
                    label = p.title,
                    sublabel = p.sub,
                    icon = icon,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Demo fingerprints are displayed. The production version computes SHA-256 over the image and metadata.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}