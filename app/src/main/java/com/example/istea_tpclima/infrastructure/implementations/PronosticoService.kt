package com.example.istea_tpclima.infrastructure.implementations

import com.example.istea_tpclima.core.services.IPronosticoService
import com.example.istea_tpclima.infrastructure.shared.ApiRouter
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class PronosticoService : IPronosticoService
{
    private val client = HttpClientProvider.client
    private val APIKey = "d867b80e9ff8e2822fde457ee118deba"
    override suspend fun get(nombre: String): List<String> {
        val respuesta = client.get(ApiRouter.FORECAST){
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