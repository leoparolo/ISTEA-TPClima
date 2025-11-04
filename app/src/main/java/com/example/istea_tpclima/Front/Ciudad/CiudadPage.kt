package com.example.istea_tpclima.Front.Ciudad

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Infrastructure.Storage.Prefs
import com.example.istea_tpclima.R

data class Ciudad(
    val nombre: String,
    val temperatura: String,
    val clima: String,
    val imagenRes: Int,
    val pais: String = "AR",
    val id: String = nombre.lowercase().replace(" ", "_") + "_${pais.lowercase()}"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CiudadesPage(
    onCiudadSeleccionada: () -> Unit = {}   // ← ahora solo navega
) {
    val ctx = LocalContext.current
    val prefs = remember { Prefs(ctx) }

    var query by remember { mutableStateOf("") }

    val listaCiudades = remember {
        listOf(
            Ciudad("City of Buenos Aires", "20 ºC", "Despejado", R.drawable.day, pais = "AR"),
            Ciudad("Córdoba", "19 ºC", "Parcial nublado", R.drawable.day, pais = "AR"),
            Ciudad("Rosario", "18 ºC", "Despejado", R.drawable.day, pais = "AR"),
            Ciudad("Mendoza", "16 ºC", "Soleado", R.drawable.day, pais = "AR"),
            Ciudad("La Plata", "17 ºC", "Parcial nublado", R.drawable.day, pais = "AR")
        )
    }

    Scaffold(
        topBar = {
            TextField(
                value = query,
                onValueChange = { query = it },
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val filtradas = listaCiudades.filter {
                    query.isBlank() || it.nombre.contains(query, ignoreCase = true)
                }
                items(filtradas) { ciudad ->
                    CiudadItem(
                        ciudad = ciudad,
                        onClick = {
                            val model = CiudadModel(
                                id = ciudad.id,
                                nombre = ciudad.nombre,
                                pais = ciudad.pais
                            )
                            // guardar en prefs
                            prefs.guardar(model)
                            // volver a Clima
                            onCiudadSeleccionada()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CiudadItem(
    ciudad: Ciudad,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = ciudad.imagenRes),
                contentDescription = ciudad.nombre,
                modifier = Modifier
                    .size(width = 100.dp, height = 60.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(text = ciudad.nombre, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = ciudad.temperatura, fontSize = 14.sp)
                Text(text = ciudad.clima, fontSize = 14.sp)
            }
        }
    }
}
