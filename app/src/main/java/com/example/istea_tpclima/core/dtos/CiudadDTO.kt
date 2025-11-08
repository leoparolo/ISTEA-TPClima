package com.example.istea_tpclima.core.dtos

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