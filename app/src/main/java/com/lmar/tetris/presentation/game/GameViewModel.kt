package com.lmar.tetris.presentation.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.lmar.tetris.domain.model.Tetromino
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        return piece.shape.all { (x, y) ->
            val newX = piece.position.first + x + dx
            val newY = piece.position.second + y + dy

            newX in 0..9 && newY in 0..19 && gameState.board.getOrNull(newY)?.getOrNull(newX) == null
        }
    }

    fun moveLeft() {
        val piece = gameState.currentPiece ?: return
        if (canMove(piece, dx = -1, dy = 0)) {
            gameState = gameState.copy(currentPiece = piece.copy(position = piece.position.first - 1 to piece.position.second))
        }
    }

    fun moveRight() {
        val piece = gameState.currentPiece ?: return
        if (canMove(piece, dx = 1, dy = 0)) {
            gameState = gameState.copy(currentPiece = piece.copy(position = piece.position.first + 1 to piece.position.second))
        }
    }

    fun rotate() {
        //rotateWithWallKick()
        rotateWithSRS(clockwise = true) //Rotación Horaria
    }

    //Rotate Normal
    /*fun rotate() {
        val piece = gameState.currentPiece ?: return
        val rotatedShape = piece.shape.map { (x, y) -> -y to x }
        val rotatedPiece = piece.copy(shape = rotatedShape)
        if (canMove(rotatedPiece, 0, 0)) {
            gameState = gameState.copy(currentPiece = rotatedPiece)
        }
    }*/


    //Rotate con Wall Kick
    /* Es una técnica usada en juegos de Tetris donde, si una rotación no es válida
    * por estar muy cerca de una pared (u otra pieza), se intenta rotar de nuevo desplazando
    * la pieza levemente hacia un lado (izquierda o derecha).
    * */
    fun rotateWithWallKick() {
        val piece = gameState.currentPiece ?: return
        val rotatedShape = piece.shape.map { (x, y) -> -y to x }

        // Intento 1: rotar en la misma posición
        var rotatedPiece = piece.copy(shape = rotatedShape)
        if (canMove(rotatedPiece, 0, 0)) {
            gameState = gameState.copy(currentPiece = rotatedPiece)
            return
        }

        // Intento 2: wall kick a la izquierda
        rotatedPiece = rotatedPiece.copy(position = piece.position.first - 1 to piece.position.second)
        if (canMove(rotatedPiece, 0, 0)) {
            gameState = gameState.copy(currentPiece = rotatedPiece)
            return
        }

        // Intento 3: wall kick a la derecha
        rotatedPiece = rotatedPiece.copy(position = piece.position.first + 2 to piece.position.second) // +2 porque ya era -1
        if (canMove(rotatedPiece, 0, 0)) {
            gameState = gameState.copy(currentPiece = rotatedPiece)
        }
    }

    /*
    * El SRS (Super Rotation System) es el sistema de rotación utilizado en los Tetris modernos, como Tetris Guideline o Tetris DS. Es más sofisticado que un simple "wall kick",
    * ya que define desplazamientos específicos (kicks) según:
    * El tipo de pieza (especialmente la "I", que tiene su propia tabla).
    * El tipo de rotación (de 0° a 90°, de 90° a 180°, etc.).
    * El sistema intenta la rotación junto con un conjunto ordenado de desplazamientos.
    * */
    fun rotateWithSRS(clockwise: Boolean = true) {
        val piece = gameState.currentPiece ?: return

        val oldRotation = piece.rotation
        val newRotation = if (clockwise) (oldRotation + 1) % 4 else (oldRotation + 3) % 4

        val rotatedShape = piece.type.shape.map { (x, y) ->
            when (newRotation % 4) {
                0 -> x to y
                1 -> -y to x
                2 -> -x to -y
                3 -> y to -x
                else -> x to y
            }
        }

        val kicks = when (piece.type) {
            Tetromino.I -> srsKicksI[oldRotation]?.get(newRotation)
            else -> srsKicks[oldRotation]?.get(newRotation)
        } ?: listOf(0 to 0)

        for ((dx, dy) in kicks) {
            val newPos = piece.position.first + dx to piece.position.second + dy
            val rotatedPiece = piece.copy(
                shape = rotatedShape,
                position = newPos,
                rotation = newRotation
            )

            if (canMove(rotatedPiece, 0, 0)) {
                gameState = gameState.copy(currentPiece = rotatedPiece)
                return
            }
        }
        // Si ninguna posición es válida, no rota
    }

    fun onDrop() {
        CoroutineScope(Dispatchers.IO).launch {
            drop()
        }
    }

    private suspend fun drop() {
        val piece = gameState.currentPiece ?: return

        if (canMove(piece, dx = 0, dy = 1)) {
            val movedPiece = piece.copy(position = piece.position.first to piece.position.second + 1)
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

    // Para piezas distintas de I y O (T, J, L, S, Z)
    val srsKicks = mapOf(
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
    val srsKicksI = mapOf(
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