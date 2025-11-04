package com.example.istea_tpclima.Infrastructure.Storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.istea_tpclima.Core.Modelos.CiudadModel
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_prefs")

class Prefs(private val context: Context) {
    private val KEY_ID = stringPreferencesKey("ciudad_id")
    private val KEY_NOMBRE = stringPreferencesKey("ciudad_nombre")
    private val KEY_PAIS = stringPreferencesKey("ciudad_pais")

    suspend fun guardar(ciudad: CiudadModel) {
        context.dataStore.edit {
            it[KEY_ID] = ciudad.id
            it[KEY_NOMBRE] = ciudad.nombre
            it[KEY_PAIS] = ciudad.pais
        }
    }

    fun leer() = context.dataStore.data.map { p ->
        val id = p[KEY_ID]
        val n = p[KEY_NOMBRE]
        val pa = p[KEY_PAIS]
        if (id != null && n != null && pa != null) CiudadModel(id, n, pa) else null
    }

    suspend fun limpiar() = context.dataStore.edit { it.clear() }
}
