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
) : ICiudadService {

    private val client = HttpClientProvider.client
    private val APIKey = "d867b80e9ff8e2822fde457ee118deba"

    // ------------------------------------------------------
    // 1) Búsqueda por nombre
    // ------------------------------------------------------
    override suspend fun get(ciudad: String): List<CiudadModel> {
        val respuesta = client.get(ApiRouter.LOCATIONS) {
            parameter("q", ciudad)
            parameter("limit", 100)
            parameter("appid", APIKey)
        }

        return if (respuesta.status == HttpStatusCode.OK) {
            respuesta.body()
        } else {
            emptyList()
        }
    }

    // ------------------------------------------------------
    // 2) Búsqueda por nombre + bandera / país
    // ------------------------------------------------------
    override suspend fun getWFlag(ciudad: String): List<CiudadModel> {
        val result = mutableListOf<CiudadModel>()
        val ciudades = get(ciudad)

        for (c in ciudades) {
            val paises = paisservice.get(c.country)

            val mapeadas = paises.map {
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

            result.addAll(mapeadas)
        }

        return result.toList()
    }

    // ------------------------------------------------------
    // 3) NUEVO — Búsqueda precisa por coordenadas
    //     Usa reverse geocoding: /geo/1.0/reverse
    // ------------------------------------------------------
    override suspend fun getPorLatLon(lat: Double, lon: Double): List<CiudadModel> {
        val respuesta = client.get(ApiRouter.REVERSE_LOCATIONS) {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("limit", 1)
            parameter("appid", APIKey)
        }

        return if (respuesta.status == HttpStatusCode.OK) {
            respuesta.body()
        } else {
            emptyList()
        }
    }
}
