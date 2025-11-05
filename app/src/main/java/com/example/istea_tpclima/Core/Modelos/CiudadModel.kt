package com.example.istea_tpclima.Core.Modelos

import kotlinx.serialization.Serializable

@Serializable
data class CiudadModel(
    val name: String,
    val lat: Float,
    val lon: Float,
    val country: String,
    val state: String = ""
)