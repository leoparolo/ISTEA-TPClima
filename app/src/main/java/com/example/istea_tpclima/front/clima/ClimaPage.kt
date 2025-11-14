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
//@Composable
//fun ClimaPage(onCambiarCiudad: () -> Unit) {
//
//
//
//    val ctx = LocalContext.current
//    val vm = remember { ClimaViewModel(repo = ClimaRepository(), prefs = Prefs(ctx)) }
//    val estado by vm.estado.collectAsState()
//
//    LaunchedEffect(Unit) { vm.procesar(ClimaIntencion.CargarPorCiudadGuardada) }

//    when (val st = estado) {
//        ClimaEstado.Cargando -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
//
//        is ClimaEstado.SinCiudadGuardada -> Box(Modifier.fillMaxSize(), Alignment.Center) {
//            Button(onClick = onCambiarCiudad) { Text("Elegir ciudad") }
//        }
//
//        is ClimaEstado.Error -> Column(
//            Modifier.fillMaxSize().padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(st.mensaje)
//            Spacer(Modifier.height(12.dp))
//            Button(onClick = { vm.procesar(ClimaIntencion.LimpiarError); vm.procesar(ClimaIntencion.Refrescar) }) {
//                Text("Reintentar")
//            }
//            Spacer(Modifier.height(12.dp))
//            Button(onClick = onCambiarCiudad) { Text("Cambiar ciudad") }
//        }

//        is ClimaEstado.Mostrando -> {
//            val data = st.data
//            Column(
//                Modifier.fillMaxSize().padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(text = data.ciudad.name, style = MaterialTheme.typography.headlineSmall)
//                Spacer(Modifier.height(8.dp))
//                Text("${data.actual.tempC} °C")
//                Text(data.actual.descripcion ?: "—")
//                Text("Humedad: ${data.actual.humedad}%")
//                Spacer(Modifier.height(16.dp))
//                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//                    Button(onClick = { vm.procesar(ClimaIntencion.Refrescar) }) { Text("Actualizar") }
//                    Button(onClick = onCambiarCiudad) { Text("Cambiar ciudad") }
//                }
//                Spacer(Modifier.height(24.dp))
//                data.proximos5Dias.forEach { d -> Text("${d.dia}: ${d.minC}° / ${d.maxC}°") }
//            }
//        }
//    }
//}