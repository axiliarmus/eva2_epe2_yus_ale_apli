package com.example.loginapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Obtener el nombre de usuario pasado desde MainActivity
        val username = intent.getStringExtra("USERNAME")

        // Encontrar el TextView y mostrar el nombre de usuario
        val welcomeMessage = findViewById<TextView>(R.id.welcome_message)
        welcomeMessage.text = "Bienvenido, $username"
    }
}