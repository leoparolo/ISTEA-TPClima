package com.example.istea_tpclima.infrastructure.implementations

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.example.istea_tpclima.core.modelos.CoordenadasModel
import com.google.android.gms.location.FusedLocationProviderClient
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LocationRepositoryTest {

    private lateinit var context: Context
    private lateinit var fused: FusedLocationProviderClient
    private lateinit var repo: LocationRepository

    @Before
    fun setup() {
        context = mockk()
        fused = mockk()
        repo = spyk(LocationRepository(context))

        mockkConstructor(FusedLocationProviderClient::class)
    }

    @Test
    fun `si no hay permisos devuelve null`() = runTest {
        every { ActivityCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_DENIED

        val result = repo.getCoordinates()

        assertNull(result)
    }

    @Test
    fun `si hay permisos y devuelve location correcta`() = runTest {
        // 1) permisos OK
        every { ActivityCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_GRANTED

        // 2) mock de Location real
        val loc = mockk<Location>()
        every { loc.latitude } returns 10.0
        every { loc.longitude } returns 20.0

        // 3) mock de Task<Location>
        val task = mockk<com.google.android.gms.tasks.Task<Location>>(relaxed = true)

        every { task.addOnSuccessListener(any()) } answers {
            val listener = arg<(Location?) -> Unit>(0)
            listener(loc)
            task
        }

        every { task.addOnFailureListener(any()) } answers {
            // no falla
            task
        }

        // 4) Mockear fused.lastLocation para que devuelva el Task mockeado
        every { fused.lastLocation } returns task

        // 5) ejecutar
        val result = repo.getCoordinates()

        // 6) verificar resultado
        assertEquals(CoordenadasModel(10f, 20f), result)
    }
}