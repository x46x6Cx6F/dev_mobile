package com.example.devmobile.ui.Pages.Home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.devmobile.library.data.Trail

@Composable
fun TrailDetailsScreen(navController: NavController, trailId: Any?) {

    val trailIdInt = if (trailId is Int) trailId else trailId.toString().toIntOrNull()

    val trails = listOf(
        Trail(1, "Calanques de Marseille à Cassis", "Une randonnée côtière spectaculaire entre Marseille et Cassis, offrant des vues imprenables sur les calanques.", 1, "20 km", "Modérée"),
        Trail(2, "Mont Puget", "Ascension du point culminant des Calanques, offrant une vue panoramique sur la mer et les falaises.", 1, "12 km", "Difficile"),
        Trail(3, "Cap Canaille", "Randonnée le long des falaises de Cassis, avec des vues magnifiques sur la mer Méditerranée.", 1, "8 km", "Modérée"),
        Trail(4, "Sainte-Baume", "Randonnée jusqu'à la grotte de Sainte-Baume, un lieu de pèlerinage avec une forêt dense et des vues sur la Sainte-Baume.", 1, "15 km", "Moyenne"),
        Trail(5, "Montagne Sainte-Victoire", "Randonnée emblématique près d'Aix-en-Provence, offrant des vues panoramiques sur la Provence.", 1, "13 km", "Moyenne"),
        Trail(6, "Cirque de Morgiou", "Randonnée dans le parc national des Calanques, avec des vues sur le cirque de Morgiou et la mer.", 1, "10 km", "Moyenne"),
        Trail(7, "Parc National des Calanques", "Exploration des calanques de Port-Miou, En-Vau et Port-Pin, avec des paysages côtiers époustouflants.", 1, "18 km", "Modérée"),
        Trail(8, "Massif des Alpilles", "Randonnée dans les Alpilles, avec des vues sur les paysages provençaux et les villages perchés.", 1, "12 km", "Moyenne"),
        Trail(9, "Gorges du Verdon", "Randonnée le long des gorges du Verdon, offrant des vues spectaculaires sur les falaises et l'eau turquoise.", 1, "14 km", "Moyenne"),
        Trail(10, "Mont Faron", "Randonnée jusqu'au sommet du Mont Faron, offrant une vue panoramique sur Toulon et la Méditerranée.", 1, "8 km", "Facile")
    )

    val trail = trails.find { it.id == trailIdInt }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            trail?.let {
                Text(text = it.name, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Distance: ${it.distance}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Difficulté: ${it.difficulty}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Description: ${it.description}", style = MaterialTheme.typography.bodyMedium)
            } ?: Text(text = "Randonnée non trouvée", textAlign = TextAlign.Center)
        }

        // Bouton de retour en haut à gauche
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
        }
    }
}
