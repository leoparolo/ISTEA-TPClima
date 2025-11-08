package com.example.istea_tpclima.core.services

import com.example.istea_tpclima.core.modelos.CiudadModel

interface ICiudadService{
    suspend fun get(ciudad: String): List<CiudadModel>
    suspend fun getWFlag(ciudad: String): List<CiudadModel>
}