package com.example.istea_tpclima.front.ciudad

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.istea_tpclima.R
import com.example.istea_tpclima.core.features.ciudades.CiudadEstado
import com.example.istea_tpclima.core.features.ciudades.CiudadIntencion
import com.example.istea_tpclima.core.modelos.CiudadModel

@Composable
fun CiudadesView(
    modifier: Modifier = Modifier,
    state: CiudadEstado,
    onAction: (CiudadIntencion) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        backgroundImage(modifier)
        Scaffold(
            modifier = modifier,
            topBar = { topBarBuscador(modifier, onAction) },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()
            ) {
                // 🔹 Botón de geolocalizar → dispara intención al ViewModel
                buttonGeolocalizar(modifier) {
                    onAction(CiudadIntencion.geolocalizar)
                }

                when (state) {
                    CiudadEstado.cargando -> CiudadCargandoView(modifier)
                    is CiudadEstado.error -> CiudadErrorView(modifier, state.mensaje)
                    CiudadEstado.vacio -> CiudadVacioView(modifier)
                    is CiudadEstado.resultado -> CiudadResultadoView(
                        ciudades = state.ciudades,
                        onAction = onAction
                    )
                }
            }
        }
    }
}

@Composable
fun backgroundImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.modern_city),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize()
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.2f))
    )
}

@Composable
fun topBarBuscador(
    modifier: Modifier = Modifier,
    onAction: (CiudadIntencion) -> Unit
) {
    var value by remember { mutableStateOf("") }
    TextField(
        value = value,
        onValueChange = {
            value = it
            onAction(CiudadIntencion.buscar(value))
        },
        placeholder = { Text("Buscar ciudades") },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun buttonGeolocalizar(
    modifier: Modifier = Modifier,
    onGeolocalizar: () -> Unit
) {
    Button(
        onClick = { onGeolocalizar() },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = null,
            tint = Color.White
        )
        Spacer(modifier = modifier.width(8.dp))
        Text(
            "Buscar por geolocalización",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CiudadCargandoView(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = modifier
                .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            CircularProgressIndicator(
                modifier = modifier.size(48.dp),
                color = Color(0xFF1565C0),
                strokeWidth = 4.dp
            )
        }
    }
}

@Composable
fun CiudadErrorView(modifier: Modifier, mensaje: String) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = modifier
                .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(text = mensaje, color = Color.Red, fontSize = 16.sp)
        }
    }
}

@Composable
fun CiudadVacioView(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = modifier
                .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(text = "No hay resultados", color = Color.Black, fontSize = 16.sp)
        }
    }
}

@Composable
fun CiudadResultadoView(
    modifier: Modifier = Modifier,
    ciudades: List<CiudadModel>,
    onAction: (CiudadIntencion) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(ciudades) { ciudad ->
            CiudadItem(ciudad) { seleccionada ->
                // 👉 La UI SOLO envía la intención
                onAction(CiudadIntencion.seleccionar(seleccionada))
            }
        }
    }
}

@Composable
fun CiudadItem(
    ciudad: CiudadModel,
    onCiudadSeleccionada: (CiudadModel) -> Unit
) {
    Card(
        onClick = { onCiudadSeleccionada(ciudad) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F7FB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp)
                )

                Column {
                    Text(
                        text = ciudad.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color(0xFF212121)
                    )
                    Text(
                        text = "${ciudad.country} - ${ciudad.countryFullName}",
                        fontSize = 14.sp,
                        color = Color(0xFF616161)
                    )
                    Text(
                        text = ciudad.state,
                        fontSize = 13.sp,
                        color = Color(0xFF9E9E9E)
                    )
                    Text(
                        text = "${ciudad.lat}, ${ciudad.lon}",
                        fontSize = 13.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(ciudad.flag)
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(true)
                    .build(),
                contentDescription = "Bandera de ${ciudad.name}",
                modifier = Modifier
                    .width(70.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Fit
            )
        }
    }
}
