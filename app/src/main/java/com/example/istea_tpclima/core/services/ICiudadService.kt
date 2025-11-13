package com.example.istea_tpclima.core.services

import com.example.istea_tpclima.core.modelos.CiudadModel

interface ICiudadService {

    // 🔹 Búsqueda por nombre (direct geocoding)
    suspend fun get(ciudad: String): List<CiudadModel>

    // 🔹 Búsqueda por nombre + enriquecida con bandera / país
    suspend fun getWFlag(ciudad: String): List<CiudadModel>

    // 🔹 NUEVO: búsqueda por coordenadas (reverse geocoding)
    suspend fun getPorLatLon(lat: Double, lon: Double): List<CiudadModel>
}
