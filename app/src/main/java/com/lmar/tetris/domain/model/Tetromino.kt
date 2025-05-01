package com.lmar.tetris.domain.model

import androidx.compose.ui.graphics.Color

enum class Tetromino(val shape: List<Pair<Int, Int>>, val color: Color) {
    I(listOf(0 to 1, 1 to 1, 2 to 1, 3 to 1), Color.Cyan),
    O(listOf(0 to 0, 1 to 0, 0 to 1, 1 to 1), Color.Yellow),
    T(listOf(1 to 0, 0 to 1, 1 to 1, 2 to 1), Color.Magenta),
    S(listOf(1 to 0, 2 to 0, 0 to 1, 1 to 1), Color.Green),
    Z(listOf(0 to 0, 1 to 0, 1 to 1, 2 to 1), Color.Red),
    J(listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1), Color.Blue),
    L(listOf(2 to 0, 0 to 1, 1 to 1, 2 to 1), Color(1f, 0.5f, 0f))

}