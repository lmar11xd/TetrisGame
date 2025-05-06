package com.lmar.tetris.presentation.game

import android.annotation.SuppressLint
import android.content.res.Configuration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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

@SuppressLint("UnusedBoxWithConstraintsScope", "ConfigurationScreenWidthHeight")
@Composable
fun GameScreen(
    navController: NavController
) {
    val game = remember { GameViewModel() }
    val gameState = game.gameState

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(key1 = gameState.level) {
        while (!gameState.isGameOver) {
            if (!game.isPaused) {
                game.onDrop()
            }
            delay((800L - (gameState.level - 1) * 60L).coerceAtLeast(200L))
        }
   }

    Scaffold { paddingValues ->
        if(isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(paddingValues),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Estadisticas
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .weight(2f),
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

                    if (!gameState.isGameOver) {
                        if (game.isPaused) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { game.resumeGame() }) {
                                BasicText("Continuar", style = TextStyle(color = Color.White))
                            }
                        } else {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { game.pauseGame() }) {
                                BasicText("Pausar", style = TextStyle(color = Color.White))
                            }
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.navigate(Screen.Home.route) }) {
                            BasicText("Salir", style = TextStyle(color = Color.White))
                        }
                    } else {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { game.restart() }) {
                            BasicText("Nuevo Juego", style = TextStyle(color = Color.White))
                        }
                    }
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .weight(3f)
                        .padding(8.dp)
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
                        modifier = Modifier.weight(2f),
                        onLeftClick = { game.moveLeft() },
                        onRightClick = { game.moveRight() },
                        onDownClick = { game.onDrop() },
                        onCenterClick = { game.rotate() }
                    )
                }
            }
        } else {
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
                                    BasicText("Continuar", style = TextStyle(color = Color.White))
                                }
                            } else {
                                Button(
                                    modifier = Modifier.width(120.dp),
                                    onClick = { game.pauseGame() }) {
                                    BasicText("Pausar", style = TextStyle(color = Color.White))
                                }
                            }

                            Button(
                                modifier = Modifier.width(120.dp),
                                onClick = { navController.navigate(Screen.Home.route) }) {
                                BasicText("Salir", style = TextStyle(color = Color.White))
                            }
                        } else {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { game.restart() }) {
                                BasicText("Nuevo Juego", style = TextStyle(color = Color.White))
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
}

@Composable
@Preview(showBackground = true)
private fun GameScreenPreview() {
    GameScreen(rememberNavController())
}