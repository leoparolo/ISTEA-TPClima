package com.example.istea_tpclima.core.modelos

import kotlinx.serialization.Serializable


@Serializable
data class PaisModel(
    val name: Nombre,
    val flags: Bandera
)

@Serializable
data class Nombre(
    val common: String,
    val official: String,
)

@Serializable
data class Bandera(
    val png: String,
    val svg: String,
    val alt: String
)