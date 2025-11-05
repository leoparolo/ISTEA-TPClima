package com.example.istea_tpclima

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.istea_tpclima.Front.Ciudad.CiudadPage
import com.example.istea_tpclima.Front.Router.Ruta

@Composable
fun MainPage() {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = Ruta.Ciudades.id
    ) {
        composable(
            route = Ruta.Ciudades.id
        ) {
            CiudadPage(navHostController)
        }
//        composable(
//            route = "clima?lat={lat}&lon={lon}&nombre={nombre}",
//            arguments =  listOf(
//                navArgument("lat") { type= NavType.FloatType },
//                navArgument("lon") { type= NavType.FloatType },
//                navArgument("nombre") { type= NavType.StringType }
//            )
//        ) {
//            val lat = it.arguments?.getFloat("lat") ?: 0.0f
//            val lon = it.arguments?.getFloat("lon") ?: 0.0f
//            val nombre = it.arguments?.getString("nombre") ?: ""
//            ClimaPage(navHostController, lat = lat, lon = lon, nombre = nombre)
//        }
    }
}