package com.example.kotlinapp4

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.io.path.appendLines
import kotlin.io.path.createFile

class MainActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewFinder: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        cameraExecutor = Executors.newSingleThreadExecutor()
        viewFinder = findViewById(R.id.preview_view)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this,
                REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        var button:Button = findViewById(R.id.button2);
        button.setOnClickListener {
            var intent: Intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }


        val path = Paths.get(filesDir.toString(),"photos")
        try {
            Files.createDirectory(path)
        } catch (e: Exception) {

        }
        val filePath = Paths.get(path.toString(), "data.txt")
        try {
            Files.createFile(filePath)
        } catch (e: Exception) {

        }

        var button2:Button = findViewById(R.id.button);
        button2.setOnClickListener {
            Files.write(filePath, (Calendar.getInstance().time.toString() + System.lineSeparator()).toByteArray(), StandardOpenOption.APPEND)
        }

    }


    private fun startCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider =
                cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }
            val cameraSelector =
                CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,
                    cameraSelector, preview)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }
    private fun allPermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all { permission: String ->
            ContextCompat.checkSelfPermission(baseContext, permission) ==
                    PackageManager.PERMISSION_GRANTED
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions,
            grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }



    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}