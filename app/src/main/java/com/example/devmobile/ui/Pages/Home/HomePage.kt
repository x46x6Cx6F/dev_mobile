package com.example.devmobile.ui.Pages.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import com.example.devmobile.library.data.Trail
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

@Composable
fun HomePage(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    var bottomSheetOffset by remember { mutableStateOf(screenHeight - 120.dp) }
    val maxOffset = screenHeight * 0.75f
    val minOffset = screenHeight * 0.25f

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

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
            MapView(context).apply {
                setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                controller.setZoom(17.0)
                controller.setCenter(org.osmdroid.util.GeoPoint(43.512623, 5.443138))
                setMultiTouchControls(true)
            }
        }, modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .offset { IntOffset(x = 0, y = bottomSheetOffset.roundToPx()) }
                .background(Color.White)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            val newOffset = (bottomSheetOffset.value + dragAmount).coerceIn(
                                minOffset.value,
                                maxOffset.value
                            )
                            bottomSheetOffset = newOffset.dp
                        }
                    )
                }
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            BottomSheetContent(navController, trails)
        }

        FloatingActionButton(
            onClick = {
                navController.navigate("qrScanner")
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Info, contentDescription = "Scanner QR Code")
        }
    }
}

@Composable
fun BottomSheetContent(navController: NavController, trails: List<Trail>) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var filteredTrails by remember { mutableStateOf(trails) }

    LaunchedEffect(searchText) {
        filteredTrails = if (searchText.isEmpty()) {
            trails
        } else {
            trails.filter { it.name.contains(searchText, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Rechercher une randonnée") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(filteredTrails.size) { index ->
                TrailItem(trail = filteredTrails[index], navController = navController)
                Spacer(modifier = Modifier.height(25.dp))
            }
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Filtres et suggestions de randonnées")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
fun TrailItem(trail: Trail, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("trailDetails/${trail.id}")
            }
    ) {
        Text(
            text = trail.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = trail.distance,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = trail.difficulty,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
