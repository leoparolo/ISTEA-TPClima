package com.example.istea_tpclima.app.navigation

sealed class Screen(val route: String) {
    data object Clima : Screen("clima")

    data object Ciudades : Screen("ciudades")
}
