package com.example.istea_tpclima.Infrastructure.Repositories.Clima

import com.example.istea_tpclima.Core.Modelos.*
import com.example.istea_tpclima.Infrastructure.Services.WeatherService
import kotlin.math.roundToInt

class ClimaRepositoryKtor(
    private val service: WeatherService = WeatherService()
) : IClimaRepository {

    override suspend fun obtenerClima(ciudad: CiudadModel): ClimaModel {
        // 1) geocoding por nombre + país
        val q = "${ciudad.nombre},${ciudad.pais}"
        val geo = service.geocodeDirect(q).firstOrNull()
            ?: error("Ciudad no encontrada")
        val lat = geo.lat ?: error("lat faltante")
        val lon = geo.lon ?: error("lon faltante")

        // 2) clima actual
        val cur = service.current(lat, lon)
        val actual = ClimaActual(
            tempC = cur.main.temp.roundToInt(),
            humedad = cur.main.humidity,
            descripcion = cur.weather.firstOrNull()?.description ?: "N/D"
        )

        // 3) forecast 5 días (agrupar por fecha YYYY-MM-DD)
        val for5 = service.forecast(lat, lon)
        val porDia = for5.list
            .groupBy { it.dtTxt.substring(0,10) }
            .entries
            .take(5)
            .map { (fecha, items) ->
                val min = items.minOf { it.main.tempMin }.roundToInt()
                val max = items.maxOf { it.main.tempMax }.roundToInt()
                ClimaDia(dia = fecha.substring(8,10), minC = min, maxC = max)
            }

        return ClimaModel(ciudad, actual, porDia)
    }
}
