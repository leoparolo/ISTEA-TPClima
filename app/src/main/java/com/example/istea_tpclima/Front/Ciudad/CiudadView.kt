package com.example.istea_tpclima.Front.Ciudad

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.istea_tpclima.Core.Features.Ciudades.CiudadEstado
import com.example.istea_tpclima.Core.Features.Ciudades.CiudadIntencion
import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Infrastructure.Storage.Prefs
import com.example.istea_tpclima.R
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CiudadesView (
    modifier: Modifier = Modifier,
    state : CiudadEstado,
    onAction: (CiudadIntencion)->Unit,
    onCiudadSeleccionada: () -> Unit = {}
) {
    var value by remember{ mutableStateOf("") }
    val ctx = LocalContext.current
    val prefs = remember { Prefs(ctx) }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        topBar = {
            TextField(
                value = value,
                onValueChange = {
                    value = it
                    onAction(CiudadIntencion.buscar(value))
                },
                placeholder = { Text("Buscar ciudades") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Buscar por geolocalización",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            when (state) {
                CiudadEstado.cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cargando...")
                    }
                }
                is CiudadEstado.error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.mensaje, color = Color.Red)
                    }
                }
                CiudadEstado.vacio -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay resultados")
                    }
                }
                is CiudadEstado.resultado -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.ciudades) { ciudad ->
                            CiudadItem(ciudad){ seleccionada ->
                                val model = CiudadModel(
                                    id = seleccionada.id,
                                    name = seleccionada.name,
                                    country = seleccionada.country,
                                    state = seleccionada.state,
                                    lon = seleccionada.lon,
                                    lat = seleccionada.lat
                                )
                                scope.launch {
                                    // Guardar en prefs
                                    withContext(Dispatchers.IO) {
                                        prefs.guardar(model)
                                    }
                                    // Volver a la pantalla de clima
                                    onCiudadSeleccionada()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun CiudadItem(ciudad: CiudadModel,
               onCiudadSeleccionada: (CiudadModel) -> Unit
) {
    Card(
        onClick = { onCiudadSeleccionada(ciudad) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.day),
                contentDescription = ciudad.name,
                modifier = Modifier
                    .size(width = 100.dp, height = 60.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(text = ciudad.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = ciudad.country, fontSize = 14.sp)
                Text(text = ciudad.state, fontSize = 14.sp)
            }
        }
    }
}