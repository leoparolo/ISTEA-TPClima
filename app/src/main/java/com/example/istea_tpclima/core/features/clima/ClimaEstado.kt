package com.example.istea_tpclima.core.features.clima

import com.example.istea_tpclima.core.modelos.ClimaModel

sealed class ClimaEstado {
    data object Cargando : ClimaEstado()
    data object SinCiudadGuardada : ClimaEstado()
    data class Mostrando(val data: ClimaModel) : ClimaEstado()
    data class Error(val mensaje: String) : ClimaEstado()
    data class Compartir(val texto: String) : ClimaEstado()
}