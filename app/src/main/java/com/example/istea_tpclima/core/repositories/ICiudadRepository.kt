package com.example.istea_tpclima.core.repositories

import com.example.istea_tpclima.core.modelos.CiudadModel

interface ICiudadRepository{
    suspend fun get(ciudad: String): List<CiudadModel>
    suspend fun getWFlag(ciudad: String): List<CiudadModel>
}