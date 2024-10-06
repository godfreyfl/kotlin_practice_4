package com.example.kotlinapp4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)


        val recyclerView: RecyclerView = findViewById(R.id.recyclere)


        val path = Paths.get(filesDir.toString(),"photos")
        val filePath = Paths.get(path.toString(), "data.txt")
        var lines = Files.readAllLines(filePath)
        val data = lines // Список данных
        val adapter = AdapterCr(data)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        var button: Button = findViewById(R.id.button3);
        button.setOnClickListener {
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}