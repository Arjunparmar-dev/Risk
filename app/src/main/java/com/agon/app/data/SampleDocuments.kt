package com.agon.app.data

// ============================================================
// Sample documents — demo data only.
// All hashes / signatures here are illustrative, not generated
// by SHA-256 or any real cryptographic API.
// ============================================================

object SampleDocuments {
    val documents: List<Document> = listOf(
        Document(
            id = "doc_grad_2026",
            name = "Graduation Certificate",
            type = DocumentType.GRADUATION_CERTIFICATE,
            ownerName = "Arjun Parmar",
            institution = "Sample University",
            year = "2026",
            protectedDate = "12 Mar 2026",
            protectedTimestamp = 1_710_700_800_000L,
            fingerprintShort = "••••A91F",
            fingerprintFull = "A4B7 2C81 9D5E 3F60 7A12 88E1 4BC9 12D6 A91F",
            signature = "Demo Valid",
            status = VerificationStatus.VERIFIED,
        ),
        Document(
            id = "doc_rent_2026",
            name = "Rent Agreement",
            type = DocumentType.RENT_AGREEMENT,
            ownerName = "Arjun Parmar",
            institution = "Apartment Owner — R. Mehta",
            year = "2026",
            protectedDate = "04 Feb 2026",
            protectedTimestamp = 1_700_640_000_000L,
            fingerprintShort = "••••72BC",
            fingerprintFull = "81D3 4F0A 6E22 9C77 B511 03FE 8A9C 4B21 72BC",
            signature = "Demo Valid",
            status = VerificationStatus.VERIFIED,
        ),
        Document(
            id = "doc_exp_2026",
            name = "Experience Certificate",
            type = DocumentType.EXPERIENCE_CERTIFICATE,
            ownerName = "Arjun Parmar",
            institution = "Sample Tech Co.",
            year = "2025",
            protectedDate = "28 Jan 2026",
            protectedTimestamp = 1_699_440_000_000L,
            fingerprintShort = "••••91DE",
            fingerprintFull = "5C2E B114 8FA3 7702 9BD4 61CE 23F8 90AB 91DE",
            signature = "Demo Valid",
            status = VerificationStatus.VERIFIED,
        ),
    )

    val tamperedDemo = Document(
        id = "doc_tampered_demo",
        name = "Tampered Document",
        type = DocumentType.CUSTOM,
        ownerName = "Modified Copy",
        institution = "Sample University",
        year = "2026",
        protectedDate = "12 Mar 2026",
        protectedTimestamp = 1_710_700_800_000L,
        fingerprintShort = "••••7F31",
        // Distinct from the graduation certificate fingerprint
        // so a comparison clearly fails.
        fingerprintFull = "B812 47DF 0E3A 6B91 2C4F 7E18 D5A0 9C32 7F31",
        signature = "Invalid",
        status = VerificationStatus.TAMPERED,
    )
}