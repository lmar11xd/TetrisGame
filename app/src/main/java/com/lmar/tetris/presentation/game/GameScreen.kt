package com.lmar.tetris.presentation.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lmar.tetris.presentation.components.ControlPanel
import com.lmar.tetris.presentation.components.TetrisBoard
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    navController: NavController
) {
    val game = remember { GameViewModel() }
    val gameState = game.gameState

    LaunchedEffect(key1 = gameState.level) {
        while (!gameState.isGameOver) {
            delay((800L - (gameState.level - 1) * 60L).coerceAtLeast(200L))
            game.onDrop()
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
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Juego de Tetris")

                BasicText(text = "Score: ${gameState.score} Level: ${gameState.level}")

                if(gameState.isGameOver) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(onClick = { game.restart() }) {
                        BasicText("Restart")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            TetrisBoard(gameState)

            //Panel de Control
            ControlPanel(
                onLeftClick = { game.moveLeft() },
                onRightClick = { game.moveRight() },
                onDownClick = { game.onDrop() },
                onCenterClick = { game.rotate() }
            )
        }
    }

}

@Composable
@Preview(showBackground = true)
private fun GameScreenPreview() {
    GameScreen(rememberNavController())
}