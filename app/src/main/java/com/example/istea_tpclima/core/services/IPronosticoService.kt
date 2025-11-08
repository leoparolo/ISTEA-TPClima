package com.example.istea_tpclima.core.services

interface IPronosticoService{
    suspend fun get(nombre: String): List<String>
}