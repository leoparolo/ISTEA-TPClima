package com.example.istea_tpclima.front.ciudad

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.istea_tpclima.front.router.enrutador
import com.example.istea_tpclima.infrastructure.implementations.CiudadRepository
import com.example.istea_tpclima.infrastructure.implementations.LocationRepository
import com.example.istea_tpclima.infrastructure.implementations.PaisRepository

@Composable
fun CiudadPage(
    navHostController:  NavHostController,
    onCiudadSeleccionada: () -> Unit = {}
) {
    val ctx = LocalContext.current
    val viewModel : CiudadesViewModel = viewModel(
        factory = CiudadesViewModelFactory(
            repositorio = CiudadRepository(paisservice = PaisRepository()),
            router = enrutador(navHostController),
            locationRepository = LocationRepository(ctx)
        )
    )
    CiudadesView(
        state = viewModel.uiState,
        onAction = { intencion ->
            viewModel.ejecutar(intencion)
        },
        onCiudadSeleccionada = onCiudadSeleccionada
    )
}
