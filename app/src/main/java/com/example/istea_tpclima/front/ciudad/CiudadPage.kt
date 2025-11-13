package com.example.istea_tpclima.front.ciudad

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.istea_tpclima.front.router.enrutador
import com.example.istea_tpclima.infrastructure.implementations.CiudadService
import com.example.istea_tpclima.infrastructure.implementations.PaisService
import com.example.istea_tpclima.infrastructure.location.LocationService
import com.example.istea_tpclima.infrastructure.storage.Prefs

@Composable
fun CiudadPage(
    navHostController: NavHostController
) {
    val ctx = LocalContext.current

    val viewModel: CiudadesViewModel = viewModel(
        factory = CiudadesViewModelFactory(
            repositorio = CiudadService(paisservice = PaisService()),
            router = enrutador(navHostController),
            locationService = LocationService(ctx),
            prefs = Prefs(ctx)
        )
    )

    CiudadesView(
        state = viewModel.uiState,
        onAction = { intencion -> viewModel.ejecutar(intencion) }
    )
}
