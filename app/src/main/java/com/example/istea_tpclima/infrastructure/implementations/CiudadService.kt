package com.example.istea_tpclima.infrastructure.implementations

import com.example.istea_tpclima.core.modelos.CiudadModel
import com.example.istea_tpclima.core.services.ICiudadService
import com.example.istea_tpclima.core.services.IPaisService
import com.example.istea_tpclima.infrastructure.shared.ApiRouter
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class CiudadService(
    private val paisservice: IPaisService
) : ICiudadService
{
    private val client = HttpClientProvider.client
    private val APIKey = "d867b80e9ff8e2822fde457ee118deba"

    override suspend fun get(ciudad: String): List<CiudadModel> {
        val respuesta = client.get(urlString = ApiRouter.LOCATIONS){
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
    override suspend fun getWFlag(ciudad: String): List<CiudadModel> {
        try {
            val result = mutableListOf<CiudadModel>()
            val ciudades = get(ciudad)
            for(c in ciudades)
            {
                val paises = paisservice.get(c.country)
                val mapToCiudad = paises.map {
                    CiudadModel(
                        name = c.name,
                        countryFullName = it.name.common,
                        flag = it.flags.svg,
                        lat = c.lat,
                        lon = c.lon,
                        country = c.country,
                        state = c.state
                    )
                }
                result.addAll(mapToCiudad)
            }
            return result.toList()
        } catch (e: Exception) {
            throw  Exception()
        }
    }
}