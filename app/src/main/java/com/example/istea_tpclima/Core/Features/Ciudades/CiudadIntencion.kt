package com.example.istea_tpclima.Core.Features.Ciudades

import com.example.istea_tpclima.Core.Modelos.CiudadModel
sealed class CiudadIntencion {
    data object geolocalizar : CiudadIntencion()
    data class buscar( val nombre:String ) : CiudadIntencion()
    data class seleccionar(val ciudad: CiudadModel) : CiudadIntencion()
}