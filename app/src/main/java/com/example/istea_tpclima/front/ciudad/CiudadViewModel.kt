package com.example.istea_tpclima.front.ciudad

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.istea_tpclima.core.features.ciudades.CiudadEstado
import com.example.istea_tpclima.core.features.ciudades.CiudadIntencion
import com.example.istea_tpclima.core.modelos.CiudadModel
import com.example.istea_tpclima.core.services.ICiudadService
import com.example.istea_tpclima.front.router.Ruta
import com.example.istea_tpclima.front.router.router
import com.example.istea_tpclima.infrastructure.location.LocationService
import kotlinx.coroutines.launch

class CiudadesViewModel(
    val repositorio: ICiudadService,
    val router: router,
    private val locationService: LocationService
) : ViewModel() {

    var uiState by mutableStateOf<CiudadEstado>(CiudadEstado.vacio)
    var ciudades: List<CiudadModel> = emptyList()

    fun ejecutar(intencion: CiudadIntencion) {
        when (intencion) {
            is CiudadIntencion.buscar -> buscar(nombre = intencion.nombre)
            is CiudadIntencion.seleccionar -> seleccionar(ciudad = intencion.ciudad)
            is CiudadIntencion.geolocalizar -> geolocalizar()
        }
    }

    private fun geolocalizar() {
        uiState = CiudadEstado.cargando

        viewModelScope.launch {
            try {
                val ciudadNombre = locationService.getCurrentCityName()

                if (ciudadNombre == null) {
                    uiState = CiudadEstado.error("No se pudo obtener tu ubicación actual")
                    return@launch
                }

                // Busco en el repositorio usando el nombre de la ciudad detectada
                ciudades = repositorio.getWFlag(ciudadNombre)

                if (ciudades.isEmpty()) {
                    uiState = CiudadEstado.error("No encontramos resultados para $ciudadNombre")
                } else {
                    // Tomo la primera ciudad encontrada y navego directo al clima
                    val ciudad = ciudades.first()
                    seleccionar(ciudad)
                }

            } catch (e: SecurityException) {
                uiState = CiudadEstado.error("La app no tiene permiso de ubicación")
            } catch (e: Exception) {
                uiState = CiudadEstado.error(
                    e.message ?: "Error al buscar por geolocalización"
                )
            }
        }
    }

    private fun buscar(nombre: String) {
        uiState = CiudadEstado.cargando
        viewModelScope.launch {
            try {
                ciudades = repositorio.getWFlag(nombre)
                uiState = if (ciudades.isEmpty()) {
                    CiudadEstado.vacio
                } else {
                    CiudadEstado.resultado(ciudades)
                }
            } catch (exeption: Exception) {
                uiState = CiudadEstado.error(exeption.message ?: "error desconocido")
            }
        }
    }

    private fun seleccionar(ciudad: CiudadModel) {
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
    private val router: router,
    private val locationService: LocationService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CiudadesViewModel::class.java)) {
            return CiudadesViewModel(repositorio, router, locationService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
