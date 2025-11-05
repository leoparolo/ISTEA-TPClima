package com.example.istea_tpclima.Core.Services

import com.example.istea_tpclima.Core.Modelos.PaisModel

interface IPaisService {
    suspend fun get(pais: String): List<PaisModel>
}