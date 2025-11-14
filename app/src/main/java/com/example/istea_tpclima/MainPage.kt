package com.example.istea_tpclima

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.istea_tpclima.front.ciudad.CiudadPage
import com.example.istea_tpclima.front.clima.ClimaPage
import com.example.istea_tpclima.front.router.BarraNavegacion

sealed class Screen(val route: String) {
    data object Clima : Screen("clima")
    data object Ciudades : Screen("ciudades")
}
@Composable
fun MainPage(modifier : Modifier) {
    val navHostController = rememberNavController()
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Screen.Clima.route

    val backgroundRes = when (currentRoute) {
        Screen.Clima.route -> R.drawable.daywide
        Screen.Ciudades.route -> R.drawable.modern_city
        else -> R.drawable.modern_city
    }
    Crossfade(targetState = backgroundRes, label = "backgroundTransition") { res ->
        backgroundImage(modifier, res)
    }
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BarraNavegacion(
                currentRoute = currentRoute,
                onNavigate = { ruta ->
                    if (ruta != currentRoute) {
                        navHostController.navigate(ruta) {
                            popUpTo(Screen.Clima.route)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navHostController,
            startDestination = Screen.Clima.route,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {
            composable(Screen.Clima.route) {
                ClimaPage()
            }

            composable(Screen.Ciudades.route) {
                CiudadPage(
                    navHostController = navHostController
                )
            }
        }
    }
}
@Composable
fun backgroundImage(modifier: Modifier,imageRes: Int)
{
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize()
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    )
}