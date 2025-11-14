package com.example.istea_tpclima.front.clima

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.istea_tpclima.core.features.clima.ClimaEstado
import com.example.istea_tpclima.core.features.clima.ClimaIntencion
import com.example.istea_tpclima.R
import com.example.istea_tpclima.core.modelos.ClimaDia
import com.example.istea_tpclima.core.modelos.ClimaModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer

@Composable
fun ClimaView(
    modifier: Modifier = Modifier,
    state : ClimaEstado,
    onAction: (ClimaIntencion)->Unit
){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()
            ) {
                when (state) {
                    is ClimaEstado.Cargando -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                    is ClimaEstado.Error -> { ClimaErrorView(modifier,state.mensaje) }
                    is ClimaEstado.Mostrando -> { ClimaResultadoView(modifier,state.data,
                        onAction) }
                    is ClimaEstado.SinCiudadGuardada -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Button(onClick = {}) { Text("Elegir ciudad") }
                    }
                }
            }
        }
    }
}
@Composable
fun ClimaErrorView(
    modifier: Modifier,
    mensaje: String)
{
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .background(
                    color = Color.White.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = mensaje,
                color = Color.Red,
                fontSize = 16.sp
            )
        }
    }
}
@Composable
fun ClimaResultadoView(
    modifier: Modifier = Modifier,
    state: ClimaModel,
    onAction: (ClimaIntencion) -> Unit
)
{
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${state.actual.tempC} °C",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PONER TEXTO DE CIUDAD",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = state.actual.descripcion,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${state.actual.humedad}%",
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                ClimaChartCard(
                    values = state.proximos5Dias
                )
            }
        }
    }
}
@Composable
fun ClimaChartCard(values: List<ClimaDia>) {
    val barras = ArrayList<BarChartData.Bar>()
    values.mapIndexed { index, value ->
        barras.add(
            BarChartData.Bar(
                label = value.dia,
                value = value.minC.toFloat(),
                color = Color.Blue
            )
        )
    }
    BarChart(
        barChartData = BarChartData(bars = barras),
        modifier = Modifier
            .fillMaxWidth(),
        labelDrawer = SimpleValueDrawer(
            drawLocation = SimpleValueDrawer.DrawLocation.XAxis
        )
    )
}