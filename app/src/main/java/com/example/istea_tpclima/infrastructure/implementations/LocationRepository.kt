package com.example.istea_tpclima.infrastructure.implementations

//import android.annotation.SuppressLint
//import android.content.Context
//import android.location.Geocoder
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.withContext
//import java.util.Locale
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.example.istea_tpclima.core.modelos.CoordenadasModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

class LocationRepository(private val context: Context) {

    private val fused: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // ------------------------------------------------------
    // 1) Obtener latitud y longitud (Opción B)
    // ------------------------------------------------------
    @SuppressLint("MissingPermission")
    suspend fun getCoordinates(): CoordenadasModel? {

        val fine = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarse = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fine != PackageManager.PERMISSION_GRANTED &&
            coarse != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        return suspendCancellableCoroutine { cont ->
            fused.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    if (loc != null) {
                        cont.resume(
                            CoordenadasModel(
                                lat = loc.latitude.toFloat(),
                                lon = loc.longitude.toFloat()
                            )
                        )
                    } else {
                        cont.resume(null)
                    }
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
    }

    // ------------------------------------------------------
    // 2) Obtener nombre de ciudad (Opción A)
    // ------------------------------------------------------
    @SuppressLint("MissingPermission")
    suspend fun getCurrentCityName(): String? {

        val fine = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarse = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fine != PackageManager.PERMISSION_GRANTED &&
            coarse != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        return suspendCancellableCoroutine { cont ->
            fused.lastLocation
                .addOnSuccessListener { loc ->
                    if (loc == null) {
                        cont.resume(null)
                        return@addOnSuccessListener
                    }

                    try {
                        val geo = Geocoder(context, Locale.getDefault())
                        val res = geo.getFromLocation(loc.latitude, loc.longitude, 1)
                        val city = res?.firstOrNull()?.locality
                        cont.resume(city)
                    } catch (e: Exception) {
                        cont.resume(null)
                    }
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
    }
}