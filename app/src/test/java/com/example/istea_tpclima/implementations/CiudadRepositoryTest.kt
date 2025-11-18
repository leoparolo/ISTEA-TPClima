package com.example.istea_tpclima.infrastructure.implementations

import com.example.istea_tpclima.core.modelos.*
import com.example.istea_tpclima.core.repositories.IPaisRepository
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field

class CiudadRepositoryTest {

    private lateinit var paisRepo: IPaisRepository
    private lateinit var repository: CiudadRepository

    // Para restaurar el HttpClient real luego
    private lateinit var originalClient: HttpClient

    @Before
    fun setup() {
        paisRepo = mockk()
        repository = CiudadRepository(paisRepo)

        // Guardamos el cliente original
        val field: Field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        originalClient = field.get(HttpClientProvider) as HttpClient
    }

    @After
    fun teardown() {
        // restauramos el cliente original
        val field: Field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        field.set(HttpClientProvider, originalClient)
    }

    // Utilidad para reemplazar HttpClientProvider.client
    private fun mockHttpClient(jsonResponse: String, status: HttpStatusCode = HttpStatusCode.OK) {
        val engine = MockEngine { request ->
            respond(
                content = jsonResponse,
                status = status,
                headers = headersOf("Content-Type" to listOf("application/json"))
            )
        }

        val mockClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val field: Field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        field.set(HttpClientProvider, mockClient)
    }

    @Test
    fun `get devuelve ciudades correctamente`() = runTest {
        val mockCities = listOf(
            CiudadModel(
                name = "Buenos Aires",
                lat = -34.6f,
                lon = -58.4f,
                country = "AR"
            )
        )

        mockHttpClient(Json.encodeToString(mockCities))

        val result = repository.get("Buenos Aires")

        assertEquals(1, result.size)
        assertEquals("Buenos Aires", result[0].name)
        assertEquals("AR", result[0].country)
    }

    @Test(expected = Exception::class)
    fun `get lanza excepción si el status no es OK`() = runTest {
        mockHttpClient("{}", HttpStatusCode.BadRequest)

        repository.get("Madrid")
    }

    @Test
    fun `getWFlag une datos de paises correctamente`() = runTest {
        val ciudad = CiudadModel("Madrid", 3f, 4f, "ES")

        mockHttpClient(Json.encodeToString(listOf(ciudad)))

        // Mock del paisRepo
        val paisMock = PaisModel(
            name = Nombre("España", "Reino de España"),
            flags = Bandera("png", "svg", "alt")
        )

        coEvery { paisRepo.get("ES") } returns listOf(paisMock)

        val result = repository.getWFlag("Madrid")

        assertEquals(1, result.size)
        assertEquals("España", result[0].countryFullName)
        assertEquals("svg", result[0].flag)
    }
}