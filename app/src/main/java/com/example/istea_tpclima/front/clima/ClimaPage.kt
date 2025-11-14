package com.example.istea_tpclima.front.clima

import androidx.compose.ui.platform.LocalContext
import com.example.istea_tpclima.infrastructure.implementations.ClimaRepository
import com.example.istea_tpclima.infrastructure.storage.Prefs
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.istea_tpclima.core.features.clima.ClimaIntencion

@Composable
fun ClimaPage(

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
    ClimaView(
        state = viewModel.uiState,
        onAction = {intencion ->
            viewModel.ejecutar(intencion)
        }
    )
}