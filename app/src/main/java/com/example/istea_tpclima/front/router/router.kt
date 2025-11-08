package com.example.istea_tpclima.front.router

interface router {
    fun navegar(ruta: Ruta )
}

sealed class Ruta(val id: String) {
    data object Ciudades: Ruta("ciudades")
    data class Clima(val lat: Float,val lon:Float, val nombre:String): Ruta("clima")
}