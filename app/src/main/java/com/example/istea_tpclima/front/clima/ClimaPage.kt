package com.example.istea_tpclima.front.clima

import androidx.compose.ui.platform.LocalContext
import com.example.istea_tpclima.infrastructure.implementations.ClimaRepository
import com.example.istea_tpclima.infrastructure.storage.Prefs
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.istea_tpclima.Screen
import com.example.istea_tpclima.core.features.clima.ClimaEstado
import com.example.istea_tpclima.core.features.clima.ClimaIntencion

@Composable
fun ClimaPage(
    navController: NavHostController
){
    val ctx = LocalContext.current
    val viewModel : ClimaViewModel = viewModel(
        factory = ClimaViewModelFactory(
            repositorio = ClimaRepository(),
            prefs = Prefs(ctx)
        )
    )

    LaunchedEffect(Unit) {
        viewModel.ejecutar(ClimaIntencion.CargarPorCiudadGuardada)
    }
    LaunchedEffect(viewModel.uiState) {
        if (viewModel.uiState is ClimaEstado.SinCiudadGuardada) {
            navController.navigate(Screen.Ciudades.route) {
                popUpTo(Screen.Clima.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    ClimaView(
        state = viewModel.uiState,
        onAction = {intencion ->
            viewModel.ejecutar(intencion)
        }
    )
}