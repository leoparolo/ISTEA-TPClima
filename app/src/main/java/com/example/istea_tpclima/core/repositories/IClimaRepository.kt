package com.example.istea_tpclima.core.repositories

import com.example.istea_tpclima.core.modelos.CiudadModel
import com.example.istea_tpclima.core.modelos.ClimaModel

interface IClimaRepository{
    suspend fun obtenerClima(ciudad: CiudadModel): ClimaModel
//    suspend fun obtenerPorCiudad(ciudad: CiudadModel): ClimaModel
//    suspend fun buscarCiudadPorNombre(nombre: String, pais: String? = null): CiudadModel?
}