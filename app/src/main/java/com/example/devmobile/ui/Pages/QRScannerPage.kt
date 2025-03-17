package com.example.devmobile.ui.Pages.QRScanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Vibrator
import android.util.Size as AndroidSize
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerPage(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCamPermission by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scanner de QR Code") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (hasCamPermission) {
            val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
            var previewUseCase by remember { mutableStateOf<Preview?>(null) }
            var analysisUseCase by remember { mutableStateOf<ImageAnalysis?>(null) }

            DisposableEffect(Unit) {
                onDispose {
                    cameraProviderFuture.get().unbindAll()
                }
            }

            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                AndroidView(
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val executor = ContextCompat.getMainExecutor(context)

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            previewUseCase = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            analysisUseCase = ImageAnalysis.Builder()
                                .setTargetResolution(AndroidSize(previewView.width, previewView.height))
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                                .also { analysisUseCase ->
                                    analysisUseCase.setAnalyzer(executor, QRCodeAnalyzer(context))
                                }

                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, previewUseCase, analysisUseCase)
                        }, executor)

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Encadré pour indiquer où placer le QR code
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 4.dp.toPx()
                    val rectSize = 200.dp.toPx()
                    val offsetX = (size.width - rectSize) / 2
                    val offsetY = (size.height - rectSize) / 2

                    drawRect(
                        color = Color.Green,
                        topLeft = Offset(offsetX, offsetY),
                        size = Size(rectSize, rectSize),
                        style = Stroke(width = strokeWidth, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
                    )
                }
            }
        }
    }
}

class QRCodeAnalyzer(private val context: Context) : ImageAnalysis.Analyzer {
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    private val scanner = BarcodeScanning.getClient(options)
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        // Traitez le QR code ici
                        println("QR Code détecté: ${barcode.rawValue}")
                        // Faire vibrer le téléphone
                        vibrator.vibrate(500)
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}
