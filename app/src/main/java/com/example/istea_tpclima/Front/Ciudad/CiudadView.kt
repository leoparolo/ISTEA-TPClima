package com.example.istea_tpclima.Front.Ciudad

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import com.example.istea_tpclima.Core.Features.Ciudades.CiudadEstado
import com.example.istea_tpclima.Core.Features.Ciudades.CiudadIntencion
import com.example.istea_tpclima.Core.Modelos.CiudadModel


@Composable
fun CiudadesView (
    modifier: Modifier = Modifier,
    state : CiudadEstado,
    onAction: (CiudadIntencion)->Unit
) {
    var value by remember{ mutableStateOf("") }

    Column(modifier = modifier) {
        TextField(
            value = value,
            label = { Text(text = "buscar por nombre") },
            onValueChange = {
                value = it
                onAction(CiudadIntencion.buscar(value))
            },
        )
        when(state) {
            CiudadEstado.cargando -> Text(text = "cargando")
            is CiudadEstado.error -> Text(text = state.mensaje)
            is CiudadEstado.resultado -> ListaDeCiudades(state.ciudades) {
                onAction(
                    CiudadIntencion.seleccionar(it)
                )
            }
            CiudadEstado.vacio -> Text(text = "No hay resultados")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDeCiudades(ciudades: List<CiudadModel>, onSelect: (CiudadModel)->Unit) {
    LazyColumn {
        items(items = ciudades) {
            Card(onClick = { onSelect(it) }) {
                Text(text = it.name)
            }
        }
    }
}