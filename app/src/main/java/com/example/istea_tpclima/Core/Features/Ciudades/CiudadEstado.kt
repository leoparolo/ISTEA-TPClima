package com.example.istea_tpclima.Core.Features.Ciudades

import com.example.istea_tpclima.Core.Modelos.CiudadModel

data class CiudadEstado(
    val texto: String,
    val listaFiltrada: List<CiudadModel>
)