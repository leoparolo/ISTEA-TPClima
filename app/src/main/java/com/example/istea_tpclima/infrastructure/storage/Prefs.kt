package com.example.istea_tpclima.infrastructure.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.istea_tpclima.core.modelos.CiudadModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// ✅ DataStore SINGLETON asociado al Context
private val Context.dataStore by preferencesDataStore(name = "clima_prefs")

class Prefs(private val context: Context) {

    companion object {
        private val KEY_NOMBRE = stringPreferencesKey("nombre")
        private val KEY_COUNTRY = stringPreferencesKey("country")
        private val KEY_COUNTRY_FULL_NAME = stringPreferencesKey("countryFullName")
        private val KEY_FLAG = stringPreferencesKey("flag")
        private val KEY_STATE = stringPreferencesKey("state")
        private val KEY_LAT = floatPreferencesKey("lat")
        private val KEY_LON = floatPreferencesKey("lon")
    }

    // Lee la ciudad guardada como Flow<CiudadModel?>
    fun leer(): Flow<CiudadModel?> =
        context.dataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs ->
                val nombre = prefs[KEY_NOMBRE]
                val country = prefs[KEY_COUNTRY]
                val countryFullName = prefs[KEY_COUNTRY_FULL_NAME]
                val flag = prefs[KEY_FLAG]
                val state = prefs[KEY_STATE]
                val lat = prefs[KEY_LAT]
                val lon = prefs[KEY_LON]

                if (nombre == null || lat == null || lon == null) {
                    null
                } else {
                    CiudadModel(
                        name = nombre,
                        country = country ?: "",
                        countryFullName = countryFullName ?: "",
                        flag = flag ?: "",
                        state = state ?: "",
                        lat = lat,
                        lon = lon
                    )
                }
            }

    // Guarda la ciudad elegida
    suspend fun guardar(ciudad: CiudadModel) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOMBRE] = ciudad.name
            prefs[KEY_COUNTRY] = ciudad.country
            prefs[KEY_COUNTRY_FULL_NAME] = ciudad.countryFullName ?: ""
            prefs[KEY_FLAG] = ciudad.flag ?: ""
            prefs[KEY_STATE] = ciudad.state ?: ""
            prefs[KEY_LAT] = ciudad.lat
            prefs[KEY_LON] = ciudad.lon
        }
    }

    // (Opcional) limpiar prefs
    suspend fun limpiar() {
        context.dataStore.edit { it.clear() }
    }
}
