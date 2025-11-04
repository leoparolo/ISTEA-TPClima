package com.example.istea_tpclima.Core.Modelos

data class ClimaActual(
    val tempC: Int,
    val humedad: Int,
    val descripcion: String
)

data class ClimaDia(
    val dia: String, // "Lun", "Mar" (o "03", "04")
    val minC: Int,
    val maxC: Int
)

data class ClimaModel(
    val ciudad: CiudadModel,
    val actual: ClimaActual,
    val proximos5Dias: List<ClimaDia>
)
