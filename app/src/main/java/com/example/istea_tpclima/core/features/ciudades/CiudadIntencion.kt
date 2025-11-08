package com.example.istea_tpclima.core.features.ciudades

import com.example.istea_tpclima.core.modelos.CiudadModel
sealed class CiudadIntencion {
    data object geolocalizar : CiudadIntencion()
    data class buscar( val nombre:String ) : CiudadIntencion()
    data class seleccionar(val ciudad: CiudadModel) : CiudadIntencion()
}