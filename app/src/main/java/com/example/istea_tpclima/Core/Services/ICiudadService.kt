package com.example.istea_tpclima.Core.Services

interface ICiudadService{
    suspend fun get(ciudad: String): List<String>
}