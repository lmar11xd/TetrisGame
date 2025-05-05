package com.lmar.tetris.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ControlPanel(
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onDownClick: () -> Unit,
    onCenterClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón a la izquierda
                Button(
                    onClick = onLeftClick,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .width(90.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }

                // Botón circular en el centro
                CircularButton(onClick = onCenterClick, modifier = Modifier.align(Alignment.Center)) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }

                // Botón a la derecha
                Button(
                    onClick = onRightClick,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .width(90.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón hacia abajo
                Button(
                    onClick = onDownClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(90.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CircularButton(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .size(64.dp),  // Tamaño del botón circular
        contentPadding = PaddingValues(0.dp)
    ) {
        content()
    }
}


@Preview(showBackground = true)
@Composable
private fun ControlPanelPreview() {
    ControlPanel(
        onLeftClick = {  },
        onRightClick = {  },
        onDownClick = {  },
        onCenterClick = {  }
    )
}