package com.example.istea_tpclima.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.istea_tpclima.Front.Clima.ClimaPage
import com.example.istea_tpclima.Front.Ciudad.CiudadesPage
import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Infrastructure.Storage.Prefs
import kotlinx.coroutines.launch

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val ctx = LocalContext.current
    val prefs = Prefs(ctx)
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Screen.Clima.route
    ) {
        composable(Screen.Clima.route) {
            ClimaPage(
                onCambiarCiudad = { navController.navigate(Screen.Ciudades.route) }
            )
        }

        composable(Screen.Ciudades.route) {
            CiudadesPage(
                onCiudadSeleccionada = { ciudad: CiudadModel ->
                    scope.launch {

                        prefs.guardar(ciudad)
                        navController.navigate(Screen.Clima.route) {
                            popUpTo(Screen.Ciudades.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
