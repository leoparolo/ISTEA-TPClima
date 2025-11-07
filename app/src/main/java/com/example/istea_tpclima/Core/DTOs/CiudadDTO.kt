package com.example.istea_tpclima.Core.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class CiudadDTO(
    val id: Long? = null,
    val name: String,
    val lat: Float,
    val lon: Float,
    val country: String,
    val state:String = ""
)