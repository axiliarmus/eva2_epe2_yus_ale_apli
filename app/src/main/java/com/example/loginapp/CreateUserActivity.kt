package com.example.loginapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateUserActivity : AppCompatActivity() {

    lateinit var usernameInput: EditText
    lateinit var passwordInput: EditText
    lateinit var createUserBtn: Button
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        // Inicializamos la base de datos
        databaseHelper = DatabaseHelper(this)

        // Referencias a los campos de entrada
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        createUserBtn = findViewById(R.id.create_user_btn)

        // Configuramos el botón de crear usuario
        createUserBtn.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Llamamos a la función para crear el nuevo usuario
                val isCreated = databaseHelper.crearUsuario(username, password)

                if (isCreated) {
                    Toast.makeText(this, "Usuario $username creado correctamente.", Toast.LENGTH_SHORT).show()
                    // Volver a la pantalla de login (opcional)
                    finish() // Esto cierra esta actividad y regresa a la actividad anterior (MainActivity)
                } else {
                    Toast.makeText(this, "Error: Ya existe un usuario con ese nombre.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor ingrese un nombre de usuario y una contraseña.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
