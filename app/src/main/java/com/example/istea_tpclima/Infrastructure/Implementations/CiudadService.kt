package com.example.istea_tpclima.Infrastructure.Implementations

import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Core.Services.ICiudadService
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class CiudadService : ICiudadService
{
    private val client = HttpClientProvider.client
    private val PARTIAL_URI = "https://api.openweathermap.org/geo/1.0/direct"
    private val APIKey = "d867b80e9ff8e2822fde457ee118deba"

    override suspend fun get(ciudad: String): List<CiudadModel> {
        val respuesta = client.get(urlString = PARTIAL_URI){
            parameter("q",ciudad)
            parameter("limit",100)
            parameter("appid",APIKey)
        }

        if (respuesta.status == HttpStatusCode.OK){
            val ciudades = respuesta.body<List<CiudadModel>>()
            return ciudades
        }else{
            throw Exception()
        }
    }
}