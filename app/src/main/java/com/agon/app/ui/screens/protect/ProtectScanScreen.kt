package com.agon.app.ui.screens.protect

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
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agon.app.ui.components.ButtonVariant
import com.agon.app.ui.components.PrimaryButton
import com.agon.app.ui.components.SampleDocumentImage
import com.agon.app.ui.components.ScanFrame
import com.agon.app.ui.components.TopBar
import com.agon.app.ui.theme.MonoLabel

@Composable
fun ProtectScanScreen(
    onBack: () -> Unit,
    onCapture: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(title = "Scan Document", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "STEP 1 OF 4",
                    color = MaterialTheme.colorScheme.primary,
                    style = MonoLabel,
                )
                Text(
                    text = "Position document inside the frame",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "Align the document so all four corners are visible. Hold steady for sharp capture.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                ScanFrame(modifier = Modifier.fillMaxWidth()) {
                    SampleDocumentImage(
                        type = com.agon.app.data.DocumentType.GRADUATION_CERTIFICATE,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                text = "Capture Document",
                onClick = onCapture,
                leadingIcon = Icons.Outlined.DocumentScanner,
                variant = ButtonVariant.Primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}