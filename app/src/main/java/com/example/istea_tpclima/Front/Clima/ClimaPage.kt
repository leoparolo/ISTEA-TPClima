package com.example.istea_tpclima.Front.Clima

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.istea_tpclima.Core.Features.Clima.ClimaEstado
import com.example.istea_tpclima.Core.Features.Clima.ClimaIntencion
import com.example.istea_tpclima.Infrastructure.Implementations.ClimaService
import com.example.istea_tpclima.Infrastructure.Storage.Prefs

@Composable
fun ClimaPage(onCambiarCiudad: () -> Unit) {
    val ctx = LocalContext.current
    val vm = remember { ClimaViewModel(repo = ClimaService(), prefs = Prefs(ctx)) }
    val estado by vm.estado.collectAsState()

    LaunchedEffect(Unit) { vm.procesar(ClimaIntencion.CargarPorCiudadGuardada) }

    when (val st = estado) {
        ClimaEstado.Cargando -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

        is ClimaEstado.SinCiudadGuardada -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Button(onClick = onCambiarCiudad) { Text("Elegir ciudad") }
        }

        is ClimaEstado.Error -> Column(
            Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(st.mensaje)
            Spacer(Modifier.height(12.dp))
            Button(onClick = { vm.procesar(ClimaIntencion.LimpiarError); vm.procesar(ClimaIntencion.Refrescar) }) {
                Text("Reintentar")
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onCambiarCiudad) { Text("Cambiar ciudad") }
        }

        is ClimaEstado.Mostrando -> {
            val data = st.data
            Column(
                Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = data.ciudad.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text("${data.actual.tempC} °C")
                Text(data.actual.descripcion ?: "—")
                Text("Humedad: ${data.actual.humedad}%")
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { vm.procesar(ClimaIntencion.Refrescar) }) { Text("Actualizar") }
                    Button(onClick = onCambiarCiudad) { Text("Cambiar ciudad") }
                }
                Spacer(Modifier.height(24.dp))
                data.proximos5Dias.forEach { d -> Text("${d.dia}: ${d.minC}° / ${d.maxC}°") }
            }
        }
    }
}