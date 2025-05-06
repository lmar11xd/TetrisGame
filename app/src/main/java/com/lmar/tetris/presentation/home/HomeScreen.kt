package com.lmar.tetris.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lmar.tetris.R
import com.lmar.tetris.app.navigation.Screen
import com.lmar.tetris.presentation.components.GlowingCard
import com.lmar.tetris.presentation.components.ShadowText

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShadowText(
                text = stringResource(R.string.app_name),
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily!!,
                fontSize = 32.sp,
                textColor = MaterialTheme.colorScheme.primary,
                shadowColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            GlowingCard(
                modifier = Modifier
                    .size(100.dp)
                    .padding(5.dp),
                glowingColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primary,
                cornerRadius = Int.MAX_VALUE.dp
            ) {
                Image(
                    painter = painterResource(R.drawable.tetris),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
            }

            Spacer(modifier = Modifier.size(32.dp))

            Button(
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                onClick = { navController.navigate(Screen.Game.route) }
            ) {
                Text("Iniciar Juego")
            }

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                modifier = Modifier.width(200.dp),
                onClick = { /* agregar acción si quieres mostrar instrucciones */ }
            ) {
                Text("Instrucciones")
            }
        }
    }
}