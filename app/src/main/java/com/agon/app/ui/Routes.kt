package com.agon.app.ui

// ============================================================
// Route constants for the navigation graph.
// Routes are namespaced to avoid collision and keep the
// protect / verify flows as independent back stacks.
// ============================================================

object Routes {
    // Bottom-nav destinations
    const val HOME = "home"
    const val VAULT = "vault"
    const val SECURITY = "security"

    // Protect flow
    const val PROTECT_INTRO = "protect/intro"
    const val PROTECT_SCAN = "protect/scan"
    const val PROTECT_PREVIEW = "protect/preview"
    const val PROTECT_OCR = "protect/ocr"
    const val PROTECT_PROCESSING = "protect/processing"
    const val PROTECT_SUCCESS = "protect/success"

    // Verify flow
    const val VERIFY_INTRO = "verify/intro"
    const val VERIFY_SCAN = "verify/scan"
    const val VERIFY_PROCESSING = "verify/processing"

    // Result is shared by verified and tampered
    const val RESULT_VERIFIED = "result/verified"
    const val RESULT_TAMPERED = "result/tampered"

    // Document details
    const val DOC_DETAILS = "doc/{docId}"

    fun docDetails(id: String) = "doc/$id"
}