package com.example.istea_tpclima.core.repositories

interface IPronosticoRepository{
    suspend fun get(nombre: String): List<String>
}