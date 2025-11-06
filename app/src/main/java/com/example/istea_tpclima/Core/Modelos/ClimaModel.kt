package com.example.istea_tpclima.Core.Modelos

data class ClimaModel(
    val ciudad: CiudadModel,
    val actual: Actual,
    val proximos5Dias: List<ClimaDia>
) {
    data class Actual(
        val tempC: Int,
        val descripcion: String,
        val humedad: Int
    )
}

data class ClimaDia(
    val dia: String,
    val minC: Int,
    val maxC: Int
)