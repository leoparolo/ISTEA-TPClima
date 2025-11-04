package com.example.istea_tpclima.Infrastructure.Storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.example.istea_tpclima.Core.Modelos.CiudadModel
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import java.io.IOException

class Prefs(context: Context) {

    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("clima_prefs") }
    )

    private val KEY_CITY = stringPreferencesKey("city_name")


    fun leer(): Flow<CiudadModel?> =
        dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { prefs ->
                val nombre = prefs[KEY_CITY]
                if (nombre.isNullOrBlank()) null else CiudadModel(nombre = nombre)
            }


    suspend fun guardar(ciudad: CiudadModel) {
        dataStore.edit { it[KEY_CITY] = ciudad.nombre }
    }
}
