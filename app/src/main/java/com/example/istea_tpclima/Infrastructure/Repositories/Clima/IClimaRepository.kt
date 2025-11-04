package com.example.istea_tpclima.Infrastructure.Repositories.Clima

import com.example.istea_tpclima.Core.Modelos.*

interface IClimaRepository {
    suspend fun obtenerClima(ciudad: CiudadModel): ClimaModel
}
