package com.example.istea_tpclima.infrastructure.shared

object ApiRouter {

    private const val BASE = "https://api.openweathermap.org"

    // --- GEO ---
    const val LOCATIONS = "$BASE/geo/1.0/direct"
    const val REVERSE_LOCATIONS = "$BASE/geo/1.0/reverse"

    // --- WEATHER ---
    const val WEATHER = "$BASE/data/2.5/weather"
    const val FORECAST = "$BASE/data/2.5/forecast"

    // --- REST COUNTRIES (para obtener bandera y nombre del país) ---
    private const val BASE_COUNTRIES = "https://restcountries.com/v3.1/alpha/"
    const val COUNTRIES = BASE_COUNTRIES
}
