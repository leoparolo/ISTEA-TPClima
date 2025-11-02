package com.example.istea_tpclima.Infrastructure.Implementations

import com.example.istea_tpclima.Core.Services.IPronosticoService
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class PronosticoService : IPronosticoService
{
    private val client = HttpClientProvider.client
    private val PARTIAL_URI = "https://api.openweathermap.org/data/2.5/forecast"
    private val APIKey = "d867b80e9ff8e2822fde457ee118deba"
    override suspend fun get(nombre: String): List<String> {
        val respuesta = client.get(PARTIAL_URI){
            parameter("q",nombre)
            parameter("units","metric")
            parameter("appid",APIKey)
        }
        if (respuesta.status == HttpStatusCode.OK){
            val forecast = respuesta.body<List<String>>()
            return forecast
        }else{
            throw Exception()
        }
    }
}