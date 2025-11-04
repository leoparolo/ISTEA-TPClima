package com.example.istea_tpclima.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.istea_tpclima.Front.Clima.ClimaPage
import com.example.istea_tpclima.Front.Ciudad.CiudadesPage

sealed class Screen(val route: String) {
    data object Clima : Screen("clima")
    data object Ciudades : Screen("ciudades")
}

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Screen.Clima.route) {

        composable(Screen.Clima.route) {
            ClimaPage(onCambiarCiudad = { nav.navigate(Screen.Ciudades.route) })
        }

        composable(Screen.Ciudades.route) {
            CiudadesPage(onCiudadSeleccionada = { nav.popBackStack() })
        }
    }
}

