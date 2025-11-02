package com.example.istea_tpclima.Infrastructure.Implementations

import com.example.istea_tpclima.Core.Services.IClimaService
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class ClimaService : IClimaService
{
    private val client = HttpClientProvider.client
    private val PARTIAL_URI = "https://api.openweathermap.org/data/2.5/weather"
    private val APIKey = "d867b80e9ff8e2822fde457ee118deba"
    override suspend fun get(lat: Float, lon: Float): String {
        val respuesta = client.get(PARTIAL_URI){
            parameter("lat",lat)
            parameter("lon",lon)
            parameter("units","metric")
            parameter("appid",APIKey)
        }
        if (respuesta.status == HttpStatusCode.OK){
            val clima = respuesta.body<String>()
            return clima
        }else{
            throw Exception()
        }
    }
}