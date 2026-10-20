package com.agon.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.agon.app.ui.Routes
import com.agon.app.ui.screens.DocumentDetailsScreen
import com.agon.app.ui.screens.HomeScreen
import com.agon.app.ui.screens.SecurityScreen
import com.agon.app.ui.screens.VaultScreen
import com.agon.app.ui.screens.protect.ProtectIntroScreen
import com.agon.app.ui.screens.protect.ProtectOcrScreen
import com.agon.app.ui.screens.protect.ProtectPreviewScreen
import com.agon.app.ui.screens.protect.ProtectProcessingScreen
import com.agon.app.ui.screens.protect.ProtectScanScreen
import com.agon.app.ui.screens.protect.ProtectSuccessScreen
import com.agon.app.ui.screens.verify.TamperedScreen
import com.agon.app.ui.screens.verify.VerifiedScreen
import com.agon.app.ui.screens.verify.VerifyIntroScreen
import com.agon.app.ui.screens.verify.VerifyProcessingScreen
import com.agon.app.ui.screens.verify.VerifyScanScreen
import com.agon.app.ui.theme.AgonAppTheme
import com.agon.app.viewmodel.VaultViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AgonAppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    // Single shared ViewModel for the prototype.
    val vm: VaultViewModel = viewModel()

    val bottomDestinations = listOf(
        BottomDest(Routes.HOME, "Home", Icons.Filled.Home),
        BottomDest(Routes.VAULT, "Vault", Icons.Filled.Lock),
        BottomDest(Routes.SECURITY, "Security", Icons.Filled.Security),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isBottomRoute = bottomDestinations.any { it.route == currentRoute }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            if (isBottomRoute) {
                BottomNav(navController = navController, items = bottomDestinations)
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
        ) {
            // ── Bottom-nav destinations ──────────────────────────
            composable(Routes.HOME) {
                HomeScreen(
                    onProtect = { navController.navigate(Routes.PROTECT_INTRO) },
                    onVerify = { navController.navigate(Routes.VERIFY_INTRO) },
                    onVault = { navController.navigate(Routes.VAULT) },
                    onOpenDocument = { id -> navController.navigate(Routes.docDetails(id)) },
                    vm = vm,
                )
            }
            composable(Routes.VAULT) {
                VaultScreen(
                    onOpenDocument = { id -> navController.navigate(Routes.docDetails(id)) },
                    vm = vm,
                )
            }
            composable(Routes.SECURITY) {
                SecurityScreen(onBack = { navController.popBackStack() })
            }

            // ── Document details ─────────────────────────────────
            composable(
                route = Routes.DOC_DETAILS,
                arguments = listOf(navArgument("docId") { type = NavType.StringType }),
            ) { entry ->
                val id = entry.arguments?.getString("docId").orEmpty()
                DocumentDetailsScreen(
                    docId = id,
                    onBack = { navController.popBackStack() },
                    onVerifyAgain = { _ ->
                        navController.navigate(Routes.VERIFY_INTRO)
                    },
                    vm = vm,
                )
            }

            // ── Protect flow ─────────────────────────────────────
            composable(Routes.PROTECT_INTRO) {
                ProtectIntroScreen(
                    onBack = { navController.popBackStack() },
                    onStart = { navController.navigate(Routes.PROTECT_SCAN) },
                )
            }
            composable(Routes.PROTECT_SCAN) {
                ProtectScanScreen(
                    onBack = { navController.popBackStack() },
                    onCapture = {
                        vm.startNewCapture()
                        navController.navigate(Routes.PROTECT_PREVIEW)
                    },
                )
            }
            composable(Routes.PROTECT_PREVIEW) {
                val captured = vm.currentCapture()
                if (captured == null) {
                    LaunchedEffect(Unit) {
                        navController.popBackStack(Routes.PROTECT_INTRO, inclusive = false)
                    }
                    return@composable
                }
                ProtectPreviewScreen(
                    captured = captured,
                    onBack = { navController.popBackStack() },
                    onRetake = { navController.popBackStack() },
                    onContinue = { navController.navigate(Routes.PROTECT_OCR) },
                )
            }
            composable(Routes.PROTECT_OCR) {
                val fields = vm.currentExtractedFields()
                if (fields == null) {
                    LaunchedEffect(Unit) {
                        navController.popBackStack(Routes.PROTECT_INTRO, inclusive = false)
                    }
                    return@composable
                }
                ProtectOcrScreen(
                    fields = fields,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate(Routes.PROTECT_PROCESSING) },
                )
            }
            composable(Routes.PROTECT_PROCESSING) {
                ProtectProcessingScreen(
                    onComplete = {
                        val doc = vm.commitCapture()
                        if (doc != null) {
                            // Clear everything up to PROTECT_INTRO and replace with success
                            navController.popBackStack(Routes.PROTECT_INTRO, inclusive = false)
                            navController.navigate(Routes.PROTECT_SUCCESS)
                        }
                    },
                )
            }
            composable(Routes.PROTECT_SUCCESS) {
                // Use the most recently added document from the vault.
                val documents by vm.documents.collectAsState()
                val doc = documents.firstOrNull()
                if (doc == null) {
                    LaunchedEffect(Unit) {
                        navController.popBackStack(Routes.HOME, inclusive = false)
                    }
                    return@composable
                }
                ProtectSuccessScreen(
                    document = doc,
                    onView = {
                        // Open the document details — clear protect flow
                        navController.popBackStack(Routes.HOME, inclusive = false)
                        navController.navigate(Routes.docDetails(doc.id))
                    },
                    onDone = {
                        navController.popBackStack(Routes.HOME, inclusive = false)
                        navController.navigate(Routes.VAULT) {
                            launchSingleTop = true
                        }
                    },
                )
            }

            // ── Verify flow ──────────────────────────────────────
            composable(Routes.VERIFY_INTRO) {
                VerifyIntroScreen(
                    onBack = { navController.popBackStack() },
                    onScan = { navController.navigate(Routes.VERIFY_SCAN) },
                )
            }
            composable(Routes.VERIFY_SCAN) {
                VerifyScanScreen(
                    onBack = { navController.popBackStack() },
                    onVerified = { navController.navigate(Routes.VERIFY_PROCESSING + "/verified") },
                    onTampered = { navController.navigate(Routes.VERIFY_PROCESSING + "/tampered") },
                )
            }
            composable(
                route = Routes.VERIFY_PROCESSING + "/{mode}",
                arguments = listOf(navArgument("mode") { type = NavType.StringType }),
            ) { entry ->
                val mode = entry.arguments?.getString("mode").orEmpty()
                val documents by vm.documents.collectAsState()
                VerifyProcessingScreen(
                    isTampered = mode == "tampered",
                    onComplete = {
                        val doc = documents.firstOrNull() ?: return@VerifyProcessingScreen
                        if (mode == "tampered") vm.recordTampered(doc.id)
                        else vm.recordVerified(doc.id)
                        val dest = if (mode == "tampered") Routes.RESULT_TAMPERED
                        else Routes.RESULT_VERIFIED
                        navController.popBackStack(Routes.VERIFY_INTRO, inclusive = false)
                        navController.navigate(dest)
                    },
                )
            }
            composable(Routes.RESULT_VERIFIED) {
                val result = vm.lastVerificationResult
                if (result is com.agon.app.viewmodel.VerificationResult.Verified) {
                    VerifiedScreen(
                        document = result.document,
                        onViewDetails = {
                            val id = result.document.id
                            navController.popBackStack(Routes.HOME, inclusive = false)
                            navController.navigate(Routes.docDetails(id))
                        },
                        onDone = {
                            navController.popBackStack(Routes.HOME, inclusive = false)
                            navController.navigate(Routes.VAULT) {
                                launchSingleTop = true
                            }
                        },
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.popBackStack(Routes.HOME, inclusive = false)
                    }
                }
            }
            composable(Routes.RESULT_TAMPERED) {
                val result = vm.lastVerificationResult
                if (result is com.agon.app.viewmodel.VerificationResult.Tampered) {
                    TamperedScreen(
                        expected = result.expected,
                        current = result.current,
                        onTryAgain = {
                            navController.popBackStack(Routes.HOME, inclusive = false)
                            navController.navigate(Routes.VERIFY_SCAN)
                        },
                        onBack = {
                            navController.popBackStack(Routes.HOME, inclusive = false)
                            navController.navigate(Routes.VAULT) {
                                launchSingleTop = true
                            }
                        },
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.popBackStack(Routes.HOME, inclusive = false)
                    }
                }
            }
        }
    }
}

private data class BottomDest(val route: String, val label: String, val icon: ImageVector)

@Composable
private fun BottomNav(navController: NavHostController, items: List<BottomDest>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        items.forEach { dest ->
            NavigationBarItem(
                selected = currentRoute == dest.route,
                onClick = {
                    if (currentRoute != dest.route) {
                        navController.navigate(dest.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(dest.icon, contentDescription = dest.label) },
                label = { Text(dest.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}