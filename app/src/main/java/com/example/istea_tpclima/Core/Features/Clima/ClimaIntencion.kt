package com.example.istea_tpclima.Core.Features.Clima

import com.example.istea_tpclima.Core.Modelos.CiudadModel

sealed class ClimaIntencion {
    data object CargarPorCiudadGuardada : ClimaIntencion()
    data class CargarPorCiudad(val ciudad: CiudadModel) : ClimaIntencion()
    data object Refrescar : ClimaIntencion()
    data object CambiarCiudadClick : ClimaIntencion()
    data object CompartirClick : ClimaIntencion()
    data object LimpiarError : ClimaIntencion()
}
