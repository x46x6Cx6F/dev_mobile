package com.example.devmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.devmobile.ui.theme.DevMobileTheme
import com.example.devmobile.ui.Pages.Home.HomePage
import com.example.devmobile.ui.Pages.Home.TrailDetailsScreen
import com.example.devmobile.ui.Pages.QRScanner.QRScannerPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevMobileTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        HomePage(navController)
                    }
                    composable("trailDetails/{trailId}") { backStackEntry ->
                        val trailId = backStackEntry.arguments?.get("trailId")
                        TrailDetailsScreen(navController, trailId)
                    }
                    composable("qrScanner") {
                        QRScannerPage(navController)
                    }
                }

            }
        }
    }
}
