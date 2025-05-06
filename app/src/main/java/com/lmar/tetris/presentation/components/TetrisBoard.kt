package com.lmar.tetris.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lmar.tetris.presentation.game.GameState


@Composable
fun TetrisBoard(gameState: GameState, modifier: Modifier = Modifier, cellSize: Dp = 24.dp) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.LightGray)
                .border(1.dp, Color.DarkGray)
        ) {
            Column {
                for (y in 0..19) {
                    val isClearing = y in gameState.clearingRows
                    val alpha = if (isClearing) 0.3f else 1f

                    Row {
                        for (x in 0..9) {
                            val color = gameState.board[y][x]
                            val isPieceBlock = gameState.currentPiece?.shape?.any { (bx, by) ->
                                val px = gameState.currentPiece.position.first + bx
                                val py = gameState.currentPiece.position.second + by
                                px == x && py == y
                            } == true

                            val blockColor = when {
                                isPieceBlock -> gameState.currentPiece.type.color
                                color != null -> color
                                else -> Color.LightGray
                            }

                            Box(
                                modifier = Modifier
                                    .size(cellSize)
                                    .background(blockColor.copy(alpha = alpha))
                                    .border(0.5.dp, Color.Black)
                            )
                        }
                    }
                }
            }
        }
    }
}
