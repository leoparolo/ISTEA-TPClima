package com.example.istea_tpclima.core.features.clima

import com.example.istea_tpclima.core.modelos.CiudadModel

sealed class ClimaIntencion {
    data object CargarPorCiudadGuardada : ClimaIntencion()
    data class CargarPorCiudad(val ciudad: CiudadModel) : ClimaIntencion()
    data object Refrescar : ClimaIntencion()
    data object CambiarCiudadClick : ClimaIntencion()
    data object CompartirClick : ClimaIntencion()
    data object LimpiarError : ClimaIntencion()
}