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
import com.example.istea_tpclima.core.repositories.ICiudadRepository
import com.example.istea_tpclima.front.router.Ruta
import com.example.istea_tpclima.front.router.router
import com.example.istea_tpclima.infrastructure.implementations.LocationRepository
import com.example.istea_tpclima.infrastructure.storage.Prefs
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CiudadesViewModel(
    val repositorio: ICiudadRepository,
    val router: router,
    private val locationRepository : LocationRepository,
    private val prefs : Prefs
) : ViewModel(){
    var uiState by mutableStateOf<CiudadEstado>(CiudadEstado.vacio)
    var ciudades : List<CiudadModel> = emptyList()
    private var searchJob : Job? = null
    fun ejecutar(intencion: CiudadIntencion){
        when(intencion){
            is CiudadIntencion.buscar       -> buscar(nombre = intencion.nombre)
            is CiudadIntencion.seleccionar  -> seleccionar(ciudad = intencion.ciudad)
            is CiudadIntencion.geolocalizar -> geolocalizar()
        }
    }

    private fun geolocalizar() {
        uiState = CiudadEstado.cargando

        viewModelScope.launch {
            try {
                // 1) Obtener coordenadas reales
                val coords = locationRepository.getCoordinates()

                if (coords == null) {
                    uiState = CiudadEstado.error("No se pudieron obtener tus coordenadas")
                    return@launch
                }

                // 2) Buscar ciudad exacta por lat/lon en la API
                val ciudadesPorCoords = repositorio.getPorLatLon(
                    coords.lat.toDouble(),
                    coords.lon.toDouble()
                )

                if (ciudadesPorCoords.isEmpty()) {
                    uiState = CiudadEstado.error("No encontramos una ciudad válida en tu ubicación")
                    return@launch
                }

                // 3) Tomamos la primera ciudad devuelta por la API
                val ciudadBase = ciudadesPorCoords.first()

                // 4) Enriquecemos con nombre de país y bandera
                ciudades = repositorio.getWFlag(ciudadBase.name)

                if (ciudades.isEmpty()) {
                    uiState = CiudadEstado.error("No encontramos detalles de esta ciudad")
                } else {
                    // 5) Usamos el MISMO flujo de selección:
                    //    guarda en Prefs + navega a Clima
                    seleccionar(ciudades.first())
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

    private fun buscar( nombre: String){
        searchJob?.cancel()
        if (nombre.isBlank()) {
            uiState = CiudadEstado.vacio
            ciudades = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            uiState = CiudadEstado.cargando
            try {
                val resultado = repositorio.getWFlag(nombre)
                ciudades = resultado
                uiState = if (resultado.isEmpty()) {
                    CiudadEstado.vacio
                } else {
                    CiudadEstado.resultado(resultado)
                }
            } catch (e: Exception) {
                uiState = CiudadEstado.error(e.message ?: "error desconocido")
            }
        }
    }

    private fun seleccionar(ciudad: CiudadModel){
        viewModelScope.launch {
            try {
                // 1) Guardamos la ciudad elegida en DataStore
                prefs.guardar(ciudad)

                // 2) Navegamos a Clima (por si en el futuro se usan params)
                val ruta = Ruta.Clima(
                    lat = ciudad.lat,
                    lon = ciudad.lon,
                    nombre = ciudad.name
                )
                router.navegar(ruta)

            } catch (e: Exception) {
                uiState = CiudadEstado.error("No se pudo guardar la ciudad seleccionada")
            }
        }
    }
}


class CiudadesViewModelFactory(
    private val repositorio: ICiudadRepository,
    private val router: router,
    private val locationRepository: LocationRepository,
    private val prefs: Prefs
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CiudadesViewModel::class.java)) {
            return CiudadesViewModel(repositorio,router, locationRepository,prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}