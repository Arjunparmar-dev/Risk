package com.agon.app.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ============================================================
// Vault repository — in-memory state for the prototype.
// All operations are local; no persistence, no network.
// ============================================================

class VaultRepository {
    private val _documents = MutableStateFlow(SampleDocuments.documents)
    val documents: StateFlow<List<Document>> = _documents.asStateFlow()

    fun findById(id: String): Document? =
        _documents.value.firstOrNull { it.id == id }

    fun addDocument(document: Document) {
        _documents.update { current -> listOf(document) + current }
    }

    fun verifiedCount(): Int =
        _documents.value.count { it.status == VerificationStatus.VERIFIED }

    fun tamperedCount(): Int =
        _documents.value.count { it.status == VerificationStatus.TAMPERED }
}

// Singleton is fine for a single-screen, single-process prototype.
object VaultGraph {
    val repository: VaultRepository by lazy { VaultRepository() }
}