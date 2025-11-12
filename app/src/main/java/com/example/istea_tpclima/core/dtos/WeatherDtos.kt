package com.example.istea_tpclima.core.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentDto(
    val weather: List<WeatherDescDto> = emptyList(),
    val main: MainDto = MainDto(),
    val humidity: Int? = null
)

@Serializable
data class ForecastDto(
    val list: List<ForecastItemDto> = emptyList(),
    val city: CityDto = CityDto()
)

@Serializable
data class ForecastItemDto(
    val dt: Long,
    val main: MainDto = MainDto(),
    val weather: List<WeatherDescDto> = emptyList()
)

@Serializable
data class CityDto(
    val timezone: Int = 0
)

@Serializable
data class MainDto(
    val temp: Double = 0.0,
    @SerialName("temp_min") val tempMin: Double = 0.0,
    @SerialName("temp_max") val tempMax: Double = 0.0,
    val humidity: Int = 0
)

@Serializable
data class WeatherDescDto(
    val description: String = ""
)

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