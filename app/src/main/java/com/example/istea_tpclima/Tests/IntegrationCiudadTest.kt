package com.example.istea_tpclima.Tests

import com.example.istea_tpclima.Infrastructure.Implementations.CiudadService
import com.example.istea_tpclima.Infrastructure.Implementations.PaisService
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val paisservice = PaisService()
    val ciudadservice = CiudadService(paisservice)


    try {
        val ciudades = ciudadservice.get("buenos aires")
        println("✅ Ciudades encontradas:")

        ciudades.forEach {
            println("${it.name} - ${it.country} - ${it.state}")
            val paises = paisservice.get(it.country)
            paises.forEach {
                println("${it.name.common} - ${it.flags.svg}")
            }
        }
    } catch (e: Exception) {
        println("❌ Error: ${e.message}")
    }
}