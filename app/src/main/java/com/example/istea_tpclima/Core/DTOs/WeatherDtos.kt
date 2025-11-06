package com.example.istea_tpclima.Core.DTOs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OpenWeatherCurrent(
    val weather: List<WeatherDesc> = emptyList(),
    val main: MainBlock = MainBlock(),
)

@Serializable
data class WeatherDesc(
    val description: String? = null
)

@Serializable
data class MainBlock(
    val temp: Double = 0.0,
    val humidity: Int = 0,
    @SerialName("temp_min") val temp_min: Double = 0.0,
    @SerialName("temp_max") val temp_max: Double = 0.0
)


@Serializable
data class OpenWeatherForecast5d(
    val city: ForecastCity = ForecastCity(),
    val list: List<ForecastItem> = emptyList()
)

@Serializable
data class ForecastCity(

    val timezone: Int = 0
)

@Serializable
data class ForecastItem(
    val dt: Int = 0,
    val main: MainBlock = MainBlock()
)