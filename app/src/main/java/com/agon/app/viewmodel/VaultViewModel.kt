package com.agon.app.viewmodel

import androidx.lifecycle.ViewModel
import com.agon.app.data.Document
import com.agon.app.data.DocumentType
import com.agon.app.data.ExtractedFields
import com.agon.app.data.SampleDocuments
import com.agon.app.data.VaultGraph
import com.agon.app.data.VerificationStatus
import kotlinx.coroutines.flow.StateFlow

// ============================================================
// VaultViewModel — single source of truth for the prototype.
// Holds the current vault, the in-flight protect / verify flows,
// and the last verification result.
// ============================================================

class VaultViewModel : ViewModel() {
    private val repo = VaultGraph.repository

    val documents: StateFlow<List<Document>> = repo.documents

    // ---------- PROTECT FLOW STATE ----------
    // The document currently being protected (in-flight capture).
    // Set by startNewCapture(), consumed when the flow finishes.
    private var inFlightDocument: Document? = null
    private var captureIndex: Int = 0

    /**
     * Begin a new protect capture. Returns the freshly-created
     * document so it can be displayed in the scanner / preview /
     * OCR / processing screens without recreating it.
     */
    fun startNewCapture(): Document {
        val idx = captureIndex
        captureIndex += 1
        val types = listOf(
            DocumentType.GRADUATION_CERTIFICATE,
            DocumentType.RENT_AGREEMENT,
            DocumentType.EXPERIENCE_CERTIFICATE,
            DocumentType.IDENTITY_DOCUMENT,
        )
        val names = listOf(
            "Graduation Certificate",
            "Rent Agreement",
            "Experience Certificate",
            "Identity Document",
        )
        val owners = listOf("Arjun Parmar", "Priya Shah", "Rohan Mehta", "Sara Iyer")
        val institutions = listOf(
            "Sample University",
            "Apartment Owner — R. Mehta",
            "Sample Tech Co.",
            "Government of Sample",
        )
        val type = types[idx % types.size]
        val name = names[idx % names.size]
        val owner = owners[idx % owners.size]
        val institution = institutions[idx % institutions.size]
        val year = "2026"
        val short = "••••" + ("%04X".format(0xA91F + idx * 0x13D))
        val full = "%04X %04X %04X %04X %04X %04X %04X %04X".format(
            0xA4B7 + idx, 0x2C81, 0x9D5E, 0x3F60, 0x7A12, 0x88E1, 0x4BC9, 0x12D6 + idx,
        )
        val doc = Document(
            id = "doc_protected_${System.currentTimeMillis()}",
            name = name,
            type = type,
            ownerName = owner,
            institution = institution,
            year = year,
            protectedDate = "Today",
            protectedTimestamp = System.currentTimeMillis(),
            fingerprintShort = short,
            fingerprintFull = full,
            signature = "Demo Valid",
            storageLocation = "Local Device",
            status = VerificationStatus.VERIFIED,
        )
        inFlightDocument = doc
        return doc
    }

    fun currentCapture(): Document? = inFlightDocument

    fun currentExtractedFields(): ExtractedFields? {
        val doc = inFlightDocument ?: return null
        return ExtractedFields(
            documentType = doc.type,
            name = doc.ownerName,
            institution = doc.institution,
            year = doc.year,
        )
    }

    /**
     * Finalise the current capture: persist it to the local
     * vault and clear the in-flight state.
     */
    fun commitCapture(): Document? {
        val doc = inFlightDocument ?: return null
        repo.addDocument(doc)
        inFlightDocument = null
        return doc
    }

    // ---------- VERIFY FLOW STATE ----------
    var lastVerificationResult: VerificationResult? = null
        private set

    fun recordVerified(documentId: String) {
        val doc = repo.findById(documentId) ?: return
        lastVerificationResult = VerificationResult.Verified(doc)
    }

    fun recordTampered(documentId: String) {
        val doc = repo.findById(documentId) ?: return
        val expected = doc
        val tampered = SampleDocuments.tamperedDemo
        lastVerificationResult = VerificationResult.Tampered(
            expected = expected,
            current = tampered,
        )
    }

    fun findById(id: String): Document? = repo.findById(id)

    fun verifiedCount(): Int = repo.verifiedCount()
    fun tamperedCount(): Int = repo.tamperedCount()
}

sealed interface VerificationResult {
    data class Verified(val document: Document) : VerificationResult
    data class Tampered(val expected: Document, val current: Document) : VerificationResult
}