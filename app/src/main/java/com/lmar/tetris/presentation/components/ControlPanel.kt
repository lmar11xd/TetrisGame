package com.lmar.tetris.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ControlPanel(
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onDownClick: () -> Unit,
    onCenterClick: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, top = 12.dp, end = 16.dp)
    ) {
        val (leftButton, rightButton, centerButton, downButton) = createRefs()

        Button(
            onClick = onLeftClick,
            modifier = Modifier
                .constrainAs(leftButton) {
                    start.linkTo(parent.start)
                    end.linkTo(centerButton.start, margin = 8.dp)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Mover a la Izquierda",
                tint = Color.White
            )
        }

        CircularButton(
            onClick = onCenterClick,
            modifier = Modifier
                .constrainAs(centerButton) {
                    top.linkTo(leftButton.top)
                    bottom.linkTo(leftButton.bottom)
                    start.linkTo(leftButton.end)
                    end.linkTo(rightButton.start)
                }
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Rotar",
                tint = Color.White
            )
        }

        Button(
            onClick = onRightClick,
            modifier = Modifier
                .constrainAs(rightButton) {
                    start.linkTo(centerButton.end, margin = 8.dp)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Mover a la Derecha",
                tint = Color.White
            )
        }

        Button(
            onClick = onDownClick,
            modifier = Modifier
                .constrainAs(downButton) {
                    top.linkTo(leftButton.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Acelerar",
                tint = Color.White
            )
        }
    }
}

@Composable
fun CircularButton(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .size(60.dp),  // Tamaño del botón circular
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