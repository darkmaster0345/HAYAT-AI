package com.hayat.data

import kotlin.math.sqrt

class VectorSearchEngine(private val dao: MedicalChunkDao) {

    /**
     * Performs a Cosine Similarity calculation between the queryEmbedding
     * and all embedding arrays in the database.
     */
    suspend fun findRelevantChunks(queryEmbedding: FloatArray): List<MedicalChunk> {
        val allChunks = dao.getAllChunks()
        return allChunks.filter { chunk ->
            val score = calculateCosineSimilarity(queryEmbedding, chunk.embedding)
            isResultValid(score)
        }.sortedByDescending { chunk ->
            calculateCosineSimilarity(queryEmbedding, chunk.embedding)
        }
    }

    /**
     * Returns true only if score > 0.82.
     */
    fun isResultValid(score: Float): Boolean {
        return score > 0.82f
    }

    /**
     * Refusal message to be shown if no valid results are found.
     */
    fun getRefusalMessage(): String {
        return "I'm sorry, but I couldn't find any relevant medical protocols to answer your request reliably."
    }

    private fun calculateCosineSimilarity(vectorA: FloatArray, vectorB: FloatArray): Float {
        if (vectorA.size != vectorB.size) return 0f

        var dotProduct = 0.0
        var normA = 0.0
        var normB = 0.0

        for (i in vectorA.indices) {
            dotProduct += vectorA[i].toDouble() * vectorB[i].toDouble()
            normA += vectorA[i].toDouble() * vectorA[i].toDouble()
            normB += vectorB[i].toDouble() * vectorB[i].toDouble()
        }

        val denominator = sqrt(normA) * sqrt(normB)
        return if (denominator > 0) (dotProduct / denominator).toFloat() else 0f
    }
}
