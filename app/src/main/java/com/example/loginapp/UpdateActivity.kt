package com.example.loginapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateActivity : AppCompatActivity() {

    private lateinit var oldUsernameInput: EditText
    private lateinit var newUsernameInput: EditText
    private lateinit var newPasswordInput: EditText
    private lateinit var updateBtn: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Inicializa la base de datos
        databaseHelper = DatabaseHelper(this)

        // Referencias a los campos de entrada y botón
        oldUsernameInput = findViewById(R.id.old_username_input)
        newUsernameInput = findViewById(R.id.new_username_input)
        newPasswordInput = findViewById(R.id.new_password_input)
        updateBtn = findViewById(R.id.update_btn)

        // Configurar el botón de actualizar usuario
        updateBtn.setOnClickListener {
            val oldUsername = oldUsernameInput.text.toString().trim()
            val newUsername = newUsernameInput.text.toString().trim()
            val newPassword = newPasswordInput.text.toString().trim()

            if (oldUsername.isNotEmpty() && newUsername.isNotEmpty() && newPassword.isNotEmpty()) {
                // Llama a la función para actualizar al usuario
                val isUpdated = databaseHelper.updateUser(oldUsername, newUsername, newPassword)

                if (isUpdated) {
                    Toast.makeText(this, "Usuario $oldUsername actualizado correctamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: Usuario no encontrado.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
