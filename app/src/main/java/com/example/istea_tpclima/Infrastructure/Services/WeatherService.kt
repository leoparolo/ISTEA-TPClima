package com.example.istea_tpclima.Infrastructure.Services

import com.example.istea_tpclima.BuildConfig
import com.example.istea_tpclima.Infrastructure.Services.dto.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WeatherService(
    private val client: io.ktor.client.HttpClient = KtorProvider.client,
    private val apiKey: String = BuildConfig.OPENWEATHER_API_KEY
) {
    private val baseGeo = "https://api.openweathermap.org/geo/1.0"
    private val baseWx  = "https://api.openweathermap.org/data/2.5"

    suspend fun geocodeDirect(query: String, limit: Int = 1): List<GeoCityDto> =
        client.get("$baseGeo/direct") {
            parameter("q", query)
            parameter("limit", limit)
            parameter("appid", apiKey)
        }.body()

    suspend fun current(lat: Double, lon: Double): CurrentDto =
        client.get("$baseWx/weather") {
            parameter("lat", lat); parameter("lon", lon)
            parameter("units", "metric"); parameter("lang", "es")
            parameter("appid", apiKey)
        }.body()

    suspend fun forecast(lat: Double, lon: Double): ForecastDto =
        client.get("$baseWx/forecast") {
            parameter("lat", lat); parameter("lon", lon)
            parameter("units", "metric"); parameter("lang", "es")
            parameter("appid", apiKey)
        }.body()
}
