package com.example.istea_tpclima.Front.Ciudad

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.istea_tpclima.Core.Features.Ciudades.CiudadEstado
import com.example.istea_tpclima.Core.Features.Ciudades.CiudadIntencion
import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Core.Services.ICiudadService
import com.example.istea_tpclima.Front.Router.Ruta
import com.example.istea_tpclima.Front.Router.router
import kotlinx.coroutines.launch

class CiudadesViewModel(
    val repositorio: ICiudadService,
    val router: router
) : ViewModel(){
    var uiState by mutableStateOf<CiudadEstado>(CiudadEstado.vacio)
    var ciudades : List<CiudadModel> = emptyList()
    fun ejecutar(intencion: CiudadIntencion){
        when(intencion){
            is CiudadIntencion.buscar -> buscar(nombre = intencion.nombre)
            is CiudadIntencion.seleccionar -> seleccionar(ciudad = intencion.ciudad)
            is CiudadIntencion.geolocalizar -> geolocalizar()
        }
    }

    private fun geolocalizar() {
        TODO("Not yet implemented")
    }

    private fun buscar( nombre: String){

        uiState = CiudadEstado.cargando
        viewModelScope.launch {
            try {
                ciudades = repositorio.get(nombre)
                if (ciudades.isEmpty()) {
                    uiState = CiudadEstado.vacio
                } else {
                    uiState = CiudadEstado.resultado(ciudades)
                }
            } catch (exeption: Exception){
                uiState = CiudadEstado.error(exeption.message ?: "error desconocido")
            }
        }
    }

    private fun seleccionar(ciudad: CiudadModel){
        val ruta = Ruta.Clima(
            lat = ciudad.lat,
            lon = ciudad.lon,
            nombre = ciudad.name
        )
        router.navegar(ruta)
    }
}


class CiudadesViewModelFactory(
    private val repositorio: ICiudadService,
    private val router: router
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CiudadesViewModel::class.java)) {
            return CiudadesViewModel(repositorio,router) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}