package com.example.devmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.devmobile.ui.Pages.Trails.RandoShow
import com.example.devmobile.ui.theme.DevMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevMobileTheme {
                val navController = rememberNavController()
                // Ajouter les routes ici et changer l'attribut 'startDestination' par la route pour la homePage
                NavHost(navController, startDestination = "show") {
                    composable("show") {
                        RandoShow(navController)
                    }
                    composable("details/{randonneeNom}") { backStackEntry ->
                        val randonneeNom = backStackEntry.arguments?.getString("randonneeNom")
                        randonneeNom?.let {
                           Text(text = it)
                        }
                    }
                }
            }
        }
    }
}