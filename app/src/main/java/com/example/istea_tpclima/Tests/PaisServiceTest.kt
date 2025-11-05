package com.example.istea_tpclima.Tests

import com.example.istea_tpclima.Infrastructure.Implementations.PaisService
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val service = PaisService()

    try {
        val paises = service.get("ar")
        paises.forEach { println("${it.name.common} - ${it.flags.svg}") }
    }catch (e: Exception){
        println("❌ Error: ${e.message}")
    }
}