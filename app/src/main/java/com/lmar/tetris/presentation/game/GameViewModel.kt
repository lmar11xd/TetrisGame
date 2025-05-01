package com.lmar.tetris.presentation.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.lmar.tetris.domain.model.Tetromino
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class GameViewModel  {//@Inject constructor(): ViewModel()
    var gameState by mutableStateOf(initialGameState())
        private set

    private fun initialGameState(): GameState {
        val board = Array(20) { arrayOfNulls<Color>(10) }
        return GameState(board = board, currentPiece = spawnPiece())
    }

    private fun spawnPiece(): TetrominoInstance {
        val piece = Tetromino.entries.random()
        return TetrominoInstance(piece, 4 to 0)
    }

    private fun canMove(piece: TetrominoInstance, dx: Int, dy: Int, rotated: Boolean = false): Boolean {
        val shape = if(rotated)
            piece.type.shape.map { -it.second to it.first } //Rotacion 90°
        else
            piece.type.shape

        return shape.all { (x, y) ->
            val newX = piece.position.first + x + dx
            val newY = piece.position.second + y + dy

            newX in 0..9 && newY in 0..19 && gameState.board.getOrNull(newY)?.getOrNull(newX) == null
        }
    }

    fun moveLeft() {
        val piece = gameState.currentPiece ?: return
        val newPiece = piece.copy(position = piece.position.first - 1 to piece.position.second)
        if (canMove(newPiece, 0, 0)) {
            gameState = gameState.copy(currentPiece = newPiece)
        }
    }

    fun moveRight() {
        val piece = gameState.currentPiece ?: return
        val newPiece = piece.copy(position = piece.position.first + 1 to piece.position.second)
        if (canMove(newPiece, 0, 0)) {
            gameState = gameState.copy(currentPiece = newPiece)
        }
    }

    fun rotate() {
        val piece = gameState.currentPiece ?: return
        val rotatedShape = piece.shape.map { (x, y) -> -y to x }
        val rotatedPiece = piece.copy(shape = rotatedShape)
        if (canMove(rotatedPiece, 0, 0)) {
            gameState = gameState.copy(currentPiece = rotatedPiece)
        }
    }

    fun onDrop() {
        CoroutineScope(Dispatchers.IO).launch {
            drop()
        }
    }

    private suspend fun drop() {
        val piece = gameState.currentPiece ?: return

        val newPosition = piece.position.first to piece.position.second + 1
        val movedPiece = piece.copy(position = newPosition)

        if (canMove(movedPiece, 0, 0)) {
            gameState = gameState.copy(currentPiece = movedPiece)
        } else {
            // Fijar pieza en el tablero
            for ((x, y) in piece.shape) {
                val px = piece.position.first + x
                val py = piece.position.second + y
                if (px in 0..9 && py in 0..19) {
                    gameState.board[py][px] = piece.type.color
                }
            }

            clearLines()

            val newPiece = spawnPiece()
            gameState = if (!canMove(newPiece, 0, 0)) {
                gameState.copy(isGameOver = true)
            } else {
                gameState.copy(currentPiece = newPiece)
            }
        }
    }

    private suspend fun clearLines() {
        val fullRows = gameState.board.withIndex().filter { it.value.all { color -> color != null } }.map { it.index }
        if (fullRows.isNotEmpty()) {
            gameState = gameState.copy(clearingRows = fullRows.toSet())

            delay(300) // Pausa breve para mostrar animación

            val newBoard = gameState.board.filterIndexed { index, _ -> index !in fullRows }.toMutableList()
            repeat(fullRows.size) {
                newBoard.add(0, arrayOfNulls<Color>(10))
            }

            val newScore = gameState.score + fullRows.size * 100
            val newLevel = (newScore / 500) + 1
            gameState = gameState.copy(
                board = newBoard.toTypedArray(),
                score = newScore,
                level = newLevel,
                clearingRows = emptySet()
            )
        }
    }

    fun restart() {
        gameState = initialGameState()
    }
}