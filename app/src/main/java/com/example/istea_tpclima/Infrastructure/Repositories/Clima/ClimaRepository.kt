package com.example.istea_tpclima.Infrastructure.Repositories.Clima

import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Core.Modelos.ClimaModel

interface ClimaRepository {
    suspend fun obtenerPorCiudad(ciudad: CiudadModel): ClimaModel
    suspend fun buscarCiudadPorNombre(nombre: String, pais: String? = null): CiudadModel?
}
