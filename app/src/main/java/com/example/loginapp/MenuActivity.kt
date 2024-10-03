package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Obtener el nombre de usuario pasado desde MainActivity
        username = intent.getStringExtra("USERNAME") ?: ""

        // Botón para ir a HomeActivity
        val goToHomeButton = findViewById<Button>(R.id.go_to_home_button)
        goToHomeButton.setOnClickListener {
            // Ir a HomeActivity y pasar el nombre de usuario
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("USERNAME", username) // Pasar el nombre de usuario a HomeActivity
            startActivity(intent)
        }

        // Botón para ir a CRUDActivity
        val goToCrudButton = findViewById<Button>(R.id.go_to_crud_button)
        goToCrudButton.setOnClickListener {
            // Ir a CRUDActivity
            val intent = Intent(this, CRUDActivity::class.java)
            startActivity(intent)
        }
    }
}
