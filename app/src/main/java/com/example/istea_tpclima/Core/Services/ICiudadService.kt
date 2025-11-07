package com.example.istea_tpclima.Core.Services

import com.example.istea_tpclima.Core.Modelos.CiudadModel

interface ICiudadService{
    suspend fun get(ciudad: String): List<CiudadModel>
    suspend fun getWFlag(ciudad: String): List<CiudadModel>
}