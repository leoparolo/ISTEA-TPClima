package com.example.istea_tpclima.infrastructure.implementations

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

class LocationRepository(private val context: Context) {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    // Devuelve el nombre de la ciudad según la ubicación actual
    @SuppressLint("MissingPermission")
    suspend fun getCurrentCityName(): String? {
        // Última ubicación conocida (requiere permisos concedidos)
        val location = client.lastLocation.await() ?: return null

        val geocoder = Geocoder(context, Locale.getDefault())

        val results = withContext(Dispatchers.IO) {
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        }

        val address = results?.firstOrNull() ?: return null

        // locality suele ser la ciudad
        return address.locality
            ?: address.subAdminArea
            ?: address.adminArea
    }
}