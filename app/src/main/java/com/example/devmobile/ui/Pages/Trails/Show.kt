package com.example.devmobile.ui.Pages.Trails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun RandoShow(navController: NavHostController) {
    Column {
        Text(text = "OK", Modifier.clickable { navController.navigate("details/Saliut") })
    }
}
