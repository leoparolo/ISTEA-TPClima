package com.example.istea_tpclima.infrastructure.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.example.istea_tpclima.core.modelos.CiudadModel
import java.io.IOException

class Prefs(context: Context) {

    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("clima_prefs") }
    )
    private val KEY_NAME = stringPreferencesKey("city_name")
    private val KEY_COUNTRY = stringPreferencesKey("city_country")
    private val KEY_STATE = stringPreferencesKey("city_state")
    private val KEY_LAT = stringPreferencesKey("city_lat")
    private val KEY_LON = stringPreferencesKey("city_lon")

    fun leer(): Flow<CiudadModel?> =
        dataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs ->
                val name = prefs[KEY_NAME]
                val lat = prefs[KEY_LAT]?.toFloatOrNull()
                val lon = prefs[KEY_LON]?.toFloatOrNull()
                val country = prefs[KEY_COUNTRY]
                val state = prefs[KEY_STATE].toString()
                if (name == null || lat == null || lon == null) null
                else CiudadModel(name, lat, lon, country ?: "", state)
            }

    suspend fun guardar(ciudad: CiudadModel) {
        dataStore.edit {
            it[KEY_NAME] = ciudad.name
            it[KEY_COUNTRY] = ciudad.country
            it[KEY_STATE] = ciudad.state ?: ""
            it[KEY_LAT] = ciudad.lat.toString()
            it[KEY_LON] = ciudad.lon.toString()
        }
    }
}