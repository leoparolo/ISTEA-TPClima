package com.example.istea_tpclima.infrastructure.implementations

import com.example.istea_tpclima.core.modelos.Bandera
import com.example.istea_tpclima.core.modelos.Nombre
import com.example.istea_tpclima.core.modelos.PaisModel
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field

class PaisRepositoryTest {

    private lateinit var repository: PaisRepository
    private lateinit var originalClient: HttpClient

    @Before
    fun setup() {
        repository = PaisRepository()
        val field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        originalClient = field.get(HttpClientProvider) as HttpClient
    }

    @After
    fun tearDown() {
        val field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        field.set(HttpClientProvider, originalClient)
    }

    private fun mockHttpClient(content: String, status: HttpStatusCode) {
        val engine = MockEngine {
            respond(
                content,
                status,
                headersOf("Content-Type" to listOf("application/json"))
            )
        }

        val mockClient = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }

        val field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        field.set(HttpClientProvider, mockClient)
    }

    @Test
    fun `devuelve pais cuando API responde OK`() = runTest {
        val pais = listOf(
            PaisModel(
                name = Nombre("Argentina", "Argentina República"),
                flags = Bandera("flag.png", "flag.svg", "desc")
            )
        )

        mockHttpClient(
            Json.encodeToString(pais),
            HttpStatusCode.OK
        )

        val result = repository.get("AR")
        assertEquals(1, result.size)
        assertEquals("Argentina", result.first().name.common)
    }

    @Test(expected = Exception::class)
    fun `lanza error si API responde error`() = runTest {
        mockHttpClient("{}", HttpStatusCode.BadRequest)
        repository.get("AR")
    }
}