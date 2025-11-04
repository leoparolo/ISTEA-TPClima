package com.example.istea_tpclima.Infrastructure.Services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoCityDto(
    val name: String? = null,
    val country: String? = null,
    val lat: Double? = null,
    val lon: Double? = null
)

@Serializable
data class CurrentDto(
    val main: MainDto,
    val weather: List<WeatherDto> = emptyList()
)
@Serializable data class MainDto(val temp: Double, val humidity: Int)
@Serializable data class WeatherDto(val description: String? = null)

@Serializable
data class ForecastDto(val list: List<StepDto> = emptyList())
@Serializable
data class StepDto(@SerialName("dt_txt") val dtTxt: String, val main: StepMainDto)
@Serializable
data class StepMainDto(@SerialName("temp_min") val tempMin: Double,
                       @SerialName("temp_max") val tempMax: Double)
