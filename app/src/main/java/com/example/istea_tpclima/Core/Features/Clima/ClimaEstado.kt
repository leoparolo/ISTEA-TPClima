package com.example.istea_tpclima.Core.Features.Clima

import com.example.istea_tpclima.Core.Modelos.ClimaModel

sealed class ClimaEstado {
    data object Cargando : ClimaEstado()
    data object SinCiudadGuardada : ClimaEstado()
    data class Mostrando(val data: ClimaModel) : ClimaEstado()
    data class Error(val mensaje: String) : ClimaEstado()
}
