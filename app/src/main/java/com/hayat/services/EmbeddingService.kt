package com.hayat.services

import android.util.Log
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File
import java.util.*
import kotlin.math.sqrt

/**
 * EmbeddingService handles text normalization, tokenization, and inference
 * using the e5-small-v2 ONNX model for on-device vector generation.
 *
 * @param modelFile The ONNX model file, typically resolved from app-specific storage.
 */
class EmbeddingService(private val modelFile: File) : AutoCloseable {
    private val TAG = "EmbeddingService"

    private var ortEnv: OrtEnvironment = OrtEnvironment.getEnvironment()
    private var ortSession: OrtSession? = null

    init {
        loadModel()
    }

    private fun loadModel() {
        try {
            if (modelFile.exists()) {
                val options = OrtSession.SessionOptions()
                // Optimize for mobile execution
                options.setIntraOpNumThreads(2)
                ortSession = ortEnv.createSession(modelFile.absolutePath, options)
                Log.d(TAG, "ONNX Model loaded successfully from ${modelFile.absolutePath}")
            } else {
                Log.e(TAG, "Model file not found at ${modelFile.absolutePath}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading ONNX model: ${e.message}")
        }
    }

    /**
     * Preprocessing: Cleans user input by lowercasing and removing special characters.
     */
    fun normalizeText(input: String): String {
        return input.lowercase(Locale.ROOT)
            .replace(Regex("[^a-z0-9\\s]"), "")
            .trim()
    }

    /**
     * Simple Tokenizer: Prepares the input string for the model.
     * Note: E5 models typically require a "query: " prefix for asymmetric retrieval.
     */
    private fun tokenize(text: String): LongArray {
        // e5-small-v2 uses BERT-style tokenization (WordPiece).
        // This is a simplified version for demonstration as per requirements.
        val processedText = "query: " + normalizeText(text)

        // Dummy tokenization logic: converting chars to longs as a placeholder.
        // A production app would use a proper WordPiece tokenizer with a vocab file.
        return processedText.split(" ").flatMap { word ->
            word.map { it.toLong() }
        }.toLongArray()
    }

    /**
     * Generates an embedding vector for the given input string.
     * Performs inference on a background thread with a 500ms timeout.
     */
    suspend fun generateEmbedding(input: String): FloatArray? = withContext(Dispatchers.Default) {
        withTimeoutOrNull(500L) {
            try {
                val session = ortSession ?: run {
                    Log.e(TAG, "Inference failed: ONNX Session is null")
                    return@withTimeoutOrNull null
                }

                val tokens = tokenize(input)
                val shape = longArrayOf(1, tokens.size.toLong())

                // Prepare input tensors
                val inputTensor = OnnxTensor.createTensor(ortEnv, java.nio.LongBuffer.wrap(tokens), shape)
                val attentionMask = LongArray(tokens.size) { 1L }
                val attentionMaskTensor = OnnxTensor.createTensor(ortEnv, java.nio.LongBuffer.wrap(attentionMask), shape)

                val inputs = mapOf(
                    "input_ids" to inputTensor,
                    "attention_mask" to attentionMaskTensor
                )

                // Run inference
                try {
                    session.run(inputs).use { results ->
                        if (results.size() == 0) return@use null

                        val outputTensor = results.get(0).value

                        // Handle potential nested output arrays depending on model export format
                        val rawOutput = when (outputTensor) {
                            is Array<*> -> {
                                if (outputTensor.isNotEmpty() && outputTensor[0] is FloatArray) {
                                    outputTensor[0] as FloatArray
                                } else if (outputTensor.isNotEmpty() && outputTensor[0] is Array<*>) {
                                    (outputTensor[0] as Array<*>)[0] as? FloatArray
                                } else null
                            }
                            else -> null
                        }

                        rawOutput?.let { normalizeL2(it) }
                    }
                } finally {
                    inputTensor.close()
                    attentionMaskTensor.close()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Inference error: ${e.message}")
                null
            }
        } ?: run {
            Log.e(TAG, "Embedding generation timed out or failed. Fallingback to System Busy.")
            null
        }
    }

    /**
     * Normalizes the FloatArray to unit length (L2 normalization) for accurate cosine similarity.
     */
    private fun normalizeL2(vector: FloatArray): FloatArray {
        var sumSquared = 0.0
        for (value in vector) {
            sumSquared += value.toDouble() * value
        }
        val magnitude = sqrt(sumSquared).toFloat()

        return if (magnitude > 1e-9f) {
            FloatArray(vector.size) { i -> vector[i] / magnitude }
        } else {
            vector
        }
    }

    override fun close() {
        try {
            ortSession?.close()
            ortEnv.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing ONNX resources: ${e.message}")
        }
    }
}
