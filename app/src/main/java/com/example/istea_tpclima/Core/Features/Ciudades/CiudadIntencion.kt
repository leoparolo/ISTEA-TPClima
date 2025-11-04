package com.example.istea_tpclima.Core.Features.Ciudades

import com.example.istea_tpclima.Core.Modelos.CiudadModel

sealed class CiudadIntencion {
    object geolocalizar : CiudadIntencion()
    data class buscar(val texto: String) : CiudadIntencion()
    data class seleccionarCiudad(val Ciudad : CiudadModel) : CiudadIntencion()
}