package com.example.istea_tpclima.front.ciudad

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.istea_tpclima.front.router.enrutador
import com.example.istea_tpclima.infrastructure.implementations.CiudadService
import com.example.istea_tpclima.infrastructure.implementations.PaisService

@Composable
fun CiudadPage(
    navHostController:  NavHostController,
    onCiudadSeleccionada: () -> Unit = {}
) {
    val viewModel : CiudadesViewModel = viewModel(
        factory = CiudadesViewModelFactory(
            repositorio = CiudadService(paisservice = PaisService()),
            router = enrutador(navHostController)
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
