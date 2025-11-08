package com.example.istea_tpclima.core.features.ciudades

import com.example.istea_tpclima.core.modelos.CiudadModel

sealed class CiudadEstado {
    data object vacio: CiudadEstado()
    data object cargando: CiudadEstado()
    data class resultado( val ciudades : List<CiudadModel> ) : CiudadEstado()
    data class error(val mensaje: String): CiudadEstado()
}