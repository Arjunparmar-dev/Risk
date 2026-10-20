package com.agon.app.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// ZERO-TRUST DOCUMENT VAULT — PREMIUM CYBERSECURITY PALETTE
// Dark-first, near-black surfaces, cyan + green accents
// ============================================================

// ── Surfaces (near-black with cool blue undertones) ──────────
val BgBase = Color(0xFF050B12)        // deepest background
val BgSurface = Color(0xFF0B141C)     // primary surface
val BgSurfaceHigh = Color(0xFF121E29) // raised surface (cards)
val BgSurfaceHigher = Color(0xFF182635) // hover / selected
val BgSurfaceVariant = Color(0xFF1F2C3A) // input fields
val BgOutline = Color(0xFF2A3A4A)     // subtle borders
val BgOutlineHigh = Color(0xFF3A4D60) // prominent borders

// ── Primary palette (mint-cyan security accent) ──────────────
val Primary = Color(0xFF00E5C7)       // signature cyan-mint
val PrimaryDim = Color(0xFF008C7A)    // pressed / inactive
val PrimaryGlow = Color(0x5500E5C7)   // glow / 33% alpha
val PrimaryContainer = Color(0xFF003E36)
val OnPrimary = Color(0xFF001512)

// ── Secondary (cool security green) ─────────────────────────
val Secondary = Color(0xFF5EEAD4)
val SecondaryDim = Color(0xFF2DD4BF)
val OnSecondary = Color(0xFF001512)

// ── Tertiary (data / status accent) ──────────────────────────
val Tertiary = Color(0xFF7DD3FC)      // info blue
val OnTertiary = Color(0xFF001523)

// ── Text (high-contrast off-whites) ──────────────────────────
val TextHi = Color(0xFFE6F1F5)        // primary text
val TextMid = Color(0xFFB5C8D3)       // secondary text
val TextLow = Color(0xFF8AA0AC)       // tertiary text
val TextDim = Color(0xFF5B6E78)       // hint / disabled

// ── Status semantics ─────────────────────────────────────────
val StatusOk = Color(0xFF34D399)      // verified green
val StatusOkDim = Color(0xFF064E3B)
val StatusWarn = Color(0xFFFBBF24)    // amber
val StatusWarnDim = Color(0xFF422F0A)
val StatusDanger = Color(0xFFFF5C7C)  // tamper-detected red
val StatusDangerDim = Color(0xFF4A1422)
val StatusInfo = Color(0xFF60A5FA)

// ── Brand legacy (kept to avoid breaking Color refs) ─────────
val Purple80 = Primary
val PurpleGrey80 = Secondary
val Pink80 = StatusDanger
val Purple40 = PrimaryDim
val PurpleGrey40 = TextLow
val Pink40 = StatusDangerDim