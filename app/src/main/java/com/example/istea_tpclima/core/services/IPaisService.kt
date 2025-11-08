package com.example.istea_tpclima.core.services

import com.example.istea_tpclima.core.modelos.PaisModel

interface IPaisService {
    suspend fun get(pais: String): List<PaisModel>
}