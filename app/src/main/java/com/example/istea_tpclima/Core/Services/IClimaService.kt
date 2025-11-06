package com.example.istea_tpclima.Core.Services

import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Core.Modelos.ClimaModel

interface IClimaService{
    suspend fun obtenerClima(ciudad: CiudadModel): ClimaModel
//    suspend fun obtenerPorCiudad(ciudad: CiudadModel): ClimaModel
//    suspend fun buscarCiudadPorNombre(nombre: String, pais: String? = null): CiudadModel?
}