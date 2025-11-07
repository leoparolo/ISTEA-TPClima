package com.example.istea_tpclima.Infrastructure.Implementations

import com.example.istea_tpclima.Core.Modelos.CiudadModel
import com.example.istea_tpclima.Core.Services.ICiudadService
import com.example.istea_tpclima.Core.Services.IPaisService
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlin.collections.forEach

class CiudadService(
    private val paisservice: IPaisService
) : ICiudadService
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