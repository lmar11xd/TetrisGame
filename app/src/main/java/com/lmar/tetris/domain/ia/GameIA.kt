package com.lmar.tetris.domain.ia

class GameIA {

    companion object {
        private const val TAG = "GameIA"

        // Para piezas distintas de I y O (T, J, L, S, Z)
        private val  SRS_KICKS = mapOf(
            0 to mapOf(1 to listOf(0 to 0, -1 to 0, -1 to 1, 0 to -2, -1 to -2)), // 0 -> R
            1 to mapOf(0 to listOf(0 to 0, 1 to 0, 1 to -1, 0 to 2, 1 to 2)),     // R -> 0
            1 to mapOf(2 to listOf(0 to 0, 1 to 0, 1 to -1, 0 to 2, 1 to 2)),     // R -> 2
            2 to mapOf(1 to listOf(0 to 0, -1 to 0, -1 to 1, 0 to -2, -1 to -2)), // 2 -> R
            2 to mapOf(3 to listOf(0 to 0, 1 to 0, 1 to 1, 0 to -2, 1 to -2)),    // 2 -> L
            3 to mapOf(2 to listOf(0 to 0, -1 to 0, -1 to -1, 0 to 2, -1 to 2)),  // L -> 2
            3 to mapOf(0 to listOf(0 to 0, -1 to 0, -1 to -1, 0 to 2, -1 to 2)),  // L -> 0
            0 to mapOf(3 to listOf(0 to 0, 1 to 0, 1 to 1, 0 to -2, 1 to -2))     // 0 -> L
        )

        // Para pieza I
        private val SRS_KICKS_I = mapOf(
            0 to mapOf(1 to listOf(0 to 0, -2 to 0, 1 to 0, -2 to -1, 1 to 2)),    // 0 -> R
            1 to mapOf(0 to listOf(0 to 0, 2 to 0, -1 to 0, 2 to 1, -1 to -2)),   // R -> 0
            1 to mapOf(2 to listOf(0 to 0, -1 to 0, 2 to 0, -1 to 2, 2 to -1)),   // R -> 2
            2 to mapOf(1 to listOf(0 to 0, 1 to 0, -2 to 0, 1 to -2, -2 to 1)),   // 2 -> R
            2 to mapOf(3 to listOf(0 to 0, 2 to 0, -1 to 0, 2 to 1, -1 to -2)),   // 2 -> L
            3 to mapOf(2 to listOf(0 to 0, -2 to 0, 1 to 0, -2 to -1, 1 to 2)),   // L -> 2
            3 to mapOf(0 to listOf(0 to 0, 1 to 0, -2 to 0, 1 to -2, -2 to 1)),   // L -> 0
            0 to mapOf(3 to listOf(0 to 0, -1 to 0, 2 to 0, -1 to 2, 2 to -1))    // 0 -> L
        )

    }

}