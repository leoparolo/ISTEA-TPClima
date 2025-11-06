package com.example.istea_tpclima.Infrastructure.Implementations

import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Core.Modelos.PaisModel
import com.example.istea_tpclima.Core.Services.IPaisService
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class PaisService : IPaisService
{
    private val client = HttpClientProvider.client
    private val PARTIAL_URI = "https://restcountries.com/v3.1/alpha/"

    override suspend fun get(pais: String): List<PaisModel> {
        val respuesta = client.get(urlString = PARTIAL_URI + pais)

        if (respuesta.status == HttpStatusCode.OK){
            val pais = respuesta.body<List<PaisModel>>()
            return pais
        }else{
            throw Exception()
        }
    }
}