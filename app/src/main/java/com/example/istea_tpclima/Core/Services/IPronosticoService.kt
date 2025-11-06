package com.example.istea_tpclima.Core.Services

interface IPronosticoService{
    suspend fun get(nombre: String): List<String>
}