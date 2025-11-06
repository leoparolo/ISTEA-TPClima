package com.example.istea_tpclima.Core.Features.Ciudades

import com.example.istea_tpclima.Core.Modelos.CiudadModel

sealed class CiudadEstado {
    data object vacio: CiudadEstado()
    data object cargando: CiudadEstado()
    data class resultado( val ciudades : List<CiudadModel> ) : CiudadEstado()
    data class error(val mensaje: String): CiudadEstado()
}