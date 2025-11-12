package com.example.istea_tpclima.infrastructure.implementations

import com.example.istea_tpclima.core.dtos.OpenWeatherCurrent
import com.example.istea_tpclima.core.dtos.OpenWeatherForecast5d
import com.example.istea_tpclima.core.modelos.CiudadModel
import com.example.istea_tpclima.core.modelos.ClimaDia
import com.example.istea_tpclima.core.modelos.ClimaModel
import com.example.istea_tpclima.core.repositories.IClimaRepository
import com.example.istea_tpclima.infrastructure.shared.ApiRouter
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToInt

class ClimaRepository : IClimaRepository
{
    private val client = HttpClientProvider.client
    private val APIKey = "d867b80e9ff8e2822fde457ee118deba"
//    override suspend fun get(lat: Float, lon: Float): String {
//        val respuesta = client.get(PARTIAL_URI){
//            parameter("lat",lat)
//            parameter("lon",lon)
//            parameter("units","metric")
//            parameter("appid",APIKey)
//        }
//        if (respuesta.status == HttpStatusCode.OK){
//            val clima = respuesta.body<String>()
//            return clima
//        }else{
//            throw Exception()
//        }
//    }
    override suspend fun obtenerClima(ciudad: CiudadModel): ClimaModel {

        val current: OpenWeatherCurrent = client.get(ApiRouter.WEATHER) {
            parameter("appid", APIKey)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("q", ciudad.name)
        }.body()

        val forecast: OpenWeatherForecast5d = client.get(ApiRouter.FORECAST) {
            parameter("appid", APIKey)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("q", ciudad.name)
        }.body()

        val actual = ClimaModel.Actual(
            tempC = current.main.temp.roundToInt(),
            descripcion = current.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "—",
            humedad = current.main.humidity
        )

        val zone = ZoneId.of(secondsToEtcGmt(forecast.city.timezone))
        val byDay = forecast.list.groupBy { it.dt.toDayName(zone) }

        val proximos = byDay.entries
            .take(5)
            .map { (day, items) ->
                val min = items.minOf { it.main.temp_min }.roundToInt()
                val max = items.maxOf { it.main.temp_max }.roundToInt()
                ClimaDia(dia = day, minC = min, maxC = max)
            }

        return ClimaModel(ciudad = ciudad, actual = actual, proximos5Dias = proximos)
    }
    private fun Int.toDayName(zone: ZoneId): String {
        val fmt = DateTimeFormatter.ofPattern("EEE").withZone(zone)
        return fmt.format(Instant.ofEpochSecond(this.toLong()))
    }


    private fun secondsToEtcGmt(seconds: Int): String {
        val hours = seconds / 3600
        val sign = if (hours >= 0) "-" else "+"
        return "Etc/GMT$sign${abs(hours)}"
    }
}