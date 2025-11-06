package com.example.istea_tpclima

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.istea_tpclima.Front.Ciudad.CiudadPage
import com.example.istea_tpclima.Front.Clima.ClimaPage
import com.example.istea_tpclima.Front.Router.Ruta

sealed class Screen(val route: String) {
    data object Clima : Screen("clima")
    data object Ciudades : Screen("ciudades")
}
@Composable
fun MainPage() {
    val navHostController = rememberNavController()
    NavHost(navController = navHostController,
        startDestination = Screen.Clima.route)
    {
        composable(Screen.Clima.route) {
            ClimaPage(onCambiarCiudad = { navHostController.navigate(Screen.Ciudades.route) })
        }

        composable(Screen.Ciudades.route) {
            CiudadPage(onCiudadSeleccionada = { navHostController.popBackStack() },
                navHostController = navHostController)
        }
    }
}