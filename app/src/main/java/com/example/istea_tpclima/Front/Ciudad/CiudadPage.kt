package com.example.istea_tpclima.Front.Ciudad

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.istea_tpclima.Front.Router.enrutador
import com.example.istea_tpclima.Infrastructure.Implementations.CiudadService

@Composable
fun CiudadPage(
    navHostController:  NavHostController,
    onCiudadSeleccionada: () -> Unit = {}
) {
    val viewModel : CiudadesViewModel = viewModel(
        factory = CiudadesViewModelFactory(
            repositorio = CiudadService(),
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
