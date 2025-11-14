package com.example.istea_tpclima.front.clima

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.istea_tpclima.core.features.ciudades.CiudadEstado
import com.example.istea_tpclima.core.features.clima.ClimaEstado
import com.example.istea_tpclima.core.features.clima.ClimaIntencion
import com.example.istea_tpclima.core.modelos.CiudadModel
import com.example.istea_tpclima.front.ciudad.CiudadesViewModel
import com.example.istea_tpclima.infrastructure.implementations.ClimaRepository
import com.example.istea_tpclima.infrastructure.storage.Prefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ClimaViewModel(
    private val repo: ClimaRepository,
    private val prefs: Prefs
) : ViewModel() {
    var uiState by mutableStateOf<ClimaEstado>(ClimaEstado.Cargando)

    fun ejecutar(intencion: ClimaIntencion)
    {
        when(intencion){
            is ClimaIntencion.CargarPorCiudadGuardada   -> cargarGuardada()
            is ClimaIntencion.CargarPorCiudad           -> cargar(intencion.ciudad)
            is ClimaIntencion.Refrescar                 -> refrescar()
            is ClimaIntencion.CambiarCiudadClick        -> uiState = ClimaEstado.SinCiudadGuardada
            is ClimaIntencion.CompartirClick            -> compartir()
            is ClimaIntencion.LimpiarError              -> uiState = ClimaEstado.Cargando
        }
    }

    private fun cargarGuardada() {
        uiState = ClimaEstado.Cargando
        viewModelScope.launch {
            val c: CiudadModel? = prefs.leer().firstOrNull()
            if (c == null) {
                uiState = ClimaEstado.SinCiudadGuardada
            } else {
                cargar(c)
            }
        }
    }

    private fun cargar(ciudad: CiudadModel) {
        uiState = ClimaEstado.Cargando
        viewModelScope.launch {
            try {
                val data = repo.obtenerClima(ciudad)
                uiState = ClimaEstado.Mostrando(data)
            } catch (e: Exception) {
                uiState = ClimaEstado.Error("No se pudo cargar el clima")
            }
        }
    }

    private fun refrescar() {
        val c = (uiState as? ClimaEstado.Mostrando)?.data?.ciudad ?: return
        cargar(c)
    }
    private fun compartir(){
        viewModelScope.launch {
            val ciudad = prefs.leer().firstOrNull()

            if (ciudad != null) {
                val texto = """
                📍 Ciudad: ${ciudad.name}  
                🇨🇴 País: ${ciudad.countryFullName}  
                🌡️ Lat: ${ciudad.lat}, Lon: ${ciudad.lon}
            """.trimIndent()

                uiState = ClimaEstado.Compartir(texto)
            }
        }

    }
}

class ClimaViewModelFactory(
    private val repositorio: ClimaRepository,
    private val prefs: Prefs
) : ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClimaViewModel::class.java)) {
            return ClimaViewModel(repositorio, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}