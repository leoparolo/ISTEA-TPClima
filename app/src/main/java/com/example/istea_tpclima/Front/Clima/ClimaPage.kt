package com.example.istea_tpclima.Front.Clima

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.istea_tpclima.Core.Features.Clima.ClimaEstado
import com.example.istea_tpclima.Core.Features.Clima.ClimaIntencion
import com.example.istea_tpclima.Core.Features.Clima.ClimaViewModel
import com.example.istea_tpclima.Core.Modelos.ClimaDia
import com.example.istea_tpclima.Infrastructure.Repositories.Clima.ClimaRepositoryKtor
import com.example.istea_tpclima.Infrastructure.Storage.Prefs

@Composable
fun ClimaPage(
    onCambiarCiudad: () -> Unit
) {
    val ctx = LocalContext.current
    val vm = remember { ClimaViewModel(ClimaRepositoryKtor(), Prefs(ctx)) }
    val estado by vm.estado.collectAsState()

    LaunchedEffect(Unit) {
        vm.procesar(ClimaIntencion.CargarPorCiudadGuardada)
    }

    when (val st = estado) {
        ClimaEstado.Cargando -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        ClimaEstado.SinCiudadGuardada -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = onCambiarCiudad) { Text("Elegir ciudad") }
        }

        is ClimaEstado.Error -> Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(st.mensaje, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { vm.procesar(ClimaIntencion.CargarPorCiudadGuardada) }) {
                Text("Reintentar")
            }
        }

        is ClimaEstado.Mostrando -> PantallaClima(
            ui = st,
            onRefrescar = { vm.procesar(ClimaIntencion.Refrescar) },
            onCambiarCiudad = onCambiarCiudad,
            onCompartir = {
                val d = st.data
                val share = "Clima en ${d.ciudad.nombre}, ${d.ciudad.pais}: " +
                        "${d.actual.tempC}°C, ${d.actual.descripcion}, " +
                        "Humedad ${d.actual.humedad}%"
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, share)
                }
                ctx.startActivity(Intent.createChooser(intent, "Compartir pronóstico"))
            }
        )
    }
}

@Composable
private fun PantallaClima(
    ui: ClimaEstado.Mostrando,
    onRefrescar: () -> Unit,
    onCambiarCiudad: () -> Unit,
    onCompartir: () -> Unit
) {
    val d = ui.data
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${d.actual.tempC}°C", style = MaterialTheme.typography.displayMedium)
            Column(horizontalAlignment = Alignment.End) {
                Text("${d.ciudad.nombre} - ${d.ciudad.pais}")
                Text(d.actual.descripcion)
                Text("Humedad ${d.actual.humedad}%")
            }
        }
        Spacer(Modifier.height(12.dp))
        Row {
            OutlinedButton(onClick = onCambiarCiudad) { Text("Cambiar ciudad") }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onCompartir) { Text("Compartir") }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onRefrescar) { Text("Refrescar") }
        }
        Spacer(Modifier.height(24.dp))
        Text("Próximos 5 días", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        GraficoBarras(d.proximos5Dias)
    }
}

@Composable
private fun GraficoBarras(items: List<ClimaDia>) {
    val max = (items.maxOfOrNull { it.maxC } ?: 1).coerceAtLeast(1)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEach { dia ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(Modifier.height(120.dp).width(40.dp)) {
                    val h = size.height
                    val hMin = (dia.minC / max.toFloat()) * h
                    val hMax = (dia.maxC / max.toFloat()) * h

                    // barra min
                    drawRect(
                        color = MaterialTheme.colorScheme.primary,
                        topLeft = Offset(0f, h - hMin),
                        size = Size(width = size.width / 2f - 4f, height = hMin)
                    )
                    // barra max
                    drawRect(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.55f),
                        topLeft = Offset(size.width / 2f + 4f, h - hMax),
                        size = Size(width = size.width / 2f - 4f, height = hMax)
                    )
                }
                Text(dia.dia)
                Text("${dia.minC}/${dia.maxC}°")
            }
        }
    }
}
