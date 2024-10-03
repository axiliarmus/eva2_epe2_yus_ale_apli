package com.example.loginapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var createBtn: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        // Inicializa la base de datos
        databaseHelper = DatabaseHelper(this)

        // Referencias a los campos de entrada y botón
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        createBtn = findViewById(R.id.create_btn)

        // Configurar el botón de crear usuario
        createBtn.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Llama a la función para agregar al usuario
                val isAdded = databaseHelper.addUser(username, password)

                if (isAdded) {
                    Toast.makeText(this, "Usuario $username creado correctamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: No se pudo crear el usuario.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
