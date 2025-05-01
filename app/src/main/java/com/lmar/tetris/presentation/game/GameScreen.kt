package com.lmar.tetris.presentation.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun GameScreen() {
    val game = remember { GameViewModel() }
    val gameState = game.gameState

    LaunchedEffect(key1 = gameState.level) {
        while (!gameState.isGameOver) {
            delay((800L - (gameState.level - 1) * 60L).coerceAtLeast(200L))
            game.onDrop()
        }
    }

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues)
                .onKeyEvent {
                    if (it.type == KeyEventType.KeyDown) {
                        when (it.key) {
                            Key.DirectionLeft -> {
                                game.moveLeft()
                                true
                            }
                            Key.DirectionRight -> {
                                game.moveRight()
                                true
                            }
                            Key.DirectionDown -> { game.onDrop(); true }
                            Key.DirectionUp -> { game.rotate(); true }
                            else -> false
                        }
                    } else false
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Juego de Tetris")

            BasicText(text = "Score: ${gameState.score} Level: ${gameState.level}")

            Spacer(modifier = Modifier.size(16.dp))

            TetrisBoard(gameState)

            if(gameState.isGameOver) {
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = { game.restart() }) {
                    BasicText("Restart")
                }
            }
        }
    }

}

@Composable
fun TetrisBoard(gameState: GameState) {
    Box(
        modifier = Modifier
            .background(Color.DarkGray)
            .border(2.dp, Color.Black)
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
                                .size(24.dp)
                                .background(blockColor.copy(alpha = alpha))
                                .border(0.5.dp, Color.Black)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun GameScreenPreview() {
    GameScreen()
}