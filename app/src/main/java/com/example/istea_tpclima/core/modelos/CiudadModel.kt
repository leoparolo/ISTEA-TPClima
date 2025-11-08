package com.example.istea_tpclima.core.modelos

import kotlinx.serialization.Serializable

@Serializable
data class CiudadModel(
    val name: String,
    val lat: Float,
    val lon: Float,
    val country: String,
    val countryFullName: String? = "",
    val state: String = "",
    val flag: String? = ""
)