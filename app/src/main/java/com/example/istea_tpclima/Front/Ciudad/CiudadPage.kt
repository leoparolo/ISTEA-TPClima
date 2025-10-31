package com.example.istea_tpclima.Front.Ciudad

import androidx.compose.foundation.Image
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
import com.example.istea_tpclima.R

data class Ciudad(
    val nombre: String,
    val temperatura: String,
    val clima: String,
    val imagenRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CiudadesPage() {
    var query by remember { mutableStateOf("") }

    val listaCiudades = remember {
        listOf(
            Ciudad("City of Buenos Aires", "20 ºC", "Despejado", R.drawable.day),
            Ciudad("City of Buenos Aires", "20 ºC", "Despejado", R.drawable.day),
            Ciudad("City of Buenos Aires", "20 ºC", "Despejado", R.drawable.day),
            Ciudad("City of Buenos Aires", "20 ºC", "Despejado", R.drawable.day),
            Ciudad("City of Buenos Aires", "20 ºC", "Despejado", R.drawable.day)
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaCiudades) { ciudad ->
                    CiudadItem(ciudad)
                }
            }
        }
    }
}

@Composable
fun CiudadItem(ciudad: Ciudad) {
    Card(
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
