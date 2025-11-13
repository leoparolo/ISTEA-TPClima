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
import com.example.istea_tpclima.infrastructure.storage.Prefs
import kotlinx.coroutines.launch

class CiudadesViewModel(
    private val repositorio: ICiudadService,
    private val router: router,
    private val locationService: LocationService,
    private val prefs: Prefs
) : ViewModel() {

    var uiState by mutableStateOf<CiudadEstado>(CiudadEstado.vacio)
    var ciudades: List<CiudadModel> = emptyList()

    fun ejecutar(intencion: CiudadIntencion) {
        when (intencion) {
            is CiudadIntencion.buscar      -> buscar(intencion.nombre)
            is CiudadIntencion.seleccionar -> seleccionar(intencion.ciudad)
            is CiudadIntencion.geolocalizar -> geolocalizar()
        }
    }

    private fun geolocalizar() {
        uiState = CiudadEstado.cargando

        viewModelScope.launch {
            try {
                // 1) Obtener coordenadas reales
                val coords = locationService.getCoordinates()

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
                uiState = CiudadEstado.error("No otorgaste permisos de ubicación")
            } catch (e: Exception) {
                uiState = CiudadEstado.error(e.message ?: "Error al geolocalizar")
            }
        }
    }

    private fun buscar(nombre: String) {
        uiState = CiudadEstado.cargando

        viewModelScope.launch {
            try {
                ciudades = repositorio.getWFlag(nombre)
                uiState =
                    if (ciudades.isEmpty()) CiudadEstado.vacio
                    else CiudadEstado.resultado(ciudades)

            } catch (e: Exception) {
                uiState = CiudadEstado.error(e.message ?: "Error desconocido")
            }
        }
    }

    // 🔹 Punto central: TODA selección de ciudad pasa por acá
    //    - guarda en Prefs
    //    - navega a la ruta Clima
    private fun seleccionar(ciudad: CiudadModel) {
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
    private val repositorio: ICiudadService,
    private val router: router,
    private val locationService: LocationService,
    private val prefs: Prefs
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CiudadesViewModel::class.java)) {
            return CiudadesViewModel(repositorio, router, locationService, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
