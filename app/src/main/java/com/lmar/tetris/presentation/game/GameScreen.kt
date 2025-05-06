package com.lmar.tetris.presentation.game

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lmar.tetris.R
import com.lmar.tetris.app.navigation.Screen
import com.lmar.tetris.presentation.components.ControlPanel
import com.lmar.tetris.presentation.components.ShadowText
import com.lmar.tetris.presentation.components.TetrisBoard
import kotlinx.coroutines.delay

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameScreen(
    navController: NavController
) {
    val game = remember { GameViewModel() }
    val gameState = game.gameState

    LaunchedEffect(key1 = gameState.level) {
        while (!gameState.isGameOver) {
            if (!game.isPaused) {
                game.onDrop()
            }
            delay((800L - (gameState.level - 1) * 60L).coerceAtLeast(200L))
        }
   }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Estadisticas
            Column(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShadowText(
                    text = stringResource(R.string.app_name),
                    fontFamily = MaterialTheme.typography.displayLarge.fontFamily!!,
                    fontSize = 32.sp,
                    textColor = MaterialTheme.colorScheme.primary,
                    shadowColor = MaterialTheme.colorScheme.primary
                )

                BasicText(text = "Score: ${gameState.score} Level: ${gameState.level}")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (!gameState.isGameOver) {
                        if (game.isPaused) {
                            Button(
                                modifier = Modifier.width(120.dp),
                                onClick = { game.resumeGame() }) {
                                BasicText("Continuar")
                            }
                        } else {
                            Button(
                                modifier = Modifier.width(120.dp),
                                onClick = { game.pauseGame() }) {
                                BasicText("Pausar")
                            }
                        }

                        Button(
                            modifier = Modifier.width(120.dp),
                            onClick = { navController.navigate(Screen.Home.route) }) {
                            BasicText("Salir")
                        }
                    } else {
                        Button(
                            modifier = Modifier.width(120.dp),
                            onClick = { game.restart() }) {
                            BasicText("Nuevo Juego")
                        }
                    }
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .weight(1f) // ocupa el espacio vertical disponible
            ) {
                val boardHeight = 20
                val boardWidth = 10

                val maxCellHeight = maxHeight / boardHeight
                val maxCellWidth = maxWidth / boardWidth
                val cellSize = minOf(maxCellHeight, maxCellWidth) // evita que se desborde horizontalmente

                TetrisBoard(
                    gameState = gameState,
                    cellSize = cellSize
                )
            }

            //Panel de Control
            if (!game.isPaused && !gameState.isGameOver) {
                ControlPanel(
                    onLeftClick = { game.moveLeft() },
                    onRightClick = { game.moveRight() },
                    onDownClick = { game.onDrop() },
                    onCenterClick = { game.rotate() }
                )
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
private fun GameScreenPreview() {
    GameScreen(rememberNavController())
}