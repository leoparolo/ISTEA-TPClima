package com.example.istea_tpclima.infrastructure.implementations

import com.example.istea_tpclima.core.modelos.PaisModel
import com.example.istea_tpclima.core.services.IPaisService
import com.example.istea_tpclima.infrastructure.shared.ApiRouter
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class PaisService : IPaisService
{
    private val client = HttpClientProvider.client

    override suspend fun get(pais: String): List<PaisModel> {
        val respuesta = client.get(urlString = ApiRouter.COUNTRIES + pais)

        if (respuesta.status == HttpStatusCode.OK){
            val pais = respuesta.body<List<PaisModel>>()
            return pais
        }else{
            throw Exception()
        }
    }
}