package com.lmar.tetris.presentation.game

import androidx.compose.ui.graphics.Color
import com.lmar.tetris.domain.model.Tetromino

data class GameState(
    val board: Array<Array<Color?>>,
    val currentPiece: TetrominoInstance?,
    val score: Int = 0,
    val level: Int = 1,
    val isGameOver: Boolean = false,
    val clearingRows: Set<Int> = emptySet()
)

data class TetrominoInstance(
    val type: Tetromino,
    val position: Pair<Int, Int>, // Posicion de referencia
    val shape: List<Pair<Int, Int>> = type.shape
)