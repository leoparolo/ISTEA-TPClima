package com.example.istea_tpclima.front.ciudad

import com.example.istea_tpclima.core.features.ciudades.CiudadEstado
import com.example.istea_tpclima.core.features.ciudades.CiudadIntencion
import com.example.istea_tpclima.core.modelos.CiudadModel
import com.example.istea_tpclima.core.repositories.ICiudadRepository
import com.example.istea_tpclima.front.router.Ruta
import com.example.istea_tpclima.front.router.router
import com.example.istea_tpclima.infrastructure.implementations.LocationRepository
import com.example.istea_tpclima.infrastructure.storage.Prefs
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CiudadesViewModelTest {

    private lateinit var repo: ICiudadRepository
    private lateinit var router: router
    private lateinit var prefs: Prefs
    private lateinit var locRepo: LocationRepository
    private lateinit var vm: CiudadesViewModel

    @Before
    fun setup() {
        repo = mockk()
        router = mockk(relaxed = true)
        prefs = mockk(relaxed = true)
        locRepo = mockk()

        vm = CiudadesViewModel(repo, router, locRepo, prefs)
    }

    @Test
    fun `buscar ciudad actualiza estado`() = runTest {
        val mockCiudad = CiudadModel("Madrid", 10f, 20f, "ES")

        coEvery { repo.getWFlag("Mad") } returns listOf(mockCiudad)

        vm.ejecutar(CiudadIntencion.buscar("Mad"))

        assert(vm.uiState is CiudadEstado.resultado)
        assertEquals(1, vm.ciudades.size)
    }

    @Test
    fun `seleccionar ciudad guarda en prefs y navega`() = runTest {
        val ciudad = CiudadModel("Roma", 40f, 20f, "IT")

        vm.ejecutar(CiudadIntencion.seleccionar(ciudad))

        coVerify { prefs.guardar(ciudad) }
        verify { router.navegar(any()) }
    }

    @Test
    fun `geolocalizar obtiene coords y busca ciudad`() = runTest {
        coEvery { locRepo.getCoordinates() } returns com.example.istea_tpclima.core.modelos.CoordenadasModel(10f, 20f)

        val base = CiudadModel("Paris", 10f, 20f, "FR")
        coEvery { repo.getPorLatLon(any(), any()) } returns listOf(base)
        coEvery { repo.getWFlag("Paris") } returns listOf(base)

        vm.ejecutar(CiudadIntencion.geolocalizar)

        coVerify { repo.getPorLatLon(10.0, 20.0) }
        assert(vm.uiState !is CiudadEstado.error)
    }
}