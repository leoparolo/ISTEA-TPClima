package com.example.istea_tpclima.Infrastructure.Repositories.Clima

import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Core.Modelos.ClimaModel

interface IClimaRepository {
    suspend fun obtenerClima(ciudad: CiudadModel): ClimaModel
}
