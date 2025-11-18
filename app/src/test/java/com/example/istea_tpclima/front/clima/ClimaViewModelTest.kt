package com.example.istea_tpclima.front.clima

import com.example.istea_tpclima.core.features.clima.ClimaEstado
import com.example.istea_tpclima.core.features.clima.ClimaIntencion
import com.example.istea_tpclima.core.modelos.CiudadModel
import com.example.istea_tpclima.core.modelos.ClimaDia
import com.example.istea_tpclima.core.modelos.ClimaModel
import com.example.istea_tpclima.infrastructure.implementations.ClimaRepository
import com.example.istea_tpclima.infrastructure.storage.Prefs
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ClimaViewModelTest {

    private lateinit var repo: ClimaRepository
    private lateinit var prefs: Prefs
    private lateinit var vm: ClimaViewModel

    @Before
    fun setup() {
        repo = mockk()
        prefs = mockk()
        vm = ClimaViewModel(repo, prefs)
    }

    @Test
    fun `cargar ciudad guardada la muestra`() = runTest {
        val ciudad = CiudadModel("Roma", 10f, 20f, "IT")
        val clima = ClimaModel(ciudad, ClimaModel.Actual(20, "Soleado", 60), emptyList())

        every { prefs.leer() } returns flowOf(ciudad)
        coEvery { repo.obtenerClima(ciudad) } returns clima

        vm.ejecutar(ClimaIntencion.CargarPorCiudadGuardada)

        assert(vm.uiState is ClimaEstado.Mostrando)
    }

    @Test
    fun `refrescar vuelve a cargar clima`() = runTest {
        val ciudad = CiudadModel("Roma", 10f, 20f, "IT")
        val clima = ClimaModel(ciudad, ClimaModel.Actual(20, "Soleado", 60), emptyList())

        vm.uiState = ClimaEstado.Mostrando(clima)

        coEvery { repo.obtenerClima(ciudad) } returns clima

        vm.ejecutar(ClimaIntencion.Refrescar)

        assert(vm.uiState is ClimaEstado.Mostrando)
    }

    @Test
    fun `compartir genera texto correctamente`() = runTest {
        val ciudad = CiudadModel("Madrid", 10f, 10f, "ES", "España")

        every { prefs.leer() } returns flowOf(ciudad)

        vm.ejecutar(ClimaIntencion.CompartirClick)

        val state = vm.uiState as ClimaEstado.Compartir

        assert(state.texto.contains("Ciudad: Madrid"))
        assert(state.texto.contains("País: España"))
    }
}