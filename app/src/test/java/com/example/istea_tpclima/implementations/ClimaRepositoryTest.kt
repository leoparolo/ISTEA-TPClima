package com.example.istea_tpclima.infrastructure.implementations

import com.example.istea_tpclima.core.dtos.*
import com.example.istea_tpclima.core.modelos.CiudadModel
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
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

class ClimaRepositoryTest {

    private lateinit var repository: ClimaRepository
    private lateinit var originalClient: HttpClient

    @Before
    fun setUp() {
        repository = ClimaRepository()

        val field: Field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        originalClient = field.get(HttpClientProvider) as HttpClient
    }

    @After
    fun restore() {
        val field: Field = HttpClientProvider::class.java.getDeclaredField("client")
        field.isAccessible = true
        field.set(HttpClientProvider, originalClient)
    }

    private fun mockHttpClientSequential(vararg responses: Pair<String, HttpStatusCode>) {
        var index = 0

        val engine = MockEngine { _ ->
            val (content, status) = responses[index]
            index++
            respond(
                content = content,
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
    fun `obtenerClima devuelve datos correctos`() = runTest {
        val ciudad = CiudadModel("Madrid", 40.0f, -3.7f, "ES")

        val current = OpenWeatherCurrent(
            main = MainBlock(temp = 12.5, humidity = 60),
            weather = listOf(WeatherDesc(description = "nublado"))
        )

        val forecast = OpenWeatherForecast5d(
            city = ForecastCity(timezone = 3600),
            list = listOf(
                ForecastItem(
                    dt = 1700000000,
                    main = MainBlock(temp_min = 5.0, temp_max = 10.0)
                )
            )
        )

        mockHttpClientSequential(
            Json.encodeToString(current) to HttpStatusCode.OK,
            Json.encodeToString(forecast) to HttpStatusCode.OK
        )

        val result = repository.obtenerClima(ciudad)

        assertEquals("Madrid", result.ciudad.name)
        assertEquals(12, result.actual.tempC)
        assertEquals("nublado", result.actual.descripcion)
        assertEquals(1, result.proximos5Dias.size)
    }

    @Test(expected = Exception::class)
    fun `obtenerClima falla si weather devuelve error`() = runTest {
        val ciudad = CiudadModel("Madrid", 40f, -3f, "ES")

        mockHttpClientSequential(
            "{}" to HttpStatusCode.BadRequest,   // weather falla
            "{}" to HttpStatusCode.OK            // forecast OK pero no importa
        )

        repository.obtenerClima(ciudad)
    }

    @Test(expected = Exception::class)
    fun `obtenerClima falla si forecast devuelve error`() = runTest {
        val ciudad = CiudadModel("Madrid", 40f, -3f, "ES")

        mockHttpClientSequential(
            "{}" to HttpStatusCode.OK,            // weather OK
            "{}" to HttpStatusCode.BadRequest     // forecast falla
        )

        repository.obtenerClima(ciudad)
    }
}