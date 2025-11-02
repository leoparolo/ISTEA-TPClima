package com.example.istea_tpclima.Core.Services

interface IClimaService{
    suspend fun get(lat: Float, lon: Float) : String
}