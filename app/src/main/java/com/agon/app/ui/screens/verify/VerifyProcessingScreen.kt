package com.agon.app.ui.screens.verify

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Hub
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
import com.agon.app.ui.components.Step
import com.agon.app.ui.components.StepProgressList
import com.agon.app.ui.components.StepStatus
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoHashSmall
import com.agon.app.ui.theme.MonoLabel
import com.agon.app.ui.theme.StatusOk
import kotlinx.coroutines.delay

private enum class Phase(val title: String, val sub: String) {
    FINGERPRINT("Calculating Fingerprint", "Hashing current document"),
    LOCAL("Checking Local Record", "Looking up matching record"),
    SIGNATURE("Verifying Signature", "Validating device-bound signature"),
    COMPARE("Comparing Document", "Final integrity check"),
}

@Composable
fun VerifyProcessingScreen(
    isTampered: Boolean,
    onComplete: () -> Unit,
) {
    var phase by remember { mutableStateOf(Phase.FINGERPRINT) }

    LaunchedEffect(Unit) {
        phase = Phase.FINGERPRINT
        delay(900)
        phase = Phase.LOCAL
        delay(900)
        phase = Phase.SIGNATURE
        delay(900)
        phase = Phase.COMPARE
        delay(700)
        onComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Verification", onBack = { /* locked */ })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                text = "RUNNING VERIFICATION",
                color = MaterialTheme.colorScheme.primary,
                style = MonoLabel,
            )
            AnimatedContent(
                targetState = phase,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "vp",
            ) { p ->
                val icon = when (p) {
                    Phase.FINGERPRINT -> Icons.Outlined.Fingerprint
                    Phase.LOCAL -> Icons.Outlined.Hub
                    Phase.SIGNATURE -> Icons.Outlined.Key
                    Phase.COMPARE -> Icons.Outlined.Shield
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ProcessingRings(
                        label = p.title,
                        sublabel = p.sub,
                        icon = icon,
                    )
                }
            }
            StepProgressList(
                steps = listOf(
                    Step(
                        "Calculating Fingerprint",
                        if (phase == Phase.FINGERPRINT) "Computing SHA-256 (demo)" else "Step complete",
                        if (phase.ordinal >= Phase.FINGERPRINT.ordinal) StepStatus.ACTIVE else StepStatus.PENDING,
                    ),
                    Step(
                        "Checking Local Record",
                        if (phase.ordinal < Phase.LOCAL.ordinal) "Pending" else "Local vault scanned",
                        when {
                            phase.ordinal < Phase.LOCAL.ordinal -> StepStatus.PENDING
                            phase.ordinal == Phase.LOCAL.ordinal -> StepStatus.ACTIVE
                            else -> StepStatus.DONE
                        },
                    ),
                    Step(
                        "Verifying Signature",
                        if (phase.ordinal < Phase.SIGNATURE.ordinal) "Pending" else "Signature checked",
                        when {
                            phase.ordinal < Phase.SIGNATURE.ordinal -> StepStatus.PENDING
                            phase.ordinal == Phase.SIGNATURE.ordinal -> StepStatus.ACTIVE
                            else -> StepStatus.DONE
                        },
                    ),
                    Step(
                        "Comparing Document",
                        if (phase.ordinal < Phase.COMPARE.ordinal) "Pending" else "Integrity verified",
                        when {
                            phase.ordinal < Phase.COMPARE.ordinal -> StepStatus.PENDING
                            phase.ordinal == Phase.COMPARE.ordinal -> StepStatus.ACTIVE
                            else -> StepStatus.DONE
                        },
                    ),
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Demo behaviour — fingerprint, signature, and integrity are simulated for the prototype.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}