package com.example.istea_tpclima.Infrastructure.Repositories.Clima

import com.example.istea_tpclima.BuildConfig
import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Core.Modelos.ClimaDia
import com.example.istea_tpclima.Core.Modelos.ClimaModel
import com.example.istea_tpclima.Infrastructure.Services.dto.OpenWeatherCurrent
import com.example.istea_tpclima.Infrastructure.Services.dto.OpenWeatherForecast5d
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToInt

class ClimaRepositoryKtor : IClimaRepository {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        install(Logging) { level = LogLevel.INFO }
    }

    private val base = "https://api.openweathermap.org"
    private val apiKey: String = BuildConfig.OPENWEATHER_API_KEY

    override suspend fun obtenerClima(ciudad: CiudadModel): ClimaModel {

        val current: OpenWeatherCurrent = client.get("$base/data/2.5/weather") {
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("q", ciudad.nombre)
        }.body()

        val forecast: OpenWeatherForecast5d = client.get("$base/data/2.5/forecast") {
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("q", ciudad.nombre)
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
