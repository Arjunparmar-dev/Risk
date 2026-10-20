package com.agon.app.data

import kotlinx.serialization.Serializable

// ============================================================
// Document models
// All fingerprint / signature values shown in this prototype
// are DEMO values. They are clearly labeled and NOT generated
// by real cryptographic operations.
// ============================================================

@Serializable
enum class DocumentType(val displayName: String) {
    GRADUATION_CERTIFICATE("Graduation Certificate"),
    RENT_AGREEMENT("Rent Agreement"),
    EXPERIENCE_CERTIFICATE("Experience Certificate"),
    IDENTITY_DOCUMENT("Identity Document"),
    CUSTOM("Custom Document");

    companion object {
        fun fromDisplayName(name: String): DocumentType =
            entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) } ?: CUSTOM
    }
}

@Serializable
enum class VerificationStatus { VERIFIED, TAMPERED, UNKNOWN }

@Serializable
data class Document(
    val id: String,
    val name: String,
    val type: DocumentType,
    val ownerName: String,
    val institution: String,
    val year: String,
    val protectedDate: String,           // human-readable e.g. "12 Mar 2026"
    val protectedTimestamp: Long,         // epoch millis for sorting
    val fingerprintShort: String,        // e.g. "••••A91F"
    val fingerprintFull: String,          // full hex (display only, demo)
    val signature: String,               // "Demo Valid" / "Invalid"
    val storageLocation: String = "Local Device",
    val status: VerificationStatus = VerificationStatus.VERIFIED,
)

data class ExtractedFields(
    val documentType: DocumentType,
    val name: String,
    val institution: String,
    val year: String,
)