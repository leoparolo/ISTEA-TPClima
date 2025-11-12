package com.example.istea_tpclima.front.clima

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.istea_tpclima.core.features.clima.ClimaEstado
import com.example.istea_tpclima.core.features.clima.ClimaIntencion
import com.example.istea_tpclima.core.modelos.CiudadModel
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

    private val _estado = MutableStateFlow<ClimaEstado>(ClimaEstado.Cargando)
    val estado: StateFlow<ClimaEstado> = _estado.asStateFlow()

    fun procesar(i: ClimaIntencion) = when (i) {
        ClimaIntencion.CargarPorCiudadGuardada -> cargarGuardada()
        is ClimaIntencion.CargarPorCiudad      -> cargar(i.ciudad)
        ClimaIntencion.Refrescar               -> refrescar()
        ClimaIntencion.CambiarCiudadClick      -> _estado.value = ClimaEstado.SinCiudadGuardada
        ClimaIntencion.CompartirClick          -> Unit
        ClimaIntencion.LimpiarError            -> _estado.value = ClimaEstado.Cargando
    }

    private fun cargarGuardada() {
        _estado.value = ClimaEstado.Cargando
        viewModelScope.launch {
            val c: CiudadModel? = prefs.leer().firstOrNull()
            if (c == null) {
                _estado.value = ClimaEstado.SinCiudadGuardada
            } else {
                cargar(c)
            }
        }
    }

    private fun cargar(ciudad: CiudadModel) {
        _estado.value = ClimaEstado.Cargando
        viewModelScope.launch {
            try {
                val data = repo.obtenerClima(ciudad)
                _estado.value = ClimaEstado.Mostrando(data)
            } catch (e: Exception) {
                _estado.value = ClimaEstado.Error("No se pudo cargar el clima")
            }
        }
    }

    private fun refrescar() {
        val c = (estado.value as? ClimaEstado.Mostrando)?.data?.ciudad ?: return
        cargar(c)
    }
}