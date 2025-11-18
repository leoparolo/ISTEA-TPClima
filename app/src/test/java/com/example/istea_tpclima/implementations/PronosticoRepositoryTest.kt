package com.example.istea_tpclima.infrastructure.implementations

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field

class PronosticoRepositoryTest {

    private lateinit var repository: PronosticoRepository
    private lateinit var originalClient: HttpClient

    @Before
    fun setup() {
        repository = PronosticoRepository()
        val field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        originalClient = field.get(HttpClientProvider) as HttpClient
    }

    @After
    fun restore() {
        val field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        field.set(HttpClientProvider, originalClient)
    }

    private fun mockHttp(content: String, status: HttpStatusCode) {
        val engine = MockEngine {
            respond(
                content,
                status,
                headersOf("Content-Type" to listOf("application/json"))
            )
        }

        val client = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }

        val field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        field.set(HttpClientProvider, client)
    }

    @Test
    fun `get devuelve lista si todo sale bien`() = runTest {
        val mockList = listOf("soleado", "nublado")

        mockHttp(Json.encodeToString(mockList), HttpStatusCode.OK)

        val result = repository.get("Buenos Aires")

        assertEquals(2, result.size)
        assertEquals("soleado", result[0])
    }

    @Test(expected = Exception::class)
    fun `lanza error si status != 200`() = runTest {
        mockHttp("{}", HttpStatusCode.BadRequest)
        repository.get("Madrid")
    }
}