package com.example.istea_tpclima.core.repositories

import com.example.istea_tpclima.core.modelos.PaisModel

interface IPaisRepository {
    suspend fun get(pais: String): List<PaisModel>
}